package programs.comparator.com.reader;

import org.springframework.stereotype.Service;
import programs.comparator.com.entities.Program;
import programs.comparator.com.entities.ProgramComparator;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DirectoryReader implements IDirectoryReader {

    // list all files from this path
    public static List<Path> getListFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return result;

    }

    // list all directories from this path
    public static List<Path> getListDirectories(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isDirectory)
                    .collect(Collectors.toList());
        }
        return result;

    }
    //build list of new and old programs
    public List<Program> buildListPrograms(Path path) throws IOException {
        List<Program> allProgramsList = new ArrayList<>();
        List<Path> listDirectories = getListDirectories(path);
        final int[] i = {0};

        listDirectories.forEach(dir->{
            //la méthode getListDirectories retourne meme le dossier parent, donc on doit l'exclu, on traite que les sous dossier : new et old
            if(!dir.endsWith(path.getFileName())){
                try {
                    List<Path> listFiles = getListFiles(dir);
                    for(Path file : listFiles){
                        Program program = new Program();
                        program.setId(i[0]);
                        program.setName(file.getFileName().toString());
                        program.setSize((int)file.toFile().length());
                        String SHA = org.apache.commons.codec.digest.DigestUtils.sha256Hex(new FileInputStream(file.toFile()));
                        program.setMd5Code(SHA);
                        if(dir.endsWith("NewPrograms")){
                            program.setNewVersion(true);
                        }else if(dir.endsWith("OldPrograms")){
                            program.setNewVersion(false);
                        }
                        allProgramsList.add(program);
                        i[0]++;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        /*System.out.println("************list new programs***********************");
        listNewPrograms.forEach(program1 -> System.out.println("*"+program1.toString()+"*"));
        System.out.println("************list old programs***********************");
        listOldPrograms.forEach(program1 -> System.out.println("*"+program1.toString()+"*"));*/

        allProgramsList.forEach(program->System.out.println("*"+program.toString()+"*"));
        return  allProgramsList;

    }

    //compare old and new programs
    public  List<ProgramComparator> getListDuplicatPrograms(Path path) throws IOException {
        final int[] i = {0};

        List<Program> allProgramsList = this.buildListPrograms(path);
        List<Program> listNewPrograms = allProgramsList.stream()
                                       .filter(program -> program.getNewVersion())
                                       .collect(Collectors.toList());

        List<Program> listOldPrograms = allProgramsList.stream()
                .filter(program -> !program.getNewVersion())
                .collect(Collectors.toList());

        System.out.println("************lister les nouveaux programmes***********************");
        listNewPrograms.forEach(program1 -> System.out.println("*"+program1.toString()+"*"));
        System.out.println("************lister les anciens programmes***********************");
        listOldPrograms.forEach(program1 -> System.out.println("*"+program1.toString()+"*"));

        //compare two lists
        List<ProgramComparator> programComparatorList = new ArrayList<>();
        listNewPrograms.forEach(newProgram -> {
            ProgramComparator programComparator = new ProgramComparator();
            String newProgramName = newProgram.getName();
            Program oldProgram = listOldPrograms.stream()
                    .filter(pro -> pro.getName().equals(newProgramName))
                    .findFirst().orElse(null);
            if(oldProgram != null) {
                programComparator.setId(i[0]);
                programComparator.setNewProgram(newProgram);
                programComparator.setOldProgram(oldProgram);
                programComparatorList.add(programComparator);
            }
            i[0]++;
        });
        //System.out.println("List program en double");
        //programComparatorList.forEach(a-> System.out.println(a.toString()));
        return programComparatorList;
    }
    //compare old and new programs
    public List<ProgramComparator> compareOldNewPrograms(Path path) throws IOException {
        List<ProgramComparator> listProgramsBeforeComparaison = this.getListDuplicatPrograms(path);
        List<ProgramComparator> listProgramsAfterComparaison = new ArrayList<>();
        listProgramsBeforeComparaison.forEach(programs ->{

            String md5CodeOldProgram = programs.getOldProgram().getMd5Code();
            String md5CodeNewProgram = programs.getNewProgram().getMd5Code();

            int sizeOldProgram = programs.getOldProgram().getSize();
            int sizeNewProgram = programs.getNewProgram().getSize();

            if(md5CodeOldProgram.equals(md5CodeNewProgram) && sizeOldProgram == sizeNewProgram){
                programs.setIsIdentique(true);
            }else{
                //on peut
                programs.setIsIdentique(false);
            }
            listProgramsAfterComparaison.add(programs);
        });

        System.out.println("List program en double");
        listProgramsAfterComparaison.forEach(a-> System.out.println(a.toString()));
        return listProgramsAfterComparaison;
    }

}








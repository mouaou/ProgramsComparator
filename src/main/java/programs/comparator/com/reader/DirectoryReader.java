package programs.comparator.com.reader;

import programs.comparator.com.entities.ListPrograms;
import programs.comparator.com.entities.Program;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryReader {

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
    public ListPrograms buildListPrograms(Path path) throws IOException {
        List<Path> listDirectories = getListDirectories(path);
        List<Program> listNewPrograms = new ArrayList<>();
        List<Program> listOldPrograms = new ArrayList<>();
        ListPrograms Lists = new ListPrograms();

        listDirectories.forEach(dir->{
            try {
                List<Path> listFiles = getListFiles(dir);
                for(Path file : listFiles){
                    Program program = new Program();
                    program.setName(file.getFileName().toString());
                    program.setSize(file.toFile().length());
                    String SHA = org.apache.commons.codec.digest.DigestUtils.sha256Hex(new FileInputStream(file.toFile()));
                    program.setSHACode(SHA);
                    if(dir.endsWith("NewPrograms")){
                        program.setNewVersion(true);
                        listNewPrograms.add(program);
                    }else if(dir.endsWith("OldPrograms")){
                        program.setNewVersion(false);
                        listOldPrograms.add(program);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Lists.setNew(listNewPrograms);
        Lists.setOld(listOldPrograms);
        return Lists;
        // System.out.println("*****list new programs********");
       //  listNewPrograms.forEach(program1 -> System.out.println(""+program1.toString()+""));
      //   System.out.println("*****list old programs********");
      //  listOldPrograms.forEach(program1 -> System.out.println(""+program1.toString()+""));
    }

}

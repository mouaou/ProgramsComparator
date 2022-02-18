package programs.comparator.com.reader;

import programs.comparator.com.entities.Program;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public static void buildListPrograms(Path path) throws IOException {
        List<Path> listDirectories = getListDirectories(path);
        listDirectories.forEach(dir->{
            try {
                List<Path> listFiles = getListFiles(dir);
                for(Path file : listFiles){
                    Program program = new Program();
                    program.setName(file.getFileName().toString());
                    program.setSize(file.toFile().length());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void buildProgramsListClassic(String dirLocation){
        List<Program> programsList = new ArrayList<>();

        List<File> files = null;

        {
            try {
                files = Files.list(Paths.get(dirLocation))
                        .map(Path::toFile)
                        .collect(Collectors.toList());
                for(File file : files){
                    if(file.isDirectory()){
                        buildProgramsListClassic(file.getPath());
                    }
                    else{
                        Program program = new Program();
                        program.setName(file.getName());
                        program.setSize(file.length());
                        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(file));
                        program.setDateCreation(file.lastModified());
                        program.setMd5Code(md5);
                        programsList.add(program);
                    }

                }
            } catch (IOException  e) {
                e.printStackTrace();
            }

            programsList.forEach(program1 -> System.out.println("*"+program1.toString()+"*"));
        }
    }




}

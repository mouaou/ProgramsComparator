package programs.comparator.com.reader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import programs.comparator.com.entities.Program;
import programs.comparator.com.entities.ProgramComparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
                        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(file.toFile()));
                        program.setMd5Code(md5);
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

        System.out.println("************list new programs***********************");
        listNewPrograms.forEach(program1 -> System.out.println("*"+program1.toString()+"*"));
        System.out.println("************list old programs***********************");
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

    //write result of comparaison in Excel

    public void generateExcelComparaisonFile(Path pathDirectory, Path outPutExcelPath) throws IOException {
        List<ProgramComparator> listProgramsAfterComparaison = this.compareOldNewPrograms(pathDirectory);
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Programs Comparaison");

        // Create header CellStyle
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.BLACK.index);
        CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
        // fill foreground color ...
        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.index);
        // and solid fill pattern produces solid grey cell fill
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // Styling border of cell.
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        //This data needs to be written (Object[])
        Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
        data.put(0, new Object[] {"ID", "Old Program", "Code MD5 OldProg","Taille en octe OldProg",
                "New Program", "Code MD5 NewProg","Taille en octe NewProg","Old and New areIdentique"
        });

        listProgramsAfterComparaison.forEach(programComparator -> {
            data.put(programComparator.getId(),new Object[]{programComparator.getId(),
                    programComparator.getOldProgram().getName(),
                    programComparator.getOldProgram().getMd5Code(),
                    programComparator.getOldProgram().getSize(),
                    programComparator.getNewProgram().getName(),
                    programComparator.getNewProgram().getMd5Code(),
                    programComparator.getNewProgram().getSize(),
                    programComparator.getIsIdentique()});
        });


        //Iterate over data and write to sheet
        Set<Integer> keyset = data.keySet();
        int rownum = 0;
        for (Integer key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                // this line creates a cell in the next column of that row
                Cell cell = row.createCell(cellnum++);
                // if rownum is 1 (first row was created before) then set header CellStyle
                if (rownum == 1) cell.setCellStyle(headerCellStyle);
                if (rownum != 1)cell.setCellStyle(style);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
                else if (obj instanceof Boolean){
                    cell.setCellValue((Boolean) obj);                }
            }
        }
        try
        {
            //Write the workbook in file system
            File f = new File(outPutExcelPath.toString());
            if(f.exists() && !f.isDirectory()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(new File(outPutExcelPath.toString()));
            workbook.write(out);
            out.close();
            System.out.println("comparaisonPrograms.xlsx written successfully on disk.");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    }








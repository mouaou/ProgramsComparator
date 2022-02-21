package programs.comparator.com.writer;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import programs.comparator.com.entities.ProgramComparator;
import programs.comparator.com.reader.IDirectoryReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
@Service
public class FileWriter implements IFileWriter {
    @Autowired
    private IDirectoryReader iDirectoryReader;
    @Override
    public void generateExcelComparaisonFile(Path pathDirectory, Path outPutExcelPath) throws IOException {
        //write result of comparaison in Excel
        List<ProgramComparator> listProgramsAfterComparaison = iDirectoryReader.compareOldNewPrograms(pathDirectory);
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
                    cell.setCellValue((boolean) obj);                }
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

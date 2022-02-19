package programs.comparator.com.writer;

import java.io.IOException;
import java.nio.file.Path;

public interface IFileWriter {
    void generateExcelComparaisonFile(Path pathDirectory, Path outPutExcelPath) throws IOException;
}

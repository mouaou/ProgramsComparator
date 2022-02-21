package programs.comparator.com.reader;

import programs.comparator.com.entities.ProgramComparator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IDirectoryReader {
    List<ProgramComparator> compareOldNewPrograms(Path path) throws IOException;
}

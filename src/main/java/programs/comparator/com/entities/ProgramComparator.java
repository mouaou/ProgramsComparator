package programs.comparator.com.entities;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProgramComparator {
    private Integer id;
    private Program oldProgram;
    private Program newProgram;
    private Boolean isIdentique;
}

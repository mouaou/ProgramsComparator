package programs.comparator.com.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@ToString
public class ListPrograms {

    private List<Program> New=new ArrayList<Program>();
    private List<Program> Old=new ArrayList<Program>();

}

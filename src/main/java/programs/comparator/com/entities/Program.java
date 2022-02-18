package programs.comparator.com.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@NoArgsConstructor
@Data
@ToString(exclude = "id")
public class Program {

    private Long id;
    private String name;
    private Long size;
    private Long dateCreation;
    private String md5Code;
    private Boolean newVersion;

}

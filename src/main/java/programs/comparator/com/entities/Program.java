package programs.comparator.com.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
@Data
@ToString
public class Program {

    @ToString.Exclude private Integer id;
    private String name;
    private Integer size;
    @ToString.Exclude private Long dateCreation;
    private String md5Code;
    private Boolean newVersion;

}

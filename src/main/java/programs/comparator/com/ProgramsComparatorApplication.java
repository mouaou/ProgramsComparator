package programs.comparator.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import programs.comparator.com.entities.ListPrograms;
import programs.comparator.com.reader.DirectoryReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ProgramsComparatorApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ProgramsComparatorApplication.class, args);
		DirectoryReader directoryReader = new DirectoryReader();
		ListPrograms Lists = new ListPrograms();
		Path s = Paths.get("C:\\Users\\zelmahi\\Desktop\\Patch_SGMA\\PROD_SGMA\\TestProject");

		Lists = directoryReader.buildListPrograms(s);
		System.out.println("*****list new programs********");
		  Lists.getNew().forEach(program1 -> System.out.println(""+program1.toString()+""));
		System.out.println("*****list old programs********");
		  Lists.getOld().forEach(program1 -> System.out.println(""+program1.toString()+""));

	}

}

package programs.comparator.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import programs.comparator.com.reader.DirectoryReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ProgramsComparatorApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ProgramsComparatorApplication.class, args);
		DirectoryReader directoryReader = new DirectoryReader();

		Path s = Paths.get("C:\\Users\\zelmahi\\Desktop\\Patch_SGMA\\PROD_SGMA\\TestProject");
		
		directoryReader.buildListPrograms(s);


	}

}

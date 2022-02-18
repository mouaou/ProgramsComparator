package programs.comparator.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import programs.comparator.com.reader.DirectoryReader;

@SpringBootApplication
public class ProgramsComparatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgramsComparatorApplication.class, args);
		DirectoryReader directoryReader = new DirectoryReader();
		String dirLocation = "D:/sopra/programs";

		directoryReader.buildProgramsListClassic(dirLocation);


	}

}

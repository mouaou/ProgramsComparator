package programs.comparator.com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import programs.comparator.com.writer.IFileWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ProgramsComparatorApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ProgramsComparatorApplication.class, args);
	}
	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {
		@Autowired
		private  IFileWriter iFileWriter;
		private  Path pathDirectory = Paths.get("C:\\Users\\zelmahi\\Desktop\\Patch_SGMA\\PROD_SGMA\\TestProject");
		private  Path pathComparaisonExcel = Paths.get("C:\\Users\\zelmahi\\Desktop\\Patch_SGMA\\PROD_SGMA\\comparaisonPrograms.xlsx");

		@Override
		public void run(String...args) throws Exception {
			iFileWriter.generateExcelComparaisonFile(pathDirectory, pathComparaisonExcel);
		}
	}

}

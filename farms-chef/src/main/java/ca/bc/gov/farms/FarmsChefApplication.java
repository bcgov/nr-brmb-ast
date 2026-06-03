package ca.bc.gov.farms;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;

@SpringBootApplication
public class FarmsChefApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmsChefApplication.class, args);
	}

	@Command(name = "print")
	public String print(@Option String jsonFilePath) throws Exception {
		return Files.readString(Path.of(jsonFilePath));
	}
}

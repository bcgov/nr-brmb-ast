package ca.bc.gov.farms;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties({ "id" })
abstract class IdMixin {
}

@Component
public class JsonCommands {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonCommands() {
        objectMapper.addMixIn(Map.class, IdMixin.class);
    }

    @Command(name = "print")
    public String print(@Option String jsonFilePath) {
        try {
            String rawJson = Files.readString(Path.of(jsonFilePath));

            Object jsonObject = objectMapper.readValue(rawJson, Object.class);

            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            return "Invalid JSON: " + e.getMessage();
        }
    }
}

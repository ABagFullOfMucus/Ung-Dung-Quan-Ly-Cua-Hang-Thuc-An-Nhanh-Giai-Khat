package persistence;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    public static String[] readLines(String path) {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            return new String[0];
        }
        try {
            return Files.readAllLines(p, StandardCharsets.UTF_8).toArray(new String[0]);
        } catch (IOException e) {
            return new String[0];
        }
    }

    public static boolean writeLines(String path, String[] lines) {
        Path filePath = Paths.get(path);
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

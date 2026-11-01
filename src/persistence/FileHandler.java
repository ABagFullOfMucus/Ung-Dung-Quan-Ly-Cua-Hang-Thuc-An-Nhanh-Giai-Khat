package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static List<String> readLines(String path) {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(Files.readAllLines(p, StandardCharsets.UTF_8));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static boolean writeLines(String path, List<String> lines) {
        Path filePath = Paths.get(path);
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

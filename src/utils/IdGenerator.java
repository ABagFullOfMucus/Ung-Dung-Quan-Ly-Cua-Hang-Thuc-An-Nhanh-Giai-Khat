package utils;

public class IdGenerator {
    public static String next(String prefix, String[] existingIds) {
        int maxId = 0;
        for (String id : existingIds) {
            if (id.startsWith(prefix)) {
                try {
                    int number = Integer.parseInt(
                            id.substring(prefix.length()));
                    if (number > maxId) {
                        maxId = number;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return prefix + String.format("%03d", maxId + 1); // độ rộng tối thiểu là 3 chữ số
    }
}

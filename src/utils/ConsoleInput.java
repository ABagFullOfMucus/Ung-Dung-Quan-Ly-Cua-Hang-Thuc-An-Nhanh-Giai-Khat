package utils;

public class ConsoleInput {

    public static String readRequiredText(String msg) {
        return InputValidator.getNonEmptyString(msg);
    }

    public static int readPositiveInt(String msg) {
        while (true) {
            int value = InputValidator.getInt(msg);
            if (value > 0) {
                return value;
            }
            System.out.println("Giá trị phải lớn hơn 0.");
        }
    }

    public static double readPositiveDouble(String msg) {
        while (true) {
            double value = InputValidator.getDouble(msg);
            if (value > 0) {
                return value;
            }
            System.out.println("Giá trị phải lớn hơn 0.");
        }
    }

    public static int readInt(String prompt) {
        return InputValidator.getInt(prompt);
    }

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = InputValidator.getInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Vui lòng nhập trong khoảng " + min + " - " + max + ".");
        }
    }

    public static double readDouble(String prompt) {
        return InputValidator.getDouble(prompt);
    }

    public static String readNonEmptyString(String prompt) {
        return InputValidator.getNonEmptyString(prompt);
    }

    public static String readLine(String prompt) {
        return InputValidator.getLine(prompt);
    }

    public static boolean readBoolean(String prompt) {
        return InputValidator.getBoolean(prompt);
    }

    public static void pause() {
        InputValidator.pause("Nhấn Enter để tiếp tục...");
    }
}

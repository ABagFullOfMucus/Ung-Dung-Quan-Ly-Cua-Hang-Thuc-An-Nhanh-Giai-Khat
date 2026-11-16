package utils;

public class Validator {
    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        String value = phone.trim();
        return value.matches("^0\\d{9,10}$");
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String value = email.trim();
        if (value.isEmpty()) {
            return false;
        }
        return value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}

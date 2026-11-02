package utils;

public class Validator {
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        String phoneRegex = "^0[35789]\\d{8}$";
        return phone.trim().matches(phoneRegex);
    }

    /**
     * Kiểm tra định dạng email hợp lệ
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        return email.trim().matches(emailRegex);
    }
}

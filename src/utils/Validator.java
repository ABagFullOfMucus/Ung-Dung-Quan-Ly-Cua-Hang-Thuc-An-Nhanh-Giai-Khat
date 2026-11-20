package utils;

public final class Validator {
    private static final String PHONE_REGEX = "^(0\\d{9}|\\+84\\d{9})$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String USERNAME_REGEX = "^[A-Za-z0-9._-]{3,30}$";
    private static final String SAFE_TEXT_REGEX = "^[^|\\r\\n]*$";

    private Validator() {
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPhone(String phone) {
        if (!isNotBlank(phone)) {
            return false;
        }
        return phone.trim().matches(PHONE_REGEX);
    }

    public static boolean isValidEmail(String email) {
        if (!isNotBlank(email)) {
            return false;
        }
        return email.trim().matches(EMAIL_REGEX);
    }

    public static boolean isValidUsername(String username) {
        if (!isNotBlank(username)) {
            return false;
        }
        return username.matches(USERNAME_REGEX);
    }

    public static boolean isSafeText(String text) {
        if (text == null) {
            return true;
        }
        return text.matches(SAFE_TEXT_REGEX);
    }
}
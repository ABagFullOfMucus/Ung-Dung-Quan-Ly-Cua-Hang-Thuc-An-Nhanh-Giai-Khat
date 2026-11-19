package persistence.converter.base;

import persistence.TextConverter;

public abstract class BaseTextConverter<T> implements TextConverter<T> {
    protected static final String SEPARATOR = "|";

    protected String join(Object... values) { // có thể truyền bao nhiêu tham số cũng được, linh hoạt hơn truyền chay.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(SEPARATOR);
            }
            sb.append(clean(values[i]));
        }
        return sb.toString();
    }

    protected String[] split(String line) {
        String[] parts = line.split("\\|", -1); // -1 là tránh việc phần tử cuối là "" dẫn đến split làm mất số lượng
                                                // phần tử ban đầu.
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    protected String clean(Object value) {
        if (value == null)
            return "";
        return String.valueOf(value).replace(SEPARATOR, " ");
    }
}

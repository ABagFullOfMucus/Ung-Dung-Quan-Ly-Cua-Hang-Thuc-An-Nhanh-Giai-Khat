package utils;

import model.base.BaseEntity;

/**
 * Tiện ích thao tác mảng tĩnh thuần ([]), không dùng Collection, Lambda hay
 * Reflection.
 */
public final class ArrayUtils {
    private ArrayUtils() {
    }

    /**
     * Trích danh sách mã ID (không chứa null) từ mảng thực thể cho IdGenerator.
     */
    public static String[] extractIds(BaseEntity[] entities) {
        if (entities == null) {
            return new String[0];
        }
        int validCount = 0;
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] != null && entities[i].getId() != null) {
                validCount++;
            }
        }
        String[] ids = new String[validCount];
        int idx = 0;
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] != null && entities[i].getId() != null) {
                ids[idx++] = entities[i].getId();
            }
        }
        return ids;
    }

    /**
     * Đếm số phần tử khác null trong mảng.
     */
    public static int countNonNull(Object[] array) {
        if (array == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Lọc bỏ toàn bộ phần tử null dư thừa từ mảng nguồn sang mảng đích đúng kích
     * thước.
     */
    public static <T> T[] compact(T[] source, T[] destination) {
        if (source == null || destination == null) {
            return destination;
        }
        int idx = 0;
        for (int i = 0; i < source.length && idx < destination.length; i++) {
            if (source[i] != null) {
                destination[idx++] = source[i];
            }
        }
        return destination;
    }

    /**
     * Gộp 2 mảng (bỏ qua null) vào mảng đích đã cấp phát đúng kích thước.
     */
    public static <T> T[] merge(T[] first, T[] second, T[] destination) {
        if (destination == null) {
            return null;
        }
        int idx = 0;
        if (first != null) {
            for (int i = 0; i < first.length && idx < destination.length; i++) {
                if (first[i] != null) {
                    destination[idx++] = first[i];
                }
            }
        }
        if (second != null) {
            for (int i = 0; i < second.length && idx < destination.length; i++) {
                if (second[i] != null) {
                    destination[idx++] = second[i];
                }
            }
        }
        return destination;
    }

    /**
     * Xóa phần tử tại vị trí index và chép các phần tử còn lại sang mảng đích.
     */
    public static <T> T[] removeAt(T[] source, int index, T[] destination) {
        if (source == null || destination == null || index < 0 || index >= source.length) {
            return destination;
        }
        int idx = 0;
        for (int i = 0; i < source.length && idx < destination.length; i++) {
            if (i != index) {
                destination[idx++] = source[i];
            }
        }
        return destination;
    }
}
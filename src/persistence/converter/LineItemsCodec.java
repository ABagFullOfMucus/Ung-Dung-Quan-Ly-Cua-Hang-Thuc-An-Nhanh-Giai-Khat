package persistence.converter;

import model.transaction.LineItem;

/**
 * Mã hóa danh sách dòng hàng của một giao dịch thành một trường text duy nhất.
 * Định dạng: id~name~quantity~unitAmount;id~name~quantity~unitAmount
 * Không dùng ký tự "|" vì đó là phân cách của cả dòng.
 */
public final class LineItemsCodec {

    private LineItemsCodec() {
    }

    public static String encode(LineItem[] items) {
        if (items == null || items.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (LineItem item : items) {
            if (item == null || item.getItem() == null) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(';');
            }
            builder.append(sanitize(item.getItem().getId())).append('~')
                    .append(sanitize(item.getItem().getName())).append('~')
                    .append(item.getQuantity()).append('~')
                    .append(item.getUnitAmount());
        }
        return builder.toString();
    }

    public static Entry[] decode(String raw) {
        if (raw == null || raw.isEmpty()) {
            return new Entry[0];
        }
        String[] chunks = raw.split(";");
        Entry[] temp = new Entry[chunks.length];
        int count = 0;
        for (String chunk : chunks) {
            if (chunk == null || chunk.isBlank()) {
                continue;
            }
            String[] fields = chunk.split("~", -1);
            if (fields.length < 4) {
                continue;
            }
            try {
                String id = fields[0].trim();
                String name = fields[1];
                int quantity = Integer.parseInt(fields[2].trim());
                double unitAmount = Double.parseDouble(fields[3].trim());
                if (id.isEmpty() || quantity <= 0 || unitAmount < 0) {
                    continue;
                }
                temp[count++] = new Entry(id, name, quantity, unitAmount);
            } catch (NumberFormatException e) {
                // Bỏ qua dòng hàng bị hỏng, giữ phần còn lại của giao dịch.
            }
        }
        Entry[] result = new Entry[count];
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }

    private static String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('|', ' ')
                .replace('~', ' ')
                .replace(';', ' ')
                .replace('\r', ' ')
                .replace('\n', ' ')
                .trim();
    }

    public static final class Entry {
        private final String id;
        private final String name;
        private final int quantity;
        private final double unitAmount;

        public Entry(String id, String name, int quantity, double unitAmount) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.unitAmount = unitAmount;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getUnitAmount() {
            return unitAmount;
        }
    }
}

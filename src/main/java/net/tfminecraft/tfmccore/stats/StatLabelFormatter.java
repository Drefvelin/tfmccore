package net.tfminecraft.tfmccore.stats;

public final class StatLabelFormatter {
    private StatLabelFormatter() {}

    public static String format(String statKey) {
        if (statKey == null || statKey.isBlank()) {
            return "";
        }

        String[] parts = statKey.split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(part.substring(0, 1).toUpperCase());
            if (part.length() > 1) {
                builder.append(part.substring(1).toLowerCase());
            }
        }
        return builder.toString();
    }

    public static String formatCategoryTitle(String categoryId) {
        return format(categoryId) + " stats:";
    }
}

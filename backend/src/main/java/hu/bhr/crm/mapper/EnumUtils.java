package hu.bhr.crm.mapper;

public class EnumUtils {
    private EnumUtils() {}

    public static String normalizeEnumName(String input) {
        return input.trim()
                .toUpperCase()
                .replaceAll("[\\s\\-_]+", "_");
    }
}

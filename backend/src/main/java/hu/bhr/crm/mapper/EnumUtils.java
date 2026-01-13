package hu.bhr.crm.mapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EnumUtils {

    public static String normalizeEnumName(String input) {
        return input.trim()
                .toUpperCase()
                .replaceAll("[\\s\\-_]+", "_");
    }
}

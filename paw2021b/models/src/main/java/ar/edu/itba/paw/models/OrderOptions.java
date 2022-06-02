package ar.edu.itba.paw.models;

import java.util.Arrays;

public enum OrderOptions {
    TITLE_ASC,
    TITLE_DESC,
    DATE_ASC,
    DATE_DESC,
    LIKES_ASC,
    LIKES_DESC;

    public static boolean contains(final String value) {
        return Arrays.stream(values()).map(Enum::name).anyMatch(code -> code.equals(value));
    }


}

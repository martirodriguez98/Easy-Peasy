package ar.edu.itba.paw.models;

import java.util.Arrays;

public enum OrderOptionsUsers {
    NAME_ASC,
    NAME_DESC;

    public static boolean contains(final String value){
        return Arrays.stream(values()).map(Enum::name).anyMatch(code->code.equals(value));
    }
}

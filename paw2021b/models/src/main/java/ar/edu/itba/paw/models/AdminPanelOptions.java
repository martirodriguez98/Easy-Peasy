package ar.edu.itba.paw.models;

import java.util.Arrays;

public enum AdminPanelOptions {
    BY_USERNAME,
    BY_ID;

    public static boolean contains(final String value){
        return Arrays.stream(values()).map(Enum::name).anyMatch(code->code.equals(value));
    }

}

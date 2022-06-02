package ar.edu.itba.paw.models;

import java.util.Arrays;

public enum ReportsPanelOptions {
    BY_REPORTER,
    BY_REPORTED,
    NEWEST,
    OLDEST;

    public static boolean contains(final String value){
        return Arrays.stream(values()).map(Enum::name).anyMatch(code->code.equals(value));
    }
}

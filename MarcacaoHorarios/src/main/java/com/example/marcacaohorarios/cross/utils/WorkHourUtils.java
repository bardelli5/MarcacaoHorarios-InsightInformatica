package com.example.marcacaohorarios.cross.utils;

public class WorkHourUtils {
    public static int stringToHourInt(String hour) {
        return Integer.parseInt(hour.split(":")[0]);
    }

    public static int stringToMinuteInt(String hour) {
        return Integer.parseInt(hour.split(":")[1]);
    }
}

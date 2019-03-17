package com.fanok.mdpu24v1;

public class Week {
    public static final int red = 0;
    public static final int green = 1;

    private static int week;

    public static int getWeek() {
        return week;
    }

    public static void setWeek(int week) {
        Week.week = week;
    }

    public static String getDayName(int dayNumber) {
        switch (dayNumber) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
        }
        return "Not Day";
    }
}

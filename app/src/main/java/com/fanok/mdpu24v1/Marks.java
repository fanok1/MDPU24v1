package com.fanok.mdpu24v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Marks {

    private static SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("UA"));
    private String name;
    private HashMap<Date, Integer> marks = new HashMap<>();
    private int modul;
    private int sum;

    public Marks(String name) {
        this.name = name;
    }


    public void setMark(String date, int mark) throws ParseException {
        if (date.equals("0")) {
            this.modul = mark;
        } else {
            marks.put(ft.parse(date), mark);
            this.sum += mark;
        }
    }

    public HashMap<Date, Integer> getMarks() {
        return marks;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return marks.size();
    }

    public int getModul() {
        return modul;
    }

    public int abs() {
        return sum / marks.size() * 4;
    }

    public int itog() {
        return abs() + modul;
    }

}

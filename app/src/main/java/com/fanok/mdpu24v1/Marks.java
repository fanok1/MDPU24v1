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

    public Marks(String name) {
        this.name = name;
    }


    public void setMark(String date, int mark) throws ParseException {
        marks.put(ft.parse(date), mark);
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


}

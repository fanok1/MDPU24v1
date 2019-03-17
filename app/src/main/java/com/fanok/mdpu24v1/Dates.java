package com.fanok.mdpu24v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class Dates {

    private static SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("UA"));
    private HashSet<Date> dates = new HashSet<>();

    public ArrayList<Date> getDates() {
        return new ArrayList<>(dates);
    }

    public void setDates(String date) throws ParseException {
        this.dates.add(ft.parse(date));
    }

    public void setDates(String pattern, String date) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat(pattern, Locale.forLanguageTag("UA"));
        this.dates.add(ft.parse(date));
    }

    public ArrayList<Date> sort() {
        ArrayList<Date> arrayList = new ArrayList<>(dates);
        boolean b = true;
        while (b) {
            b = false;
            for (int i = 0; i < arrayList.size() - 1; i++) {
                if (arrayList.get(i).getTime() > arrayList.get(i + 1).getTime()) {
                    Date date = arrayList.get(i);
                    arrayList.set(i, arrayList.get(i + 1));
                    arrayList.set(i + 1, date);
                    b = true;
                }
            }
        }

        return arrayList;
    }
}

package com.fanok.mdpu24v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Absenteeism {

    private static SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("UA"));
    private String name;
    private HashMap<Date, Para> absenteeism = new HashMap<>();

    public Absenteeism(String name) {
        this.name = name;
    }


    public void setAbsenteeism(String date, int absenteeism) throws ParseException {
        if (date.isEmpty() || absenteeism == -1) return;
        if (this.absenteeism.containsKey(ft.parse(date))) {
            this.absenteeism.get(ft.parse(date)).setPara(absenteeism);
        } else {
            Para p = new Para();
            p.setPara(absenteeism);
            this.absenteeism.put(ft.parse(date), p);
        }
    }

    public HashMap<Date, Para> getAbsenteeism() {
        return absenteeism;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return absenteeism.size();
    }


}


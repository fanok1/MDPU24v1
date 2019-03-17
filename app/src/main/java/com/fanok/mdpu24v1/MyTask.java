package com.fanok.mdpu24v1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyTask {
    private String name;
    private String text;
    private Date date;

    public MyTask(String name, String text, long date) {
        this.name = name;
        this.text = text;
        this.date = new Date(date);
    }

    public MyTask() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.forLanguageTag("UA"));
        return dateFormat.format(date);
    }

    public void setDate(long date) {
        this.date = new Date(date);
    }
}

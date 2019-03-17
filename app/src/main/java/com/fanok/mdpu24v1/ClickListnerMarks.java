package com.fanok.mdpu24v1;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClickListnerMarks implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    public static View keybord;
    private static String name;
    private static String date;
    private static String modul;
    @SuppressLint("StaticFieldLeak")
    private static TextView textView;
    private String nameLocal;
    private String dateLocal;
    private String modulLocal;

    public ClickListnerMarks(String name, Date date, String modul) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("UA"));
        nameLocal = name;
        dateLocal = dateFormat.format(date);
        modulLocal = modul;
    }

    public static String getModul() {
        return modul;
    }

    public static String getName() {
        return name;
    }

    public static String getDate() {
        return date;
    }

    public static TextView getTextView() {
        return textView;
    }

    @Override
    public void onClick(View view) {
        if (keybord != null) keybord.setVisibility(View.VISIBLE);
        textView = (TextView) view;
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ClickListnerMarks.name = nameLocal;
        ClickListnerMarks.date = dateLocal;
        ClickListnerMarks.modul = modulLocal;
    }
}

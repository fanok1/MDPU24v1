package com.fanok.mdpu24v1;

import android.view.View;
import android.widget.TextView;

import com.fanok.mdpu24v1.dowland.InsertDataInSql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClickListnerAbsenteeism implements View.OnClickListener {

    private String name;
    private String date;
    private String modul;
    private int para;

    public ClickListnerAbsenteeism(String name, Date date, String modul, int para) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("UA"));
        this.name = name;
        this.date = dateFormat.format(date);
        this.modul = modul;
        this.para = para;
    }

    @Override
    public void onClick(View view) {
        TextView textView = (TextView) view;
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        String url;
        if (textView.getText().length() != 0) {
            textView.setText("");
            url = view.getContext().getResources().getString(R.string.server_api) + "delete_absenteeism.php";
        } else {
            textView.setText("н");
            url = view.getContext().getResources().getString(R.string.server_api) + "add_absenteeism.php";
        }
        InsertDataInSql sql = new InsertDataInSql(view, url);
        if (sql.isOnline()) {
            sql.setData("name", name);
            sql.setData("date", date);
            sql.setData("modul", modul);
            sql.setData("para", String.valueOf(para));
            sql.execute();
        }
    }
}

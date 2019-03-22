package com.fanok.mdpu24v1;

import android.annotation.SuppressLint;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.fanok.mdpu24v1.dowland.InsertDataInSql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClickListnerMarks implements TextInputEditText.OnEditorActionListener {

    private static String predmet;
    @SuppressLint("StaticFieldLeak")
    private TextView textView;

    public ClickListnerMarks(String name, Date date, String modul) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("UA"));
        nameLocal = name;
        if (date == null) dateLocal = "";
        else dateLocal = dateFormat.format(date);
        modulLocal = modul;
    }

    private String nameLocal;
    private String dateLocal;
    private String modulLocal;

    public static void setPredmet(String predmet) {
        ClickListnerMarks.predmet = predmet;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENDCALL) {
            String url = textView.getContext().getResources().getString(R.string.server_api) + "update_marks.php";
            String mark = textView.getText().toString();
            InsertDataInSql sql = new InsertDataInSql(textView, url);
            if (sql.isOnline()) {
                sql.setData("predmet", predmet);
                sql.setData("name", nameLocal);
                sql.setData("date", dateLocal);
                sql.setData("mark", mark);
                sql.setData("modul", modulLocal);
                sql.execute();
            } else {
                Toast.makeText(textView.getContext(), textView.getResources().getText(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }
}

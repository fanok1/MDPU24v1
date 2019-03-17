package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fanok.mdpu24v1.Absenteeism;
import com.fanok.mdpu24v1.ClickListnerAbsenteeism;
import com.fanok.mdpu24v1.Dates;
import com.fanok.mdpu24v1.Para;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ParceJsonAbsenteeism extends AsyncTask<Void, Void, ArrayList<Absenteeism>> {


    private String json;
    private int modul;
    @SuppressLint("StaticFieldLeak")
    private TableLayout firstColum;
    @SuppressLint("StaticFieldLeak")
    private TableLayout table;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    private Dates dates = new Dates();
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public ParceJsonAbsenteeism(String json, View view, int modul) {
        this.json = json;
        this.firstColum = view.findViewById(R.id.firstColum);
        this.table = view.findViewById(R.id.table);
        this.context = table.getContext();
        this.modul = modul;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressBar != null) progressBar.setVisibility(ProgressBar.VISIBLE);

    }

    @Override
    protected ArrayList<Absenteeism> doInBackground(Void... voids) {
        ArrayList<Absenteeism> absenteeisms = new ArrayList<>();
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            boolean flag = false;
            JsonObject jMarks = jsonArray.get(i).getAsJsonObject();
            String name = jMarks.get("name").getAsString();
            for (int j = 0; j < absenteeisms.size(); j++) {
                if (absenteeisms.get(j).getName().equals(name)) {
                    try {
                        absenteeisms.get(j).setAbsenteeism(jMarks.get("day").getAsString(), jMarks.get("mark").getAsInt());
                        dates.setDates(jMarks.get("day").getAsString());
                        flag = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
            if (!flag) {
                Absenteeism mark = new Absenteeism(name);
                try {
                    mark.setAbsenteeism(jMarks.get("day").getAsString(), jMarks.get("mark").getAsInt());
                    dates.setDates(jMarks.get("day").getAsString());
                    absenteeisms.add(mark);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        return absenteeisms;
    }

    @Override
    protected void onPostExecute(ArrayList<Absenteeism> absenteeisms) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.forLanguageTag("UA"));
        super.onPostExecute(absenteeisms);
        ArrayList<Date> dates = this.dates.sort();
        TableRow row = new TableRow(context);
        TableRow.LayoutParams lpRow = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lpRow);
        if (!dates.isEmpty()) row.addView(createTextView("Имя/дата"), 0);
        firstColum.addView(row, 0);
        row = new TableRow(context);
        row.setLayoutParams(lpRow);
        for (int i = 0; i < dates.size(); i++) {
            row.addView(createTextView(dateFormat.format(dates.get(i))), i);
        }
        table.addView(row);

        row = new TableRow(context);
        row.setLayoutParams(lpRow);
        if (!dates.isEmpty()) row.addView(createTextView("Пара"), 0);
        firstColum.addView(row, 1);
        row = new TableRow(context);
        row.setLayoutParams(lpRow);

        for (int i = 0; i < dates.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(context);
            TableRow.LayoutParams lpTextView = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(lpTextView);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 4; j++) {
                TextView tx = createTextView(j + 1);
                linearLayout.addView(tx, j);
            }
            row.addView(linearLayout, i);
        }
        table.addView(row);

        for (int i = 0; i < absenteeisms.size(); i++) {
            row = new TableRow(context);
            row.setLayoutParams(lpRow);
            row.addView(createTextView(absenteeisms.get(i).getName()), 0);
            firstColum.addView(row);
            row = new TableRow(context);
            row.setLayoutParams(lpRow);
            HashMap<Date, Para> marksForStudent = absenteeisms.get(i).getAbsenteeism();
            for (int j = 0; j < dates.size(); j++) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setLayoutParams(lpRow);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (int z = 1; z < 5; z++) {
                    TextView textView;
                    try {
                        if (marksForStudent.containsKey(dates.get(j)) && marksForStudent.get(dates.get(j)).conteins(z))
                            textView = createTextView("н");
                        else textView = createTextView("");
                    } catch (RuntimeException ignored) {
                        textView = createTextView("");
                    }
                    if (TypeTimeTable.getType() != TypeTimeTable.studentTimeTable)
                        textView.setOnClickListener(new ClickListnerAbsenteeism(absenteeisms.get(i).getName(), dates.get(j), String.valueOf(modul), z));
                    linearLayout.addView(textView);
                }
                row.addView(linearLayout);
            }
            table.addView(row);
        }
        if (progressBar != null) progressBar.setVisibility(ProgressBar.GONE);
    }

    private TextView createTextView(String text) {
        TextView textView = textView();
        textView.setText(text);
        return textView;
    }

    private TextView createTextView(int text) {
        TextView textView = textView();
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(String.valueOf(text));
        return textView;
    }

    private TextView textView() {
        TextView textView = new TextView(context);
        TableRow.LayoutParams lpTextView = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1);
        lpTextView.setMargins(2, 2, 2, 2);
        textView.setLayoutParams(lpTextView);
        textView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        textView.setPadding(5, 0, 5, 0);
        textView.setTextSize(20);
        return textView;
    }


}
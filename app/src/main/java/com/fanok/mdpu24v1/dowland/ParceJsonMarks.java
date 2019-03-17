package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fanok.mdpu24v1.ClickListnerMarks;
import com.fanok.mdpu24v1.Dates;
import com.fanok.mdpu24v1.Marks;
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

public class ParceJsonMarks extends AsyncTask<Void, Void, ArrayList<Marks>> {


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

    public ParceJsonMarks(String json, View view, int modul) {
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
    protected ArrayList<Marks> doInBackground(Void... voids) {
        ArrayList<Marks> marks = new ArrayList<>();
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            boolean flag = false;
            JsonObject jMarks = jsonArray.get(i).getAsJsonObject();
            String name = jMarks.get("name").getAsString();
            for (int j = 0; j < marks.size(); j++) {
                if (marks.get(j).getName().equals(name)) {
                    try {
                        marks.get(j).setMark(jMarks.get("day").getAsString(), jMarks.get("mark").getAsInt());
                        dates.setDates(jMarks.get("day").getAsString());
                        flag = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
            if (!flag) {
                Marks mark = new Marks(name);
                try {
                    mark.setMark(jMarks.get("day").getAsString(), jMarks.get("mark").getAsInt());
                    dates.setDates(jMarks.get("day").getAsString());
                    marks.add(mark);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        return marks;
    }

    @Override
    protected void onPostExecute(ArrayList<Marks> marks) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.forLanguageTag("UA"));
        super.onPostExecute(marks);
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
        for (int i = 0; i < marks.size(); i++) {
            row = new TableRow(context);
            row.setLayoutParams(lpRow);
            row.addView(createTextView(marks.get(i).getName()), 0);
            firstColum.addView(row);
            row = new TableRow(context);
            row.setLayoutParams(lpRow);
            HashMap<Date, Integer> marksForStudent = marks.get(i).getMarks();
            for (int j = 0; j < dates.size(); j++) {
                TextView textView;
                try {
                    int mark = marksForStudent.get(dates.get(j));
                    textView = createTextView(mark);
                } catch (RuntimeException ignored) {
                    textView = createTextView("");
                }
                if (TypeTimeTable.getType() != TypeTimeTable.studentTimeTable)
                    textView.setOnClickListener(new ClickListnerMarks(marks.get(i).getName(), dates.get(j), String.valueOf(modul)));
                row.addView(textView, j);
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
        TableRow.LayoutParams lpTextView = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        lpTextView.setMargins(2, 2, 2, 2);
        textView.setLayoutParams(lpTextView);
        textView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        textView.setPadding(5, 0, 5, 0);
        textView.setTextSize(20);
        return textView;
    }


}
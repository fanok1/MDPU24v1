package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanok.mdpu24v1.MyClickListner;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.TimeTable;
import com.fanok.mdpu24v1.Week;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ParceJson extends AsyncTask<Void, Void, ArrayList<TimeTable>> {

    @SuppressLint("StaticFieldLeak")
    private String json;
    private ArrayList<View> views;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;

    public ParceJson(String json, ArrayList<View> views) {
        this.json = json;
        this.views = views;
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
    protected ArrayList<TimeTable> doInBackground(Void... voids) {
        ArrayList<TimeTable> timeTables = new ArrayList<>();
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        jsonArray = jsonArray.get(Week.getWeek()).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            TimeTable timeTable = new TimeTable();
            JsonObject jtimeTable = jsonArray.get(i).getAsJsonObject();
            timeTable.setLesson(jtimeTable.get("lesson").getAsString());
            int type = jtimeTable.get("type").getAsInt();
            switch (type) {
                case 0:
                    timeTable.setType("Лекция");
                case 1:
                    timeTable.setType("Практ.");
            }
            timeTable.setNumber(jtimeTable.get("number").getAsInt());
            timeTable.setAud(jtimeTable.get("aud").getAsString());
            timeTable.setName(jtimeTable.get("attr").getAsString() + " " + jtimeTable.get("name").getAsString());
            if (!jtimeTable.get("photo").isJsonNull())
                timeTable.setPhoto(jtimeTable.get("photo").getAsString());
            timeTables.add(timeTable);
        }
        return timeTables;
    }

    @Override
    protected void onPostExecute(ArrayList<TimeTable> timeTables) {
        super.onPostExecute(timeTables);
        for (int i = 0; i < timeTables.size(); i++) {
            @SuppressLint("CutPasteId") TextView title = views.get(i).findViewById(R.id.title_id);
            title.setText(timeTables.get(i).getLesson());

            TextView type = views.get(i).findViewById(R.id.type_id);
            type.setText(timeTables.get(i).getType());

            TextView name = views.get(i).findViewById(R.id.prepod_id);
            name.setText(timeTables.get(i).getName());

            TextView aud = views.get(i).findViewById(R.id.aud_id);
            aud.setText(timeTables.get(i).getAud());

            setTime(views.get(i).findViewById(R.id.time_start), views.get(i).findViewById(R.id.time_end), timeTables.get(i).getNumber());

            views.get(i).setOnClickListener(new MyClickListner(timeTables.get(i).getPhoto(), timeTables.get(i).getName()));


        }
        for (int i = timeTables.size(); i < views.size(); i++) {
            @SuppressLint("CutPasteId") TextView textView = views.get(i).findViewById(R.id.title_id);
            textView.setText("      Нет пары");
        }


        if (progressBar != null) progressBar.setVisibility(ProgressBar.GONE);
    }

    private void setTime(TextView start, TextView end, int number) {
        final int paraTime = 80;
        final int peremena = 15;
        final int dayStart = 480;

        int paraStart = dayStart + (paraTime + peremena) * number;
        int paraEnd = paraStart + paraTime;


        start.setText(convertTimeToString(paraStart));
        end.setText(convertTimeToString(paraEnd));
    }

    private String convertTimeToString(int minutes) {
        if (minutes > 24 * 60) return "0:00";
        int hours = minutes / 60;
        minutes -= hours * 60;
        if (minutes < 10) return hours + ":0" + minutes;
        return hours + ":" + minutes;
    }
}
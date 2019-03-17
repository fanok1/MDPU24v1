package com.fanok.mdpu24v1.dowland;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;

import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.Week;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DowlandJson extends DowladParent {
    private ArrayList<View> views;
    private String result;
    private int day;

    public DowlandJson(@NonNull View view, @NonNull String url, ArrayList<View> views, int day) {
        super(view, url);
        this.views = views;
        int type = TypeTimeTable.getType();
        setData("type", String.valueOf(type));
        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        String login = mPref.getString("login", "");
        setData("login", login);
        setData("day", String.valueOf(day));
        this.day = day;
        if (type == TypeTimeTable.curatorTimeTable) setData("group", TypeTimeTable.getGroup());
        else setData("group", "");
    }


    @Override
    protected void parce(Document data) {
        result = data.getElementsByTag("body").text();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        SharedPreferences preferences = getView().getContext().getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Week.getDayName(day), result);
        editor.apply();
        super.onPostExecute(aVoid);
        ParceJson parceJson = new ParceJson(result, views);
        parceJson.setProgressBar(getProgressBar());
        parceJson.execute();

    }
}

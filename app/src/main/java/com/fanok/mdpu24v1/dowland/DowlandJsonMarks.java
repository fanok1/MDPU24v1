package com.fanok.mdpu24v1.dowland;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;

import com.fanok.mdpu24v1.StartActivity;

import org.jsoup.nodes.Document;

import static android.content.Context.MODE_PRIVATE;

public class DowlandJsonMarks extends DowladParent {

    private int modul;
    private String result;

    public DowlandJsonMarks(@NonNull View view, @NonNull String url, int modul) {
        super(view, url);
        this.modul = modul;
    }

    @Override
    protected void parce(Document data) {
        result = data.getElementsByTag("body").text();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        SharedPreferences preferences = getView().getContext().getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("modul_" + modul, result);
        editor.apply();
        super.onPostExecute(aVoid);
        ParceJsonMarks parceJson = new ParceJsonMarks(result, getView(), modul);
        parceJson.setProgressBar(getProgressBar());
        parceJson.execute();
    }
}

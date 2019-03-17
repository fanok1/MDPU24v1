package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.fanok.mdpu24v1.StartActivity;

import org.jsoup.nodes.Document;

import static android.content.Context.MODE_PRIVATE;

public class DowlandJsonCurator extends DowladParent {

    @SuppressLint("StaticFieldLeak")
    private ListView listView;
    private String result;
    private SharedPreferences.Editor editor;

    public DowlandJsonCurator(@NonNull View view, @NonNull String url, ListView listView) {
        super(view, url);
        this.listView = listView;
        SharedPreferences preferences = getView().getContext().getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();
    }

    public ListView getListView() {
        return listView;
    }

    @Override
    protected void parce(Document data) {
        result = data.getElementsByTag("body").text();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        editor.putString("Curator", result);
        editor.apply();
        super.onPostExecute(aVoid);
        ParceJsonCurator parceJson = new ParceJsonCurator(result, listView);
        parceJson.setProgressBar(getProgressBar());
        parceJson.execute();
    }
}

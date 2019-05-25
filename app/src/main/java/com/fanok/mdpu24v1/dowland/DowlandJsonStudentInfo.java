package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.activity.MainActivity;

import org.jsoup.nodes.Document;

import static android.content.Context.MODE_PRIVATE;

public class DowlandJsonStudentInfo extends DowladParent {

    @SuppressLint("StaticFieldLeak")
    private ListView listView;
    private String name;
    private String result;
    private SharedPreferences.Editor editor;
    @SuppressLint("StaticFieldLeak")
    private MainActivity activity;

    @SuppressLint("CommitPrefEdits")
    public DowlandJsonStudentInfo(@NonNull View view, @NonNull String url, ListView listView, String name, @NonNull MainActivity activity) {
        super(view, url);
        this.activity = activity;
        this.listView = listView;
        this.name = name;
        setData("name", name);

        SharedPreferences preferences = getView().getContext().getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public ListView getListView() {
        return listView;
    }

    public String getName() {
        return name;
    }

    public String getResult() {
        return result;
    }

    @Override
    protected void parce(Document data) {
        result = data.getElementsByTag("body").text();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        editor.putString("Student_" + name, result);
        editor.apply();
        super.onPostExecute(aVoid);
        ParceJsonStudentInfo parceJson = new ParceJsonStudentInfo(activity, result, listView);
        parceJson.setProgressBar(getProgressBar());
        parceJson.execute();
    }
}

package com.fanok.mdpu24v1.dowland;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.fanok.mdpu24v1.activity.MainActivity;

public class DowlandJsonTask extends DowlandJsonStudentInfo {

    public DowlandJsonTask(@NonNull View view, @NonNull String url, ListView listView, String name, MainActivity activity) {
        super(view, url, listView, name, activity);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        getEditor().putString("Task_" + getName(), getResult());
        getEditor().apply();
        ParceJsonTask parceJson = new ParceJsonTask(getResult(), getListView());
        parceJson.setProgressBar(getProgressBar());
        parceJson.execute();
    }
}

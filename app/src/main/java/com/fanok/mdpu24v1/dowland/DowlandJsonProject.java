package com.fanok.mdpu24v1.dowland;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.fanok.mdpu24v1.activity.MainActivity;

public class DowlandJsonProject extends DowlandJsonStudentInfo {


    public DowlandJsonProject(@NonNull View view, @NonNull String url, ListView listView, String name, MainActivity activity) {
        super(view, url, listView, name, activity);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        getEditor().putString("Project_" + getName(), getResult());
        getEditor().apply();
        ParceJsonProject parceJson = new ParceJsonProject(getResult(), getListView(), getName());
        parceJson.setProgressBar(getProgressBar());
        parceJson.execute();
    }
}

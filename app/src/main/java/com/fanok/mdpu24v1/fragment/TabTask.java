package com.fanok.mdpu24v1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.activity.MainActivity;
import com.fanok.mdpu24v1.dowland.DowlandJsonTask;
import com.fanok.mdpu24v1.dowland.ParceJsonTask;

import java.util.Objects;

public class TabTask extends TabStudentInfo {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferences mPref = Objects.requireNonNull(getActivity()).getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        final String url = getResources().getString(R.string.server_api) + "get_task.php";
        DowlandJsonTask dowlandJson = new DowlandJsonTask(view, url, getListView(), getName(), (MainActivity) getActivity());
        if (dowlandJson.isOnline()) {
            dowlandJson.setProgressBar(view.findViewById(R.id.progressBar));
            dowlandJson.setData("level", String.valueOf(mPref.getInt("level", 0)));
            dowlandJson.setData("login", mPref.getString("login", ""));
            dowlandJson.execute();
        } else {
            String json = mPref.getString("Task_" + getName(), "");
            if (!json.isEmpty()) {
                ParceJsonTask parceJson = new ParceJsonTask(json, getListView());
                parceJson.setProgressBar(view.findViewById(R.id.progressBar));
                parceJson.execute();
            }

        }
    }
}

package com.fanok.mdpu24v1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.dowland.DowlandJsonProject;
import com.fanok.mdpu24v1.dowland.ParceJsonProject;

import java.util.Objects;

public class TabProject extends TabStudentInfo {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferences mPref = Objects.requireNonNull(getActivity()).getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        final String url = getResources().getString(R.string.server_api) + "get_projets.php";
        DowlandJsonProject dowlandJson = new DowlandJsonProject(view, url, getListView(), getName());
        if (dowlandJson.isOnline()) {
            dowlandJson.setProgressBar(view.findViewById(R.id.progressBar));
            dowlandJson.setData("type", String.valueOf(TypeTimeTable.getType()));
            dowlandJson.setData("login", mPref.getString("login", ""));
            dowlandJson.execute();
        } else {
            String json = mPref.getString("Project_" + getName(), "");
            if (!json.isEmpty()) {
                ParceJsonProject parceJson = new ParceJsonProject(json, getListView(), getName());
                parceJson.setProgressBar(view.findViewById(R.id.progressBar));
                parceJson.execute();
            }

        }
    }
}

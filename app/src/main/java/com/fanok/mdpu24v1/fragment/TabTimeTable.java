package com.fanok.mdpu24v1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.Week;
import com.fanok.mdpu24v1.dowland.DowlandJson;
import com.fanok.mdpu24v1.dowland.ParceJson;

import java.util.ArrayList;
import java.util.Objects;

public class TabTimeTable extends Fragment {

    ArrayList<View> views = new ArrayList<>();
    private int day;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_table, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.day = bundle.getInt("day");
        }
        for (int i = 0; i < 5; i++) {
            String id = "para" + i;
            int resId = view.getResources().getIdentifier(id, "id", view.getContext().getPackageName());
            this.views.add(view.findViewById(resId));
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final String url = getResources().getString(R.string.server_api) + "get_time_table.php";
        super.onViewCreated(view, savedInstanceState);
        DowlandJson dowlandJson = new DowlandJson(view, url, views, day);
        if (dowlandJson.isOnline()) {
            dowlandJson.setProgressBar(view.findViewById(R.id.progressBar));
            dowlandJson.execute();
        } else {
            SharedPreferences mPref = Objects.requireNonNull(getActivity()).getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
            String json = mPref.getString(Week.getDayName(day), "");
            if (!json.isEmpty()) {
                ParceJson parceJson = new ParceJson(json, views);
                parceJson.setProgressBar(view.findViewById(R.id.progressBar));
                parceJson.execute();
            }

        }
    }
}

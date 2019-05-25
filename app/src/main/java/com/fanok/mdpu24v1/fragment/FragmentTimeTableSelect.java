package com.fanok.mdpu24v1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.activity.MainActivity;
import com.fanok.mdpu24v1.dowland.DowlandGroupsCurator;

import java.util.Objects;

public class FragmentTimeTableSelect extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final String url = getResources().getString(R.string.server_api) + "get_groups_curator.php";
        View view = inflater.inflate(R.layout.fragment_time_table_select, container, false);
        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);

        ListView listView = view.findViewById(R.id.listView);
        DowlandGroupsCurator dowland = new DowlandGroupsCurator(view, url, listView);
        if (dowland.isOnline()) {
            String login = mPref.getString("login", "");
            dowland.setData("login", login);
            dowland.setProgressBar(view.findViewById(R.id.progressBar));
            dowland.execute();
        } else
            Objects.requireNonNull((MainActivity) getActivity()).showMenuFragment(new FragmentTimeTable(), true);
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (i == 0) {
                TypeTimeTable.setType(view.getContext(), TypeTimeTable.teacherTimeTable);
            } else {
                TypeTimeTable.setType(view.getContext(), TypeTimeTable.curatorTimeTable);
                TypeTimeTable.setGroup(view.getContext(), adapterView.getItemAtPosition(i).toString());
            }
            Objects.requireNonNull((MainActivity) getActivity()).showMenuFragment(new FragmentTimeTable(), true);
        });
        return view;
    }
}


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
import com.fanok.mdpu24v1.dowland.DowlandGroupsCurator;

import java.util.Objects;

public class FragmentMarckSelect extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final String url = getResources().getString(R.string.server_api) + "get_groups_marks.php";
        View view = inflater.inflate(R.layout.fragment_time_table_select, container, false);
        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);

        ListView listView = view.findViewById(R.id.listView);
        DowlandGroupsCurator dowland = new DowlandGroupsCurator(view, url, listView);
        if (dowland.isOnline()) {
            String login = mPref.getString("login", "");
            dowland.setData("login", login);
            dowland.setData("type", String.valueOf(TypeTimeTable.getType()));
            dowland.setProgressBar(view.findViewById(R.id.progressBar));
            dowland.execute();
        } else startFragment();
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (TypeTimeTable.getType() == TypeTimeTable.curatorTimeTable && i == 0) {
                TypeTimeTable.setType(view.getContext(), TypeTimeTable.teacherTimeTable);
                startFragmentThis();
                return;
            }
            TypeTimeTable.setGroup(view.getContext(), adapterView.getItemAtPosition(i).toString());
            startFragment();
        });
        return view;
    }

    protected void startFragmentThis() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMarckSelect()).commit();
    }

    protected void startFragment() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMarks()).commit();
    }
}


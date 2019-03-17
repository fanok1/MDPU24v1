package com.fanok.mdpu24v1.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.activity.CuratorAddActivity;
import com.fanok.mdpu24v1.dowland.DowlandJsonCurator;
import com.fanok.mdpu24v1.dowland.ParceJsonCurator;

public class FragmentCurator extends android.support.v4.app.Fragment {


    int level;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curator, container, false);
        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("activity", 8);
        editor.apply();


        level = mPref.getInt("level", 0);
        String login = mPref.getString("login", "");
        setHasOptionsMenu(true);


        final String url = getResources().getString(R.string.server_api) + "get_curator_houers.php";

        ListView listView = view.findViewById(R.id.listView);

        DowlandJsonCurator dowlandJson = new DowlandJsonCurator(view, url, listView);
        if (dowlandJson.isOnline()) {
            dowlandJson.setProgressBar(view.findViewById(R.id.progressBar));
            dowlandJson.setData("level", String.valueOf(level));
            dowlandJson.setData("login", login);
            dowlandJson.execute();
        } else {
            String json = mPref.getString("Curator", "");
            if (!json.isEmpty()) {
                ParceJsonCurator parceJson = new ParceJsonCurator(json, listView);
                parceJson.setProgressBar(view.findViewById(R.id.progressBar));
                parceJson.execute();
            }

        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.plus_button_menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(menuItem -> {
            if (level != 4) return false;
            Intent intent = new Intent(getContext(), CuratorAddActivity.class);
            startActivity(intent);
            return false;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}


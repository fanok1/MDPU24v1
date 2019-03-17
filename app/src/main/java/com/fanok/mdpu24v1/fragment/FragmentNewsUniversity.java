package com.fanok.mdpu24v1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.dowland.DowlandNews;

public class FragmentNewsUniversity extends android.support.v4.app.Fragment {

    public static int offset = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_university, container, false);

        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("activity", 0);
        editor.apply();

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.refresh);
        ListView listView = view.findViewById(R.id.listView);
        dowland(view, progressBar, refreshLayout, listView);


        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            dowland(view, progressBar, refreshLayout, listView);
        });

        return view;
    }

    private void dowland(View view, ProgressBar progressBar, SwipeRefreshLayout refreshLayout, ListView listView) {
        final String url = getResources().getString(R.string.news_api);
        DowlandNews dowlandNews = new DowlandNews(view, url, listView);
        if (dowlandNews.isOnline()) {
            dowlandNews.clear();
            dowlandNews.setProgressBar(progressBar);
            dowlandNews.setRefreshLayout(refreshLayout);
            dowlandNews.setData("offset", "0");
            offset = 10;
            dowlandNews.execute();
        } else {
            Toast.makeText(view.getContext(), getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }


}

package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public abstract class DowladParent extends AsyncTask<Void, Void, Void> {

    private String url;
    private HashMap<String, String> data = new HashMap<>();
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private View view;
    @SuppressLint("StaticFieldLeak")
    private SwipeRefreshLayout refreshLayout;

    DowladParent(@NonNull View view, @NonNull String url) {

        this.url = url;
        this.view = view;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(@NonNull ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public View getView() {
        return view;
    }

    public String getUrl() {
        return url;
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public void setRefreshLayout(@NonNull SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(@NonNull HashMap<String, String> data) {
        this.data = data;
    }

    public void setData(@NonNull String key, @NonNull String value) {
        this.data.put(key, value);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressBar != null) progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (refreshLayout != null) refreshLayout.setRefreshing(false);
        if (progressBar != null) progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Connection connection = Jsoup.connect(url);
        if (data.size() != 0) connection.data(data);
        connection.method(Connection.Method.POST);
        connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
        try {
            Document data = connection.post();
            parce(data);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    protected void parce(Document data) {
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

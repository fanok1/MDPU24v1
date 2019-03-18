package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.fanok.mdpu24v1.StartActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DowlandLevel extends AsyncTask<Void, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String url;
    private String login;

    public DowlandLevel(@NonNull Context context, @NonNull String url, @NonNull String login) {
        this.context = context;
        this.url = url;
        this.login = login;
    }


    @Override
    protected String doInBackground(Void... voids) {
        Connection connection = Jsoup.connect(url);
        connection.method(Connection.Method.POST);
        connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
        connection.data("login", login);
        try {
            Document data = connection.post();
            return data.getElementsByTag("body").text();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        SharedPreferences preferences = context.getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("level", Integer.parseInt(s));
        editor.apply();
        super.onPostExecute(s);
    }
}

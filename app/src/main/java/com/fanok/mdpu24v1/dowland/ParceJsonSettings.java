package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ProgressBar;

import com.fanok.mdpu24v1.MySettings;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.TimeTable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ParceJsonSettings extends AsyncTask<Void, Void, MySettings> {

    private String json;
    @SuppressLint("StaticFieldLeak")
    private View view;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar progressBar;

    ParceJsonSettings(String json, View view) {
        this.json = json;
        this.view = view;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressBar != null) progressBar.setVisibility(ProgressBar.VISIBLE);

    }

    @Override
    protected MySettings doInBackground(Void... voids) {
        ArrayList<TimeTable> timeTables = new ArrayList<>();
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject jsonSettings = jsonElement.getAsJsonObject();
        MySettings settings = new MySettings();
        settings.setLogin(jsonSettings.get("login").getAsString());
        settings.setName(jsonSettings.get("name").getAsString());
        settings.setPassword("password");
        settings.setPhone(jsonSettings.get("phone").getAsString());
        settings.setEmail(jsonSettings.get("email").getAsString());
        settings.setPhoto(jsonSettings.get("photo").getAsString());

        return settings;
    }

    @Override
    protected void onPostExecute(MySettings settings) {
        super.onPostExecute(settings);

        TextInputEditText login = view.findViewById(R.id.login);
        TextInputEditText name = view.findViewById(R.id.name);
        TextInputEditText password = view.findViewById(R.id.password);
        TextInputEditText phone = view.findViewById(R.id.phone);
        TextInputEditText email = view.findViewById(R.id.email);
        TextInputEditText photo = view.findViewById(R.id.photo);

        login.setText(settings.getLogin());
        name.setText(settings.getName());
        password.setText(settings.getPassword());
        phone.setText(settings.getPhone());
        email.setText(settings.getEmail());
        photo.setText(settings.getPhoto());

        if (progressBar != null) progressBar.setVisibility(ProgressBar.GONE);
    }
}
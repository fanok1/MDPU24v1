package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.fanok.mdpu24v1.activity.MainActivity;

public class InsertDataInSqlConfirmStudent extends InsertDataInSql {

    @SuppressLint("StaticFieldLeak")
    private MainActivity activity;

    public InsertDataInSqlConfirmStudent(@NonNull View view, @NonNull String url, MainActivity activity) {
        super(view, url);
        this.activity = activity;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Fragment frg = activity.getSupportFragmentManager().findFragmentByTag(String.valueOf(MainActivity.countFragmentIntoStack));
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}

package com.fanok.mdpu24v1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;


public class AddAbsenteeismActivity extends AddMarksActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutPredmet().setVisibility(View.GONE);
        getMark().setHint(R.string.para);
        getLayoutMark().setHint(getMark().getContext().getResources().getString(R.string.para));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onClick(View view) {
        String modul;
        if (getModul1().isChecked()) modul = "0";
        else modul = "1";
        if (getName().getText().toString().isEmpty()) {
            getLayoutName().setErrorEnabled(true);
            getLayoutName().setError(view.getResources().getString(R.string.error_required));
        }
        if (getDate().getText().toString().isEmpty()) {
            getLayoutDate().setErrorEnabled(true);
            getLayoutDate().setError(view.getResources().getString(R.string.error_required));
        }
        if (getMark().getText().toString().isEmpty() || Integer.parseInt(getMark().getText().toString()) < 1 || Integer.parseInt(getMark().getText().toString()) > 4) {
            getLayoutMark().setErrorEnabled(true);
            getLayoutMark().setError(view.getResources().getString(R.string.error_incorrect_data));
        }
        if (getLayoutName().isErrorEnabled() || getLayoutDate().isErrorEnabled() || getLayoutMark().isErrorEnabled())
            return;

        String url = view.getContext().getResources().getString(R.string.server_api) + "add_absenteeism.php";
        InsertDataInSql sql = new InsertDataInSql(view, url);
        if (sql.isOnline()) {
            sql.setData("name", getName().getText().toString());
            sql.setData("date", getDate().getText().toString());
            sql.setData("para", getMark().getText().toString());
            sql.setData("modul", modul);
            sql.execute();
        } else {
            Toast.makeText(this, view.getResources().getText(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
        }


    }
}

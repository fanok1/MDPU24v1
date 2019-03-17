package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.r0adkll.slidr.Slidr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CuratorAddActivity extends ResetPaswordActivity {
    private ProgressBar prograsBar;
    private TextInputLayout layoutTask;
    private TextInputLayout layoutDate;
    private TextInputEditText task;
    private TextInputEditText date;


    @Override
    protected void onResume() {
        super.onResume();
        date.setText(RegistrationActivity.groupName);
        RegistrationActivity.groupName = "";
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curator_add);
        RegistrationActivity.groupName = "";
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        layoutTask = findViewById(R.id.layoutTask);
        layoutDate = findViewById(R.id.layoutDate);
        task = findViewById(R.id.task);
        date = findViewById(R.id.date);
        prograsBar = findViewById(R.id.progress);
        Button button = findViewById(R.id.button);

        date.setShowSoftInputOnFocus(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        date.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PopupDatePicker.class);
            startActivity(intent);
        });
        date.setOnFocusChangeListener((view, b) -> {
            if (b) {
                layoutDate.setErrorEnabled(false);
                Intent intent = new Intent(view.getContext(), PopupDatePicker.class);
                startActivity(intent);
            } else {
                Date dateEnd = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("ua"));
                try {
                    dateEnd = dateFormat.parse(date.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dateEnd == null || date.getText().toString().isEmpty() || dateEnd.before(new Date())) {
                    layoutDate.setErrorEnabled(true);
                    layoutDate.setError(view.getResources().getString(R.string.error_date));
                }
            }
        });

        task.setOnFocusChangeListener((view, b) -> {
            if (b) {
                layoutTask.setErrorEnabled(false);
            } else {
                if (task.getText().toString().isEmpty()) {
                    layoutTask.setErrorEnabled(true);
                    layoutTask.setError(view.getResources().getString(R.string.error_required));
                }
            }
        });


        button.setOnClickListener(view -> {
            Date dateEnd = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("ua"));
            try {
                dateEnd = dateFormat.parse(date.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (dateEnd == null || date.getText().toString().isEmpty() || dateEnd.before(new Date())) {
                layoutDate.setErrorEnabled(true);
                layoutDate.setError(view.getResources().getString(R.string.error_date));
            }
            if (task.getText().toString().isEmpty()) {
                layoutTask.setErrorEnabled(true);
                layoutTask.setError(view.getResources().getString(R.string.error_required));
            }
            if (layoutTask.isErrorEnabled() || layoutDate.isErrorEnabled())
                return;

            String url = view.getResources().getString(R.string.server_api) + "curator_houers_add.php";
            InsertDataInSql inSql = new InsertDataInSql(view, url);
            SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
            String login = mPref.getString("login", "");

            if (inSql.isOnline()) {
                inSql.setData("login", login);
                assert dateEnd != null;
                inSql.setData("date", String.valueOf(dateEnd.getTime()));
                inSql.setData("task", task.getText().toString());
                inSql.setProgressBar(prograsBar);
                inSql.execute();
            } else {
                Toast.makeText(view.getContext(), view.getResources().getText(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}


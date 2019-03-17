package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.r0adkll.slidr.Slidr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TaskAddActivity extends ResetPaswordActivity {
    private ProgressBar prograsBar;
    private boolean firstFocus = false;

    private TextInputLayout layoutGroup;
    private TextInputLayout layoutName;
    private TextInputLayout layoutTask;
    private TextInputLayout layoutDate;

    private TextInputEditText group;
    private TextInputEditText name;
    private TextInputEditText task;
    private TextInputEditText date;

    private int n;

    @Override
    protected void onResume() {
        super.onResume();

        switch (n) {
            case 1:
                group.setText(RegistrationActivity.groupName);
                TypeTimeTable.setGroup(RegistrationActivity.groupName);
                break;
            case 2:
                name.setText(RegistrationActivity.groupName);
                break;
            case 3:
                date.setText(RegistrationActivity.groupName);
                break;
        }

        n = 0;
        RegistrationActivity.groupName = "";
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        n = 0;
        RegistrationActivity.groupName = "";
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        layoutGroup = findViewById(R.id.layoutGroup);
        layoutName = findViewById(R.id.layoutName);
        layoutTask = findViewById(R.id.layoutTask);
        layoutDate = findViewById(R.id.layoutDate);
        group = findViewById(R.id.group);
        name = findViewById(R.id.name);
        task = findViewById(R.id.task);
        date = findViewById(R.id.date);
        prograsBar = findViewById(R.id.progress);
        Button button = findViewById(R.id.button);


        group.setShowSoftInputOnFocus(false);
        name.setShowSoftInputOnFocus(false);
        date.setShowSoftInputOnFocus(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        group.setOnFocusChangeListener((view, b) -> {
            if (!firstFocus) {
                firstFocus = true;
                return;
            }
            if (b) {
                layoutGroup.setErrorEnabled(false);
                showPopup(1, view, getResources().getString(R.string.server_api) + "get_groups_student.php");
            } else {
                if (group.getText().toString().isEmpty()) {
                    layoutGroup.setErrorEnabled(true);
                    layoutGroup.setError(view.getResources().getString(R.string.error_required));
                }
            }
        });
        group.setOnClickListener(view -> showPopup(1, view, getResources().getString(R.string.server_api) + "get_groups_student.php"));

        name.setOnFocusChangeListener((view, b) -> {
            if (b) {
                if (group.getText().toString().isEmpty()) {
                    layoutName.setErrorEnabled(true);
                    layoutName.setError(view.getResources().getString(R.string.error_group_is_empty));
                } else {
                    layoutName.setErrorEnabled(false);
                    showPopup(2, view, getResources().getString(R.string.server_api) + "get_student.php");
                }
            } else {
                if (name.getText().toString().isEmpty()) {
                    layoutName.setErrorEnabled(true);
                    layoutName.setError(view.getResources().getString(R.string.error_required));
                }
            }
        });
        name.setOnClickListener(view -> {
            if (group.getText().toString().isEmpty()) {
                layoutName.setErrorEnabled(true);
                layoutName.setError(view.getResources().getString(R.string.error_group_is_empty));
            } else {
                layoutName.setErrorEnabled(false);
                showPopup(2, view, getResources().getString(R.string.server_api) + "get_student.php");
            }
        });

        date.setOnClickListener(view -> showPopup(3, view, ""));
        date.setOnFocusChangeListener((view, b) -> {
            if (b) {
                layoutDate.setErrorEnabled(false);
                showPopup(3, view, "");
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
            if (name.getText().toString().isEmpty()) {
                layoutName.setErrorEnabled(true);
                layoutName.setError(view.getResources().getString(R.string.error_required));
            }
            if (dateEnd == null || date.getText().toString().isEmpty() || dateEnd.before(new Date())) {
                layoutDate.setErrorEnabled(true);
                layoutDate.setError(view.getResources().getString(R.string.error_date));
            }
            if (task.getText().toString().isEmpty()) {
                layoutTask.setErrorEnabled(true);
                layoutTask.setError(view.getResources().getString(R.string.error_required));
            }
            if (layoutName.isErrorEnabled() || layoutTask.isErrorEnabled() || layoutDate.isErrorEnabled())
                return;

            String url = view.getResources().getString(R.string.server_api) + "task_add.php";
            InsertDataInSql inSql = new InsertDataInSql(view, url);

            if (inSql.isOnline()) {
                inSql.setData("name", name.getText().toString());
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


    private void showPopup(int n, View view, String url) {
        this.n = n;
        Intent intent;
        if (n != 3) {
            intent = new Intent(view.getContext(), PopupGroupSearchActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("type", "");
        } else intent = new Intent(view.getContext(), PopupDatePicker.class);
        startActivity(intent);
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


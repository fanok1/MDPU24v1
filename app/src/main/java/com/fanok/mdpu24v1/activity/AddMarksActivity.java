package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.fanok.mdpu24v1.fragment.FragmentMarks;
import com.r0adkll.slidr.Slidr;


public class AddMarksActivity extends AppCompatActivity {

    private static final int PREDMET = 0;
    private static final int NAME = 1;
    private static final int DATE = 2;
    private android.support.design.widget.TextInputEditText predmet;
    private android.support.design.widget.TextInputEditText name;
    private android.support.design.widget.TextInputEditText date;
    private android.support.design.widget.TextInputEditText mark;
    private RadioButton modul1;
    private TextInputLayout layoutPredmet;
    private TextInputLayout layoutName;
    private TextInputLayout layoutDate;
    private TextInputLayout layoutMark;
    private int flag;

    public android.support.design.widget.TextInputEditText getName() {
        return name;
    }

    public android.support.design.widget.TextInputEditText getDate() {
        return date;
    }

    public android.support.design.widget.TextInputEditText getMark() {
        return mark;
    }

    public RadioButton getModul1() {
        return modul1;
    }

    public TextInputLayout getLayoutPredmet() {
        return layoutPredmet;
    }

    public TextInputLayout getLayoutName() {
        return layoutName;
    }

    public TextInputLayout getLayoutDate() {
        return layoutDate;
    }

    public TextInputLayout getLayoutMark() {
        return layoutMark;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_marks_layout);
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RegistrationActivity.groupName = getIntent().getStringExtra("predmet");

        predmet = findViewById(R.id.predmet);
        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        mark = findViewById(R.id.mark);
        layoutPredmet = findViewById(R.id.layoutPredmet);
        layoutName = findViewById(R.id.layoutName);
        layoutDate = findViewById(R.id.layoutDate);
        layoutMark = findViewById(R.id.layoutMark);
        modul1 = findViewById(R.id.modul1);
        Button button = findViewById(R.id.add_mark);

        predmet.setKeyListener(null);
        name.setKeyListener(null);
        date.setKeyListener(null);


        mark.requestFocus();

        predmet.setOnClickListener(this::showPredmetSelector);
        predmet.setOnFocusChangeListener((view, b) -> {
            if (b) {
                layoutPredmet.setErrorEnabled(false);
                showPredmetSelector(view);
            } else {
                if (predmet.getText().toString().isEmpty()) {
                    layoutPredmet.setErrorEnabled(true);
                    layoutPredmet.setError(view.getResources().getString(R.string.error_required));
                }
            }
        });


        name.setOnClickListener(this::showNemeSelector);
        name.setOnFocusChangeListener((view, b) -> {
            if (b) {
                layoutName.setErrorEnabled(false);
                showNemeSelector(view);
            } else {
                if (name.getText().toString().isEmpty()) {
                    layoutName.setErrorEnabled(true);
                    layoutName.setError(view.getResources().getString(R.string.error_required));
                }
            }
        });

        date.setOnClickListener(this::showDateSelector);
        date.setOnFocusChangeListener((view, b) -> {
            if (b) {
                layoutDate.setErrorEnabled(false);
                showDateSelector(view);
            } else {
                if (date.getText().toString().isEmpty()) {
                    layoutDate.setErrorEnabled(true);
                    layoutDate.setError(view.getResources().getString(R.string.error_required));
                }
            }
        });

        mark.setOnFocusChangeListener((view, b) -> {
            if (b) layoutMark.setErrorEnabled(false);
            else {
                if (mark.getText().toString().isEmpty() || Integer.parseInt(mark.getText().toString()) < 1 || Integer.parseInt(mark.getText().toString()) > 5) {
                    layoutMark.setErrorEnabled(true);
                    layoutMark.setError(view.getResources().getString(R.string.error_incorrect_data));
                }
            }
        });

        mark.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_ENDCALL) {
                onClick(textView);
            }
            return false;
        });

        button.setOnClickListener(this::onClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentMarks.flag = true;
        switch (flag) {
            case PREDMET:
                predmet.setText(RegistrationActivity.groupName);
                break;
            case NAME:
                name.setText(RegistrationActivity.groupName);
                break;
            case DATE:
                date.setText(RegistrationActivity.groupName);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onClick(View view) {
        String modul;
        if (modul1.isChecked()) modul = "0";
        else modul = "1";
        if (predmet.getText().toString().isEmpty()) {
            layoutPredmet.setErrorEnabled(true);
            layoutPredmet.setError(view.getResources().getString(R.string.error_required));
        }
        if (name.getText().toString().isEmpty()) {
            layoutName.setErrorEnabled(true);
            layoutName.setError(view.getResources().getString(R.string.error_required));
        }
        if (date.getText().toString().isEmpty()) {
            layoutDate.setErrorEnabled(true);
            layoutDate.setError(view.getResources().getString(R.string.error_required));
        }
        if (mark.getText().toString().isEmpty() || Integer.parseInt(mark.getText().toString()) < 1 || Integer.parseInt(mark.getText().toString()) > 5) {
            layoutMark.setErrorEnabled(true);
            layoutMark.setError(view.getResources().getString(R.string.error_incorrect_data));
        }
        if (layoutPredmet.isErrorEnabled() || layoutName.isErrorEnabled() || layoutDate.isErrorEnabled() || layoutMark.isErrorEnabled())
            return;

        String url = view.getContext().getResources().getString(R.string.server_api) + "update_marks.php";
        InsertDataInSql sql = new InsertDataInSql(view, url);
        if (sql.isOnline()) {
            sql.setData("predmet", predmet.getText().toString());
            sql.setData("name", name.getText().toString());
            sql.setData("date", date.getText().toString());
            sql.setData("mark", mark.getText().toString());
            sql.setData("modul", modul);
            sql.execute();
        } else {
            Toast.makeText(this, view.getResources().getText(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
        }


    }

    private void showPredmetSelector(View view) {
        flag = PREDMET;
        final String url = getResources().getString(R.string.server_api) + "get_marks_predmet.php";
        Intent intent = new Intent(view.getContext(), PopupGroupSearchActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("type", TypeTimeTable.getType());
        startActivity(intent);
    }

    private void showNemeSelector(View view) {
        flag = NAME;
        final String url = getResources().getString(R.string.server_api) + "get_students_list.php";
        Intent intent = new Intent(view.getContext(), PopupGroupSearchActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("type", "");
        startActivity(intent);
    }

    private void showDateSelector(View view) {
        flag = DATE;
        Intent intent = new Intent(view.getContext(), PopupDatePicker.class);
        startActivity(intent);
    }

}

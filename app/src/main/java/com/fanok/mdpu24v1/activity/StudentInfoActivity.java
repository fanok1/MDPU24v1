package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.fanok.mdpu24v1.R;
import com.r0adkll.slidr.Slidr;

import java.util.Objects;


public class StudentInfoActivity extends ResetPaswordActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        android.support.design.widget.TextInputEditText name = findViewById(R.id.name);
        android.support.design.widget.TextInputEditText email = findViewById(R.id.email);
        android.support.design.widget.TextInputEditText phone = findViewById(R.id.phone);
        Bundle arguments = getIntent().getExtras();
        name.setKeyListener(null);
        email.setKeyListener(null);
        phone.setKeyListener(null);
        if (arguments != null) {
            name.setText(Objects.requireNonNull(arguments.get("name")).toString());
            email.setText(Objects.requireNonNull(arguments.get("email")).toString());
            phone.setText(Objects.requireNonNull(arguments.get("phone")).toString());
        }

        email.setOnFocusChangeListener((view, b) -> {
            if (b) {
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode(String.valueOf(email.getText()));
                Uri uri = Uri.parse(uriText);
                send.setData(uri);
                startActivity(Intent.createChooser(send, "Отправка письма..."));
                email.clearFocus();
            }
        });

        phone.setOnFocusChangeListener((view, b) -> {
            if (b) {
                String dial = "tel:" + phone.getText();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                phone.clearFocus();
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
}


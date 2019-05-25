package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.activity.setings.ChangeSetingsParent;
import com.fanok.mdpu24v1.activity.setings.ChangeSetingsPassword;
import com.fanok.mdpu24v1.activity.setings.ChangeSetingsPhoto;
import com.fanok.mdpu24v1.dowland.DowlandJsonSettings;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.google.firebase.auth.FirebaseAuth;
import com.r0adkll.slidr.Slidr;


public class SetingsActivity extends AppCompatActivity {


    private ProgressBar prograsBar;
    private boolean firstFocus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        findViewById(R.id.button).setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
            String url = getResources().getString(R.string.server_api) + "delete_token.php";
            InsertDataInSql inSql = new InsertDataInSql(view, url);
            String token = preferences.getString("token", "");
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            if (inSql.isOnline()) {
                inSql.setData("token", token);
                inSql.execute();
            }
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        TextInputEditText login = findViewById(R.id.login);
        TextInputEditText name = findViewById(R.id.name);
        TextInputEditText password = findViewById(R.id.password);
        TextInputEditText phone = findViewById(R.id.phone);
        TextInputEditText email = findViewById(R.id.email);
        TextInputEditText photo = findViewById(R.id.photo);

        prograsBar = findViewById(R.id.progressBar);

        login.setKeyListener(null);
        name.setKeyListener(null);
        password.setKeyListener(null);
        phone.setKeyListener(null);
        email.setKeyListener(null);
        photo.setKeyListener(null);

        login.setOnFocusChangeListener((view, b) -> {
            if (b && !firstFocus) {
                firstFocus = !firstFocus;
                start(new Intent(view.getContext(), ChangeSetingsParent.class), getResources().getString(R.string.decription_change_login), "set_login.php", login.getText().toString(), InputType.TYPE_CLASS_TEXT, "login", "^\\w{1,20}$", "Имя пользвателя");
            }
        });
        login.setOnClickListener(view -> start(new Intent(view.getContext(), ChangeSetingsParent.class), getResources().getString(R.string.decription_change_login), "set_login.php", login.getText().toString(), InputType.TYPE_CLASS_TEXT, "login", "^\\w{1,20}$", "Имя пользвателя"));

        name.setOnFocusChangeListener((view, b) -> {
            if (b) {
                start(new Intent(view.getContext(), ChangeSetingsParent.class), getResources().getString(R.string.decription_change_name), "set_name.php", name.getText().toString(), InputType.TYPE_CLASS_TEXT, "name", "^[А-ЯІЄЇ][а-яієї]+ ([А-ЯІЄЇ]\\.){2}$", "Имя");
            }
        });

        name.setOnClickListener(view -> start(new Intent(view.getContext(), ChangeSetingsParent.class), getResources().getString(R.string.decription_change_name), "set_name.php", name.getText().toString(), InputType.TYPE_CLASS_TEXT, "name", "^[А-ЯІЄЇ][а-яієї]+ ([А-ЯІЄЇ]\\.){2}$", "Имя"));

        password.setOnFocusChangeListener((view, b) -> {
            if (b) {
                password.setText("");
                start(new Intent(view.getContext(), ChangeSetingsPassword.class), getResources().getString(R.string.decription_change_password), "set_password.php", password.getText().toString(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, "", "^\\w{6,}$", "Пароль");
            }
        });

        password.setOnClickListener(view -> {
            password.setText("");
            start(new Intent(view.getContext(), ChangeSetingsPassword.class), getResources().getString(R.string.decription_change_password), "set_password.php", password.getText().toString(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, "", "^\\w{6,}$", "Пароль");
        });

        phone.setOnFocusChangeListener((view, b) -> {
            if (b) {
                start(new Intent(view.getContext(), ChangeSetingsParent.class), getResources().getString(R.string.decription_change_phone), "set_phone.php", phone.getText().toString(), InputType.TYPE_CLASS_PHONE, "", "^\\+380(39|67|68|96|97|98|50|66|95|99|63|93|91|92|94)\\d{7}$", "Телефон");
            }
        });

        phone.setOnClickListener(view -> start(new Intent(view.getContext(), ChangeSetingsParent.class), getResources().getString(R.string.decription_change_phone), "set_phone.php", phone.getText().toString(), InputType.TYPE_CLASS_PHONE, "", "^\\+380(39|67|68|96|97|98|50|66|95|99|63|93|91|92|94)\\d{7}$", "Телефон"));

        email.setOnFocusChangeListener((view, b) -> {
            if (b) {
                start(new Intent(view.getContext(), ChangeSetingsParent.class), getResources().getString(R.string.decription_change_email), "set_email.php", email.getText().toString(), InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, "", "^[a-zA-Z0-9.-_]+@([a-z]+\\.+)+[a-z]+$", "Email");
            }
        });

        email.setOnClickListener(view -> start(new Intent(view.getContext(), ChangeSetingsParent.class), getResources().getString(R.string.decription_change_email), "set_email.php", email.getText().toString(), InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, "", "^[a-zA-Z0-9.-_]+@([a-z]+\\.+)+[a-z]+$", "Email"));

        photo.setOnFocusChangeListener((view, b) -> {
            if (b && prograsBar.getVisibility() != View.VISIBLE)
                startActivity(new Intent(view.getContext(), ChangeSetingsPhoto.class));
        });

        photo.setOnClickListener(view -> {
            if (prograsBar.getVisibility() != View.VISIBLE) {
                startActivity(new Intent(view.getContext(), ChangeSetingsPhoto.class));
            }
        });
    }

    private void start(@NonNull Intent intent, @NonNull String description, @NonNull String url, @NonNull String value, int inputType, @NonNull String name, @NonNull String pattern, @NonNull String hint) {
        if (prograsBar.getVisibility() == View.VISIBLE) return;
        intent.putExtra("description", description);
        intent.putExtra("url", getResources().getString(R.string.server_api) + url);
        intent.putExtra("value", value);
        intent.putExtra("inputType", inputType);
        intent.putExtra("name", name);
        intent.putExtra("pattern", pattern);
        intent.putExtra("hint", hint);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstFocus = false;
        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        String loginName = mPref.getString("login", "");
        String url = getResources().getString(R.string.server_api) + "get_settings.php";
        DowlandJsonSettings dowlandJson = new DowlandJsonSettings(findViewById(R.id.parent), url);
        if (dowlandJson.isOnline()) {
            dowlandJson.setData("login", loginName);
            dowlandJson.setProgressBar(prograsBar);
            dowlandJson.execute();
        } else {
            Toast.makeText(this, getResources().getText(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

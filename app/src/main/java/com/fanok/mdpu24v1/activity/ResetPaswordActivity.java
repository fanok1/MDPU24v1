package com.fanok.mdpu24v1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.r0adkll.slidr.Slidr;


public class ResetPaswordActivity extends AppCompatActivity {

    private static final String patterm = "^([a-zA-Z0-9.-_]+@([a-z]+\\.+)+[a-z]+)|((\\+?38)?0(39|67|68|96|97|98|50|66|95|99|63|93|91|92|94)\\d{7})|(\\w+)$";
    private android.support.design.widget.TextInputEditText mEmail;
    private android.support.design.widget.TextInputEditText mPassword;
    private android.support.design.widget.TextInputEditText mPasswordConfirm;
    private TextInputLayout layoutPasswordConfirm;
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;

    public static void empty(String text, TextInputLayout layout, String error) {
        if (!text.matches("^\\w+$")) {
            layout.setErrorEnabled(true);
            layout.setError(error);
        }
    }

    public String getEmail() {
        return mEmail.getText().toString();
    }

    public String getPassword() {
        return mPassword.getText().toString();
    }

    public String getPasswordConfirm() {
        return mPasswordConfirm.getText().toString();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        mPassword = findViewById(R.id.passwordReset);
        mPasswordConfirm = findViewById(R.id.passwordConfirmReset);
        mEmail = findViewById(R.id.emailReset);
        layoutPassword = findViewById(R.id.layoutPasswordReset);
        layoutPasswordConfirm = findViewById(R.id.layoutPasswordConfirmReset);
        layoutEmail = findViewById(R.id.layoutEmailReset);
        Button button = findViewById(R.id.reset_passwoed);


        mPasswordConfirm.setOnFocusChangeListener((view, b) -> RegistrationActivity.equalsPassword(b, getPasswordConfirm(), getPassword(), layoutPasswordConfirm, getResources().getString(R.string.error_incorrect_password_confirm)));
        mPassword.setOnFocusChangeListener((View view, boolean b) -> RegistrationActivity.editTextEmpty(b, getPassword(), layoutPassword, getResources().getString(R.string.error_incorrect_data)));
        mEmail.setOnFocusChangeListener((View view, boolean b) -> RegistrationActivity.checkPatern(b, getEmail(), patterm, layoutEmail, getResources().getString(R.string.error_incorrect_data)));


        button.setOnClickListener(this::onClick);
        mPasswordConfirm.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_ENDCALL) {
                onClick(textView);
            }
            return false;
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClick(View view) {
        final String url = getResources().getString(R.string.server_api) + "reset_password.php";
        empty(getPassword(), layoutPassword, getResources().getString(R.string.error_incorrect_data));
        empty(getPasswordConfirm(), layoutPasswordConfirm, getResources().getString(R.string.error_incorrect_data));
        empty(getEmail(), layoutEmail, getResources().getString(R.string.error_incorrect_data));
        if (layoutPassword.isErrorEnabled() || layoutPasswordConfirm.isErrorEnabled() || layoutEmail.isErrorEnabled())
            return;
        InsertDataInSql inSql = new InsertDataInSql(view, url);
        inSql.setData("login", getEmail());
        inSql.setData("password", getPassword());
        inSql.setProgressBar(view.findViewById(R.id.progressBar));
        if (inSql.isOnline()) {
            inSql.execute();
        } else
            Toast.makeText(this, getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
    }
}

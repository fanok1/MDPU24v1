package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RegistrationActivity extends ResetPaswordActivity {
    public static String groupName;
    private Button mButtonRegistration;
    private android.support.design.widget.TextInputEditText mLogin;
    private android.support.design.widget.TextInputEditText mFirstName;
    private android.support.design.widget.TextInputEditText mPassword;
    private android.support.design.widget.TextInputEditText mPasswordConfirm;
    private android.support.design.widget.TextInputEditText mPhone;
    private android.support.design.widget.TextInputEditText mEmail;
    private android.support.design.widget.TextInputEditText mGroup;
    private TextInputLayout layoutFirstName;
    private TextInputLayout layoutPasswordConfirm;
    private TextInputLayout layoutPhone;
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutGroup;
    private List<android.support.design.widget.TextInputEditText> editTextList;
    private List<TextInputLayout> layoutList;
    private String error;
    private String type;
    private String photo;

    public static void equalsPassword(boolean b, String passwordConfirm, String password, TextInputLayout layout, String error) {
        if (!b && (!passwordConfirm.equals(password) || password.length() < 6)) {
            layout.setErrorEnabled(true);
            layout.setError(error);
        } else layout.setErrorEnabled(false);
    }

    public static void editTextEmpty(boolean b, String text, TextInputLayout layout, String error) {
        if (!b && !text.matches("^[A-Za-z0-9._-]+$")) {
            layout.setErrorEnabled(true);
            layout.setError(error);
        } else layout.setErrorEnabled(false);
    }

    public static void checkPatern(boolean b, String text, String patterm, TextInputLayout layout, String error) {
        if (!b && !text.trim().matches(patterm)) {
            layout.setErrorEnabled(true);
            layout.setError(error);
        } else layout.setErrorEnabled(false);
    }

    public static String convertPhoneFormat(String phone) {
        if (phone.length() == 12) return "+" + phone;
        else if (phone.length() == 10) return "+38" + phone;
        else return phone;
    }

    public String getLogin() {
        return mLogin.getText().toString();
    }

    public String getFirstName() {
        return mFirstName.getText().toString();
    }

    public String getPassword() {
        return mPassword.getText().toString();
    }

    public String getPasswordConfirm() {
        return mPasswordConfirm.getText().toString();
    }

    public String getPhone() {
        return mPhone.getText().toString();
    }

    public String getEmail() {
        return mEmail.getText().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGroup.setText(groupName);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupName = "";
        setContentView(R.layout.activity_registration);
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        initVar();

        init();


    }

    private void initVar() {

        mButtonRegistration = findViewById(R.id.registration_button);

        mLogin = findViewById(R.id.login);
        mFirstName = findViewById(R.id.firstName);
        mPassword = findViewById(R.id.password);
        mPasswordConfirm = findViewById(R.id.passwordConfirm);
        mPhone = findViewById(R.id.phone);
        mEmail = findViewById(R.id.email);
        mGroup = findViewById(R.id.group);
        layoutFirstName = findViewById(R.id.layoutFirstName);
        TextInputLayout layoutLogin = findViewById(R.id.layoutLogin);
        TextInputLayout layoutPassword = findViewById(R.id.layoutPassword);
        layoutPasswordConfirm = findViewById(R.id.layoutPasswordConfirm);
        layoutPhone = findViewById(R.id.layoutPhone);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutGroup = findViewById(R.id.layoutGroup);

        error = getResources().getString(R.string.error_incorrect_data);

        editTextList = new ArrayList<>();
        layoutList = new ArrayList<>();


        editTextList.add(mLogin);
        editTextList.add(mPassword);
        editTextList.add(mPasswordConfirm);

        layoutList.add(layoutLogin);
        layoutList.add(layoutPassword);
        layoutList.add(layoutPasswordConfirm);
        layoutList.add(layoutFirstName);
        layoutList.add(layoutPhone);
        layoutList.add(layoutEmail);
        layoutList.add(layoutGroup);

        groupName = "";

    }

    private void init() {


        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            mLogin.setText(Objects.requireNonNull(arguments.get("login")).toString());
            type = Objects.requireNonNull(arguments.get("type")).toString();
            if (arguments.get("email") != null) {
                mEmail.setText(Objects.requireNonNull(arguments.get("email")).toString());
                mEmail.setEnabled(false);
            }
            if (arguments.get("photo") != null) {
                photo = Objects.requireNonNull(arguments.get("photo")).toString();
            } else photo = "";

        }


        mGroup.setKeyListener(null);

        mButtonRegistration.setOnClickListener(this::onClick);

        for (int i = 0; i < editTextList.size(); i++) {
            android.support.design.widget.TextInputEditText editText = editTextList.get(i);
            TextInputLayout layout = layoutList.get(i);
            editText.setOnFocusChangeListener((View view, boolean b) -> editTextEmpty(b, editText.getText().toString(), layout, error));
        }

        mFirstName.setOnFocusChangeListener((view, b) -> checkPatern(b, getFirstName(), "^[А-Я][а-я]+ [А-Я]\\.[А-Я]\\.$", layoutFirstName, error));


        mGroup.setOnFocusChangeListener((View view, boolean b) -> {
            if (b) {
                showPopup(view);
            } else {
                if (mGroup.getText().toString().isEmpty()) layoutGroup.setErrorEnabled(true);
                else layoutGroup.setErrorEnabled(false);
            }
        });

        mPasswordConfirm.setOnFocusChangeListener((View view, boolean b) -> equalsPassword(b, getPasswordConfirm(), getPassword(), layoutPasswordConfirm, getResources().getString(R.string.error_incorrect_password_confirm)));

        mPhone.setOnFocusChangeListener((View view, boolean b) -> {
            String patterm = "^(\\+?38)?0(39|67|68|96|97|98|50|66|95|99|63|93|91|92|94)\\d{7}$";
            checkPatern(b, getPhone(), patterm, layoutPhone, getResources().getString(R.string.error_incorrect_phone));
            if (!b || !layoutPhone.isErrorEnabled()) mPhone.setText(convertPhoneFormat(getPhone()));
        });


        mEmail.setOnFocusChangeListener((View view, boolean b) -> {
            String patterm = "^[a-zA-Z0-9.-_]+@([a-z]+\\.+)+[a-z]+$";
            checkPatern(b, getEmail(), patterm, layoutEmail, getResources().getString(R.string.error_incorrect_email));
        });


        mGroup.setOnClickListener(this::showPopup);

        mEmail.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_ENDCALL) {
                onClick(textView);
            }
            return false;
        });
    }

    private void showPopup(View view) {
        final String url = getResources().getString(R.string.server_api) + "groups_get.php";
        Intent intent = new Intent(view.getContext(), PopupGroupSearchActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("type", "");
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClick(View view) {
        final String url = getResources().getString(R.string.server_api) + "user_add.php";
        for (int i = 0; i < editTextList.size(); i++) {
            empty(editTextList.get(i).getText().toString(), layoutList.get(i), error);
        }
        if (groupName.isEmpty()) layoutGroup.setErrorEnabled(true);
        checkPatern(false, getPhone(), "^(\\+?38)?0(39|67|68|96|97|98|50|66|95|99|63|93|91|92|94)\\d{7}$", layoutPhone, getResources().getString(R.string.error_incorrect_phone));
        checkPatern(false, getEmail(), "^[a-zA-Z0-9.-_]+@([a-z]+\\.+)+[a-z]+$", layoutEmail, getResources().getString(R.string.error_incorrect_email));
        checkPatern(false, getFirstName(), "^[А-Я][а-я]+ [А-Я]\\.[А-Я]\\.$", layoutFirstName, error);
        equalsPassword(false, getPasswordConfirm(), getPassword(), layoutPasswordConfirm, getResources().getString(R.string.error_incorrect_password_confirm));

        for (int i = 0; i < layoutList.size(); i++) {
            if (layoutList.get(i).isErrorEnabled()) return;
        }
        InsertDataInSql dataInSql = new InsertDataInSql(view, url);
        if (dataInSql.isOnline()) {
            dataInSql.setProgressBar(view.findViewById(R.id.registration_progress));
            dataInSql.setData("login", mLogin.getText().toString());
            dataInSql.setData("password", mPassword.getText().toString());
            dataInSql.setData("name", mFirstName.getText().toString());
            dataInSql.setData("group", mGroup.getText().toString());
            dataInSql.setData("email", mEmail.getText().toString());
            dataInSql.setData("phone", mPhone.getText().toString());
            if (!photo.isEmpty())
                dataInSql.setData("photo", photo);
            dataInSql.setData("type", type);
            dataInSql.execute();
        } else
            Toast.makeText(this, getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
    }
}


package com.fanok.mdpu24v1.activity.setings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fanok.mdpu24v1.MySingleton;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.activity.RegistrationActivity;
import com.r0adkll.slidr.Slidr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.fanok.mdpu24v1.StartActivity.PREF_NAME;

public class ChangeSetingsParent extends AppCompatActivity {

    protected int inputType;
    protected String pattern;
    protected TextInputLayout textInputLayout;
    private String descripion;
    private String url;
    private String value;
    private String name;
    private TextInputEditText input;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_setings_layout);
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle arguments = getIntent().getExtras();
        String hint = "";
        if (arguments != null) {
            url = Objects.requireNonNull(arguments.get("url")).toString();
            descripion = Objects.requireNonNull(arguments.get("description")).toString();
            inputType = Integer.parseInt(Objects.requireNonNull(arguments.get("inputType")).toString());
            value = Objects.requireNonNull(arguments.get("value")).toString();
            name = Objects.requireNonNull(arguments.get("name")).toString();
            pattern = Objects.requireNonNull(arguments.get("pattern")).toString();
            hint = Objects.requireNonNull(arguments.get("hint")).toString();
        }

        textInputLayout = findViewById(R.id.input_layout);
        textInputLayout.setHint(hint);
        TextView textView = findViewById(R.id.description);
        input = findViewById(R.id.input);
        input.setInputType(inputType);
        input.setText(value);
        input.setOnFocusChangeListener((view, b) -> {
            if (b) {
                input.setSelection(input.getText().length());
                textInputLayout.setErrorEnabled(false);
            } else {
                validate();
            }
        });
        textView.setText(descripion);


    }

    protected void validate() {
        RegistrationActivity.checkPatern(false, input.getText().toString(), pattern, textInputLayout, getResources().getString(R.string.error_incorrect_data));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button_ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.ok) {

            validate();
            if (textInputLayout.isErrorEnabled()) return false;
            insert();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void insert() {
        SharedPreferences mPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String login = mPref.getString("login", "");
        JSONObject request = new JSONObject();
        try {
            request.put("data", input.getText().toString());
            request.put("login", login);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    try {
                        if (response.getInt("status") == 1) {
                            if (!name.equals("")) {
                                SharedPreferences.Editor editor = mPref.edit();
                                editor.putString(name, input.getText().toString());
                                editor.apply();
                            }
                            finish();
                        } else {
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            Toast.makeText(getApplicationContext(), response.getString("massage"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}

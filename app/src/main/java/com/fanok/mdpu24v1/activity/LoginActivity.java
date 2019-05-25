package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fanok.mdpu24v1.MySingleton;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    public static int RC_SIGN_IN = 101;
    public static String TAG = "MainActivity";

    private android.support.design.widget.TextInputEditText mLogin;
    private android.support.design.widget.TextInputEditText mPassword;
    private TextInputLayout layoutLogin;
    private TextInputLayout layoutPassword;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private View signUpGoogleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button mButtonSignUp = findViewById(R.id.sign_in_button);
        Button mButtonRegistration = findViewById(R.id.registration_button);
        TextView mResetPassword = findViewById(R.id.rememberPassword);
        mLogin = findViewById(R.id.login);
        mPassword = findViewById(R.id.password);
        layoutLogin = findViewById(R.id.layoutLogin);
        layoutPassword = findViewById(R.id.layoutPassword);
        progressBar = findViewById(R.id.login_progress);
        SignInButton mSignInButton = findViewById(R.id.googleSingUp);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();


        mButtonSignUp.setOnClickListener(this::onClick);


        mButtonRegistration.setOnClickListener((View view) -> {
            Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
            intent.putExtra("login", getLogin());
            intent.putExtra("type", "0");
            startActivity(intent);
        });

        mLogin.setOnFocusChangeListener((View view, boolean b) -> RegistrationActivity.editTextEmpty(b, getLogin(), layoutLogin, getResources().getString(R.string.error_incorrect_data)));

        mPassword.setOnFocusChangeListener((View view, boolean b) -> RegistrationActivity.editTextEmpty(b, getPassword(), layoutPassword, getResources().getString(R.string.error_incorrect_data)));

        mResetPassword.setOnClickListener(view -> startActivity(new Intent(view.getContext(), ResetPaswordActivity.class)));

        mPassword.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_ENDCALL) {
                onClick(textView);
            }
            return false;
        });

        mSignInButton.setOnClickListener(this::signIn);

    }


    private String getPassword() {
        return mPassword.getText().toString();
    }

    private String getLogin() {
        return mLogin.getText().toString();
    }

    private void onClick(View view) {

        final String url = getResources().getString(R.string.server_api) + "signup.php";

        RegistrationActivity.editTextEmpty(false, getLogin(), layoutLogin, getResources().getString(R.string.error_incorrect_data));
        RegistrationActivity.editTextEmpty(false, getPassword(), layoutPassword, getResources().getString(R.string.error_incorrect_data));
        if (layoutLogin.isErrorEnabled() || layoutPassword.isErrorEnabled()) return;
        JSONObject request = new JSONObject();
        try {
            request.put("user", getLogin());
            request.put("password", getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressBar.setVisibility(ProgressBar.VISIBLE);
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> jReqest(response, view), error -> {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void signIn(View v) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        signUpGoogleView = v;
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account, signUpGoogleView);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, View view) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail();
                            String url = getResources().getString(R.string.server_api) + "signup_google.php";
                            JSONObject request = new JSONObject();
                            try {
                                request.put("email", email);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                                    (Request.Method.POST, url, request, response -> jReqest(response, view), error -> {
                                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                                        Toast.makeText(getApplicationContext(),
                                                error.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                            MySingleton.getInstance(view.getContext()).addToRequestQueue(jsArrayRequest);


                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Ошибка входа.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                });
    }

    private void jReqest(JSONObject response, View view) {
        try {
            if (response.getInt("status") == 1) {
                SharedPreferences preferences = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("login", response.getString("login"));
                editor.putString("name", response.getString("name"));
                editor.putString("photo", response.getString("photo"));
                editor.putInt("level", response.getInt("level"));
                editor.putString("groupName", response.getString("group"));
                String urlToken = getResources().getString(R.string.server_api) + "add_token.php";
                InsertDataInSql inSql = new InsertDataInSql(view, urlToken);
                inSql.setData("login", response.getString("login"));
                inSql.setData("level", response.getString("level"));
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w("LoginActivity", "getInstanceId failed", task.getException());
                            } else {
                                String token = Objects.requireNonNull(task.getResult()).getToken();
                                if (inSql.isOnline()) {
                                    inSql.setData("token", token);
                                    inSql.execute();
                                }
                                editor.putString("token", token);
                            }
                            editor.apply();
                            startActivity(new Intent(this, MainActivity.class));
                        });
            } else if (response.getInt("status") == 2) {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                intent.putExtra("login", "");
                intent.putExtra("type", "2");
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    String photo = Objects.requireNonNull(user.getPhotoUrl()).toString();
                    intent.putExtra("email", email);
                    intent.putExtra("photo", photo);
                }
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), response.getString("massage"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

}


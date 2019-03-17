package com.fanok.mdpu24v1.activity.setings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.activity.RegistrationActivity;

public class ChangeSetingsPassword extends ChangeSetingsParent {

    TextInputEditText editText;
    TextInputLayout textInputLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textInputLayout = new TextInputLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textInputLayout.setLayoutParams(lp);
        editText = new TextInputEditText(this);
        editText.setLayoutParams(lp);
        editText.setInputType(super.inputType);
        editText.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                validate();
            } else textInputLayout.setEnabled(false);
        });
        textInputLayout.addView(editText);
        LinearLayout linearLayout = findViewById(R.id.content);
        linearLayout.addView(textInputLayout, 2);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.ok) {

            validate();
            if (super.textInputLayout.isErrorEnabled()) return false;
            if (textInputLayout.isErrorEnabled()) return false;
            insert();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void validate() {
        super.validate();
        RegistrationActivity.checkPatern(false, editText.getText().toString(), super.pattern, textInputLayout, getResources().getString(R.string.error_incorrect_password_confirm));
    }
}

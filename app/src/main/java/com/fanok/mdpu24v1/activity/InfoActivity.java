package com.fanok.mdpu24v1.activity;

import com.fanok.mdpu24v1.R;

public class InfoActivity extends PopupInfoTeacher {
    @Override
    protected void init() {
        getImageView().setImageDrawable(getDrawable(R.drawable.gerb));
        getTextView().setText(getResources().getText(R.string.progam_info));
    }
}

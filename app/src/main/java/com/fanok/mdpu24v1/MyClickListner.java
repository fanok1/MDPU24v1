package com.fanok.mdpu24v1;

import android.content.Intent;
import android.view.View;

import com.fanok.mdpu24v1.activity.PopupInfoTeacher;

public class MyClickListner implements View.OnClickListener {

    private String photo;
    private String name;

    public MyClickListner(String photo, String name) {
        this.photo = photo;
        this.name = name;
    }

    @Override
    public void onClick(View view) {
        if (!photo.isEmpty()) {
            Intent intent = new Intent(view.getContext(), PopupInfoTeacher.class);
            intent.putExtra("photo", photo);
            intent.putExtra("name", name);
            view.getContext().startActivity(intent);
        }
    }
}

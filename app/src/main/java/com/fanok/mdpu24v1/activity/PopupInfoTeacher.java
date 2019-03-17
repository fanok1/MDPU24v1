package com.fanok.mdpu24v1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanok.mdpu24v1.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class PopupInfoTeacher extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_teacher_info);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        getWindow().setAttributes(params);

        imageView = findViewById(R.id.photo);
        textView = findViewById(R.id.name);
        init();
    }

    protected void init() {
        Bundle arguments = getIntent().getExtras();
        String photo = Objects.requireNonNull(Objects.requireNonNull(arguments).get("photo")).toString();
        String name = Objects.requireNonNull(Objects.requireNonNull(arguments).get("name")).toString();
        Picasso.get().load(photo).into(imageView);
        textView.setText(name);
    }
}

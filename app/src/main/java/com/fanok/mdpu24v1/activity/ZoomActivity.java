package com.fanok.mdpu24v1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fanok.mdpu24v1.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ZoomActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_activity);

        Bundle arguments = getIntent().getExtras();
        PhotoView photoView = findViewById(R.id.photo_view);
        if (arguments != null) {
            String url = Objects.requireNonNull(arguments.get("url")).toString();
            if (url.contains(".jpg") || url.contains(".png")) {
                Picasso.get().load(url).into(photoView);
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_and_scale, R.anim.re_zoom_and_scale);
    }
}



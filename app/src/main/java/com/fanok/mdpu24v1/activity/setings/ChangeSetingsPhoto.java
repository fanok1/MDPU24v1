package com.fanok.mdpu24v1.activity.setings;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanok.mdpu24v1.PhotoUploadRequest;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.google.gson.Gson;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class ChangeSetingsPhoto extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 123;
    private final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;
    private ProgressBar progressBar;
    private String imageValue;
    private int requestCode;
    private int resultCode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_photo_layout);
        Slidr.attach(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        requestStoragePermission();

        Button button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        imageValue = mPref.getString("photo", "");
        if (!imageValue.equals("null")) {
            Picasso.get().load(imageValue).into(imageView);
        }


        button.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Выберете фото"), PICK_IMAGE_REQUEST);
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_button_ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (progressBar.getVisibility() != View.VISIBLE) finish();
        }

        if (item.getItemId() == R.id.ok) {
            progressBar.setVisibility(View.VISIBLE);
            SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
            String name = mPref.getString("login", "");
            String url = getResources().getString(R.string.server_api) + "upload_photo.php";
            String path = getPath(filePath);
            try {
                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(this, uploadId, url)
                        .addFileToUpload(path, "image")
                        .addParameter("name", name)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setDelegate(new UploadServiceBroadcastReceiver() {
                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                super.onCompleted(context, uploadInfo, serverResponse);
                                PhotoUploadRequest obj = new Gson().fromJson(serverResponse.getBodyAsString(), PhotoUploadRequest.class);
                                if (!obj.isError()) {
                                    SharedPreferences preferences = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("photo", obj.getUrl());

                                    editor.apply();
                                }
                                if (uploadInfo.getNotificationID() != null) {
                                    NotificationManager notificationManager =
                                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    Objects.requireNonNull(notificationManager).cancel(uploadInfo.getNotificationID());
                                }
                                progressBar.setVisibility(View.GONE);
                                finish();
                            }
                        })
                        .setMaxRetries(2)
                        .startUpload();

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

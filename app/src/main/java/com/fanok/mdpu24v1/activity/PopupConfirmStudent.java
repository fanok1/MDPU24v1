package com.fanok.mdpu24v1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.dowland.InsertDataInSql;
import com.fanok.mdpu24v1.fragment.FragmentStudentInfo;

import java.util.Objects;

public class PopupConfirmStudent extends AppCompatActivity {

    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_student_confirm);


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

        Bundle arguments = getIntent().getExtras();
        name = Objects.requireNonNull(Objects.requireNonNull(arguments).get("name")).toString();

        Button confirm = findViewById(R.id.confirn);
        Button cancel = findViewById(R.id.cancel);

        confirm.setOnClickListener(view -> click(view, 1));
        cancel.setOnClickListener(view -> click(view, 0));


    }

    private void click(View view, int b) {
        final String url = getResources().getString(R.string.server_api) + "confirm_student.php";
        InsertDataInSql inSql = new InsertDataInSql(view, url);
        if (inSql.isOnline()) {
            inSql.setData("name", name);
            inSql.setData("type", String.valueOf(b));
            inSql.setProgressBar(findViewById(R.id.progressBar));
            inSql.execute();
            finish();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentStudentInfo()).commit();
        }
    }
}

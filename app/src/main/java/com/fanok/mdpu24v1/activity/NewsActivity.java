package com.fanok.mdpu24v1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.dowland.DowlandNewsPage;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);
        FragmentManager fragMan = getSupportFragmentManager();
        DowlandNewsPage page = new DowlandNewsPage(findViewById(R.id.news_conteiner), url, fragMan);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        page.setProgressBar(progressBar);
        if (page.isOnline()) {
            page.execute();
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}

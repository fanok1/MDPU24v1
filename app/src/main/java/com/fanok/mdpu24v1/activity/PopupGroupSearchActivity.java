package com.fanok.mdpu24v1.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.dowland.DowlandGroupsName;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PopupGroupSearchActivity extends AppCompatActivity {
    private MaterialSearchView searchView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> grouplist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_group_search);

        Bundle arguments = getIntent().getExtras();
        String url = null;
        String type = null;
        if (arguments != null) {
            url = Objects.requireNonNull(arguments.get("url")).toString();
            type = Objects.requireNonNull(arguments.get("type")).toString();
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        getWindow().setAttributes(params);


        Toolbar toolbar = findViewById(R.id.toolbarPopup);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Выберете...");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finishAfterTransition());

        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);
        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        String login = mPref.getString("login", "");

        DowlandGroupsName groups = new DowlandGroupsName(findViewById(R.id.popup), Objects.requireNonNull(url), listView, grouplist);
        if (groups.isOnline()) {
            if (type != null) groups.setData("type", type);
            groups.setData("login", login);
            groups.setData("group", TypeTimeTable.getGroup());
            groups.setProgressBar(findViewById(R.id.progressBarGroup));
            groups.execute();
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_no_internet_conection), Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            RegistrationActivity.groupName = adapterView.getItemAtPosition(i).toString();
            finish();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView.setMenuItem(searchItem);
        searchView.setHint("Поиск...");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<String> newList = new ArrayList<>();
                for (int i = 0; i < grouplist.size(); i++) {
                    if (grouplist.get(i).contains(newText)) {
                        newList.add(grouplist.get(i));
                    }
                    adapter = new ArrayAdapter<>(PopupGroupSearchActivity.this, android.R.layout.simple_list_item_1, newList);
                    listView.setAdapter(adapter);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}

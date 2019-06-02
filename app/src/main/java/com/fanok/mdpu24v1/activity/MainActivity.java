package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.fragment.FragmentAbsenteeism;
import com.fanok.mdpu24v1.fragment.FragmentAbsenteeismSelect;
import com.fanok.mdpu24v1.fragment.FragmentChatSelect;
import com.fanok.mdpu24v1.fragment.FragmentCurator;
import com.fanok.mdpu24v1.fragment.FragmentMarckSelect;
import com.fanok.mdpu24v1.fragment.FragmentMarks;
import com.fanok.mdpu24v1.fragment.FragmentNewsUniversity;
import com.fanok.mdpu24v1.fragment.FragmentProjects;
import com.fanok.mdpu24v1.fragment.FragmentProjectsSelect;
import com.fanok.mdpu24v1.fragment.FragmentStudentInfo;
import com.fanok.mdpu24v1.fragment.FragmentTask;
import com.fanok.mdpu24v1.fragment.FragmentTimeTable;
import com.fanok.mdpu24v1.fragment.FragmentTimeTableSelect;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int level;
    private View headerView;
    public static int countFragmentIntoStack;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        level = mPref.getInt("level", 0);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        MenuItem student = navigationView.getMenu().findItem(R.id.student);
        MenuItem connection = navigationView.getMenu().findItem(R.id.connection);

        MenuItem absenteeism = navigationView.getMenu().findItem(R.id.absenteeism);
        if (level == 3) {
            student.setEnabled(false);
            absenteeism.setEnabled(false);
            connection.setVisible(false);
        }

        if (savedInstanceState == null) {
            int activity = mPref.getInt("activity", 0);
            switch (activity) {
                case 1:
                    showMenuFragment(new FragmentStudentInfo(), R.id.student);
                    break;
                case 2:
                    showMenuFragment(new FragmentTimeTable(), R.id.timetable);
                    break;
                case 3:
                    showMenuFragment(new FragmentMarks(), R.id.marks);
                    break;
                case 4:
                    showMenuFragment(new FragmentAbsenteeism(), R.id.absenteeism);
                    break;
                case 5:
                    showMenuFragment(new FragmentNewsUniversity(), R.id.news);
                    startActivity(new Intent(this, ChatActivity.class));
                    break;
                case 6:
                    showMenuFragment(new FragmentTask(), R.id.tasks);
                    break;
                case 7:
                    showMenuFragment(new FragmentProjects(), R.id.projects);
                    break;
                case 8:
                    showMenuFragment(new FragmentCurator(), R.id.curator);
                    break;
                case 9:
                    showMenuFragment(new FragmentNewsUniversity(), R.id.news);
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("url", "http://www.dfn.mdpu.org.ua/");
                    intent.putExtra("type", activity);
                    intent.putExtra("title", "ДФН");
                    startActivity(intent);
                    break;
                case 10:
                    showMenuFragment(new FragmentNewsUniversity(), R.id.news);
                    Intent intent1 = new Intent(this, WebViewActivity.class);
                    intent1.putExtra("url", "https://mdpu.org.ua/");
                    intent1.putExtra("type", activity);
                    intent1.putExtra("title", "МДПУ");
                    startActivity(intent1);
                    break;
                case 0:
                default:
                    showMenuFragment(new FragmentNewsUniversity(), R.id.news);
                    break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (countFragmentIntoStack == 1) {
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("Выход");
                ad.setMessage("Вы уверены что хотите выйти?");
                ad.setPositiveButton("Да", (dialog, arg1) -> {
                    super.onBackPressed();
                    super.onBackPressed();
                });
                ad.setNegativeButton("Нет", (dialogInterface, i) -> {
                });
                ad.setCancelable(true);
                ad.show();
            } else {
                super.onBackPressed();
                countFragmentIntoStack--;
            }
        }
    }

    public void showMenuFragment(Fragment fragment, int chekItemId) {
        navigationView.setCheckedItem(chekItemId);
        showMenuFragment(fragment);
    }

    public void showMenuFragment(Fragment fragment) {
        showFragment(R.id.fragment_container, fragment);
    }

    protected void showFragment(int resId, Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        Fragment lastFragment = fragmentManager.findFragmentByTag(String.valueOf(countFragmentIntoStack));
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }

        countFragmentIntoStack++;
        transaction.add(resId, fragment, String.valueOf(countFragmentIntoStack)).setBreadCrumbShortTitle(String.valueOf(countFragmentIntoStack));


        transaction.addToBackStack(String.valueOf(countFragmentIntoStack));


        transaction.commit();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.timetable) {
            switch (level) {
                case 1:
                case 2:
                    TypeTimeTable.setType(TypeTimeTable.studentTimeTable);
                    break;
                case 3:
                    TypeTimeTable.setType(TypeTimeTable.teacherTimeTable);
                    break;
            }

            if (level == 4)
                showMenuFragment(new FragmentTimeTableSelect());
            else
                showMenuFragment(new FragmentTimeTable());
        } else if (id == R.id.student) {
            showMenuFragment(new FragmentStudentInfo());
        } else if (id == R.id.marks) {
            switch (level) {
                case 1:
                    TypeTimeTable.setType(TypeTimeTable.studentTimeTable);
                    break;
                case 2:
                    TypeTimeTable.setType(TypeTimeTable.starostaTimeTable);
                    break;
                case 3:
                    TypeTimeTable.setType(TypeTimeTable.teacherTimeTable);
                    break;
                case 4:
                    TypeTimeTable.setType(TypeTimeTable.curatorTimeTable);
                    break;
            }
            if (level == 3 || level == 4) {
                showMenuFragment(new FragmentMarckSelect());
            } else
                showMenuFragment(new FragmentMarks());

        } else if (id == R.id.absenteeism) {
            switch (level) {
                case 1:
                    TypeTimeTable.setType(TypeTimeTable.studentTimeTable);
                    break;
                case 2:
                    TypeTimeTable.setType(TypeTimeTable.starostaTimeTable);
                    break;
                case 3:
                    TypeTimeTable.setType(TypeTimeTable.teacherTimeTable);
                    break;
                case 4:
                    TypeTimeTable.setType(TypeTimeTable.curatorTimeTable);
                    break;
            }
            if (level != 3) {
                if (level == 4)
                    showMenuFragment(new FragmentAbsenteeismSelect());
                else
                    showMenuFragment(new FragmentAbsenteeism());
            }

        } else if (id == R.id.chat) {
            if (level == 4) {
                showMenuFragment(new FragmentChatSelect());
            } else if (level != 3)
                startActivity(new Intent(this, ChatActivity.class));

        } else if (id == R.id.tasks) {
            if (level != 3) {
                showMenuFragment(new FragmentTask());
            }
        } else if (id == R.id.curator) {
            if (level != 3) {
                showMenuFragment(new FragmentCurator());
            }
        } else if (id == R.id.news) {
            showMenuFragment(new FragmentNewsUniversity());
        } else if (id == R.id.dfn) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", "http://www.dfn.mdpu.org.ua/");
            intent.putExtra("type", 9);
            intent.putExtra("title", "ДФН");
            startActivity(intent);
        } else if (id == R.id.site) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", "https://mdpu.org.ua/");
            intent.putExtra("type", 10);
            intent.putExtra("title", "МДПУ");
            startActivity(intent);
        } else if (id == R.id.grafic) {

        } else if (id == R.id.setngs) {
            startActivity(new Intent(this, SetingsActivity.class));
        } else if (id == R.id.info) {
            startActivity(new Intent(this, InfoActivity.class));
        } else if (id == R.id.projects) {
            switch (level) {
                case 1:
                    TypeTimeTable.setType(TypeTimeTable.studentTimeTable);
                    break;
                case 2:
                    TypeTimeTable.setType(TypeTimeTable.starostaTimeTable);
                    break;
                case 3:
                    TypeTimeTable.setType(TypeTimeTable.teacherTimeTable);
                    break;
                case 4:
                    TypeTimeTable.setType(TypeTimeTable.curatorTimeTable);
                    break;
            }
            if (level == 3 || level == 4) {
                showMenuFragment(new FragmentProjectsSelect());
            } else
                showMenuFragment(new FragmentProjects());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView name = headerView.findViewById(R.id.name_header);
        TextView login = headerView.findViewById(R.id.login_header);

        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        String nameValue = mPref.getString("name", "");
        String loginValue = mPref.getString("login", "");
        String imageValue = mPref.getString("photo", "");
        if (level != 3 && level != 4) TypeTimeTable.setGroup(mPref.getString("groupName", ""));


        login.setText(loginValue);
        name.setText(nameValue);
        TextView textImage = headerView.findViewById(R.id.image_text);
        CircleImageView imageView = headerView.findViewById(R.id.image_header);
        String text = "" + nameValue.charAt(0) + nameValue.charAt(nameValue.indexOf(" ") + 1);
        textImage.setText(text.toUpperCase());
        if (!imageValue.equals("null")) {
            Picasso.get().load(imageValue).into(imageView);
        }
    }
}

package com.fanok.mdpu24v1.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fanok.mdpu24v1.BackPress;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPref = getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        level = mPref.getInt("level", 0);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentStudentInfo()).commit();
                    navigationView.setCheckedItem(R.id.student);
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentTimeTable()).commit();
                    navigationView.setCheckedItem(R.id.timetable);
                    break;
                case 3:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMarks()).commit();
                    navigationView.setCheckedItem(R.id.marks);
                    break;
                case 4:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAbsenteeism()).commit();
                    navigationView.setCheckedItem(R.id.absenteeism);
                    break;
                case 5:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentNewsUniversity()).commit();
                    startActivity(new Intent(this, ChatActivity.class));
                    navigationView.setCheckedItem(R.id.news);
                    break;
                case 6:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentTask()).commit();
                    navigationView.setCheckedItem(R.id.tasks);
                    break;
                case 7:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProjects()).commit();
                    navigationView.setCheckedItem(R.id.projects);
                    break;
                case 8:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCurator()).commit();
                    navigationView.setCheckedItem(R.id.curator);
                    break;
                case 0:
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentNewsUniversity()).commit();
                    navigationView.setCheckedItem(R.id.news);
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

            if (BackPress.getFragment().size() < 2) {
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("Выход");
                ad.setMessage("Вы уверены что хотите выйти?");
                ad.setPositiveButton("Да", (dialog, arg1) -> {
                    super.onBackPressed();
                });
                ad.setNegativeButton("Нет", (dialogInterface, i) -> {
                });
                ad.setCancelable(true);
                ad.show();
            }
        }
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentTimeTableSelect()).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentTimeTable()).commit();
        } else if (id == R.id.student) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentStudentInfo()).commit();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMarckSelect()).commit();
            } else
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMarks()).commit();

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
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAbsenteeismSelect()).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAbsenteeism()).commit();
            }

        } else if (id == R.id.chat) {
            if (level == 4) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentChatSelect()).commit();
            } else if (level != 3)
                startActivity(new Intent(this, ChatActivity.class));

        } else if (id == R.id.tasks) {
            if (level != 3) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentTask()).commit();
            }
        } else if (id == R.id.curator) {
            if (level != 3) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCurator()).commit();
            }
        } else if (id == R.id.news) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentNewsUniversity()).commit();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProjectsSelect()).commit();
            } else
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProjects()).commit();
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

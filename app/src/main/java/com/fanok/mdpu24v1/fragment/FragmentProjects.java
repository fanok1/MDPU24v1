package com.fanok.mdpu24v1.fragment;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TinyDB;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.activity.MainActivity;
import com.fanok.mdpu24v1.activity.ProjectAddActivity;
import com.fanok.mdpu24v1.adapter.PagerProjectAdaptor;
import com.fanok.mdpu24v1.dowland.DowlandStudentTab;

import java.util.ArrayList;
import java.util.Objects;

import static com.fanok.mdpu24v1.activity.ChatActivity.ACTION;

public class FragmentProjects extends android.support.v4.app.Fragment {

    private BroadcastReceiver br;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("activity", 7);
        editor.apply();


        int level = mPref.getInt("level", 0);
        String name = mPref.getString("name", "");
        setHasOptionsMenu(true);
        TabLayout tab = view.findViewById(R.id.tabLayout);
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        ViewPager pager = view.findViewById(R.id.viewPager);
        FragmentManager fm = getChildFragmentManager();
        final String url = getResources().getString(R.string.server_api) + "get_student_project.php";

        DowlandStudentTab dowland = new DowlandStudentTab(view, url, tab, pager, fm);
        if (dowland.isOnline()) {
            dowland.setData("group", TypeTimeTable.getGroup());
            dowland.setData("level", String.valueOf(level));
            dowland.setData("name", name);
            dowland.setProgressBar(view.findViewById(R.id.progressBar));
            dowland.execute();
        } else {
            TinyDB tinyDB = new TinyDB(getContext());
            ArrayList<String> students = tinyDB.getListString("StudentList");
            if (students.size() > 0) {
                for (int i = 0; i < students.size(); i++) {
                    tab.addTab(tab.newTab().setText(students.get(i)));
                }
                FragmentPagerAdapter pagerAdapter = new PagerProjectAdaptor(fm, tab.getTabCount(), students);
                pager.setAdapter(pagerAdapter);
                pager.setOffscreenPageLimit(pagerAdapter.getCount() > 1 ? pagerAdapter.getCount() - 1 : 1);
                pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
            }

        }

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.cancel(3);
                }
                Objects.requireNonNull((MainActivity) getActivity()).showMenuFragment(new FragmentProjects());
            }
        };

        IntentFilter intFilt = new IntentFilter(ACTION);
        Objects.requireNonNull(getContext()).registerReceiver(br, intFilt);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Objects.requireNonNull(getContext()).unregisterReceiver(br);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.plus_button_menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(menuItem -> {
            if (TypeTimeTable.getType() != TypeTimeTable.teacherTimeTable) return false;
            Intent intent = new Intent(getContext(), ProjectAddActivity.class);
            startActivity(intent);
            return false;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}


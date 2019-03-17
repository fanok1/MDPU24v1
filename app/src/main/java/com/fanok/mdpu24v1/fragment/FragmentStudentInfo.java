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
import com.fanok.mdpu24v1.activity.RegistrationActivity;
import com.fanok.mdpu24v1.adapter.PagerStudentInfoAdaptor;
import com.fanok.mdpu24v1.dowland.DowlandStudentGroups;

import java.util.ArrayList;
import java.util.Objects;

import static com.fanok.mdpu24v1.activity.ChatActivity.ACTION;

public class FragmentStudentInfo extends android.support.v4.app.Fragment {

    private int level;
    private BroadcastReceiver br;
    private SharedPreferences mPref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        start(1);


        level = mPref.getInt("level", 0);
        String login = mPref.getString("login", "");
        setHasOptionsMenu(true);
        TabLayout tab = view.findViewById(R.id.tabLayout);
        ViewPager pager = view.findViewById(R.id.viewPager);
        FragmentManager fm = getChildFragmentManager();
        final String url = getResources().getString(R.string.server_api) + "get_groups_student.php";

        DowlandStudentGroups dowland = getDowland(view, url, tab, pager, fm);
        if (dowland.isOnline()) {
            dowland.setData("login", login);
            dowland.setProgressBar(view.findViewById(R.id.progressBar));
            dowland.execute();
        } else {
            TinyDB tinyDB = new TinyDB(getContext());
            ArrayList<String> groups = tinyDB.getListString("GroupsList");
            if (groups.size() > 0) {
                for (int i = 0; i < groups.size(); i++) {
                    tab.addTab(tab.newTab().setText(groups.get(i)));
                }
                FragmentPagerAdapter pagerAdapter = getAdapter(fm, tab.getTabCount(), groups);
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
                clearNotification(context);
            }
        };

        IntentFilter intFilt = new IntentFilter(ACTION);
        Objects.requireNonNull(getContext()).registerReceiver(br, intFilt);

        return view;
    }

    protected void clearNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentStudentInfo()).commit();
    }

    protected DowlandStudentGroups getDowland(View view, String url, TabLayout tab, ViewPager pager, FragmentManager fm) {
        return new DowlandStudentGroups(view, url, tab, pager, fm);
    }

    protected FragmentPagerAdapter getAdapter(FragmentManager fm, int tabCount, ArrayList<String> groups) {
        return new PagerStudentInfoAdaptor(fm, tabCount, groups);
    }

    protected void start(int n) {

        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("activity", n);
        editor.apply();
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
            if (level != 4) return false;
            startActivity(getIntent());
            return false;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected Intent getIntent() {
        Intent intent = new Intent(getContext(), RegistrationActivity.class);
        intent.putExtra("login", "");
        intent.putExtra("type", "1");
        return intent;
    }

}


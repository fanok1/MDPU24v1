package com.fanok.mdpu24v1.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.TinyDB;
import com.fanok.mdpu24v1.activity.MainActivity;
import com.fanok.mdpu24v1.activity.TaskAddActivity;
import com.fanok.mdpu24v1.adapter.PagerTaskAdaptor;
import com.fanok.mdpu24v1.dowland.DowlandStudentGroups;
import com.fanok.mdpu24v1.dowland.DowlandTaskGroups;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentTask extends FragmentStudentInfo {

    @Override
    protected FragmentPagerAdapter getAdapter(FragmentManager fm, int tabCount, ArrayList<String> groups) {
        return new PagerTaskAdaptor(fm, tabCount, groups);
    }

    @Override
    protected void dowland() {
    }

    @Override
    public void onResume() {
        super.onResume();
        String login = getmPref().getString("login", "");
        final String url = getResources().getString(R.string.server_api) + "get_groups_student.php";

        DowlandStudentGroups dowland = getDowland(getView(), url, getTab(), getPager(), getFm());
        if (dowland.isOnline()) {
            dowland.setData("login", login);
            if (getView() != null) {
                dowland.setProgressBar(getView().findViewById(R.id.progressBar));
            }
            dowland.execute();
        } else {
            TinyDB tinyDB = new TinyDB(getContext());
            ArrayList<String> groups = tinyDB.getListString("GroupsList");
            if (groups.size() > 0) {
                for (int i = 0; i < groups.size(); i++) {
                    getTab().addTab(getTab().newTab().setText(groups.get(i)));
                }
                FragmentPagerAdapter pagerAdapter = getAdapter(getFm(), getTab().getTabCount(), groups);
                getPager().setAdapter(pagerAdapter);
                getPager().setOffscreenPageLimit(pagerAdapter.getCount() > 1 ? pagerAdapter.getCount() - 1 : 1);
                getPager().addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(getTab()));
            }

        }

        getTab().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getPager().setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void start(int n) {
        super.start(6);
    }

    @Override
    protected DowlandStudentGroups getDowland(View view, String url, TabLayout tab, ViewPager pager, FragmentManager fm) {
        return new DowlandTaskGroups(view, url, tab, pager, fm);
    }

    @Override
    protected Intent getIntent() {
        return new Intent(getContext(), TaskAddActivity.class);
    }

    @Override
    protected void clearNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(2);
        }
        Objects.requireNonNull((MainActivity) getActivity()).showMenuFragment(new FragmentTask());
    }
}

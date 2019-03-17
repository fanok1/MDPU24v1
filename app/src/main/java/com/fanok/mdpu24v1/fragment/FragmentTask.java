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
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentTask()).commit();
    }
}

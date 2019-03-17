package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fanok.mdpu24v1.adapter.PagerProjectAdaptor;

import java.util.ArrayList;

public class DowlandStudentTab extends DowlandStudentGroups {

    @SuppressLint("StaticFieldLeak")
    private TabLayout tab;
    @SuppressLint("StaticFieldLeak")
    private ViewPager pager;
    private FragmentManager fm;

    public DowlandStudentTab(@NonNull View view, @NonNull String url, TabLayout tab, ViewPager pager, FragmentManager fm) {
        super(view, url, tab, pager, fm);
    }

    @Override
    protected FragmentPagerAdapter getAdapter(FragmentManager fm, int tabCount, ArrayList<String> groups) {
        return new PagerProjectAdaptor(fm, tabCount, groups);
    }

    @Override
    protected void insertTotinyDB(String name) {
        super.insertTotinyDB("StudentList");
    }
}



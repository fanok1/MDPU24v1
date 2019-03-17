package com.fanok.mdpu24v1.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanok.mdpu24v1.fragment.TabTimeTable;


public class PagerAdaptor extends FragmentPagerAdapter {

    private int tabCount;

    public PagerAdaptor(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        TabTimeTable tabTimeTable = new TabTimeTable();
        Bundle bundle = new Bundle();
        bundle.putInt("day", position);
        tabTimeTable.setArguments(bundle);
        return tabTimeTable;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

package com.fanok.mdpu24v1.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanok.mdpu24v1.fragment.TabAbsenteeism;


public class PagerAbsenteeismAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public PagerAbsenteeismAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        TabAbsenteeism tabAbsenteeism = new TabAbsenteeism();
        Bundle bundle = new Bundle();
        bundle.putInt("modul", position);
        tabAbsenteeism.setArguments(bundle);
        return tabAbsenteeism;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

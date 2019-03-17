package com.fanok.mdpu24v1.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fanok.mdpu24v1.fragment.TabMarks;


public class PagerMrksAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private String lesson;

    public PagerMrksAdapter(FragmentManager fm, int tabCount, String lesson) {
        super(fm);
        this.tabCount = tabCount;
        this.lesson = lesson;
    }

    @Override
    public Fragment getItem(int position) {
        TabMarks tabMarks = new TabMarks();
        Bundle bundle = new Bundle();
        bundle.putInt("modul", position);
        bundle.putString("lesson", lesson);
        tabMarks.setArguments(bundle);
        return tabMarks;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

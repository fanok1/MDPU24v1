package com.fanok.mdpu24v1.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanok.mdpu24v1.fragment.TabStudentInfo;

import java.util.ArrayList;


public class PagerStudentInfoAdaptor extends FragmentPagerAdapter {

    private static ArrayList<String> name;
    private int tabCount;

    public PagerStudentInfoAdaptor(FragmentManager fm, int tabCount, ArrayList<String> name) {
        super(fm);
        this.tabCount = tabCount;
        PagerStudentInfoAdaptor.name = name;
    }

    public static ArrayList<String> getName() {
        return name;
    }

    @Override
    public Fragment getItem(int position) {
        TabStudentInfo tabStudentInfo = new TabStudentInfo();
        Bundle bundle = new Bundle();
        bundle.putString("name", name.get(position));
        tabStudentInfo.setArguments(bundle);
        return tabStudentInfo;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

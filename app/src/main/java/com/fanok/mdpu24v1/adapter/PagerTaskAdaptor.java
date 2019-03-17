package com.fanok.mdpu24v1.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fanok.mdpu24v1.fragment.TabTask;

import java.util.ArrayList;

public class PagerTaskAdaptor extends PagerStudentInfoAdaptor {
    public PagerTaskAdaptor(FragmentManager fm, int tabCount, ArrayList<String> name) {
        super(fm, tabCount, name);
    }

    @Override
    public Fragment getItem(int position) {
        TabTask tabTask = new TabTask();
        Bundle bundle = new Bundle();
        bundle.putString("name", getName().get(position));
        tabTask.setArguments(bundle);
        return tabTask;
    }
}

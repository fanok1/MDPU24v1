package com.fanok.mdpu24v1.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.fanok.mdpu24v1.fragment.TabProject;

import java.util.ArrayList;

public class PagerProjectAdaptor extends PagerStudentInfoAdaptor {
    public PagerProjectAdaptor(FragmentManager fm, int tabCount, ArrayList<String> name) {
        super(fm, tabCount, name);
    }

    @Override
    public Fragment getItem(int position) {
        TabProject tabProject = new TabProject();
        Bundle bundle = new Bundle();
        bundle.putString("name", getName().get(position));
        tabProject.setArguments(bundle);
        return tabProject;
    }
}

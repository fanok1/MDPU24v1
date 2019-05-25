package com.fanok.mdpu24v1.fragment;

import com.fanok.mdpu24v1.activity.MainActivity;

import java.util.Objects;

public class FragmentProjectsSelect extends FragmentMarckSelect {
    @Override
    protected void startFragmentThis() {
        Objects.requireNonNull((MainActivity) getActivity()).showMenuFragment(new FragmentProjectsSelect(), true);
    }

    @Override
    protected void startFragment() {
        Objects.requireNonNull((MainActivity) getActivity()).showMenuFragment(new FragmentProjects(), true);
    }
}


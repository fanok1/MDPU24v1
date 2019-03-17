package com.fanok.mdpu24v1.fragment;

import com.fanok.mdpu24v1.R;

import java.util.Objects;

public class FragmentProjectsSelect extends FragmentMarckSelect {
    @Override
    protected void startFragmentThis() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProjectsSelect()).commit();
    }

    @Override
    protected void startFragment() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProjects()).commit();
    }
}


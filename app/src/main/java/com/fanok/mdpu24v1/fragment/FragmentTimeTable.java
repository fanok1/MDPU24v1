package com.fanok.mdpu24v1.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.Week;
import com.fanok.mdpu24v1.adapter.PagerAdaptor;

import java.util.Objects;

public class FragmentTimeTable extends android.support.v4.app.Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("activity", 2);
        editor.apply();

        setHasOptionsMenu(true);
        TabLayout tab = view.findViewById(R.id.tabLayout);
        tab.addTab(tab.newTab().setText(R.string.mondey));
        tab.addTab(tab.newTab().setText(R.string.tuesday));
        tab.addTab(tab.newTab().setText(R.string.wednesday));
        tab.addTab(tab.newTab().setText(R.string.thursday));
        tab.addTab(tab.newTab().setText(R.string.friday));
        ViewPager pager = view.findViewById(R.id.viewPager);
        int color;
        if (Week.getWeek() == Week.green)
            color = getResources().getColor(R.color.green);
        else color = getResources().getColor(R.color.red);
        tab.setSelectedTabIndicatorColor(color);
        tab.setTabTextColors(Objects.requireNonNull(tab.getTabTextColors()).getDefaultColor(), color);
        FragmentPagerAdapter pagerAdapter = new PagerAdaptor(getChildFragmentManager(), tab.getTabCount());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(pagerAdapter.getCount() > 1 ? pagerAdapter.getCount() - 1 : 1);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.switch_button_menu, menu);
        SwitchCompat switchCompat = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switch_button);
        if (Week.getWeek() == Week.green) switchCompat.setChecked(true);
        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) Week.setWeek(Week.green);
            else Week.setWeek(Week.red);
            Objects.requireNonNull(getActivity()).recreate();
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}


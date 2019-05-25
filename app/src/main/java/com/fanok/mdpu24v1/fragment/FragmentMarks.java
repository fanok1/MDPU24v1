package com.fanok.mdpu24v1.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanok.mdpu24v1.ClickListnerMarks;
import com.fanok.mdpu24v1.R;
import com.fanok.mdpu24v1.StartActivity;
import com.fanok.mdpu24v1.TypeTimeTable;
import com.fanok.mdpu24v1.activity.AddMarksActivity;
import com.fanok.mdpu24v1.activity.PopupGroupSearchActivity;
import com.fanok.mdpu24v1.activity.RegistrationActivity;
import com.fanok.mdpu24v1.adapter.PagerMrksAdapter;

public class FragmentMarks extends android.support.v4.app.Fragment {

    public static boolean flag = false;
    private android.support.design.widget.TextInputEditText predmet;
    private TabLayout tab;
    private ViewPager pager;
    private boolean firstFocus;

    @Override
    public void onResume() {
        super.onResume();
        firstFocus = true;
        predmet.requestFocus();


        if (flag) {
            RegistrationActivity.groupName = predmet.getText().toString();
            flag = false;
        }

        predmet.setText(RegistrationActivity.groupName);
        ClickListnerMarks.setPredmet(RegistrationActivity.groupName);

        if (!RegistrationActivity.groupName.isEmpty()) {
            FragmentStatePagerAdapter pagerAdapter = new PagerMrksAdapter(getChildFragmentManager(), tab.getTabCount(), RegistrationActivity.groupName);
            pagerAdapter.notifyDataSetChanged();
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
        }

        if (getView() == null) {
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RegistrationActivity.groupName = "";
        View view = inflater.inflate(R.layout.fragment_marks, container, false);
        SharedPreferences mPref = view.getContext().getSharedPreferences(StartActivity.PREF_NAME, StartActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt("activity", 3);
        editor.apply();
        setHasOptionsMenu(true);
        predmet = view.findViewById(R.id.predmet);
        predmet.setKeyListener(null);
        predmet.setOnClickListener(this::showPopup);
        predmet.setOnFocusChangeListener((View view1, boolean b) -> {
            if (b && !firstFocus) {
                showPopup(view1);
            }
            firstFocus = false;
        });


        tab = view.findViewById(R.id.tabLayout);
        pager = view.findViewById(R.id.viewPager);

        return view;
    }

    private void showPopup(View view) {
        final String url = getResources().getString(R.string.server_api) + "get_marks_predmet.php";
        Intent intent = new Intent(view.getContext(), PopupGroupSearchActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("type", TypeTimeTable.getType());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.plus_button_menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(menuItem -> {
            if (TypeTimeTable.getType() == TypeTimeTable.studentTimeTable) return false;
            Intent intent = new Intent(getContext(), AddMarksActivity.class);
            intent.putExtra("predmet", predmet.getText().toString());
            startActivity(intent);
            return false;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

}


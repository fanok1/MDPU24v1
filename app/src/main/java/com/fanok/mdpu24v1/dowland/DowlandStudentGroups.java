package com.fanok.mdpu24v1.dowland;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fanok.mdpu24v1.TinyDB;
import com.fanok.mdpu24v1.adapter.PagerStudentInfoAdaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class DowlandStudentGroups extends DowladParent {

    @SuppressLint("StaticFieldLeak")
    private TabLayout tab;
    @SuppressLint("StaticFieldLeak")
    private ViewPager pager;
    private FragmentManager fm;
    private ArrayList<String> grouplist;

    public DowlandStudentGroups(@NonNull View view, @NonNull String url, TabLayout tab, ViewPager pager, FragmentManager fm) {
        super(view, url);
        this.tab = tab;
        this.pager = pager;
        this.fm = fm;
        grouplist = new ArrayList<>();
    }

    @Override
    protected void parce(Document data) {
        try {
            JSONArray jArray = new JSONArray(data.getElementsByTag("body").text());
            for (int i = 0; i < jArray.length(); i++) {
                grouplist.add(jArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        insertTotinyDB("GroupsList");
        for (int i = 0; i < grouplist.size(); i++) {
            tab.addTab(tab.newTab().setText(grouplist.get(i)));
        }
        FragmentPagerAdapter pagerAdapter = getAdapter(fm, tab.getTabCount(), grouplist);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(pagerAdapter.getCount() > 1 ? pagerAdapter.getCount() - 1 : 1);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        super.onPostExecute(aVoid);
    }

    protected FragmentPagerAdapter getAdapter(FragmentManager fm, int tabCount, ArrayList<String> groups) {
        return new PagerStudentInfoAdaptor(fm, tabCount, groups);
    }

    protected void insertTotinyDB(String name) {
        TinyDB tinyDB = new TinyDB(getView().getContext());
        tinyDB.putListString(name, grouplist);
    }
}



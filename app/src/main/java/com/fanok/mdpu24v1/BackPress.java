package com.fanok.mdpu24v1;

import java.util.ArrayList;

public class BackPress {
    public static boolean b = false;
    private static ArrayList<android.support.v4.app.Fragment> fragment = new ArrayList<>();
    private static ArrayList<Integer> menuItem = new ArrayList<>();

    public static ArrayList<android.support.v4.app.Fragment> getFragment() {
        return fragment;
    }

    public static void addFragment(android.support.v4.app.Fragment fragment, int itemMenu) {
        BackPress.fragment.add(fragment);
        BackPress.menuItem.add(itemMenu);
    }

    public static android.support.v4.app.Fragment getLast() {
        android.support.v4.app.Fragment f = fragment.get(fragment.size() - 2);
        fragment.remove(fragment.size() - 1);
        return f;
    }

    public static int getLastItem() {
        int n = menuItem.get(menuItem.size() - 2);
        menuItem.remove(menuItem.size() - 1);
        return n;
    }
}

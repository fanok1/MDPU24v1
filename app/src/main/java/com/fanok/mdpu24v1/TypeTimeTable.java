package com.fanok.mdpu24v1;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class TypeTimeTable {
    public static final int studentTimeTable = 0;
    public static final int teacherTimeTable = 1;
    public static final int curatorTimeTable = 2;
    public static final int starostaTimeTable = 3;

    private static String group = "";
    private static int type = 0;

    public static String getGroup() {
        return group;
    }

    public static void setGroup(String group) {
        TypeTimeTable.group = group;
    }

    public static void setGroup(Context context, String group) {
        SharedPreferences preferences = context.getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TypeTimeTable_group", group);
        editor.apply();
        TypeTimeTable.group = group;
    }

    public static int getType() {
        return type;
    }

    public static void setType(int type) {
        TypeTimeTable.type = type;
    }

    public static void setType(Context context, int type) {
        SharedPreferences preferences = context.getSharedPreferences(StartActivity.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("TypeTimeTable_type", type);
        editor.apply();
        TypeTimeTable.type = type;
    }
}

package com.theost.roomapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    public static final String PREF_KEY_COUNTER = "key_counter";

    public static void putInteger(Context context, String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInteger(Context context, String key) {
        return getInteger(context, key, 0);
    }

    public static int getInteger(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
    }

}

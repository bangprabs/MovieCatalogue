package net.prabowoaz.moviecatalogue.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    public static final String PREF_USERPREFERENCES = "prabowoazPreferences";

    public static final String PREF_DAILY = PREF_USERPREFERENCES+".daily";
    public static final String PREF_RELEASE = PREF_USERPREFERENCES+".release";
    public static final String PREE_OPEN_APPS = PREF_USERPREFERENCES+".openApps";

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    public static void saveBoolPref(Context context, String key, boolean data) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(key, data);
        editor.commit();
    }

    public static boolean getBoolPref(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        boolean data = preferences.getBoolean(key, false);
        return data;
    }

    public static void clear(Context context, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key).apply();
    }
}

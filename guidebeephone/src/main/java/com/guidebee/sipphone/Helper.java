package com.guidebee.sipphone;

import android.content.Context;
import android.preference.PreferenceManager;


public class Helper {

    public static String getConfig(Context context,String key,String defaultValue){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key,defaultValue);
    }

    public static boolean getConfig(Context context,String key,boolean defaultValue){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static int getConfig(Context context,String key,int defaultValue){
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

}

package com.koingdev.aseanop;

import android.content.Context;
import android.content.SharedPreferences;



public class SharePre {

    public static void save(Context context, String fileName, String key, String value ){
        SharedPreferences shareSetting = context.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = shareSetting.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String Open(Context context, String fileName, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, 0);
        String gets = sharedPreferences.getString(key, "");
        return gets;
    }
}

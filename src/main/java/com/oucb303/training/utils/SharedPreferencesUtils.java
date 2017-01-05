package com.oucb303.training.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by huzhiming on 2017/1/4.
 */

public class SharedPreferencesUtils
{
    private static SharedPreferencesUtils configUtils;
    private static Context context;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPreferences;

    public static SharedPreferencesUtils getInstance(Context c)
    {
        context = c;
        if (configUtils == null)
            configUtils = new SharedPreferencesUtils();
        return configUtils;
    }

    public void saveValue(String key, String value)
    {
        if (editor == null || sharedPreferences == null)
        {
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        editor.putString(key, value);
        editor.commit();
    }

    public String readValue(String key)
    {
        if (sharedPreferences == null)
        {
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, "");
    }
}

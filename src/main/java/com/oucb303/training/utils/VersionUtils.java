package com.oucb303.training.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by huzhiming on 2016/12/11.
 */

public class VersionUtils
{
    public static PackageInfo getAppVersion(Context context)
    {
        PackageInfo info = null;

        PackageManager manager = context.getPackageManager();
        try
        {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        Log.d(Constant.LOG_TAG, info.packageName + "-" + info.versionCode + "-" + info.versionName);
        return info;
    }
}

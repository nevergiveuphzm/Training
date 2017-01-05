package com.oucb303.training.utils;

import android.content.Context;

/**
 * Created by huzhiming on 2017/1/4.
 */

public class ConfigParamUtils
{
    //更改默认设备个数
    public static void changeDefaultDeviceNum(Context context, int num)
    {
        SharedPreferencesUtils.getInstance(context).saveValue("defaultDeviceNum", num + "");
    }

    public static int getDefaultDeviceNum(Context context)
    {
        //获取配置参数  默认的设备个数
        String temp = SharedPreferencesUtils.getInstance(context).readValue("defaultDeviceNum");
        int defaultDeviceNum = 8;
        if (!temp.equals(""))
            defaultDeviceNum = new Integer(temp);
        return defaultDeviceNum;
    }
}

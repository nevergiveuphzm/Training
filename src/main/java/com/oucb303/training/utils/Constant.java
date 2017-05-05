package com.oucb303.training.utils;

import android.os.Environment;

/**
 * Created by baichangcai on 2016/9/25.
 */
public class Constant
{
    //服务器域名,IP
    //public final static String SERVER_IP = "http://www.saflight.com.cn";
    public final static String SERVER_IP = "http://www.saflight.com.cn/saflight";

    public final static String LOG_TAG = "TRAINING_DEBUG";

    //app文件存储路径
    public final static String APP_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Saflight/";
    //下载路径
    public final static String DOWNLOAD_PATH = APP_STORAGE_PATH + "/Download/";
    public static String[] EXCELTITLE = { "项目名称", "学号", "总次数", "设备个数", "得分", "总时间", "分组数", "运动时间", "是否上传" };

}

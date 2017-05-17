package com.oucb303.training.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by huzhiming on 2016/12/20.
 */

public class FileUtils
{
    /**
     * 保存下载的文件
     */
    public static void saveFile(byte[] data, String fileName)
    {
        File temp = new File(Constant.DOWNLOAD_PATH);
        if (!temp.exists())
            temp.mkdirs();
        try
        {
            File file = new File(Constant.DOWNLOAD_PATH + fileName);
            file.deleteOnExit();
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();
        } catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
    }
   //获取文件
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
    //获取文件路径
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;

    }
}

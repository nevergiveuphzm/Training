package com.oucb303.training.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.oucb303.training.model.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huzhiming on 16/9/23.
 * Description：解析设备灯返回数据
 */

public class DataAnalyzeUtils
{
    //解析返回的电量信息
    public static List<Map<String, Object>> analyzePowerData(String data, Context context)
    {
        List<Map<String, Object>> powerInfos = new ArrayList<>();
        //低电量设备
        List<String> lowPowerDevice = new ArrayList<>();
        String data_time = data;
        Log.d(Constant.LOG_TAG, data);
        for (int i = 0; i < data_time.length(); i++)
        {
            if (data_time.charAt(i) == '#')
            {
                //数据总长度
                int digit = new Integer(data_time.substring(i + 1, i + 3));
                //设备编号
                String num = data_time.substring(i + 3, i + 4);
                //电量
                int power = new Integer(data_time.substring(i + 6, i + digit));
                System.out.println(digit + "-" + num + "-" + power);
                if (power >= 59)
                {
                    power = 59;
                }
                else if (power <= 49)
                {
                    lowPowerDevice.add(num);
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("deviceNum", num.charAt(0));
                map.put("power", power - 49);
                powerInfos.add(map);
            }
        }

        //存在低电量设备
        if (lowPowerDevice.size() > 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("警告");
            String unstr = lowPowerDevice.toString();
            builder.setMessage("                          " + unstr
                    + "号设备电量过低，请更换电池！\n");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                }
            });
            AlertDialog alertdialog = builder.create();
            alertdialog.setCancelable(false);
            alertdialog.show();
        }
        return powerInfos;
    }

    public static List<Map<String, Object>> analyzeTimeData(String data)
    {
        Log.d(Constant.LOG_TAG, "origin Data:" + data);
        List<Map<String, Object>> timeList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++)
        {
            if (data.charAt(i) == '#' && (data.length() - i) >= 7)
            {
                String str1 = data.substring(i + 1, i + 3);
                //数据总长度
                int digit = new Integer(str1);
                String str2 = data.substring(i + 3, i + 4);

                String str3 = data.substring(i + 6);
                //Log.d(Constant.LOG_TAG, str1 + "-" + str2 + "-" + str3);
                //设备编号
                String num = str2;

                String regex = "\\d*";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(str3);
                while (m.find())
                {
                    if (!"".equals(m.group()))
                    {
                        str3 = m.group();
                        break;
                    }
                }

                //时间
                int time = new Integer(str3);
                //Log.d(Constant.LOG_TAG, digit + "-" + num + "-" + time);
                Map<String, Object> map = new HashMap<>();
                map.put("deviceNum", num.charAt(0));
                map.put("time", time);
                timeList.add(map);
                break;
            }
        }
        return timeList;
    }
}

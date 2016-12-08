package com.oucb303.training.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.oucb303.training.model.Constant;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.model.TimeInfo;

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


/**
 * 解析返回的地址、电量、编号信息
 * <p>
 * 短地址:#09J@*32236
 * 电量: #09A@)90
 */
public class DataAnalyzeUtils
{
    //解析返回的电量信息
    public static List<DeviceInfo> analyzePowerData(String data)
    {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        //低电量设备
        //List<String> lowPowerDevice = new ArrayList<>();
        Log.d(Constant.LOG_TAG, "origin Power Data" + data);
        for (int i = 0; i < data.length(); i++)
        {
            if (data.charAt(i) == '#' && (data.length() - i) >= 7 && (data.charAt(i + 5) == '*' || data.charAt(i + 5) == '*'))
            {
                //设备编号
                char num = data.charAt(i + 3);
                String address = "";
                int power = 0;
                if (data.charAt(i + 5) == '*')
                {
                    address = data.substring(i + 6, i + 11);
                } else if (data.charAt(5) == ')')
                {
                    String temp = data.substring(i + 5);
                    String regex = "\\d*";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(temp);
                    while (m.find())
                    {
                        if (!"".equals(m.group()))
                        {
                            temp = m.group();
                            break;
                        }
                    }
                    //电量
                    power = new Integer(temp);

                    if (power >= 59)
                        power = 10;
                    else if (power <= 49)
                        power = 0;
                    else
                        power -= 49;
                }

                boolean exist = false;
                //检测设备是否已经添加到数组中
                for (DeviceInfo info : deviceInfos)
                {
                    if (info.getDeviceNum() == num)
                    {
                        exist = true;
                        if (data.charAt(i + 5) == '*')
                            info.setAddress(address);
                        else if (data.charAt(5) == ')')
                            info.setPower(power);
                        break;
                    }
                }
                if (!exist)
                {
                    DeviceInfo info = new DeviceInfo();
                    info.setDeviceNum(num);
                    info.setPower(power);
                    info.setAddress(address);
                    deviceInfos.add(info);
                    Log.d(Constant.LOG_TAG, info.toString());
                }
            }
        }

       /* //存在低电量设备
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
        }*/

        return deviceInfos;
    }

    public static List<TimeInfo> analyzeTimeData(String data)
    {
        Log.d(Constant.LOG_TAG, "origin Time Data:" + data);
        List<TimeInfo> timeList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++)
        {
            if (data.charAt(i) == '#' && (data.length() - i) >= 7 && data.charAt(i + 5) == '(')
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
                TimeInfo timeInfo = new TimeInfo();
                timeInfo.setDeviceNum(num.charAt(0));
                timeInfo.setTime(time);

                boolean flag = false;
                for (TimeInfo info : timeList)
                {
                    if (info.getDeviceNum() == timeInfo.getDeviceNum())
                    {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    timeList.add(timeInfo);
                //break;
            }
        }
        return timeList;
    }

    public static List<Map<String, String>> analyzeAddressData(String data)
    {
        Log.d(Constant.LOG_TAG, "origin address Data:" + data);
        List<Map<String, String>> res = new ArrayList<>();
        for (int i = 0; i < data.length(); i++)
        {
            if (data.charAt(i) == '#' && (data.length() - i) >= 7 && data.charAt(i + 5) == '*')
            {
                String address = data.substring(i + 6);
                String deviceNum = data.charAt(i + 3) + "";

                String regex = "\\d*";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(address);
                while (m.find())
                {
                    if (!"".equals(m.group()))
                    {
                        address = m.group();
                        break;
                    }
                }
                Map<String, String> map = new HashMap<>();
                map.put("deviceNum", deviceNum);
                map.put("address", address);
                res.add(map);
            }
        }
        return res;
    }

    //判断数据是否有效
    public static boolean dataIsValid(String data)
    {
        if (data.length() < 7)
            return false;
        char num = data.charAt(3);
        if (data.charAt(0) == '#' && ((num >= 'A' && num <= 'Z') || (num >= 'a' && num <= 'f')))
            return true;
        else
            return false;
    }

}

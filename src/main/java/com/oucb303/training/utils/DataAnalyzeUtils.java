package com.oucb303.training.utils;

import android.util.Log;

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
 * 电量: #09A@*1234598
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
            if (data.charAt(i) == '#' && (data.length() - i) >= 7 && data.charAt(i + 5) == '*')
            {
                //设备编号
                char num = data.charAt(i + 3);
                String address = data.substring(i + 6, i + 11);

                String temp = data.substring(i + 11);
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
                int power = new Integer(temp);

                power++;
                if (power >= 59)
                    power = 10;
                else if (power <= 49)
                    power = 0;
                else
                    power -= 49;

                boolean exist = false;
                //检测设备是否已经添加到数组中
                for (DeviceInfo info : deviceInfos)
                {
                    if (info.getDeviceNum() == num)
                    {
                        exist = true;
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

    /**
     * 解析返回来的时间数据
     * #A(128
     */

    public static List<TimeInfo> analyzeTimeData(String data)
    {
        Log.d(Constant.LOG_TAG, "origin Time Data:" + data);
        List<TimeInfo> timeList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++)
        {
            if (data.charAt(i) == '#' && (data.length() - i) >= 4 && data.charAt(i + 2) == '(')
            {

                //设备编号
                char num = data.charAt(i + 1);

                String str = data.substring(i + 3);

                String regex = "\\d*";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(str);
                while (m.find())
                {
                    if (!"".equals(m.group()))
                    {
                        str = m.group();
                        break;
                    }
                }

                //时间
                int time = new Integer(str);

                boolean flag = false;
                for (TimeInfo info : timeList)
                {
                    if (info.getDeviceNum() == num)
                    {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                {
                    TimeInfo timeInfo = new TimeInfo();
                    timeInfo.setDeviceNum(num);
                    timeInfo.setTime(time);
                    timeList.add(timeInfo);
                    Log.d(Constant.LOG_TAG, timeInfo.toString());
                }
                //break;
            }
        }
        return timeList;
    }

    /**
     * 解析返回的协调器panID
     * #)FF01
     */
    public static String analyzePAN_ID(String data)
    {
        for (int i = 0; i < data.length(); i++)
        {
            if (data.charAt(i) == '#' && data.length() >= 6 && data.charAt(1) == ')')
            {
                String panid = data.substring(i + 2, i + 6);
                return panid;
            }
        }
        return "";
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

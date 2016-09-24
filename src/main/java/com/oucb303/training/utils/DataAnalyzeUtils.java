package com.oucb303.training.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huzhiming on 16/9/23.
 * Description：解析设备灯返回数据
 */

public class DataAnalyzeUtils
{
    //解析返回的电量信息
    public static List<Map<String, Object>> analyzePowerData(String data)
    {
        List<Map<String, Object>> powerInfos = new ArrayList<>();
        String data_time = data;
        for (int i = 0; i < data_time.length(); i++)
        {
            if (data_time.charAt(i) == '#')
            {
                //数据总长度
                String digit = data_time.substring(i + 1, i + 3);
                //设备编号
                String num = data_time.substring(i + 3, i + 4);
                //电量
                int power = new Integer(data_time.substring(i + 6, i + 8));
                System.out.println(digit + "-" + num + "-" + power);
                if (power >= 59)
                {
                    power = 59;
                }
                else if (power <= 49)
                {
                    power = 49;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("deviceNum", num);
                map.put("power", power - 49);
                powerInfos.add(map);
            }
        }
        return powerInfos;
    }
}

package com.oucb303.training.device;

import android.util.Log;

import com.oucb303.training.utils.Constant;
import com.oucb303.training.model.DeviceInfo;

import java.util.Arrays;

/**
 * Created by huzhiming on 2016/11/10.
 * <p>
 * description:命令组合
 */

public class Order
{
    /*灯的颜色*/
    public static enum LightColor
    {
        /*无、蓝色、红色、绿色、蓝-红、*/
        NONE, BLUE, RED, GREEN, BLUE_RED,BLUE_GREEN,RED_GREEN,BLUE_RED_GREEN
    }

    /*蜂鸣器*/
    public static enum VoiceMode
    {
        /*无、短响、响一秒、响两秒*/
        NONE, SHORT, ONE, TWO
    }

    /*灯光模式*/
    public static enum LightModel
    {
        /*无、外圈、里圈、全部、关灯*/
        NONE, OUTER, CENTER, ALL, TURN_OFF
    }

    /*感应模式*/
    public static enum ActionModel
    {
        /*无、红外,触碰、全部、关闭*/
        NONE, LIGHT, TOUCH, ALL, TURN_OFF
    }

    /*闪烁模式*/
    public static enum BlinkModel
    {
        /*无、慢闪、快闪、先闪后亮*/
        NONE, SLOW, FAST, BLINK_AND_TURN_ON
    }

    /*感应毁灭时操作*/
    public static enum EndVoice
    {
        /*无、扑灭时响*/
        NONE, SHORT
    }


    /*获取命令*/
    public static String getOrder(char num, LightColor color, VoiceMode voiceMode, BlinkModel blinkModel,
                                  LightModel lightModel, ActionModel actionModel, EndVoice endVoice)
    {

        String order = "=";
        boolean exist = false;
        for (DeviceInfo info : Device.DEVICE_LIST)
        {
            if (info.getDeviceNum() == num)
            {
                order += info.getAddress();
                exist = true;
                break;
            }
        }
        if (!exist)
            return "";

        String str1 = orderToBinaryString(color.ordinal(), 3);
        String str2 = orderToBinaryString(voiceMode.ordinal(), 2);
        String str3 = orderToBinaryString(blinkModel.ordinal(), 2);

        String str4 = orderToBinaryString(lightModel.ordinal(), 3);
        String str5 = orderToBinaryString(actionModel.ordinal(), 3);
        String str6 = endVoice.ordinal() + "";

        String operation1 = "0" + str1 + str2 + str3;
        String operation2 = "0" + str4 + str5 + str6;

        char c1 = binaryStringToChar(operation1);
        char c2 = binaryStringToChar(operation2);
        Log.d(Constant.LOG_TAG, "C1:" + c1 + "  C2:" + c2);
        order = order + "" + c1 + c2;
        Log.d(Constant.LOG_TAG, "order: deviceNum(" + num + ") length(" + order.length() + ")  content(" + order + ")");
        return order;
    }

    //获取灯编号组合
    private static String getLightIds(String lightIds)
    {
        char[] lights = new char[35];
        Arrays.fill(lights, '0');
        for (int i = 0; i < lightIds.length(); i++)
        {
            char c = lightIds.charAt(i);
            //大写字母
            if (c >= 'A' && c <= 'Z')
            {
                lights[c - 'A'] = '1';
            } else if (c >= 'a' && c <= 'z')//小写字母
            {
                lights[c - 'a' + 26] = '1';
            }
        }
        String res = "";
        for (int i = 0; i < 5; i++)
        {
            String temp = "0";
            for (int j = 0; j < 7; j++)
                temp += lights[i * 7 + j];

            //Log.d("AAAA", "Light char " + i + " :" + temp);
            res += binaryStringToChar(temp);
        }
        return res;
    }

    /*8位二进制字符串转char*/
    private static char binaryStringToChar(String str)
    {
        int len = 8;

        int value = 0;
        for (int i = 0; i < len; i++)
        {
            if (str.charAt(i) == '0')
                value = value * 2;
            else if (str.charAt(i) == '1')
                value = value * 2 + 1;
        }
        //Log.d("AAAA", "values:" + value);
        return (char) value;
    }

    private static String orderToBinaryString(int order, int length)
    {
        String res = Integer.toBinaryString(order);
        int j = length - res.length();
        for (int i = 0; i < j; i++)
            res = "0" + res;
        return res;
    }


}
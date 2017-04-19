package com.oucb303.training.utils;

import java.util.Random;

/**
 * Created by huzhiming on 16/9/25.
 * Description：
 */

public class RandomUtils
{
    public static int getRandomNum(int max)
    {
        Random random = new Random();
        //random.nextInt(1000)表示生成0-1000之内的随机数，取模后是100以内随机数
        return random.nextInt(1000) % max;
    }
    /**
     * 转为对应字母
     * for example: "A"->"G"
     * @param num
     * @return
     */
    public static char charChange(char num){
        String str = null;
        switch (String.valueOf(num)){
            case "A":
                str = "G";
                break;
            case "B":
                str = "H";
                break;
            case "C":
                str = "I";
                break;
            case "D":
                str = "J";
                break;
            case "E":
                str = "K";
                break;
            case "F":
                str = "L";
                break;
        }
        return str.charAt(0);
    }
}

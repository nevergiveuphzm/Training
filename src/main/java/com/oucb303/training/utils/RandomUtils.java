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
}

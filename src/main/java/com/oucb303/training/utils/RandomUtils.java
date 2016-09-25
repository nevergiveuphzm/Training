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
        return random.nextInt(1000) % max;
    }
}

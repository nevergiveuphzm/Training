package com.oucb303.training.model;

import java.util.Comparator;

/**
 * Created by huzhiming on 16/10/12.
 * Description：
 */

public class PowerInfoComparetor implements Comparator
{
    @Override
    public int compare(Object object1, Object object2)
    {
        PowerInfo info1 = (PowerInfo) object1;
        PowerInfo info2 = (PowerInfo) object2;
        return info1.getDeviceNum() - info2.getDeviceNum();
    }
}

package com.oucb303.training.model;

/**
 * Created by huzhiming on 16/10/13.
 * Description：设备返回的时间
 */

public class TimeInfo
{
    //设备编号
    private char deviceNum;
    //返回的时间
    private int time;
    //数据是否有效
    private boolean valid = true;

    public char getDeviceNum()
    {
        return deviceNum;
    }

    public void setDeviceNum(char deviceNum)
    {
        this.deviceNum = deviceNum;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public boolean isValid()
    {
        return valid;
    }

    public void setValid(boolean valid)
    {
        this.valid = valid;
    }

    @Override
    public String toString()
    {
        return "TimeInfo{" +
                "deviceNum=" + deviceNum +
                ", time=" + time +
                ", valid=" + valid +
                '}';
    }
}

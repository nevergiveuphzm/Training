package com.oucb303.training.model;

/**
 * Created by huzhiming on 16/10/12.
 * Description：
 */

public class DeviceInfo
{
    public DeviceInfo()
    {

    }

    public DeviceInfo(char num)
    {
        this.deviceNum = num;
    }

    //设备编号
    private char deviceNum;
    //电量
    private int power;
    //短地址
    private String address;


    public char getDeviceNum()
    {
        return deviceNum;
    }

    public void setDeviceNum(char deviceNum)
    {
        this.deviceNum = deviceNum;
    }

    public int getPower()
    {
        return power;
    }

    public void setPower(int power)
    {
        this.power = power;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Override
    public String toString()
    {
        return "DeviceInfo{" +
                "deviceNum=" + deviceNum +
                ", power=" + power +
                ", address='" + address + '\'' +
                '}';
    }
}

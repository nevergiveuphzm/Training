package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by huzhiming on 16/10/8.
 * Description：序列编程中的设备灯亮参数
 */

@Entity
public class Light
{
    @Id(autoincrement = true)
    private Long id;
    private Long groupId;
    //设备编号
    private int num;
    private int deviceNum;
    //感应距离
    private int distance = 40;
    //超时时间 单位秒
    private int overTime = 5;
    //感应模式 1:触碰 2:感应 3:同时
    private int actionMode = 1;
    //灯光模式 1:外圈 2:里圈  3:全部
    private int lightMode = 1;
    //灯光颜色  1:蓝色 2:红色 3:平红
    private int lightColor = 1;

    @Override
    public String toString()
    {
        return "Light{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", num=" + num +
                ", distance=" + distance +
                ", overTime=" + overTime +
                ", actionMode=" + actionMode +
                ", lightMode=" + lightMode +
                '}';
    }

    @Generated(hash = 1635982966)
    public Light(Long id, Long groupId, int num, int deviceNum, int distance,
            int overTime, int actionMode, int lightMode, int lightColor) {
        this.id = id;
        this.groupId = groupId;
        this.num = num;
        this.deviceNum = deviceNum;
        this.distance = distance;
        this.overTime = overTime;
        this.actionMode = actionMode;
        this.lightMode = lightMode;
        this.lightColor = lightColor;
    }

    @Generated(hash = 1552532464)
    public Light()
    {
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getGroupId()
    {
        return this.groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public int getNum()
    {
        return this.num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getDistance()
    {
        return this.distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public int getOverTime()
    {
        return this.overTime;
    }

    public void setOverTime(int overTime)
    {
        this.overTime = overTime;
    }

    public int getActionMode()
    {
        return this.actionMode;
    }

    public void setActionMode(int actionMode)
    {
        this.actionMode = actionMode;
    }

    public int getLightMode()
    {
        return this.lightMode;
    }

    public void setLightMode(int lightMode)
    {
        this.lightMode = lightMode;
    }

    public int getLightColor()
    {
        return this.lightColor;
    }

    public void setLightColor(int lightColor)
    {
        this.lightColor = lightColor;
    }

    public int getDeviceNum() {
        return this.deviceNum;
    }

    public void setDeviceNum(int deviceNum) {
        this.deviceNum = deviceNum;
    }


}

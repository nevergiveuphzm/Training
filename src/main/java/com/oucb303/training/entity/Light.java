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
    //实际编号
    private String realNum;
    //设备编号
    private int num;

    //感应距离
    private int distance = 40;
    //超时时间 单位秒
    private int overTime = 5;
    //感应模式 0:触碰 1:感应 2:同时
    private int actionMode = 0;
    //灯光模式 0:中间 1:全部  2:外圈
    private int lightMode = 0;

    @Override
    public String toString()
    {
        return "Light{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", realNum='" + realNum + '\'' +
                ", num=" + num +
                ", distance=" + distance +
                ", overTime=" + overTime +
                ", actionMode=" + actionMode +
                ", lightMode=" + lightMode +
                '}';
    }

    @Generated(hash = 167120662)
    public Light(Long id, Long groupId, String realNum, int num, int distance,
            int overTime, int actionMode, int lightMode) {
        this.id = id;
        this.groupId = groupId;
        this.realNum = realNum;
        this.num = num;
        this.distance = distance;
        this.overTime = overTime;
        this.actionMode = actionMode;
        this.lightMode = lightMode;
    }
    @Generated(hash = 1552532464)
    public Light() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getGroupId() {
        return this.groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public String getRealNum() {
        return this.realNum;
    }
    public void setRealNum(String realNum) {
        this.realNum = realNum;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getDistance() {
        return this.distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
    public int getOverTime() {
        return this.overTime;
    }
    public void setOverTime(int overTime) {
        this.overTime = overTime;
    }
    public int getActionMode() {
        return this.actionMode;
    }
    public void setActionMode(int actionMode) {
        this.actionMode = actionMode;
    }
    public int getLightMode() {
        return this.lightMode;
    }
    public void setLightMode(int lightMode) {
        this.lightMode = lightMode;
    }


}

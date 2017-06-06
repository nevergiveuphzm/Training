package com.oucb303.training.model;

/**
 * Created by huzhiming on 16/9/22.
 * Description：设备灯
 */

public class Light
{
    //人为编号
    private int num;
    //是否勾选
    private boolean checked;
    //是否点中
    private boolean isChoosed;
    //灯的图片
    private int imageId;

    //亮灯参数 ===============
    //感应距离
    private int distance = 40;
    //超时时间
    private int overTime = 2;
    //感应模式
    private int actionMode = 1;
    //灯光模式
    private int lightMode = 1;
    //灯的颜色
    private int lightColor = 1;


    public Light(int imageId)
    {
        this.imageId = imageId;
    }

    public Light(int num, boolean checked, int imageId)
    {
        this.num = num;
        this.checked = checked;
        this.imageId = imageId;
    }

    public Light(int num, boolean checked)
    {
        this.num = num;
        this.checked = checked;
    }

    public int getImageId()
    {
        return imageId;
    }

    public void setImageId(int imageId)
    {
        this.imageId = imageId;
    }


    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public int getOverTime()
    {
        return overTime;
    }

    public void setOverTime(int overTime)
    {
        this.overTime = overTime;
    }

    public int getLightMode()
    {
        return lightMode;
    }

    public void setLightMode(int lightMode)
    {
        this.lightMode = lightMode;
    }

    public int getActionMode()
    {
        return actionMode;
    }

    public void setActionMode(int actionMode)
    {
        this.actionMode = actionMode;
    }

    public int getLightColor()
    {
        return lightColor;
    }

    public void setLightColor(int lightColor)
    {
        this.lightColor = lightColor;
    }

    public boolean isChoosed()
    {
        return isChoosed;
    }

    public void setChoosed(boolean choosed)
    {
        isChoosed = choosed;
    }

    @Override
    public String toString()
    {
        return "Light{" +
                ", num=" + num +
                ", checked=" + checked +
                ", imageId=" + imageId +
                ", distance=" + distance +
                ", overTime=" + overTime +
                ", actionMode=" + actionMode +
                ", lightMode=" + lightMode +
                ", lightColor=" + lightColor +
                '}';
    }
}

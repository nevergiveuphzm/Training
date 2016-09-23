package com.oucb303.training.device;

/**
 * Created by huzhiming on 16/9/22.
 * Description：设备灯
 */

public class Light
{
    //设备灯的实际编号
    private char realNo;
    //人为编号
    private int num;
    //灯的图片
    private int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Light(int imageId) {
        this.imageId = imageId;
    }
}

package com.oucb303.training.model;

/**
 * Created by huzhiming on 16/9/22.
 * Description：设备灯
 */

public class Light {
    //设备灯的实际编号
    private char realNo;
    //人为编号
    private int num;
    //是否被选中
    private boolean checked;
    //灯的图片
    private int imageId;

    public Light(int imageId) {
        this.imageId = imageId;
    }

    public Light(int num, boolean checked, int imageId) {
        this.num = num;
        this.checked = checked;
        this.imageId = imageId;
    }

    public Light(int num, boolean checked) {
        this.num = num;
        this.checked = checked;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public char getRealNo() {
        return realNo;
    }

    public void setRealNo(char realNo) {
        this.realNo = realNo;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

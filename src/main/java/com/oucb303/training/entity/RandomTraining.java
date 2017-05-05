package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by BaiChangCai on 2017/4/13.
 */
@Entity
public class RandomTraining {
    @Id(autoincrement = true)
    private Long id;
    //项目名称
    private String trainingName;
    //学生编号
    private String studentNum;
    //总次数
    private int totalTimes;
    //设备个数
    private int deviceNum;
    //总时间（毫秒）
    private int totalTime;
    //运动时间
    private String trainingTime;
    //是否已上传
    private boolean isUpload;



    @Generated(hash = 747434278)
    public RandomTraining() {
    }



    @Generated(hash = 1155218811)
    public RandomTraining(Long id, String trainingName, String studentNum, int totalTimes, int deviceNum, int totalTime, String trainingTime, boolean isUpload) {
        this.id = id;
        this.trainingName = trainingName;
        this.studentNum = studentNum;
        this.totalTimes = totalTimes;
        this.deviceNum = deviceNum;
        this.totalTime = totalTime;
        this.trainingTime = trainingTime;
        this.isUpload = isUpload;
    }



    @Override
    public String toString() {
        return "[" + id + "," +trainingName+","+ studentNum + "," + totalTimes + "," + deviceNum + "," + totalTime + "," + trainingTime + "，" + isUpload + "]";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public int getDeviceNum() {
        return this.deviceNum;
    }

    public void setDeviceNum(int deviceNum) {
        this.deviceNum = deviceNum;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }


    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public String getStudentNum() {
        return this.studentNum;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public String getTrainingName() {
        return this.trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }



    public String getTrainingTime() {
        return this.trainingTime;
    }



    public void setTrainingTime(String trainingTime) {
        this.trainingTime = trainingTime;
    }
}


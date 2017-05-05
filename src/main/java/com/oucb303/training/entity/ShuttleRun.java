package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by BaiChangCai on 2017/1/8.
 * Description:折返跑课程实体类
 */
@Entity
public class ShuttleRun {
    @Id(autoincrement = true)
    private Long id;

    //学生编号
    private String studentNum;
    //分组数
    private int groupNum;
    //训练强度：2【50*2】  4【50*4】  6【50*6】...
    private int intension;
    //总时间（毫秒）
    private int totalTime;
    //运动时间
    private String trainingTime;
    //是否已上传
    private boolean isUpload;

    @Generated(hash = 2132780409)
    public ShuttleRun() {
    }

    @Generated(hash = 1570938668)
    public ShuttleRun(Long id, String studentNum, int groupNum, int intension, int totalTime,
            String trainingTime, boolean isUpload) {
        this.id = id;
        this.studentNum = studentNum;
        this.groupNum = groupNum;
        this.intension = intension;
        this.totalTime = totalTime;
        this.trainingTime = trainingTime;
        this.isUpload = isUpload;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "["+id+","+studentNum+","+intension+","+groupNum+","+totalTime+","+trainingTime+"，"+isUpload+"]";
    }


    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStudentNum() {
        return this.studentNum;
    }
    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public int getTotalTime() {
        return this.totalTime;
    }
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getIntension() {
        return this.intension;
    }

    public void setIntension(int intension) {
        this.intension = intension;
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public int getGroupNum() {
        return this.groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public String getTrainingTime() {
        return this.trainingTime;
    }

    public void setTrainingTime(String trainingTime) {
        this.trainingTime = trainingTime;
    }





}

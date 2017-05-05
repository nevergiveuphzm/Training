package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by baichangcai on 2017/5/3.
 * 训练的基本数据，代表大部分训练项目
 */
@Entity
public class BaseTrainingData {
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
    //得分
    private int score;
    //总时间（毫秒）
    private int totalTime;
    //分组数
    private int groupNum;
    //运动时间
    private String trainingTime;
    //是否已上传
    private boolean isUpload;

    @Override
    public String toString() {
        return "[" + id + ","+trainingName+"," + studentNum + "," + totalTimes + "," + deviceNum +","+score+ "," + totalTime +","+groupNum +"," + trainingTime + "，" + isUpload + "]";
    }



    @Generated(hash = 616983474)
    public BaseTrainingData() {
    }



    @Generated(hash = 779557050)
    public BaseTrainingData(Long id, String trainingName, String studentNum, int totalTimes, int deviceNum, int score, int totalTime, int groupNum, String trainingTime,
            boolean isUpload) {
        this.id = id;
        this.trainingName = trainingName;
        this.studentNum = studentNum;
        this.totalTimes = totalTimes;
        this.deviceNum = deviceNum;
        this.score = score;
        this.totalTime = totalTime;
        this.groupNum = groupNum;
        this.trainingTime = trainingTime;
        this.isUpload = isUpload;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingName() {
        return this.trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getStudentNum() {
        return this.studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public int getTotalTimes() {
        return this.totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
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

    public int getGroupNum() {
        return this.groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public int getScores() {
        return this.score;
    }

    public void setScores(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    public String getTrainingTime() {
        return this.trainingTime;
    }



    public void setTrainingTime(String trainingTime) {
        this.trainingTime = trainingTime;
    }
}

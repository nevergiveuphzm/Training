package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by BaiChangCai on 2017/1/14.
 * Description:纵跳摸高数据实体
 */
@Entity
public class JumpHigh {
    @Id(autoincrement = true)
    private Long id;
    //学生编号
    private String studentNum;
    //得分
    private int score;
    //设备个数
    private int deviceNum;
    //总时间（毫秒）
    private int totalTime;
    //运动时间
    private Date trainingTime;
    //是否已上传
    private boolean isUpload;

    @Generated(hash = 16824627)
    public JumpHigh(Long id, String studentNum, int score, int deviceNum, int totalTime, Date trainingTime,
            boolean isUpload) {
        this.id = id;
        this.studentNum = studentNum;
        this.score = score;
        this.deviceNum = deviceNum;
        this.totalTime = totalTime;
        this.trainingTime = trainingTime;
        this.isUpload = isUpload;
    }
    @Generated(hash = 1173666394)
    public JumpHigh() {
    }

    @Override
    public String toString() {
        return "["+id+","+studentNum+","+score+","+totalTime+","+trainingTime+","+deviceNum+"，"+isUpload+"]";
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
    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
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
    public Date getTrainingTime() {
        return this.trainingTime;
    }
    public void setTrainingTime(Date trainingTime) {
        this.trainingTime = trainingTime;
    }
    public boolean getIsUpload() {
        return this.isUpload;
    }
    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }
}

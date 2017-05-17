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
    //分组数
    private int groupNum;
    //设备个数
    private int deviceNum;
    //总时间（毫秒）
    private int totalTime;
    //运动时间
    private String trainingTime;
    //是否已上传
    private boolean isUpload;


    @Generated(hash = 1173666394)
    public JumpHigh() {
    }

    @Generated(hash = 800887681)
    public JumpHigh(Long id, String studentNum, int score, int groupNum, int deviceNum, int totalTime, String trainingTime,
            boolean isUpload) {
        this.id = id;
        this.studentNum = studentNum;
        this.score = score;
        this.groupNum = groupNum;
        this.deviceNum = deviceNum;
        this.totalTime = totalTime;
        this.trainingTime = trainingTime;
        this.isUpload = isUpload;
    }

    @Override
    public String toString() {
        return "["+id+","+studentNum+","+score+","+groupNum+","+totalTime+","+trainingTime+","+deviceNum+"，"+isUpload+"]";
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

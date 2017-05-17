package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
/**
 * Created by BaiChangCai on 2017/4/10.
 */
@Entity
public class SitUps {
    @Id(autoincrement = true)
    private Long id;
    //项目名称
    private String trainingName;
    //学生编号
    private String studentNum;
    //得分
    private int score;
    //分组数
    private int groupNum;
    //总时间（毫秒）
    private int totalTime;
    //运动时间
    private String trainingTime;
    //是否已上传
    private boolean isUpload;

    @Generated(hash = 1710175482)
    public SitUps() {
    }
    @Generated(hash = 805512872)
    public SitUps(Long id, String trainingName, String studentNum, int score, int groupNum, int totalTime,
            String trainingTime, boolean isUpload) {
        this.id = id;
        this.trainingName = trainingName;
        this.studentNum = studentNum;
        this.score = score;
        this.groupNum = groupNum;
        this.totalTime = totalTime;
        this.trainingTime = trainingTime;
        this.isUpload = isUpload;
    }
    @Override
    public String toString() {
        return "["+id+","+trainingName+","+studentNum+","+score+","+groupNum+","+totalTime+","+trainingTime+", "+isUpload+"]";
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
    public String getTrainingName() {
        return this.trainingName;
    }
    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
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

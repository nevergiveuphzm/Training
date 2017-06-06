package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by HP on 2017/6/5.
 */
@Entity
public class WireNetConfront {
    @Id(autoincrement = true)
    private Long id;
    //项目名称
    private String trainingName;
    //学生编号
    private String StudentNum;
    //分组数
    private int groupNum;
    //得分
    private int score;
    //设备个数
    private int totalNum;
//    //总时间（毫秒）
//    private int totalTime;
    //运动时间
    private int trainingTime;
    //是否已上传
    private boolean isUpLoad;
    @Generated(hash = 654007047)
    public WireNetConfront(Long id, String trainingName, String StudentNum,
            int groupNum, int score, int totalNum, int trainingTime,
            boolean isUpLoad) {
        this.id = id;
        this.trainingName = trainingName;
        this.StudentNum = StudentNum;
        this.groupNum = groupNum;
        this.score = score;
        this.totalNum = totalNum;
        this.trainingTime = trainingTime;
        this.isUpLoad = isUpLoad;
    }
    @Generated(hash = 31794164)
    public WireNetConfront() {
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
        return this.StudentNum;
    }
    public void setStudentNum(String StudentNum) {
        this.StudentNum = StudentNum;
    }
    public int getGroupNum() {
        return this.groupNum;
    }
    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }
    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getTotalNum() {
        return this.totalNum;
    }
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
    public int getTrainingTime() {
        return this.trainingTime;
    }
    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }
    public boolean getIsUpLoad() {
        return this.isUpLoad;
    }
    public void setIsUpLoad(boolean isUpLoad) {
        this.isUpLoad = isUpLoad;
    }

    @Override
    public String toString() {
        return "WireNetConfront{" +
                "id=" + id +
                ", trainingName='" + trainingName + '\'' +
                ", StudentNum='" + StudentNum + '\'' +
                ", groupNum=" + groupNum +
                ", score=" + score +
                ", totalNum=" + totalNum +
                ", trainingTime=" + trainingTime +
                ", isUpLoad=" + isUpLoad +
                '}';
    }
}

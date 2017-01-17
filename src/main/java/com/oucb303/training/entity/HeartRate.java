package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by BaiChangCai on 2017/1/8.
 * Description:心率实体类
 */
@Entity
public class HeartRate {
    @Id(autoincrement = true)
    private Long id;
    //运动课程编号
    @NotNull
    private Long courseId;
    //心率
    @NotNull
    private int rate;
    //运动时间
    @NotNull
    private Date trainingTime;
    @Generated(hash = 1519060964)
    public HeartRate(Long id, @NotNull Long courseId, int rate,
            @NotNull Date trainingTime) {
        this.id = id;
        this.courseId = courseId;
        this.rate = rate;
        this.trainingTime = trainingTime;
    }
    @Generated(hash = 1430820581)
    public HeartRate() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCourseId() {
        return this.courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public int getRate() {
        return this.rate;
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
    public Date getTrainingTime() {
        return this.trainingTime;
    }
    public void setTrainingTime(Date trainingTime) {
        this.trainingTime = trainingTime;
    }
    
}

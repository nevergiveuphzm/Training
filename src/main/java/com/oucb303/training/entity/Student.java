package com.oucb303.training.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by BaiChangCai on 2017/1/8.
 * Description:学生实体类
 */
@Entity
public class Student {
    @Id(autoincrement = true)
    private Long id;
    //学生编号
    @NotNull
    @Unique
    private String studentNum;
    //学生姓名
    private String studentName;
    //性别
    private String sex;
    //年龄
    private int age;
    //学校
    private String school;
    //班级
    private String classRoom;
    @Generated(hash = 1422656352)
    public Student(Long id, @NotNull String studentNum, String studentName,
            String sex, int age, String school, String classRoom) {
        this.id = id;
        this.studentNum = studentNum;
        this.studentName = studentName;
        this.sex = sex;
        this.age = age;
        this.school = school;
        this.classRoom = classRoom;
    }
    @Generated(hash = 1556870573)
    public Student() {
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
    public String getStudentName() {
        return this.studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSchool() {
        return this.school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getClassRoom() {
        return this.classRoom;
    }
    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }



}

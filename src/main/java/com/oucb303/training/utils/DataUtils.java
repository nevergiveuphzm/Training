package com.oucb303.training.utils;

import android.content.Context;
import android.widget.Toast;

import com.oucb303.training.App;
import com.oucb303.training.daoservice.BaseTrainingDataSer;
import com.oucb303.training.daoservice.DribblingGameSer;
import com.oucb303.training.daoservice.JumpHighSer;
import com.oucb303.training.daoservice.RandomTrainingSer;
import com.oucb303.training.daoservice.ShuttleRunSer;
import com.oucb303.training.daoservice.SitUpsSer;
import com.oucb303.training.entity.BaseTrainingData;
import com.oucb303.training.entity.DribblingGame;
import com.oucb303.training.entity.JumpHigh;
import com.oucb303.training.entity.RandomTraining;
import com.oucb303.training.entity.ShuttleRun;
import com.oucb303.training.entity.SitUps;

import org.greenrobot.greendao.annotation.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baichangcai on 2017/5/3.
 */
public class DataUtils {
    //平均值
    public static int getAvg(int[] scores){
        int sum = 0;
        for(int i = 0;i<scores.length;i++){
            sum+=scores[i];
        }
        return sum/scores.length;
    }
    //总时间
    public static int getSum(int[] scores){
        int sum = 0;
        for(int i = 0;i<scores.length;i++){
            sum+=scores[i];
        }
        return sum;
    }
    //装入查询出的数据
    public static ArrayList<ArrayList<String>> getTrainingData(String startTime, String endTime) {
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        BaseTrainingDataSer baseTrainingDataSer = new BaseTrainingDataSer(App.getDaoSession());
        DribblingGameSer dribblingGameSer = new DribblingGameSer(App.getDaoSession());
        JumpHighSer jumpHighSer = new JumpHighSer(App.getDaoSession());
        SitUpsSer sitUpsSer = new SitUpsSer(App.getDaoSession());
        ShuttleRunSer shuttleRunSer = new ShuttleRunSer(App.getDaoSession());
        RandomTrainingSer randomTrainingSer = new RandomTrainingSer(App.getDaoSession());

        List<DribblingGame> dribblingGameList = dribblingGameSer.selectByTimeSpan("where TRAINING_TIME between ? and ?",new String[]{startTime,endTime});
        List<BaseTrainingData> baseTrainingDataList =baseTrainingDataSer.selectByTimeSpan("where TRAINING_TIME between ? and ?",new String[]{startTime,endTime});
        List<JumpHigh> jumpHighList = jumpHighSer.selectByTimeSpan("where TRAINING_TIME between ? and ?",new String[]{startTime,endTime});
        List<SitUps> sitUpsList = sitUpsSer.selectByTimeSpan("where TRAINING_TIME between ? and ?",new String[]{startTime,endTime});
        List<ShuttleRun> shuttleRunList = shuttleRunSer.selectByTimeSpan("where TRAINING_TIME between ? and ?",new String[]{startTime,endTime});
        List<RandomTraining> randomTrainingList = randomTrainingSer.selectByTimeSpan("where TRAINING_TIME between ? and ?",new String[]{startTime,endTime});
        for(int i=0;i<randomTrainingList.size();i++){
            ArrayList<String> beanList=new ArrayList<>();
            beanList.add(randomTrainingList.get(i).getTrainingName());
            beanList.add(randomTrainingList.get(i).getStudentNum());
            beanList.add(randomTrainingList.get(i).getTotalTimes()+"");
            beanList.add(randomTrainingList.get(i).getDeviceNum()+"");
            beanList.add(randomTrainingList.get(i).getTotalTime()+"");
            beanList.add(randomTrainingList.get(i).getTotalTime()+"");
            beanList.add(1+"");
            beanList.add(randomTrainingList.get(i).getTrainingTime()+"");
            beanList.add(randomTrainingList.get(i).getIsUpload()+"");
            dataList.add(beanList);
        }
        for(int i=0;i<shuttleRunList.size();i++){
            ArrayList<String> beanList=new ArrayList<>();
            beanList.add("折返跑训练");
            beanList.add(shuttleRunList.get(i).getStudentNum());
            beanList.add(shuttleRunList.get(i).getIntension()+"");
            beanList.add(shuttleRunList.get(i).getGroupNum()+"");
            beanList.add(shuttleRunList.get(i).getTotalTime()+"");
            beanList.add(shuttleRunList.get(i).getTotalTime()+"");
            beanList.add(shuttleRunList.get(i).getGroupNum()+"");
            beanList.add(shuttleRunList.get(i).getTrainingTime()+"");
            beanList.add(shuttleRunList.get(i).getIsUpload()+"");
            dataList.add(beanList);
        }
        for(int i=0;i<sitUpsList.size();i++){
            ArrayList<String> beanList=new ArrayList<>();
            beanList.add(sitUpsList.get(i).getTrainingName());
            beanList.add(sitUpsList.get(i).getStudentNum());
            beanList.add(sitUpsList.get(i).getScore()+"");
            beanList.add(2*sitUpsList.get(i).getGroupNum()+"");
            beanList.add(sitUpsList.get(i).getScore()+"");
            beanList.add(sitUpsList.get(i).getTotalTime()+"");
            beanList.add(sitUpsList.get(i).getGroupNum()+"");
            beanList.add(sitUpsList.get(i).getTrainingTime()+"");
            beanList.add(sitUpsList.get(i).getIsUpload()+"");
            dataList.add(beanList);
        }
        for(int i=0;i<jumpHighList.size();i++){
            ArrayList<String> beanList=new ArrayList<>();
            beanList.add("纵跳摸高");
            beanList.add(jumpHighList.get(i).getStudentNum());
            beanList.add(jumpHighList.get(i).getScore()+"");
            beanList.add(jumpHighList.get(i).getDeviceNum()+"");
            beanList.add(jumpHighList.get(i).getScore()+"");
            beanList.add(jumpHighList.get(i).getTotalTime()+"");
            beanList.add(jumpHighList.get(i).getGroupNum()+"");
            beanList.add(jumpHighList.get(i).getTrainingTime()+"");
            beanList.add(jumpHighList.get(i).getIsUpload()+"");
            dataList.add(beanList);
        }
        for(int i=0;i<dribblingGameList.size();i++){
            ArrayList<String> beanList=new ArrayList<>();
            beanList.add(dribblingGameList.get(i).getTrainingName());
            beanList.add(dribblingGameList.get(i).getStudentNum());
            beanList.add(dribblingGameList.get(i).getScore()+"");
            beanList.add(dribblingGameList.get(i).getDeviceNum()+"");
            beanList.add(dribblingGameList.get(i).getScore()+"");
            beanList.add(dribblingGameList.get(i).getTotalTime()+"");
            beanList.add(dribblingGameList.get(i).getGroupNum()+"");
            beanList.add(dribblingGameList.get(i).getTrainingTime()+"");
            beanList.add(dribblingGameList.get(i).getIsUpload()+"");
            dataList.add(beanList);
        }
        for(int i=0;i<baseTrainingDataList.size();i++){
            ArrayList<String> beanList=new ArrayList<>();
            beanList.add(baseTrainingDataList.get(i).getTrainingName());
            beanList.add(baseTrainingDataList.get(i).getStudentNum());
            beanList.add(baseTrainingDataList.get(i).getTotalTimes()+"");
            beanList.add(baseTrainingDataList.get(i).getDeviceNum()+"");
            beanList.add(baseTrainingDataList.get(i).getScore()+"");
            beanList.add(baseTrainingDataList.get(i).getTotalTime()+"");
            beanList.add(baseTrainingDataList.get(i).getGroupNum()+"");
            beanList.add(baseTrainingDataList.get(i).getTrainingTime()+"");
            beanList.add(baseTrainingDataList.get(i).getIsUpload()+"");
            dataList.add(beanList);
        }
        return dataList;
    }
}

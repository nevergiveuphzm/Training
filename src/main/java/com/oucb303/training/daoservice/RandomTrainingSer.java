package com.oucb303.training.daoservice;

import com.oucb303.training.entity.DaoSession;

import com.oucb303.training.entity.RandomTrainingDao;
import com.oucb303.training.entity.RandomTraining;

import java.util.List;
/**
 * Created by BaiChangCai on 2017/4/13.
 * * Description:增删改查  换物跑数据
 */

public class RandomTrainingSer {
    private RandomTrainingDao randomTrainingDao;
    public RandomTrainingSer(DaoSession session){randomTrainingDao = session.getRandomTrainingDao();}
    //添加运球比赛数据
    public long addRandomTraining( RandomTraining randomTraining){
        long resId = randomTrainingDao.insert(randomTraining);
        return resId;
    }
    //查询所有数据
    public List< RandomTraining> loadAllRandomTraining(){
        return randomTrainingDao.loadAll();
    }
    //查询某一时间段内的数据
    public List<RandomTraining> selectByTimeSpan(String where,String[] strings){
        return randomTrainingDao.queryRaw(where,strings);
    }
    //删除所有数据
    public void deleteAll(){
        randomTrainingDao.deleteAll();
    }

}

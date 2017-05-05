package com.oucb303.training.daoservice;

import com.oucb303.training.entity.BaseTrainingData;
import com.oucb303.training.entity.BaseTrainingDataDao;
import com.oucb303.training.entity.DaoSession;


import java.util.List;

/**
 * Created by baichangcai on 2017/5/3.
 * 增删改查训练数据
 */
public class BaseTrainingDataSer {
    private BaseTrainingDataDao baseTrainingDataDao;
    public BaseTrainingDataSer(DaoSession session){baseTrainingDataDao = session.getBaseTrainingDataDao();}
    //添加运球比赛数据
    public long addBaseTrainingData( BaseTrainingData baseTrainingData){
        long resId = baseTrainingDataDao.insert(baseTrainingData);
        return resId;
    }
    //查询所有数据
    public List< BaseTrainingData> loadAllBaseTrainingData(){
        return baseTrainingDataDao.loadAll();
    }
    //查询某一时间段内的数据
    public List<BaseTrainingData> selectByTimeSpan(String where,String[] strings){
        return baseTrainingDataDao.queryRaw(where,strings);
    }
    //删除所有数据
    public void deleteAll(){
        baseTrainingDataDao.deleteAll();
    }
}

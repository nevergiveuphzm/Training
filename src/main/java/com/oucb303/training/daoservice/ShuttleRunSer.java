package com.oucb303.training.daoservice;

import com.oucb303.training.entity.DaoSession;
import com.oucb303.training.entity.Sequence;
import com.oucb303.training.entity.ShuttleRun;
import com.oucb303.training.entity.ShuttleRunDao;

import java.util.List;

/**
 * Created by BaiChangCai on 2017/1/8.
 * Description:增删改查   折返跑数据
 */
public class ShuttleRunSer {
    private ShuttleRunDao shuttleRunDao;
    public ShuttleRunSer(DaoSession session){
         shuttleRunDao = session.getShuttleRunDao();
    }
    //添加折返跑数据
    public long addShuttleRun(ShuttleRun shuttleRun){
        long resId = shuttleRunDao.insert(shuttleRun);
        return resId;
    }
    //查询所有数据
    public List<ShuttleRun> loadAllShuttleRun()
    {
        return shuttleRunDao.loadAll();
    }
    //查询某一时间段内的数据
    public List<ShuttleRun> selectByTimeSpan(String where,String[] strings){
        return shuttleRunDao.queryRaw(where,strings);
    }
    //清空所有数据
    public void deleteAll(){
        shuttleRunDao.deleteAll();
    }
}

package com.oucb303.training.daoservice;

import com.oucb303.training.entity.DaoSession;
import com.oucb303.training.entity.DribblingGame;
import com.oucb303.training.entity.DribblingGameDao;

import java.util.List;
/**
 * Created by 40799_000 on 2017/4/10.
 * Description:增删改查   运球比赛数据
 */

public class DribblingGameSer {
    private DribblingGameDao dribblingGameDao;
    public DribblingGameSer(DaoSession session){dribblingGameDao = session.getDribblingGameDao();}
    //添加仰卧起坐数据
    public long addDribblingGame(DribblingGame dribblingGame){
        long resId = dribblingGameDao.insert(dribblingGame);
        return resId;
    }
    //查询所有仰卧起坐数据
    public List<DribblingGame> loadAllDribblingGame(){
        return dribblingGameDao.loadAll();
    }
    //查询某一时间段内的数据
    public List<DribblingGame> selectByTimeSpan(String where,String[] strings){
        return dribblingGameDao.queryRaw(where,strings);
    }
    //删除所有数据
    public void deleteAll(){
        dribblingGameDao.deleteAll();
    }
}

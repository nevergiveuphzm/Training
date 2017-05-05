package com.oucb303.training.daoservice;

import com.oucb303.training.entity.DaoSession;
import com.oucb303.training.entity.SitUps;
import com.oucb303.training.entity.SitUpsDao;


import java.util.List;

/**
 * Created by BaiChangCai on 2017/4/10.
 * Description:增删改查   仰卧起坐数据
 */

public class SitUpsSer {
    private SitUpsDao sitUpsDao;
    public SitUpsSer(DaoSession session){
        sitUpsDao = session.getSitUpsDao();
    }
    //添加折返跑数据
    public long addSitUps(SitUps sitUps){
        long resId = sitUpsDao.insert(sitUps);
        return resId;
    }
    //查询所有数据
    public List<SitUps> loadAllSitUps()
    {
        return sitUpsDao.loadAll();
    }
    //查询某一时间段内的数据
    public List<SitUps> selectByTimeSpan(String where,String[] strings){
        return sitUpsDao.queryRaw(where,strings);
    }
    //清空所有数据
    public void deleteAll(){
        sitUpsDao.deleteAll();
    }
}

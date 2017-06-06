package com.oucb303.training.daoservice;

import com.oucb303.training.entity.DaoSession;
import com.oucb303.training.entity.WireNetConfront;
import com.oucb303.training.entity.WireNetConfrontDao;


import java.util.List;

/**
 * Created by HP on 2017/6/5.
 */
public class WireNetConfrontSer {
    private WireNetConfrontDao wireNetConfrontDao;

    public WireNetConfrontSer(DaoSession session) {
        wireNetConfrontDao = session.getWireNetConfrontDao();
    }
    //添加隔网对抗数据
    public long addWireNetConfront(WireNetConfront wireNetConfront){
        long resId = wireNetConfrontDao.insert(wireNetConfront);
        return resId;
    }
    //查询所有隔网对抗数据
    public List<WireNetConfront> loadAllWireNetConfront(){
        return wireNetConfrontDao.loadAll();
    }
    //查询某一段时间的数据
    public List<WireNetConfront> queryByTimeSpan(String where,String[] strings)
    {
        return wireNetConfrontDao.queryRaw(where,strings);
    }
    //删除所有数据
    public void deleteAll(){
        wireNetConfrontDao.deleteAll();
    }
}

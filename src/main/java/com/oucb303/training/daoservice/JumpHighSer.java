package com.oucb303.training.daoservice;

import com.oucb303.training.entity.DaoSession;
import com.oucb303.training.entity.JumpHigh;
import com.oucb303.training.entity.JumpHighDao;

import java.util.List;

/**
 * Created by BaiChangCai on 2017/1/14.
 * Description:增删改查  纵跳摸高数据
 */
public class JumpHighSer {
    private JumpHighDao jumpHighDao;
    public JumpHighSer(DaoSession session){jumpHighDao = session.getJumpHighDao();}
    //添加仰卧起坐数据
    public long addJumpHigh(JumpHigh jumpHigh){
        long resId = jumpHighDao.insert(jumpHigh);
        return resId;
    }
    //查询所有仰卧起坐数据
    public List<JumpHigh> loadAllShuttleRun(){
        return jumpHighDao.loadAll();
    }
    //查询某一时间段内的数据
    public List<JumpHigh> selectByTimeSpan(String where,String[] strings){
        return jumpHighDao.queryRaw(where,strings);
    }
    //删除所有数据
    public void deleteAll(){
        jumpHighDao.deleteAll();
    }
}

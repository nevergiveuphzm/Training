package com.oucb303.training.daoservice;

import com.oucb303.training.entity.DaoSession;
import com.oucb303.training.entity.LightDao;
import com.oucb303.training.entity.Sequence;
import com.oucb303.training.entity.SequenceDao;
import com.oucb303.training.entity.SequenceGroup;
import com.oucb303.training.entity.SequenceGroupDao;
import com.oucb303.training.model.Light;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huzhiming on 16/10/8.
 * Description：
 */

public class SequenceSer
{
    private SequenceDao sequenceDao;
    private SequenceGroupDao sequenceGroupDao;
    private LightDao lightDao;

    public SequenceSer(DaoSession session)
    {
        sequenceDao = session.getSequenceDao();
        sequenceGroupDao = session.getSequenceGroupDao();
        lightDao = session.getLightDao();
    }

    //添加序列
    public long addSequence(List<Map<String, Object>> list, String seqName)
    {
        Sequence sequence = new Sequence();
        sequence.setName(seqName);
        sequence.setCreateTime(new Date());

        QueryBuilder<Sequence> builder = sequenceDao.queryBuilder();
        List<Sequence> res = builder.where(SequenceDao.Properties.Name.eq(seqName)).list();
        if (res != null && res.size() > 0)
            return -1;
        long seqId = sequenceDao.insert(sequence);
        for (Map<String, Object> map : list)
        {
            if (map != null)
            {
                SequenceGroup group = new SequenceGroup();
                group.setStep((int) map.get("step_name"));
                int delayTime = (int) (new Double(map.get("delay_time").toString()) * 1000);
                group.setDelayTime(delayTime);
                group.setSeqId(seqId);
                long groupId = sequenceGroupDao.insert(group);
                List<Light> lights = (List<Light>) map.get("list_light");
                for (Light light : lights)
                {
                    com.oucb303.training.entity.Light enLight = new com.oucb303.training
                            .entity.Light();
                    enLight.setNum(light.getNum());
                    enLight.setRealNum(light.getRealNo() + "");

                    enLight.setActionMode(light.getActionMode());
                    enLight.setLightMode(light.getLightMode());
                    enLight.setDistance(light.getDistance());
                    enLight.setOverTime(light.getOverTime());
                    enLight.setLightColor(light.getLightColor());
                    enLight.setGroupId(groupId);
                    lightDao.insert(enLight);
                }
            }
        }
        return seqId;
    }

    //加载所有序列
    public List<Sequence> loadSequences()
    {
        QueryBuilder<Sequence> builder = sequenceDao.queryBuilder();
        List<Sequence> list = builder.orderDesc(SequenceDao.Properties.CreateTime).list();
        return list;
    }

    //删除序列
    public boolean delSequence(long sequenceId)
    {
        sequenceDao.deleteByKey(sequenceId);
        return false;
    }

    //加载某一序列的全部序列
    public List<SequenceGroup> loadSequenceGroups(long sequenceId)
    {
        QueryBuilder<SequenceGroup> builder = sequenceGroupDao.queryBuilder();
        builder = builder.where(SequenceGroupDao.Properties.SeqId.eq(sequenceId)).orderAsc(SequenceGroupDao.Properties.Step);
        return builder.list();
    }

}

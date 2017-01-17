package com.oucb303.training.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by huzhiming on 16/10/8.
 * Description：序列编程中的组
 */

@Entity
public class SequenceGroup
{
    @Id(autoincrement = true)
    private Long id;
    private Long seqId;
    //延迟时间 毫秒
    private int delayTime;
    //第几步
    private int step;
    @ToMany(referencedJoinProperty = "groupId")
    private List<Light> lights;

    @Override
    public String toString()
    {
        return "SequenceGroup{" +
                "id=" + id +
                ", seqId=" + seqId +
                ", delayTime=" + delayTime +
                ", step=" + step +
                ", lights=" + lights +
                '}';
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 222774766)
    private transient SequenceGroupDao myDao;
    @Generated(hash = 860685800)
    public SequenceGroup(Long id, Long seqId, int delayTime, int step) {
        this.id = id;
        this.seqId = seqId;
        this.delayTime = delayTime;
        this.step = step;
    }
    @Generated(hash = 934461468)
    public SequenceGroup() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSeqId() {
        return this.seqId;
    }
    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }
    public int getDelayTime() {
        return this.delayTime;
    }
    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }
    public int getStep() {
        return this.step;
    }
    public void setStep(int step) {
        this.step = step;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1900420288)
    public List<Light> getLights() {
        if (lights == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LightDao targetDao = daoSession.getLightDao();
            List<Light> lightsNew = targetDao._querySequenceGroup_Lights(id);
            synchronized (this) {
                if (lights == null) {
                    lights = lightsNew;
                }
            }
        }
        return lights;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 732139707)
    public synchronized void resetLights() {
        lights = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1985717169)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSequenceGroupDao() : null;
    }

}

package com.oucb303.training.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;

/**
 * Created by huzhiming on 16/10/8.
 * Description：序列编程中的序列
 */

@Entity
public class Sequence
{
    //自增
    @Id(autoincrement = true)
    private Long id;
    //序列名称
    @NotNull
    @Unique
    private String name;
    //创建时间
    private Date createTime;
    @ToMany(referencedJoinProperty = "seqId")
    List<SequenceGroup> groups;

    @Override
    public String toString()
    {
        return "Sequence{" +
                "groups=" + groups+
                ", createTime=" + createTime +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 515127902)
    private transient SequenceDao myDao;

    @Generated(hash = 527297527)
    public Sequence(Long id, @NotNull String name, Date createTime) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
    }

    @Generated(hash = 1298691442)
    public Sequence() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1854758143)
    public List<SequenceGroup> getGroups() {
        if (groups == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SequenceGroupDao targetDao = daoSession.getSequenceGroupDao();
            List<SequenceGroup> groupsNew = targetDao._querySequence_Groups(id);
            synchronized (this) {
                if (groups == null) {
                    groups = groupsNew;
                }
            }
        }
        return groups;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 464128061)
    public synchronized void resetGroups() {
        groups = null;
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
    @Generated(hash = 86850749)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSequenceDao() : null;
    }


}

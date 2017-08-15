package com.oucb303.training;

import android.app.Application;

import com.oucb303.training.device.Device;
import com.oucb303.training.entity.DaoMaster;
import com.oucb303.training.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by huzhiming on 16/10/7.
 * Description：
 */

public class App extends Application
{
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;

    private static DaoSession daoSession;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Device.CONTEXT = getApplicationContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ?
                "training-db-encrypted.db" : "training-db.db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession()
    {
        return daoSession;
    }
}
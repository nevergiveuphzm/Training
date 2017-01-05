package com.oucb303.training.threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ftdi.j2xx.FT_Device;
import com.oucb303.training.utils.Constant;

/**
 * Created by huzhiming on 16/9/7.
 * Description：
 */
public class ReceiveThread extends Thread
{
    private Handler handler;
    // 电量接收/时间接收标志
    private int threadFlag;
    // 消息标志
    private int msgFlag;
    //  设备
    private FT_Device ftDev;
    //线程标志 电量检测线程
    public final static int POWER_RECEIVE_THREAD = 1;
    // 监听设备返回时间线程
    public final static int TIME_RECEIVE_THREAD = 2;
    // 接收返回协调器PANID 线程
    public final static int PAN_ID_THREAD = 3;
    //  清除串口线程
    public final static int CLEAR_DATA_THREAD = 4;
    //  标志false 线程停止
    public static boolean THREAD_RUNNING_FLAG = false;

    public ReceiveThread(Handler handler, FT_Device device, int threadFlag, int msgFlag)
    {
        this.handler = handler;
        this.ftDev = device;
        this.threadFlag = threadFlag;
        this.msgFlag = msgFlag;
    }

    @Override
    public void run()
    {
        //检测电量线程
        if (threadFlag == POWER_RECEIVE_THREAD)
        {
            Timer.sleep(2000);
            readData();
        } else if (threadFlag == PAN_ID_THREAD)
        {
            Timer.sleep(500);
            readData();
        } else if (threadFlag == CLEAR_DATA_THREAD)
        {
            readData();
        }
        //接收设备返回时间线程
        else if (threadFlag == TIME_RECEIVE_THREAD)
        {
            THREAD_RUNNING_FLAG = true;

            while (THREAD_RUNNING_FLAG)
            {
                readData();
                try
                {
                    Thread.sleep(5);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    public void readData()
    {
        synchronized (ftDev)
        {
            int iavailable = ftDev.getQueueStatus();
            byte[] readData;
            String result = "";
            iavailable = iavailable - iavailable % 17;
            if (iavailable > 0)
            {
                readData = new byte[iavailable];
                ftDev.read(readData, iavailable);
                //返回的结果转String
                result = new String(readData);
                //Log.i("AAAA", result);
                Log.d(Constant.LOG_TAG, "origin Data: length(" + iavailable + ")  data:" + result);
            }
            Message msg = Message.obtain();
            msg.what = msgFlag;
            msg.obj = result;
            handler.sendMessage(msg);
        }
    }

    public static void stopThread()
    {
        THREAD_RUNNING_FLAG = false;
    }

}

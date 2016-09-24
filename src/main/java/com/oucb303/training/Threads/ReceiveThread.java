package com.oucb303.training.threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ftdi.j2xx.FT_Device;

/**
 * Created by huzhiming on 16/9/7.
 * Description：
 */
public class ReceiveThread extends Thread implements Runnable
{
    private Handler handler;
    // 电量接收/时间接收标志
    private int threadFlag;
    // 消息标志
    private int msgFlag;
    //  设备
    private FT_Device ftDev;
    //线程标志 电量检测和设备返回时间线程
    public final static int POWER_RECEIVE_THREAD = 1;
    public final static int TIME_RECEIVE_THREAD = 2;

    //标志为1 线程停止
    private int threadStopFlag = 0;

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
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
            readData();
        }
        //接收设备返回时间线程
        else if (threadFlag == TIME_RECEIVE_THREAD)
        {
            while (threadStopFlag == 0)
            {
                readData();
                try
                {
                    Thread.sleep(15);
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
            if (iavailable > 0)
            {
                Log.i("AAAA", "iavailable=" + iavailable);
                readData = new byte[iavailable];
                ftDev.read(readData, iavailable);
                //返回的结果转String
                result = new String(readData);
                Log.i("AAAA", result);
            }
            else
            {
                System.out.println("iavailable=" + iavailable);
            }
            Message msg = new Message();
            msg.what = msgFlag;
            msg.obj = result;
            handler.sendMessage(msg);
        }
    }

    public void stopThread()
    {
        threadStopFlag = 1;
    }
}

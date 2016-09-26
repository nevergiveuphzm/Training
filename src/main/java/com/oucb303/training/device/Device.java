package com.oucb303.training.device;

import android.content.Context;
import android.util.Log;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huzhiming on 16/9/7.
 * Description：
 */
public class Device
{
    //设备灯列表
    public static List<Map<String, Object>> DEVICE_LIST = new ArrayList<>();

    public  FT_Device ftDev;
    private  D2xxManager ftdid2xx;

    //设备数量
    public int devCount;
    //是否已经初始化
    private boolean isConfiged;
    private int index;

    /* local variables 设备参数 */
    int baudRate = 115200; /* baud rate */
    byte stopBit = 1; /* 1:1stop bits, 2:2 stop bits */
    byte dataBit = 8; /* 8:8bit, 7: 7bit */
    byte parity = 0; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */

    public Device(Context context)
    {
        try
        {
            ftdid2xx = D2xxManager.getInstance(context);
        } catch (D2xxManager.D2xxException e)
        {
            e.printStackTrace();
        }
    }

    // 关闭activity时调用该方法
    public void disconnectFunction()
    {
        devCount = -1;
        //currentIndex = -1;
        try
        {
            Thread.sleep(50);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        if (ftDev != null)
        {
            synchronized (ftDev)
            {
                if (true == ftDev.isOpen())
                {
                    ftDev.close();
                }
            }
        }
    }

    public void initConfig()
    {
        if (ftDev.isOpen() == false)
        {
            Log.e("j2xx", "SetConfig: device not open");
            return;
        }
        ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
        ftDev.setBaudRate(baudRate);
        dataBit = D2xxManager.FT_DATA_BITS_8;
        stopBit = D2xxManager.FT_STOP_BITS_1;
        parity = D2xxManager.FT_PARITY_NONE;
        ftDev.setDataCharacteristics(dataBit, stopBit, parity);
        short flowCtrlSetting;
        flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
        ftDev.setFlowControl(flowCtrlSetting, (byte) 0x0b, (byte) 0x0d);
        isConfiged = true;
    }

    //连接
    public void connectFunction(Context context)
    {
        index = 0;
        if (null == ftDev)
        {
            ftDev = ftdid2xx.openByIndex(context, index);
        }
        else
        {
            synchronized (ftDev)
            {
                ftDev = ftdid2xx.openByIndex(context, index);
            }
        }
        isConfiged = false;
        if (ftDev == null)
            Log.i("AAAA", "sdf");
    }

    // 更新连接设备列表，当重新打开程序或是熄灭屏幕之后重新打开都会执行此方法，应该次列表的设备数量一般情况下为1
    public void createDeviceList(Context context)
    {
        // 获取D2XX设备的数量，可以使用这个函数来确定连接到系统上的设备的数量
        int tempDevCount = ftdid2xx.createDeviceInfoList(context);

        if (tempDevCount > 0)
        {
            if (devCount != tempDevCount)
            {
                devCount = tempDevCount;
            }
        }
        else
        {
            devCount = -1;
            index = -1;
        }
    }

    //发送区全部设备电量命令
    public void sendGetPowerOrder()
    {
        // 获取全部设备电量指令
        String data = "#06@Zc";
        if (ftDev.isOpen() == false)
        {
            Log.e("j2xx", "SendMessage: device not open");
            return;
        }
        ftDev.setLatencyTimer((byte) 2);

        byte[] OutData = data.getBytes();
        ftDev.write(OutData, data.length());
    }

    //开一个灯命令
    public void turnOnLight(char num)
    {
        String data = "#06@" + num + "a";
        sendMessage(data);
    }
    // 关一个灯命令
    public void turnOffLight(char num)
    {
        String data = "#06@" + num + "b";
        sendMessage(data);
    }
    // 开所有的灯
    public void turnOnAllLight()
    {
        String data = "#06@Za";
        sendMessage(data);
    }
    // 关所有的灯
    public void turnOffAll()
    {
        String data = "#06@Zb";
        sendMessage(data);
    }

    private void sendMessage(String data)
    {
        if (ftDev.isOpen() == false)
        {
            Log.e("j2xx", "SendMessage: device not open");
            return;
        }
        ftDev.setLatencyTimer((byte) 128);
        byte[] OutData = data.getBytes();
        ftDev.write(OutData, data.length());
    }

}

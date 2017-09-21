package com.oucb303.training.device;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.threads.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzhiming on 16/9/7.
 * Description：
 */
public class Device
{
    //设备灯列表
    public static List<DeviceInfo> DEVICE_LIST = new ArrayList<>();
    public static Context CONTEXT;

    public static int BLOCK_TIME = 20;

    public FT_Device ftDev;
    private D2xxManager ftdid2xx;

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
            //Log.d("==========",ftdid2xx+"");
        } catch (D2xxManager.D2xxException e)
        {
            e.printStackTrace();
        }
    }

    //检测设备,如果没有返回false，否则返回
    public boolean checkDevice(Context context)
    {
        if (devCount <= 0)
        {
            Toast.makeText(context, "还未插入协调器,请插入协调器!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (DEVICE_LIST.isEmpty() || DEVICE_LIST.size() == 0)
        {
            Toast.makeText(context, "未检测到任何设备,请开启设备!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // 关闭activity时调用该方法
    public void disconnect()
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
        if (ftDev == null || !ftDev.isOpen())
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
    public void connect(Context context)
    {
        index = 0;
        if (null == ftDev)
        {
            ftDev = ftdid2xx.openByIndex(context, index);
        } else
        {
            synchronized (ftDev)
            {
                ftDev = ftdid2xx.openByIndex(context, index);
            }
        }
        isConfiged = false;
       // if (ftDev == null)
            //Log.i("AAAA", "sdf");
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
        } else
        {
            devCount = -1;
            index = -1;
        }
    }

    /**
     * 获取全部设备信息 电量、编号、短地址
     */
    public void sendGetDeviceInfo()
    {
        Log.d(Constant.LOG_TAG, " send get All DeviceInfo Order!");
        // 获取全部设备电量指令
        String data = "+04a";
        sendMessage(data);
    }

    //修改灯的PAN_ID 和 num
    public void changeLightPANID(String PAN_ID, char num, String address)
    {
        String order = "+14*" + PAN_ID + num + address;

        Log.d(Constant.LOG_TAG, "order:" + order);
        sendMessage(order);
    }

    //修改协调器PANID
    public void changeControllerPANID(String PAN_ID)
    {
        sendMessage("+08b" + PAN_ID);
    }

    public void getControllerPAN_ID()
    {
        sendMessage("+08c");
    }

    public void turnOffAllTheLight()
    {
        //关闭全部灯
        if (devCount > 0)
        {
            for (DeviceInfo info : Device.DEVICE_LIST)
            {
                sendOrder(info.getDeviceNum(),
                        Order.LightColor.NONE,
                        Order.VoiceMode.NONE,
                        Order.BlinkModel.NONE,
                        Order.LightModel.TURN_OFF,
                        Order.ActionModel.TURN_OFF,
                        Order.EndVoice.NONE);
            }
        }
    }


    //    type为0表示：折返跑，换物跑，运球比赛，大课间活动,八秒钟跑，type为1表示：纵跳摸高，仰卧起坐，胆大心细
    public void turnOnButton(int groupNum, int groupSize, int type)
    {
        if (type == 0)
        {
            for (int i = 0; i < groupNum * groupSize; i++)
            {
                sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                        //灯的颜色
                        Order.LightColor.values()[1],
                        //声音模式
                        Order.VoiceMode.NONE,
                        //闪烁模式
                        Order.BlinkModel.NONE,
                        //灯光模式
                        Order.LightModel.OUTER,
                        //感应模式
                        Order.ActionModel.NONE,
                        //感应毁灭时操作
                        Order.EndVoice.NONE);
            }
        } else
        {
            for (int k = 0; k < groupNum; k++)
            {
                for (int t = 0; t < groupSize; t++)
                {
                    sendOrder(Device.DEVICE_LIST.get(groupSize * k + t).getDeviceNum(),
                            //灯的颜色
                            Order.LightColor.values()[k % 7 + 1],
                            //声音模式
                            Order.VoiceMode.NONE,
                            //闪烁模式
                            Order.BlinkModel.NONE,
                            //灯光模式
                            Order.LightModel.OUTER,
                            //感应模式
                            Order.ActionModel.NONE,
                            //感应毁灭时操作
                            Order.EndVoice.NONE);
                }
            }
        }
    }

    public void sendOrder(char lightId, Order.LightColor color, Order.VoiceMode voiceMode, Order.BlinkModel blinkModel,
                          Order.LightModel lightModel, Order.ActionModel actionModel, Order.EndVoice endVoice)
    {
        String order = Order.getOrder(lightId, color, voiceMode, blinkModel, lightModel, actionModel, endVoice);
        if (order.equals(""))
            return;
        sendMessage(order);
    }

    private synchronized void sendMessage(String data)
    {
        Timer.sleep(BLOCK_TIME);

        if (ftDev==null)
        {

           // Log.d("--------","-------------------");
            Toast.makeText(CONTEXT,"协调器松动，请检查",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(Constant.LOG_TAG, "send message:" + data);

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

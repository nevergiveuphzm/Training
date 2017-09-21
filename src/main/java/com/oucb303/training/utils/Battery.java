package com.oucb303.training.utils;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import com.oucb303.training.device.Device;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.model.PowerInfoComparetor;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
 *电池信息类
 * create by ls 2017/9/18
 *
 */
public class Battery {

    private Device device;

    private boolean isLeave = false;
    private final int POWER_RECEIVE = 1;
    private AutoCheckPower checkPowerThread;
    Handler handler_battery;
    //private Handler handler;
    Spinner spGroupNum;
    List<DeviceInfo> currentListA = new ArrayList<>();

    TextView textView;
    public Battery(Device device, TextView textView, Handler handler_battery)
    {
        this.device = device;
        this.textView = textView;
        this.handler_battery = handler_battery;

    }

    //初始化串口
    public void initDevice() {
        checkPowerThread = new AutoCheckPower();
        checkPowerThread.start();
    }
    /**
     * 读取电量信息
     */
    public int readPowerData(String data) {
        if (isLeave)
            return 0;
        //DeviceInfo存储设备编号，电量，短地址
        List<DeviceInfo> powerInfos = DataAnalyzeUtils.analyzePowerData(data);
        Collections.sort(powerInfos, new PowerInfoComparetor());
        //清空电量列表
        if (powerInfos != null && powerInfos.size() != 0) {
            Log.d(Constant.LOG_TAG, "清空列表");
            Device.DEVICE_LIST.clear();
            //获取电量信息
            Device.DEVICE_LIST.addAll(powerInfos);
            Log.i("Device.DEVICE_LIST",  Device.DEVICE_LIST.size() + "");

            for (int i = 0; i < powerInfos.size(); i++) {
                for (int j = 0; j < currentListA.size(); j++) {
                    if (powerInfos.get(i).getDeviceNum() == currentListA.get(j).getDeviceNum()) {
                        currentListA.set(j, powerInfos.get(i));//替换
                        break;
                    }
                }
            }
        }
       // Log.i("AAA", powerInfos.size() + "");

        return Device.DEVICE_LIST.size();
    }

    /**
     * 自动检测电量
     */
    public class AutoCheckPower extends Thread {
        private boolean powerFlag = true;

        @Override
        public void run() {
            while (powerFlag) {
                //发送获取全部设备电量指令
                device.sendGetDeviceInfo();
                Timer.sleep(3000);
                device.sendGetDeviceInfo();
                Timer.sleep(3000);
                device.sendGetDeviceInfo();
                //开启接收电量的线程
                new ReceiveThread(handler, device.ftDev, ReceiveThread.POWER_RECEIVE_THREAD, POWER_RECEIVE).start();
                Timer.sleep(4000);
            }

        }
        public void setPowerFlag(){
            this.powerFlag = false;
        }

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POWER_RECEIVE:
                    String data = msg.obj.toString();
                    int size =  readPowerData(data);
                    textView.setText(size+"");
                    checkPowerThread.setPowerFlag();
                    Message msg_battery = Message.obtain();
                    msg_battery.what = 1;
                    msg_battery.obj = Device.DEVICE_LIST;
                    handler_battery.sendMessage(msg_battery);
                    break;
                default:
                    break;
            }
        }
    };

}

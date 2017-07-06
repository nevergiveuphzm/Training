package com.oucb303.training.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.PowerAdapter;
import com.oucb303.training.adpter.PowerSecondAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.model.PowerInfoComparetor;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.widget.HorizontalListView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HP on 2017/7/2.
 */
public class Test_main_activity extends AppCompatActivity {

    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.lv_battery)
    HorizontalListView lvBattery;
    @Bind(R.id.lv_battery_second)
    HorizontalListView lvBatterySecond;

    private PowerAdapter powerAdapter;
    private PowerSecondAdapter powerSecondAdapter;
    private Device device;
    private boolean isLeave = false;
    private AutoCheckPower checkPowerThread;
    private final int POWER_RECEIVE = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POWER_RECEIVE:
                    String data = msg.obj.toString();
                    readPowerData(data);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //应用程序无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.test_demo);
        ButterKnife.bind(this);
        device = new Device(Test_main_activity.this);
//        //注册USB插入和拔出广播接收者
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
//        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
//        filter.setPriority(500);
//        this.registerReceiver(mUsbReceiver, filter);
        //btnCheck.setEnabled(false);
//        Log.i("DEVICE_LIST.size()qqqqqqqqqqq",Device.DEVICE_LIST.size()+"");
//        if (Device.DEVICE_LIST.size() != 0){
        powerAdapter = new PowerAdapter(Test_main_activity.this);
        lvBattery.setAdapter(powerAdapter);
//        }
//        else {

       powerSecondAdapter = new PowerSecondAdapter(Test_main_activity.this);
       lvBatterySecond.setAdapter(powerSecondAdapter);
//        }

//        VersionUtils.getAppVersion(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDevice();
        isLeave = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        this.unregisterReceiver(mUsbReceiver);
    }

    @Override
    protected void onPause() {
        isLeave = true;
        device.disconnect();
        if (checkPowerThread != null)
            checkPowerThread.powerFlag = false;
        super.onPause();
    }

    //初始化串口
    public void initDevice() {
        device.createDeviceList(Test_main_activity.this);
        // 判断是否插入协调器，
        if (device.devCount > 0) {
            device.connect(Test_main_activity.this);
            device.initConfig();
            checkPowerThread = new AutoCheckPower();
            checkPowerThread.start();
        } else {
            // 未检测到协调器
            AlertDialog.Builder builder = new AlertDialog.Builder(Test_main_activity.this);
            builder.setTitle("温馨提示");
            builder.setMessage("                       未检测到协调器，请先插入协调器！\n");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertdialog = builder.create();
            alertdialog.show();
        }
    }

    //读取电量信息
    private void readPowerData(final String data) {
        if (isLeave)
            return;
        List<DeviceInfo> powerInfos = DataAnalyzeUtils.analyzePowerData(data);
        Collections.sort(powerInfos, new PowerInfoComparetor());
        //清空电量列表
        if (powerInfos != null && powerInfos.size() != 0) {
            Log.d(Constant.LOG_TAG, "清空列表");
            Device.DEVICE_LIST.clear();
            //tvDeviceCount.setText("共0个设备");
            //获取电量信息
            Device.DEVICE_LIST.addAll(powerInfos);
            powerAdapter.notifyDataSetChanged();
            powerSecondAdapter.notifyDataSetChanged();
        }
        Log.i("AAA", powerInfos.size() + "");

    }

    private class AutoCheckPower extends Thread {
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
                new ReceiveThread(handler, device.ftDev, ReceiveThread.POWER_RECEIVE_THREAD,
                        POWER_RECEIVE).start();
                Timer.sleep(4000);
            }
        }
    }
}

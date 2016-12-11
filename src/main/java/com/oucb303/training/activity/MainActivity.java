package com.oucb303.training.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.PowerAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.model.PowerInfoComparetor;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.DataAnalyzeUtils;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity
{
    @Bind(R.id.btn_level_one)
    Button btnLevelOne;
    @Bind(R.id.btn_level_two)
    Button btnLevelTwo;
    @Bind(R.id.btn_level_three)
    Button btnLevelThree;
    @Bind(R.id.btn_level_four)
    Button btnLevelFour;
    @Bind(R.id.btn_base_training)
    Button btnBaseTraining;
    @Bind(R.id.btn_statistic)
    Button btnStatistic;
    @Bind(R.id.btn23)
    Button btn23;
    @Bind(R.id.btn24)
    Button btn24;
    @Bind(R.id.lv_battery)
    ListView lvBattery;
    @Bind(R.id.tv_device_count)
    TextView tvDeviceCount;

    private Device device;
    private final int POWER_RECEIVE = 1;
    private PowerAdapter powerAdapter;
    private boolean powerFlag = true;


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case POWER_RECEIVE:
                    String data = msg.obj.toString();
                    readPowerData(data);
                    break;
            }
        }
    };
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String TAG = "FragL";
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
            {
                Log.i(TAG, "DETACHED...");
                notifyUSBDeviceDetach();
            }
        }
    };

    //拔出USB接口
    public void notifyUSBDeviceDetach()
    {
        device.disconnectFunction();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //应用程序无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        device = new Device(MainActivity.this);
        //注册USB插入和拔出广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);
        this.registerReceiver(mUsbReceiver, filter);
        //btnCheck.setEnabled(false);
        powerAdapter = new PowerAdapter(MainActivity.this);
        lvBattery.setAdapter(powerAdapter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(mUsbReceiver);
    }

    //初始化串口
    public void initDevice()
    {
        device.createDeviceList(MainActivity.this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connectFunction(MainActivity.this);
            device.initConfig();
            //sendGetPowerOrder();
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while (powerFlag)
                    {
                        // 发送获取全部设备电量指令
                        device.sendGetDeviceInfo();
                        //开启接收电量的线程
                        new ReceiveThread(handler, device.ftDev, ReceiveThread.POWER_RECEIVE_THREAD,
                                POWER_RECEIVE).start();

                        Timer.sleep(10000);
                    }
                }
            }).start();
        } else
        {
            // 未检测到协调器
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("温馨提示");
            builder.setMessage("                       未检测到协调器，请先插入协调器！\n");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            AlertDialog alertdialog = builder.create();
            alertdialog.show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        powerFlag = true;
        initDevice();
    }

    @Override
    protected void onPause()
    {
        powerFlag = false;
        device.disconnectFunction();
        super.onPause();
    }

    @OnClick({R.id.btn_level_one, R.id.btn_level_two, R.id.btn_level_three,
            R.id.btn_level_four, R.id.btn_base_training, R.id.btn_statistic, R.id.btn_test})
    public void onClick(View view)
    {
        int level = 0;
        Intent intent;
        switch (view.getId())
        {
            case R.id.btn_level_one:
                level = 1;
                intent = new Intent(MainActivity.this, TrainingListActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
                break;
            case R.id.btn_level_two:
                level = 2;
                intent = new Intent(MainActivity.this, TrainingListActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
                break;
            case R.id.btn_level_three:
                level = 3;
                intent = new Intent(MainActivity.this, TrainingListActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
                break;
            case R.id.btn_level_four:
                level = 4;
                intent = new Intent(MainActivity.this, TrainingListActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
                break;
            case R.id.btn_base_training:
                intent = new Intent();
                intent.setClass(MainActivity.this, BaseTrainingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_statistic:
                break;
            case R.id.btn_test:
                intent = new Intent();
                intent.setClass(MainActivity.this, TestActivity.class);
                startActivity(intent);
                break;
        }
    }


    //读取电量信息
    private void readPowerData(final String data)
    {
        //清空电量列表
        Device.DEVICE_LIST.clear();
        powerAdapter.notifyDataSetChanged();
        tvDeviceCount.setText("共0个设备");
        if (data.length() >= 7)
        {
            //获取电量信息
            List<DeviceInfo> powerInfos = DataAnalyzeUtils.analyzePowerData(data);
            Collections.sort(powerInfos, new PowerInfoComparetor());
            Device.DEVICE_LIST.addAll(powerInfos);
            if (powerInfos.size() > 0)
            {
                //更新电量信息
                powerAdapter.notifyDataSetChanged();
                tvDeviceCount.setText("共" + powerInfos.size() + "个设备");
                Log.i("AAA", powerInfos.size() + "");
            }
        }
//        } else
//        {
//            //此时是没有可用设备，弹出对话框，可以点击取消，或是点击重新检测
//            // 将主界面上所有的电量置为不可见
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("提示");
//            builder.setMessage("                       未检测到任何设备，请先开启设备！\n");
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//                }
//            });
//            builder.setPositiveButton("重新检测", new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//                    dialog.dismiss();
//                    sendGetPowerOrder();
//                }
//            });
//            AlertDialog alertdialog = builder.create();
//            alertdialog.show();
//        }
    }
}

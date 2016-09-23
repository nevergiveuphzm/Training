package com.oucb303.training.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import com.oucb303.training.R;
import com.oucb303.training.device.Device;

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
    @Bind(R.id.btn_check)
    Button btnCheck;
    @Bind(R.id.lv_battery)
    ListView lvBattery;

    private Device device;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {

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
        btnCheck.setEnabled(false);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @OnClick({R.id.btn_check, R.id.btn_level_one, R.id.btn_level_two, R.id.btn_level_three, R.id.btn_level_four, R.id.btn_base_training, R.id.btn_statistic})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_check:
                break;
            case R.id.btn_level_one:
                break;
            case R.id.btn_level_two:
                break;
            case R.id.btn_level_three:
                break;
            case R.id.btn_level_four:
                break;
            case R.id.btn_base_training:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, BaseTrainingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_statistic:
                break;
        }
    }

    //检测设备电量
    public void checkDevicePower()
    {

    }
}

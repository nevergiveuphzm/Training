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

import com.oucb303.training.R;
import com.oucb303.training.adpter.PowerAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.utils.DataAnalyzeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final int POWER_RECEIVE = 1;
    private PowerAdapter powerAdapter;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case POWER_RECEIVE:
                    readPowerData(msg.obj.toString());
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
        btnCheck.setEnabled(false);
        powerAdapter = new PowerAdapter(MainActivity.this, null);
        lvBattery.setAdapter(powerAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        device.createDeviceList(MainActivity.this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connectFunction(MainActivity.this);
            device.initConfig();
            sendGetPowerOrder();
        }
        else
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
        btnCheck.setEnabled(false);
        try
        {
            Thread.sleep(2000);
            btnCheck.setEnabled(true);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause()
    {
        device.disconnectFunction();
        super.onPause();
    }

    @OnClick({R.id.btn_check, R.id.btn_level_one, R.id.btn_level_two, R.id.btn_level_three, R.id.btn_level_four, R.id.btn_base_training, R.id.btn_statistic})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_check:
                sendGetPowerOrder();
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


    // 获得所有设备电量
    public void sendGetPowerOrder()
    {
        //清空电量列表
        powerAdapter.setPowerInfos(null);
        powerAdapter.notifyDataSetChanged();
        // 发送获取全部设备电量指令
        String data = "#06@Zc";
        device.sendGetPowerOrder(data);
        //开启接收电量的线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.POWER_RECEIVE_THREAD,
                POWER_RECEIVE).start();
        //将电量检测按钮不可见
        btnCheck.setEnabled(false);
        try
        {
            Thread.sleep(2000);
            btnCheck.setEnabled(true);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    //读取电量信息
    private void readPowerData(String data)
    {
        if (data.length() > 0)
        {
            List<Map<String, Object>> powerInfos = DataAnalyzeUtils.analyzePowerData(data);
            List<String> lowPowerDevice = new ArrayList<>();
            for (int i = 0; i < powerInfos.size(); i++)
            {
                Log.i("AAAA",(int) powerInfos.get(i).get("power")+"");
                if ((int) powerInfos.get(i).get("power") == 0)
                {
                    lowPowerDevice.add(powerInfos.get(i).get("deviceNum").toString());
                    powerInfos.remove(i);
                    i--;
                }
            }
            if (powerInfos.size() > 0)
            {
                //更新电量信息
                powerAdapter.setPowerInfos(powerInfos);
                powerAdapter.notifyDataSetChanged();
                Log.i("AAA",powerInfos.size()+"");
            }
            //存在低电量设备
            if (lowPowerDevice.size()>0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setTitle("警告");
                String unstr = lowPowerDevice.toString();
                builder.setMessage("                          " + unstr
                        + "号设备电量过低，请更换电池！\n");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });
                AlertDialog alertdialog = builder.create();
                alertdialog.setCancelable(false);
                alertdialog.show();
            }
        }
        else
        {
            //此时是没有可用设备，弹出对话框，可以点击取消，或是点击重新检测
            // 将主界面上所有的电量置为不可见
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示");
            builder.setMessage("                       未检测到任何设备，请先开启设备！\n");
            builder.setPositiveButton("重新检测", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    sendGetPowerOrder();
                }
            });
            AlertDialog alertdialog = builder.create();
            alertdialog.show();
        }
    }
}

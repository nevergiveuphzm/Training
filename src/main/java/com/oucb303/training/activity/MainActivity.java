package com.oucb303.training.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.PowerAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.model.PowerInfoComparetor;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.utils.VersionUtils;
import com.oucb303.training.widget.BraceletManager;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主界面
 */
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
    @Bind(R.id.btn_test)
    Button btnTest;
    @Bind(R.id.lv_battery)
    ListView lvBattery;
    @Bind(R.id.tv_device_count)
    TextView tvDeviceCount;
    @Bind(R.id.sw_bracelet)
    Switch swBracelet;
    @Bind(R.id.pb_bar)
    ProgressBar pbBar;
    @Bind(R.id.iv_bracelet)
    ImageView imgBracelet;

    private Device device;
    private final int POWER_RECEIVE = 1;
    private final int FIND_BRACELET = 2;//是否扫描到手环
    private PowerAdapter powerAdapter;
    private AutoCheckPower checkPowerThread;
    private boolean isLeave = false;
    private BraceletManager braceletManager;

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
                case FIND_BRACELET:
                    pbBar.setVisibility(View.GONE);
                    imgBracelet.setVisibility(View.VISIBLE);
                    braceletManager.stopScan();
                    break;
                default:
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
        device.disconnect();
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
        VersionUtils.getAppVersion(this);
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
            device.connect(MainActivity.this);
            device.initConfig();
            checkPowerThread = new AutoCheckPower();
            checkPowerThread.start();
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
        initDevice();
        isLeave = false;
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView()
    {
        //屏蔽或放开测试按钮
        SharedPreferences sp = getSharedPreferences("Training", MODE_PRIVATE);
        if (sp.getBoolean("flag_btnTest_visible", false))
            btnTest.setVisibility(View.VISIBLE);
        else
            btnTest.setVisibility(View.GONE);
        //Switch控件
        swBracelet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    pbBar.setVisibility(View.VISIBLE);
                    imgBracelet.setVisibility(View.GONE);
                    openBraceLet();
                } else
                {
                    pbBar.setVisibility(View.GONE);
                    imgBracelet.setVisibility(View.GONE);
                    braceletManager.stopScan();
                }
            }
        });
    }

    /**
     * 开启手环
     */
    private void openBraceLet()
    {
        braceletManager = new BraceletManager(this.getApplicationContext(), this);
        if (braceletManager.isBluetoothOpen())
        {
            Log.i("Bluetooth", "蓝牙开启");
        } else if (!braceletManager.getAdapter().enable())
        {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1001);
        }
        //扫描
        braceletManager.scanBracelet();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (braceletManager.isScaning())
                {
                    Timer.sleep(100);
                    if (braceletManager.isExist())
                    {
                        Message msg = Message.obtain();
                        msg.what = FIND_BRACELET;
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause()
    {
        isLeave = true;
        device.disconnect();
        if (checkPowerThread != null)
            checkPowerThread.powerFlag = false;
        super.onPause();
    }

    @OnClick({R.id.btn_level_one, R.id.btn_level_two, R.id.btn_level_three,
            R.id.btn_level_four, R.id.btn_base_training, R.id.btn_statistic, R.id.btn_test,
            R.id.btn_setting})
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
            case R.id.btn_setting:
                intent = new Intent();
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }


    //读取电量信息
    private void readPowerData(final String data)
    {
        if (isLeave)
            return;
        List<DeviceInfo> powerInfos = DataAnalyzeUtils.analyzePowerData(data);
        Collections.sort(powerInfos, new PowerInfoComparetor());
        //清空电量列表
        if (powerInfos != null && powerInfos.size() != 0)
        {
            Log.d(Constant.LOG_TAG, "清空列表");
            Device.DEVICE_LIST.clear();
            //tvDeviceCount.setText("共0个设备");
            //获取电量信息
            Device.DEVICE_LIST.addAll(powerInfos);
            powerAdapter.notifyDataSetChanged();

            tvDeviceCount.setText("共" + powerInfos.size() + "个设备");
        }
        Log.i("AAA", powerInfos.size() + "");

    }

    private class AutoCheckPower extends Thread
    {
        private boolean powerFlag = true;

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
    }

    /***
     * 接收意图的结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1001:
                if (resultCode == RESULT_OK)
                {
                    // 刚打开蓝牙实际还不能立马就能用
                } else
                {
                    Toast.makeText(this, "请打开蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}

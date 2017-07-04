package com.oucb303.training.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;


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
import com.oucb303.training.utils.VersionUtils;
import com.oucb303.training.widget.BraceletManager;
import com.oucb303.training.widget.HorizontalListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.os.Handler;
import java.util.logging.LogRecord;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2017/7/3.
 */
public class MainNewActivity extends AppCompatActivity {
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.lv_battery)
    HorizontalListView lvBattery;
    @Bind(R.id.lv_battery_second)
    HorizontalListView lvBatterySecond;
    @Bind(R.id.btn_kinetism)
    Button btnKinetism;
    @Bind(R.id.btn_technique)
    Button btnTechnique;
    @Bind(R.id.btn_combined_train)
    Button btnCombinedTrain;
    @Bind(R.id.btn_app_tools)
    Button btnAppTools;
    @Bind(R.id.btn_hacker_space)
    Button btnHackerSpace;
    @Bind(R.id.btn_setting)
    Button btnSetting;


    private Device device;
    private PowerAdapter powerAdapter;
    private PowerSecondAdapter powerSecondAdapter;
    private boolean isLeave = false;
    private final int POWER_RECEIVE = 1;
//    private final int FIND_BRACELET = 2;//是否扫描到手环
    private AutoCheckPower checkPowerThread;

    private char[] deviceNum = {};

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case POWER_RECEIVE:
                    String data = msg.obj.toString();
                    readPowerData(data);
                    break;
//                case FIND_BRACELET:
//                    pbBar.setVisibility(View.GONE);
//                    imgBracelet.setVisibility(View.VISIBLE);
//                    braceletManager.stopScan();
//                    break;
                default:
                    break;
            }
        }
    };

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        device = new Device(this);
        //注册USB插入和拔出广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);//设置权限
        this.registerReceiver(mUsbReceiver, filter);

        powerAdapter = new PowerAdapter(this);
        lvBattery.setAdapter(powerAdapter);
        powerSecondAdapter = new PowerSecondAdapter(this);
        lvBatterySecond.setAdapter(powerSecondAdapter);

        VersionUtils.getAppVersion(this);//获取apk版本
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(mUsbReceiver);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        initDevice();
        isLeave = false;
        initView();
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

    //初始化串口
    public void initDevice(){
        //更新连接设备列表
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if(device.devCount >0) {
            device.connect(this);
            device.initConfig();
            checkPowerThread = new AutoCheckPower();
            checkPowerThread.start();

        }else{
            // 未检测到协调器
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage("                       未检测到协调器，请先插入协调器！\n");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    /**
     * 初始化控件
     */
    public void initView(){
        //屏蔽或放开测试按钮
//        SharedPreferences sp = getSharedPreferences("Training", MODE_PRIVATE);
//        if (sp.getBoolean("flag_btnTest_visible", false))
//            btnTest.setVisibility(View.VISIBLE);
//        else
//            btnTest.setVisibility(View.GONE);
        //Switch控件
//        swBracelet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                if (isChecked)
//                {
//                    pbBar.setVisibility(View.VISIBLE);
//                    imgBracelet.setVisibility(View.GONE);
//                    openBraceLet();
//                } else
//                {
//                    pbBar.setVisibility(View.GONE);
//                    imgBracelet.setVisibility(View.GONE);
//                    braceletManager.stopScan();
//                }
//            }
//        });
    }

    /**
     * 开启手环
     */
   private void openBraceLet()
  {
//        braceletManager = new BraceletManager(this.getApplicationContext(), this);
//        if (braceletManager.isBluetoothOpen())
//        {
//            Log.i("Bluetooth", "蓝牙开启");
//        } else if (!braceletManager.getAdapter().enable())
//        {
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, 1001);
//        }
//        //扫描
//        braceletManager.scanBracelet();
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                while (braceletManager.isScaning())
//                {
//                    Timer.sleep(100);
//                    if (braceletManager.isExist())
//                    {
//                        Message msg = Message.obtain();
//                        msg.what = FIND_BRACELET;
//                        handler.sendMessage(msg);
//                    }
//                }
//            }
//        }).start();
 }

    @OnClick({R.id.btn_kinetism,R.id.btn_technique,R.id.btn_combined_train,R.id.btn_hacker_space,R.id.btn_setting,R.id.btn_app_tools})
    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btn_kinetism:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, NavigationActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.btn_technique:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, NavigationActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case R.id.btn_combined_train:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, CombinedTrainingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_app_tools:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, ApplicationToolsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hacker_space:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, HackerSpaceActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_setting:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 读取电量信息
     */
    public void readPowerData(String data){
        if (isLeave)
            return;
        //设备灯列表
        List<DeviceInfo> currentList = new ArrayList<>();

        //DeviceInfo存储设备编号，电量，短地址
        List<DeviceInfo> powerInfos = DataAnalyzeUtils.analyzePowerData(data);
        Collections.sort(powerInfos,new PowerInfoComparetor());
        //清空电量列表
        if (powerInfos != null && powerInfos.size() != 0){
            Log.d(Constant.LOG_TAG, "清空列表");

            Device.DEVICE_LIST.clear();
            //获取电量信息
            Device.DEVICE_LIST.addAll(powerInfos);

            currentList.addAll(powerInfos);
            //遍历current，如果没有此设备，加入
//            for (int i = 0;i<currentList.size();i++){
//                if (currentList.get(i).getDeviceNum() != )
//            }

            powerAdapter.notifyDataSetChanged();
            powerSecondAdapter.notifyDataSetChanged();
        }
        Log.i("AAA", powerInfos.size() + "");
    }

    /**
     * 自动检测电量
     */
    public class AutoCheckPower extends Thread{
        private boolean powerFlag = true;

        @Override
        public void run() {
            while(powerFlag){
                //发送获取全部设备电量指令
                device.sendGetDeviceInfo();
                Timer.sleep(3000);
                device.sendGetDeviceInfo();
                Timer.sleep(3000);
                device.sendGetDeviceInfo();
                //开启接收电量的线程
                new ReceiveThread(handler,device.ftDev,ReceiveThread.POWER_RECEIVE_THREAD,POWER_RECEIVE).start();
                Timer.sleep(4000);
            }
        }
    }

    /***
     * 接收意图的结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode)
//        {
//            case 1001:
//                if (resultCode == RESULT_OK)
//                {
//                    // 刚打开蓝牙实际还不能立马就能用
//                } else
//                {
//                    Toast.makeText(this, "请打开蓝牙", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
    }
    /**
     * 重写 onKeyDown方法
     */
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(this,"再按一次推出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}

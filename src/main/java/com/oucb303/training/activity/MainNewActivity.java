package com.oucb303.training.activity;

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
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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
import com.oucb303.training.utils.ConfigParamUtils;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.utils.VersionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2017/7/3.
 */
public class MainNewActivity extends AppCompatActivity {
    @Bind(R.id.imageView)
    ImageView imageView;
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
    @Bind(R.id.gv_item_name)
    GridView gvItemName;
    @Bind(R.id.btn_kinetism)
    Button btnKinetism;
    @Bind(R.id.btn_kinetism_1)
    Button btnKinetism1;
    @Bind(R.id.btn_technique_1)
    Button btnTechnique1;
    @Bind(R.id.btn_combined_train_1)
    Button btnCombinedTrain1;
    @Bind(R.id.btn_app_tools_1)
    Button btnAppTools1;
    @Bind(R.id.btn_hacker_space_1)
    Button btnHackerSpace1;
    @Bind(R.id.btn_setting_1)
    Button btnSetting1;
    @Bind(R.id.btn_test)
    Button btnTest;


    private Device device;
    private PowerAdapter powerAdapter;
    private PowerSecondAdapter powerSecondAdapter;
    private boolean isLeave = false;
    private final int POWER_RECEIVE = 1;
    //    private final int FIND_BRACELET = 2;//是否扫描到手环
    private AutoCheckPower checkPowerThread;

    private int num;//设备个数

    //设备灯列表
    List<DeviceInfo> currentListA = new ArrayList<>();
    char[] deviceNumA = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'};

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

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String TAG = "FragL";
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.i(TAG, "DETACHED...");
                notifyUSBDeviceDetach();
            }
        }
    };

    //拔出USB接口
    public void notifyUSBDeviceDetach() {
        device.disconnect();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        View v = findViewById(R.id.btn_test);//找到你要设透明背景的layout 的id
        v.getBackground().setAlpha(100);//0~255透明度值
        device = new Device(this);
        //注册USB插入和拔出广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);//设置权限
        this.registerReceiver(mUsbReceiver, filter);


        for (int i = 0; i < deviceNumA.length; i++) {
            DeviceInfo dev = new DeviceInfo();
            dev.setDeviceNum(deviceNumA[i]);
            dev.setPower(-1);
            dev.setAddress("");
            currentListA.add(dev);
        }

        num = ConfigParamUtils.getDefaultDeviceNum(MainNewActivity.this);//获取默认设备个数
        powerAdapter = new PowerAdapter(this, num);
        powerAdapter.setCurrentList(currentListA);
        gvItemName.setAdapter(powerAdapter);

        VersionUtils.getAppVersion(this);//获取apk版本
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mUsbReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDevice();
        isLeave = false;
        initView();
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
        //更新连接设备列表
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if (device.devCount > 0) {
            device.connect(this);
            device.initConfig();
            checkPowerThread = new AutoCheckPower();
            checkPowerThread.start();

        } else {
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
    public void initView() {
        //屏蔽或放开测试按钮
        SharedPreferences sp = getSharedPreferences("Training", MODE_PRIVATE);
        if (sp.getBoolean("flag_btnTest_visible", false))
            btnTest.setVisibility(View.VISIBLE);
        else
            btnTest.setVisibility(View.GONE);
        //Switch控件
//
    }


    @OnClick({R.id.btn_kinetism, R.id.btn_technique, R.id.btn_combined_train, R.id.btn_hacker_space, R.id.btn_setting, R.id.btn_app_tools,R.id.btn_test,
            R.id.btn_kinetism_1, R.id.btn_technique_1, R.id.btn_combined_train_1, R.id.btn_app_tools_1, R.id.btn_hacker_space_1, R.id.btn_setting_1})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_kinetism:
            case R.id.btn_kinetism_1:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, NavigationActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.btn_technique:
            case R.id.btn_technique_1:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, NavigationActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.btn_combined_train:
            case R.id.btn_combined_train_1:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, CombinedTrainingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_app_tools:
            case R.id.btn_app_tools_1:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, ApplicationToolsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hacker_space:
            case R.id.btn_hacker_space_1:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, HackerSpaceActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_setting:
            case R.id.btn_setting_1:
                intent = new Intent();
                intent.setClass(MainNewActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_test:
                View v = findViewById(R.id.btn_test);//找到你要设透明背景的layout 的id
                v.getBackground().setAlpha(200);//0~255透明度值
                intent = new Intent();
                intent.setClass(MainNewActivity.this,TestActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 读取电量信息
     */
    public void readPowerData(String data) {
        if (isLeave)
            return;
        //DeviceInfo存储设备编号，电量，短地址
        List<DeviceInfo> powerInfos = DataAnalyzeUtils.analyzePowerData(data);
        Collections.sort(powerInfos, new PowerInfoComparetor());
        //清空电量列表
        if (powerInfos != null && powerInfos.size() != 0) {
            Log.d(Constant.LOG_TAG, "清空列表");
            Device.DEVICE_LIST.clear();
            //获取电量信息
            Device.DEVICE_LIST.addAll(powerInfos);

            for (int i = 0; i < powerInfos.size(); i++) {
                for (int j = 0; j < currentListA.size(); j++) {
                    if (powerInfos.get(i).getDeviceNum() == currentListA.get(j).getDeviceNum()) {
                        currentListA.set(j, powerInfos.get(i));//替换
                        break;
                    }
                }
            }

            powerAdapter.setCurrentList(currentListA);
            powerAdapter.notifyDataSetChanged();
        }
        Log.i("AAA", powerInfos.size() + "");
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
    }


    /**
     * 重写 onKeyDown方法
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次推出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

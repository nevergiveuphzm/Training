package com.oucb303.training.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.TimingModuleAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.listener.SpinnerItemSelectedListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.PowerInfoComparetor;
import com.oucb303.training.model.TimeInfo;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.DataAnalyzeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2017/3/29.
 * 限时活动基本模块
 */
public class TimingModuleActivity extends AppCompatActivity {
    @Bind(R.id.bt_distance_cancel)
    ImageView btDistanceCancel;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save)
    ImageView imgSave;
    @Bind(R.id.tv_training_time)
    TextView tvTrainingTime;
    @Bind(R.id.img_training_time_sub)
    ImageView imgTrainingTimeSub;
    @Bind(R.id.bar_training_time)
    SeekBar barTrainingTime;
    @Bind(R.id.tv_maxTime)
    TextView tvMaxTime;
    @Bind(R.id.img_training_time_add)
    ImageView imgTrainingTimeAdd;
    @Bind(R.id.ll_training_time)
    LinearLayout llTrainingTime;
    @Bind(R.id.sp_dev_num)
    Spinner spDevNum;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.tv_device_list)
    TextView tvDeviceList;
    @Bind(R.id.img_action_mode_light)
    ImageView imgActionModeLight;
    @Bind(R.id.img_action_mode_touch)
    ImageView imgActionModeTouch;
    @Bind(R.id.img_action_mode_together)
    ImageView imgActionModeTogether;
    @Bind(R.id.img_light_color_blue)
    ImageView imgLightColorBlue;
    @Bind(R.id.img_light_color_red)
    ImageView imgLightColorRed;
    @Bind(R.id.img_light_color_blue_red)
    ImageView imgLightColorBlueRed;
    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
    @Bind(R.id.cb_end_voice)
    android.widget.CheckBox cbEndVoice;
    @Bind(R.id.cb_over_time_voice)
    android.widget.CheckBox cbOverTimeVoice;
    @Bind(R.id.ll_params)
    LinearLayout llParams;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.img_blink_mode_none)
    ImageView imgBlinkModeNone;
    @Bind(R.id.img_blink_mode_slow)
    ImageView imgBlinkModeSlow;
    @Bind(R.id.img_blink_mode_fast)
    ImageView imgBlinkModeFast;

    //device里有设备灯列表(设备编号，电量，短地址)，设备数量
    private Device device;
    //所选设备个数
    private int totalNum;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightColorCheckBox,blinkModeCheckBox;
    //训练开始的标志
    private boolean trainingFlag = false;
    //时间列表,设备编号和时间
    private List<TimeInfo> timeList = new ArrayList<>();

    //    private final int TIME_RECEIVE = 2;
    //训练时间,单位毫秒
    private int trainingTime;
    //计时器
    private Timer timer;
    //设备编号列表
    private List<Character> deviceNumList = new ArrayList<>();

    //接收到灭灯时间标志
    private final int TIME_RECEIVE = 1;
    private final int UPDATE_TIMES = 2;
    private final int STOP_TRAIN = 3;

    private TimingModuleAdapter timingModuleAdapter;

    private int level;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Timer.TIMER_FLAG:
                    //更新计时
                    String time = msg.obj.toString();
                    Log.d("右侧总时间：", "" + time);
                    tvTotalTime.setText(time);
                    //把超时的按顺序罗列出来
                    if (timer.time >= trainingTime) {
                        stopTraining();
                        for (int j = 0; j < totalNum; j++) {
                            int flag = 0;
                            for (int i = 0; i < timeList.size(); i++) {
                                if (timeList.get(i).getDeviceNum() == deviceNumList.get(j)) {
                                    flag = 1;
                                    break;
                                }
                            }
                            if (flag != 1) {
                                TimeInfo info = new TimeInfo();
                                info.setDeviceNum(deviceNumList.get(j));
                                timeList.add(info);
//                                    Log.i("ggggggggggggggg", "---" + timeList);
                            }
                        }
                        return;
                    }
                    break;
                //接收到返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data != null && data.length() >= 4) {

                        analyzeTimeData(data);

//                        if (timeList.size() == totalNum)
//                            stopTraining();
                        Message msg1 = Message.obtain();
                        msg1.what = UPDATE_TIMES;
                        msg1.obj = "";
                        handler.sendMessage(msg1);
                    }
                    break;
                //更新完成次数
                case UPDATE_TIMES:
                    timingModuleAdapter.notifyDataSetChanged();
                    break;
                case STOP_TRAIN:
                    isTrainingOver();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_module);
        ButterKnife.bind(this);

        level = getIntent().getIntExtra("level", 0);

        initView();
        device = new Device(this);
        //更新连接设备列表
        device.createDeviceList(this);
        // 判断是否插入协调器
        if (device.devCount > 0) {
            device.connect(this);
            device.initConfig();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        imgSave.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (trainingFlag)
            stopTraining();
        device.disconnect();
        super.onDestroy();
    }

    public void initView() {
        tvTitle.setText("限时活动");
        imgHelp.setVisibility(View.VISIBLE);
        imgSave.setVisibility(View.VISIBLE);

        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //选择设备个数spinner
        String[] num = new String[Device.DEVICE_LIST.size()];
        for (int i = 0; i < num.length; i++) {
            num[i] = (i + 1) + "个";
        }
        spDevNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spDevNum, num) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                totalNum = i + 1;
                String str = "";
                for (int j = 0; j < totalNum; j++) {
                    str += Device.DEVICE_LIST.get(j).getDeviceNum() + "  ";
                }
                tvDeviceList.setText(str);
            }
        });

        //设定感应模式的checkbox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);
        ////设定灯光颜色checkBox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorBlueRed};
        lightColorCheckBox = new CheckBox(1, views1);
        new CheckBoxClickListener(lightColorCheckBox);
        //设定闪烁模式checkbox组合的点击事件
        ImageView[] views2 = new ImageView[]{imgBlinkModeNone, imgBlinkModeSlow, imgBlinkModeFast,};
        blinkModeCheckBox = new CheckBox(1, views2);
        new CheckBoxClickListener(blinkModeCheckBox);

        //训练时间拖动条初始化
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 100));
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));
        tvMaxTime.setText("100");

        //初始化右侧listView
        timingModuleAdapter = new TimingModuleAdapter(this, timeList);
        lvTimes.setAdapter(timingModuleAdapter);
    }

    @OnClick({R.id.btn_off, R.id.btn_on, R.id.layout_cancel, R.id.btn_begin, R.id.img_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_off:
                device.turnOffAllTheLight();
                break;
            case R.id.btn_on:
                //totalNum组数，1：每组设备个数，0：类型
                device.turnOnButton(totalNum, 1, 0);
                break;
            case R.id.layout_cancel:
                finish();
                device.turnOffAllTheLight();
                break;
            case R.id.btn_begin:
                if (!device.checkDevice(this))
                    return;
                if (trainingFlag)
                    stopTraining();
                else
                    startTraining();
                break;
            case R.id.img_save:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 6:大课间跑圈、八秒钟跑、限时活动 ...
                bundle.putString("trainingCategory", "6");
                //每组规定时间内的所完成的次数
                bundle.putString("trainingName", "限时活动");
                int[] trainTime = new int[timeList.size()];
                for (int i = 0; i < timeList.size(); i++) {
                    trainTime[i] = timeList.get(i).getTime();
                }
                bundle.putIntArray("scores", trainTime);
                bundle.putInt("totalTimes", 1);//总次数
                bundle.putInt("totalTime", trainingTime);//训练总时间
                bundle.putInt("deviceNum", totalNum);//设备个数
                bundle.putInt("groupNum", totalNum);//分组数
                it.putExtras(bundle);
                startActivity(it);
                break;
        }
    }

    public void startTraining() {
        //训练开始
        trainingFlag = true;
        btnBegin.setText("停止");
        //训练时间
//        trainingTime = (int) (new Double(tvTrainingTime.getText().toString())*60*1000);
        trainingTime = (int) (new Double(tvTrainingTime.getText().toString()) * 1000);
        Log.d("训练时间是多少", "" + trainingTime);
        //timeList存设备编号和时间
        timeList.clear();
        //存放设备编号
        deviceNumList.clear();

        timingModuleAdapter.setTrainingTime(trainingTime);
        timingModuleAdapter.notifyDataSetChanged();

        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();
        //发送开灯命令
        for (int i = 0; i < totalNum; i++) {
            device.sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                    Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                    Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                    Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                    Order.LightModel.OUTER,
                    Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                    Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
            deviceNumList.add(Device.DEVICE_LIST.get(i).getDeviceNum());
        }

        //开启接收返回灭灯时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        //开启计时线程
        timer = new Timer(handler, trainingTime);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();
    }

    //结束训练
    public void stopTraining() {
        trainingFlag = false;
        btnBegin.setText("开始");
        imgSave.setEnabled(true);
        btnBegin.setEnabled(false);
        //结束时间线程
        if (timer != null)
            timer.stopTimer();
        device.turnOffAllTheLight();
        //暂停0.5秒,等全部数据返回结束接收线程
        Timer.sleep(500);
        //结束接收返回灭灯时间线程
        ReceiveThread.stopThread();
        btnBegin.setEnabled(true);
    }

    //判断训练是否结束
    public void isTrainingOver() {
        if (!trainingFlag)
            return;
        if (timeList.size() == totalNum) {
            stopTraining();
            Log.i("?????????>>>>>", "<<<<<<<<<<");
        }
//        if(timer.time >= trainingTime)
//            stopTraining();
    }

    //解析反回来的数据,灭一次解析一次，如果同时灭多盏灯，就是解析一次,infos里存放同时灭灯个数
    public void analyzeTimeData(final String data) {
        if (!trainingFlag)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
//                List<TimeInfo>infos = DataAnalyzeUtils.analyzeTimeData(data);
                //设备编号和时间
                timeList.addAll(DataAnalyzeUtils.analyzeTimeData(data));

                Message msg = Message.obtain();
                msg.what = STOP_TRAIN;
                msg.obj = "";
                handler.sendMessage(msg);

                Log.i("timeList:-----------", "" + timeList);
            }
        }).start();
    }
}

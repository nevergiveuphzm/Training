package com.oucb303.training.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.RandomTimeAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.listener.SpinnerItemSelectedListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.Constant;
import com.oucb303.training.model.TimeInfo;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.utils.RandomUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 随机训练
 */
public class RandomTrainingActivity extends Activity
{
    @Bind(R.id.bt_distance_cancel)
    ImageView btDistanceCancel;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_training_times)
    TextView tvTrainingTimes;
    @Bind(R.id.img_training_times_sub)
    ImageView imgTrainingTimesSub;
    @Bind(R.id.bar_training_times)
    SeekBar barTrainingTimes;
    @Bind(R.id.img_training_times_add)
    ImageView imgTrainingTimesAdd;
    @Bind(R.id.tv_delay_time)
    TextView tvDelayTime;
    @Bind(R.id.img_delay_time_sub)
    ImageView imgDelayTimeSub;
    @Bind(R.id.bar_delay_time)
    SeekBar barDelayTime;
    @Bind(R.id.img_delay_time_add)
    ImageView imgDelayTimeAdd;
    @Bind(R.id.tv_over_time)
    TextView tvOverTime;
    @Bind(R.id.img_over_time_sub)
    ImageView imgOverTimeSub;
    @Bind(R.id.bar_over_time)
    SeekBar barOverTime;
    @Bind(R.id.img_over_time_add)
    ImageView imgOverTimeAdd;
    @Bind(R.id.img_action_mode_touch)
    ImageView imgActionModeTouch;
    @Bind(R.id.img_action_mode_light)
    ImageView imgActionModeLight;
    @Bind(R.id.img_action_mode_together)
    ImageView imgActionModeTogether;
    @Bind(R.id.img_light_mode_center)
    ImageView imgLightModeCenter;
    @Bind(R.id.img_light_mode_all)
    ImageView imgLightModeAll;
    @Bind(R.id.img_light_mode_beside)
    ImageView imgLightModeBeside;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.ll_params)
    LinearLayout llParams;
    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.tv_current_times)
    TextView tvCurrentTimes;
    @Bind(R.id.tv_lost_times)
    TextView tvLostTimes;
    @Bind(R.id.tv_average_time)
    TextView tvAverageTime;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.tv_training_time)
    TextView tvTrainingTime;
    @Bind(R.id.img_training_time_sub)
    ImageView imgTrainingTimeSub;
    @Bind(R.id.bar_training_time)
    SeekBar barTrainingTime;
    @Bind(R.id.img_training_time_add)
    ImageView imgTrainingTimeAdd;
    @Bind(R.id.ll_training_time)
    LinearLayout llTrainingTime;
    @Bind(R.id.ll_training_times)
    LinearLayout llTrainingTimes;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
    @Bind(R.id.img_light_color_blue)
    ImageView imgLightColorBlue;
    @Bind(R.id.img_light_color_red)
    ImageView imgLightColorRed;
    @Bind(R.id.img_light_color_blue_red)
    ImageView imgLightColorBlueRed;
    @Bind(R.id.cb_end_voice)
    android.widget.CheckBox cbEndVoice;
    @Bind(R.id.cb_over_time_voice)
    android.widget.CheckBox cbOverTimeVoice;
    @Bind(R.id.sp_dev_num)
    Spinner spDevNum;
    @Bind(R.id.tv_device_list)
    TextView tvDeviceList;


    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox;
    //接收到电量信息标志
    private final int POWER_RECEIVE = 1;
    //接收到灭灯时间标志
    private final int TIME_RECEIVE = 2;
    //停止训练标志
    private final int STOP_TRAINING = 3;
    //更新时间运行次数等信息
    private final int LOST_TIME = 4;
    private final int IS_TRAINING_OVER = 5;

    //随机模式 0:次数随机  1:时间随机
    private int randomMode;

    private Device device;
    //运行的总次数、当前运行的次数、遗漏次数、训练的总时间
    private int totalTimes, currentTimes, lostTimes, trainingTime;
    //当前亮的灯
    private char currentLight;
    //延迟时间 单位是毫秒
    private int delayTime;
    //超时时间 单位毫秒
    private int overTime;
    //开灯命令发送后 灯持续亮的时间
    private int durationTime;
    //训练开始的时间
    private long beginTime;
    //训练开始的标志
    private boolean trainingFlag = false;
    //超时线程
    private OverTimeThread overTimeThread;
    //时间列表
    private ArrayList<TimeInfo> timeList = new ArrayList<>();
    private RandomTimeAdapter timeAdapter;
    private Timer timer;
    //选用的设备个数
    private int totalNum;

    private int level;


    private Handler timerHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == timer.TIMER_FLAG)
            {
                if (randomMode == 1 && timer.time >= trainingTime)
                {
                    Log.d(Constant.LOG_TAG + "xx:", timer.time + "");
                    timer.stopTimer();
                    stopTraining();
                }
                //开始到现在持续的时间
                tvTotalTime.setText(msg.obj.toString());
            }
        }
    };

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    //返回数据不为空
                    if (data != null && data.length() >= 4)
                    {
                        timeList.addAll(DataAnalyzeUtils.analyzeTimeData(data));
                        if (timeAdapter != null)
                        {
                            timeAdapter.notifyDataSetChanged();
                            lvTimes.setSelection(timeList.size() - 1);
                        }
                        tvCurrentTimes.setText(currentTimes + "");
                        isTrainingOver();
                    }
                    break;
                case STOP_TRAINING:
                    stopTraining();
                    break;
                //遗漏
                case LOST_TIME:
                    tvLostTimes.setText(lostTimes + "");
                    isTrainingOver();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomtraining);
        ButterKnife.bind(this);
        randomMode = getIntent().getIntExtra("randomMode", 0);
        level = getIntent().getIntExtra("level", 0);
        initView();
        device = new Device(RandomTrainingActivity.this);

        device.createDeviceList(RandomTrainingActivity.this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connectFunction(RandomTrainingActivity.this);
            device.initConfig();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onDestroy()
    {
        if (trainingFlag)
            stopTraining();
        device.disconnect();
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (trainingFlag)
        {
            Toast.makeText(RandomTrainingActivity.this, "请先停止训练后再退出!", Toast
                    .LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    public void initView()
    {
        tvTitle.setText("随机训练");
        imgHelp.setVisibility(View.VISIBLE);
        timeAdapter = new RandomTimeAdapter(this, timeList);
        lvTimes.setAdapter(timeAdapter);
        if (randomMode == 0)
        {
            //次数随机
            llTrainingTimes.setVisibility(View.VISIBLE);
            llTrainingTime.setVisibility(View.GONE);
            barTrainingTimes.setOnSeekBarChangeListener(new MySeekBarListener
                    (tvTrainingTimes, 500));
            imgTrainingTimesSub.setOnTouchListener(new AddOrSubBtnClickListener
                    (barTrainingTimes, 0));
            imgTrainingTimesAdd.setOnTouchListener(new AddOrSubBtnClickListener
                    (barTrainingTimes, 1));
        } else
        {
            //时间随机
            llTrainingTimes.setVisibility(View.GONE);
            llTrainingTime.setVisibility(View.VISIBLE);
            barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener
                    (tvTrainingTime, 30));
            imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener
                    (barTrainingTime, 0));
            imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener
                    (barTrainingTime, 1));
        }
        if (level != 0)
        {
            tvTitle.setText("换物跑");
            switch (level)
            {
                case 1:
                    level = 20;
                    break;
                case 2:
                    level = 50;
                    break;
                case 3:
                    level = 100;
                    break;
            }
            barTrainingTimes.setProgress(level);
        }
        //设置seekbar 拖动事件的监听器
        barDelayTime.setOnSeekBarChangeListener(new MySeekBarListener(tvDelayTime, 10));
        barOverTime.setOnSeekBarChangeListener(new MySeekBarListener(tvOverTime, 30));
        //设置加减按钮的监听事件
        imgDelayTimeSub.setOnTouchListener(new AddOrSubBtnClickListener
                (barDelayTime, 0));
        imgDelayTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener
                (barDelayTime, 1));
        imgOverTimeSub.setOnTouchListener(new AddOrSubBtnClickListener
                (barOverTime, 0));
        imgOverTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener
                (barOverTime, 1));

        //选择设备个数spinner
        String[] num = new String[Device.DEVICE_LIST.size()];
        for (int i = 0; i < num.length; i++)
        {
            num[i] = (i + 1) + "个";
        }
        spDevNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spDevNum, num)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                super.onItemSelected(adapterView, view, i, l);
                totalNum = i + 1;
                String str = "";
                for (int j = 0; j < totalNum; j++)
                    str += Device.DEVICE_LIST.get(j).getDeviceNum() + "  ";
                tvDeviceList.setText(str);

                //Toast.makeText(DribblingGameActivity.this, "所选择的设备个数是" + totalNum + "个", Toast.LENGTH_SHORT).show();
            }
        });

        spDevNum.setSelection(num.length - 1);


        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);
        //设定灯光模式checkBox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgLightModeBeside, imgLightModeCenter, imgLightModeAll};
        lightModeCheckBox = new CheckBox(1, views1);
        new CheckBoxClickListener(lightModeCheckBox);
        //设定灯光颜色checkBox组合的点击事件
        ImageView[] views2 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorBlueRed};
        lightColorCheckBox = new CheckBox(1, views2);
        new CheckBoxClickListener(lightColorCheckBox);
    }

    @OnClick({R.id.btn_begin, R.id.layout_cancel, R.id.img_help})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_begin:
                Toast.makeText(getApplicationContext(), "测试", Toast.LENGTH_SHORT).show();
                if (!device.checkDevice(RandomTrainingActivity.this))
                    return;
                if (!trainingFlag)
                    startTraining();
                else
                    stopTraining();
                break;
            //头部返回按钮
            case R.id.layout_cancel:
                finish();
                break;
            case R.id.img_help:
                Intent intent = new Intent(this, VideoActivity.class);
                startActivity(intent);
                break;
        }
    }

    //获取设备灯编号
    private char getLightNum()
    {
        int position = RandomUtils.getRandomNum(100) % totalNum;
        currentLight = Device.DEVICE_LIST.get(position).getDeviceNum();
        Log.d(Constant.LOG_TAG, "turn on :" + currentLight + "-" + currentTimes);
        return currentLight;
    }

    //开始训练
    public void startTraining()
    {
        //训练开始
        trainingFlag = true;
        btnBegin.setText("停止");
        //totalTimes = new Integer(tvTrainingTimes.getText().toString().trim());
        totalTimes = 10000;
        delayTime = (int) ((new Double(tvDelayTime.getText().toString().trim())) * 1000);
        overTime = new Integer(tvOverTime.getText().toString().trim()) * 1000;
        trainingTime = (int) ((new Double(tvTrainingTime.getText().toString().trim())) * 60 * 1000);
        Log.d(Constant.LOG_TAG, "系统参数:" + totalTimes + "-" + delayTime + "-" +
                overTime);
        //数据清空
        currentTimes = 0;
        durationTime = 0;
        lostTimes = 0;
        timeList.clear();
        tvAverageTime.setText("---");
        tvLostTimes.setText("---");
        timeAdapter.notifyDataSetChanged();
        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();

        //发送开灯命令
        turnOnLight();
        //开启超时线程
        overTimeThread = new OverTimeThread();
        overTimeThread.start();
        //开启接收返回灭灯时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD,
                TIME_RECEIVE).start();
        //开启计时线程
        beginTime = System.currentTimeMillis();
        timer = new Timer(timerHandler, trainingTime);
        timer.setBeginTime(beginTime);
        timer.start();
    }

    //判断训练是否结束
    public void isTrainingOver()
    {
        if (!trainingFlag)
            return;
        //次数随机
        if (randomMode == 0)
        {
            if (currentTimes < totalTimes)
                //发送开灯命令
                turnOnLight();
            else
                stopTraining();
        } else//时间随机
            turnOnLight();
    }

    //停止训练
    public void stopTraining()
    {
        trainingFlag = false;
        btnBegin.setText("开始");
        btnBegin.setEnabled(false);
        if (overTimeThread != null)
            overTimeThread.stopThread();
        if (timer != null)
            timer.stopTimer();
        device.turnOffAllTheLight();

        //计算平均时间
        int totalTime = 0;
        for (TimeInfo info : timeList)
        {
            totalTime += info.getTime();
        }
        totalTime += lostTimes * overTime;
        Log.d(Constant.LOG_TAG, totalTime + "");
        tvAverageTime.setText(new DecimalFormat("0.00").format((totalTime * 1.0 / timeList.size())) + "毫秒");
        //暂停0.5秒,等全部数据返回结束接收线程
        Timer.sleep(500);
        //结束接收返回灭灯时间线程
        ReceiveThread.stopThread();
        btnBegin.setEnabled(true);
    }

    //开灯
    private void turnOnLight()
    {
        java.util.Timer timer1 = new java.util.Timer();
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                device.sendOrder(getLightNum(),
                        Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                        Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                        Order.BlinkModel.NONE,
                        Order.LightModel.values()[lightModeCheckBox.getCheckId()],
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
                currentTimes++;
                durationTime = 0;
            }
        };
        timer1.schedule(timerTask, delayTime);

    }

    //关灯
    private void turnOffLight()
    {
        //device.turnOffLight(currentLight);
        device.sendOrder(currentLight,
                Order.LightColor.NONE,
                Order.VoiceMode.values()[cbOverTimeVoice.isChecked() ? 1 : 0],
                Order.BlinkModel.NONE,
                Order.LightModel.TURN_OFF,
                Order.ActionModel.TURN_OFF,
                Order.EndVoice.NONE);
        TimeInfo info = new TimeInfo();
        info.setDeviceNum(currentLight);
        timeList.add(info);
    }


    //超时线程
    class OverTimeThread extends Thread
    {
        private boolean stop = false;

        public void stopThread()
        {
            stop = true;
        }

        @Override
        public void run()
        {
            while (!stop)
            {
                durationTime += 100;
                if (durationTime == overTime)
                {
                    //发送关灯命令
                    turnOffLight();
                    lostTimes++;
                    Message msg = new Message();
                    msg.what = LOST_TIME;
                    handler.sendMessage(msg);
                }
                Timer.sleep(100);
            }
        }
    }
}

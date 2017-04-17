package com.oucb303.training.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.RandomTimesModuleAdapter;
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
import com.oucb303.training.utils.RandomUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2017/4/10.
 */
public class RandomTimeActivity extends AppCompatActivity {

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
    @Bind(R.id.img_training_time_add)
    ImageView imgTrainingTimeAdd;
    @Bind(R.id.training_time)
    LinearLayout TrainingTime;
    @Bind(R.id.tv_training_times)
    TextView tvTrainingTimes;
    @Bind(R.id.img_training_times_sub)
    ImageView imgTrainingTimesSub;
    @Bind(R.id.bar_training_times)
    SeekBar barTrainingTimes;
    @Bind(R.id.img_training_times_add)
    ImageView imgTrainingTimesAdd;
    @Bind(R.id.training_times)
    LinearLayout TrainingTimes;
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
    @Bind(R.id.sp_light_num)
    Spinner spLightNum;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.sp_dev_num)
    Spinner spDevNum;
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
    @Bind(R.id.tv_current_times)
    TextView tvCurrentTimes;
    @Bind(R.id.tv_lost_times)
    TextView tvLostTimes;
    @Bind(R.id.tv_average_time)
    TextView tvAverageTime;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.btn_begin)
    Button btnBegin;


    private int level;
    private Device device;
    //当前选用的设备个数
    private int totalNum;
    //当前选择的每次亮灯个数
    private int everyLightNum;
    //训练开始的标志
    private boolean trainingFlag = false;
    //运行的总次数、当前运行的次数、遗漏次数、训练的总时间
    private int totalTimes, currentTimes, lostTimes, trainingTime, currentTimes1;
    //延迟时间 单位是毫秒
    private int delayTime;
    //超时时间 单位毫秒
    private int overTime;
    //开灯命令发送后 灯持续亮的时间,单位毫秒
    private int durationTime;
    //开始时间
    private long beginTime;
    //编号和时间的列表
    private ArrayList<TimeInfo> timeList = new ArrayList<>();
    //超时线程
    private OverTimeThread overTimeThread;
    //计时线程
    private Timer timer;
    //当前亮的灯
    private char[] currentLight;
    //获取到的是当前亮的灯编号
    private char[] lastTurnOnLight;

    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox;


    private ArrayAdapter<String> adapterDeviceNum;
    private Context context;
    private RandomTimesModuleAdapter randomTimesModuleAdapter;

    //接收到电量信息标志
    private final int POWER_RECEIVE = 1;
    //接收到灭灯时间标志
    private final int TIME_RECEIVE = 2;
    //停止训练标志
    private final int STOP_TRAINING = 3;
    //更新时间运行次数等信息
    private final int LOST_TIME = 4;
    private final int UPDATE_TIMES = 5;

    Handler timerHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == timer.TIMER_FLAG)
            {
                //如果是时间随机，并且当前时间减去开始时间的时间差超过训练总时间
                if (timer.time >= trainingTime)
                {
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
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                //接收到返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    //返回数据不为空
                    if (data !=null&&data.length()>=4)
                    {
                        timeList.addAll(DataAnalyzeUtils.analyzeTimeData(data));
                        if (randomTimesModuleAdapter != null)
                        {
                            randomTimesModuleAdapter.notifyDataSetChanged();
                            //将列表移动到最后的位置
                            lvTimes.setSelection(timeList.size()-1);
                        }
                        Log.i("当前运行总次数currentTimes1：",""+currentTimes1);
                        tvCurrentTimes.setText(currentTimes1 + "");
                        isTrainingOver();
                    }
                    break;
                case LOST_TIME:
                    tvLostTimes.setText(lostTimes + "");
                    isTrainingOver();
                    break;
                //更新完成次数
                case UPDATE_TIMES:
                    randomTimesModuleAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充屏幕的UI
        setContentView(R.layout.activity_random_train);
        //返回xml中定义的视图或组件的ID
        ButterKnife.bind(this);

        level = getIntent().getIntExtra("level", 0);

        context = this;

        device = new Device(this);
        //更新连接设备列表
        device.createDeviceList(this);
        //判断是否插入协调器
        if (device.devCount > 0) {
            device.connect(this);
            device.initConfig();
        }
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        device.disconnect();
    }

    public void initView()
    {
        tvTitle.setText("时间随机");

        randomTimesModuleAdapter = new RandomTimesModuleAdapter(this,timeList);
        lvTimes.setAdapter(randomTimesModuleAdapter);

        //设备排序
        Collections.sort(Device.DEVICE_LIST,new PowerInfoComparetor());

        TrainingTimes.setVisibility(View.GONE);
        TrainingTime.setVisibility(View.VISIBLE);

        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime,30));
        //0为减，1为加
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime,1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime,0));

        //设置延时和超时的 seekbar 拖动事件的监听器
        barDelayTime.setOnSeekBarChangeListener(new MySeekBarListener(tvDelayTime,10));
        barOverTime.setOnSeekBarChangeListener(new MySeekBarListener(tvOverTime,25,5));
        //设置加减按钮的监听事件
        imgDelayTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barDelayTime,1));
        imgDelayTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barDelayTime,0));

        imgOverTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barOverTime,1));
        imgOverTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barOverTime,0));


        //初始化设备个数spinner
        String[] num = new String[Device.DEVICE_LIST.size()];
        for (int i = 0;i < num.length;i++)
            num[i] = (i + 1) + "个";

        adapterDeviceNum = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, num);
        spDevNum.setAdapter(adapterDeviceNum);

        spDevNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this,spDevNum,num) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                totalNum = i+1;
                String str = "";
                for (int j = 0;j < totalNum;j++)
                {
                    str+=Device.DEVICE_LIST.get(j).getDeviceNum()+"  ";
                }
                tvDeviceList.setText(str);
//                totalNum =  spDevNum.getSelectedItemPosition()+1;
                String[] everyTimeNum = new String[totalNum];
                for (int j = 0;j < everyTimeNum.length;j++)
                    everyTimeNum[j] = (j + 1) + "个";

                ArrayAdapter<String> adapterEveryNum = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, everyTimeNum);
                adapterEveryNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLightNum.setAdapter(adapterEveryNum);
            }
        });

        //初始化每次亮灯个数spinner
        spLightNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                everyLightNum = i+1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox)
        {
            @Override
            public void doOtherThings(int checkedId) {
                super.doOtherThings(checkedId);
                //触碰或全部
                if (checkedId == 2|| checkedId == 3)
                {
                    if (barDelayTime.getProgress() < 2)
                        barDelayTime.setProgress(2);
                }
                else
                {
                    imgDelayTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barDelayTime,0));
                }
            }
        };
        //设定灯光颜色checkBox组合的点击事件
        ImageView[] views2 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorBlueRed};
        lightColorCheckBox = new CheckBox(1, views2);
        new CheckBoxClickListener(lightColorCheckBox);
    }
    @OnClick({R.id.btn_begin, R.id.layout_cancel, R.id.img_help,R.id.btn_on,R.id.btn_off})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_begin:
                if (!device.checkDevice(this))
                    return;
                if (!trainingFlag)
                    startTraining();
                else
                    stopTraining();
                break;
            case R.id.layout_cancel:
                finish();
                device.turnOffAllTheLight();
                break;
            case R.id.btn_on:
                //totalNum组数，1：每组设备个数，0：类型
                device.turnOnButton(totalNum,1,0);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                break;
        }
    }
    //开始训练
    public void startTraining()
    {
        trainingFlag = true;
        btnBegin.setText("停止");
        //运行的总次数
        totalTimes = new Integer(tvTrainingTimes.getText().toString().trim());
        //延迟时间
        delayTime = (int) ((new Double(tvDelayTime.getText().toString().trim())) * 1000);
        //超时时间
        overTime = new Integer(tvOverTime.getText().toString().trim()) * 1000;
        //训练总时间
        trainingTime = (int) ((new Double(tvTrainingTime.getText().toString().trim())) * 60 * 1000);
        //数据清空
        currentTimes = 0;
        currentTimes1 = 0;

        lostTimes = 0;
        durationTime = 0;
        timeList.clear();
        //平均时间和遗漏次数
        tvAverageTime.setText("---");
        tvLostTimes.setText("---");
        tvCurrentTimes.setText("---");

        randomTimesModuleAdapter.notifyDataSetChanged();

        //清除串口数据
        new ReceiveThread(handler,device.ftDev,ReceiveThread.CLEAR_DATA_THREAD,0).start();
        //发送开灯命令
        turnOnLight();
        //开启超时线程
        overTimeThread = new OverTimeThread();
        overTimeThread.start();

        //开启接收返回灭灯时间线程
        new ReceiveThread(handler,device.ftDev,ReceiveThread.TIME_RECEIVE_THREAD,TIME_RECEIVE).start();

        //开启计时线程
        beginTime =  System.currentTimeMillis();
        timer = new Timer(timerHandler,trainingTime);
        timer.setBeginTime(beginTime);
        timer.start();
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
        tvAverageTime.setText(new DecimalFormat("0.00").format((totalTime * 1.0 / timeList.size())) + "毫秒");

        //暂停0.5秒,等全部数据返回,结束接收线程
        Timer.sleep(500);
        //结束接收返回灭灯时间线程
        ReceiveThread.stopThread();
        btnBegin.setEnabled(true);
    }
    //判断训练是否结束
    public void isTrainingOver()
    {
        //如果结束了
        if (!trainingFlag)
            return;
        if (currentTimes1 < totalTimes)
            turnOnLight();
        else
            stopTraining();
    }
    //获取设备编号
    public char[] getLightNum()
    {
        currentLight = new char[everyLightNum];
        for (int i = 0;i<everyLightNum;i++)
            currentLight[i] = '\0';

        int[] position = new int[everyLightNum];
        int count = 0;
        for (int i = 0;i<everyLightNum;i++)
        {
            position[i] = -1;
        }
        while (count < everyLightNum)
        {
            int num = RandomUtils.getRandomNum(100) % totalNum;
            boolean flag = true;
            for (int j = 0;j < everyLightNum;j++) {
                if (position[j] == num) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                position[count] = num;
                //当前亮的灯
                currentLight[count] = Device.DEVICE_LIST.get(position[count]).getDeviceNum();
                count++;
            }
        }
        return currentLight;
    }
    //开灯
    public void turnOnLight()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (delayTime > 0)
                    Timer.sleep(delayTime);

                lastTurnOnLight = new char[everyLightNum];
                lastTurnOnLight = getLightNum();
//                for (int i = 0;i<everyLightNum;i++)
//                {
//                    Log.i("当前亮灯编号lastTurnOnLight：",""+lastTurnOnLight[i]);
//                }

                //获取到的是当前亮的灯编号
                for (int i = 0;i<everyLightNum;i++)
                {
                    device.sendOrder(lastTurnOnLight[i],
                            Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                            Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                            Order.BlinkModel.NONE,
                            Order.LightModel.values()[1],
                            Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                            Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
                    //当前运行次数
                    currentTimes++;
                    currentTimes1 = currentTimes/everyLightNum;
                    //开灯命令发送后 灯持续亮的时间
                    durationTime = 0;
                }

            }
        }).start();
    }
    //如果超时了就要关灯
    public void turnOffLight(char deviceNum)
    {

        device.sendOrder(deviceNum,
                Order.LightColor.NONE,
                Order.VoiceMode.values()[cbOverTimeVoice.isChecked() ? 1 : 0],
                Order.BlinkModel.NONE,
                Order.LightModel.TURN_OFF,
                Order.ActionModel.TURN_OFF,
                Order.EndVoice.NONE);
        //如果超时了，也要放到timeList里，然后放到adapter里，显示在右侧的listView之中，更新listView的数据
        TimeInfo info = new TimeInfo();
        info.setDeviceNum(deviceNum);
        timeList.add(info);
        Message msg = handler.obtainMessage();
        msg.what = UPDATE_TIMES;
        msg.obj = "";
        handler.sendMessage(msg);
    }
    //超时线程,从开始训练就开始跑
    class OverTimeThread extends Thread
    {
        private boolean stop = false;
        public void stopThread()
        {
            stop = true;
        }
        @Override
        public void run() {
            while (!stop)
            {
                for (int i = 0;i<everyLightNum;i++)
                {

                    durationTime += 100;
                    //如果亮灯时间等于超时时间，关灯
                    if (durationTime == overTime)
                    {
                        Log.i("当前亮灯编号??????：",""+currentLight[i]);
                        turnOffLight(currentLight[i]);
                        lostTimes++;
                        //更新遗漏次数
                        Message msg = new Message();
                        msg.what = LOST_TIME;
                        handler.sendMessage(msg);
                    }
                    Timer.sleep(100);
                }

            }
        }
    }
}

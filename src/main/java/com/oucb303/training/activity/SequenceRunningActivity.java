package com.oucb303.training.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oucb303.training.App;
import com.oucb303.training.R;
import com.oucb303.training.adpter.RandomTimeAdapter;
import com.oucb303.training.adpter.SequenceItemListAdapter;
import com.oucb303.training.daoservice.SequenceSer;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.entity.Light;
import com.oucb303.training.entity.SequenceGroup;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.model.TimeInfo;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.DataAnalyzeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 运行设定的序列
 */
public class SequenceRunningActivity extends AppCompatActivity
{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.lv_sequence)
    ListView lvSequence;
    @Bind(R.id.tv_loop_times)
    TextView tvLoopTimes;
    @Bind(R.id.img_loop_times_sub)
    ImageView imgLoopTimesSub;
    @Bind(R.id.bar_loop_times)
    SeekBar barLoopTimes;
    @Bind(R.id.img_loop_times_add)
    ImageView imgLoopTimesAdd;
    @Bind(R.id.img_whole_delay_time_sub)
    ImageView imgWholeDelayTimeSub;
    @Bind(R.id.bar_whole_delay_time)
    SeekBar barWholeDelayTime;
    @Bind(R.id.img_whole_delay_time_add)
    ImageView imgWholeDelayTimeAdd;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;


    private final int TIME_RECEIVE = 1, STOP_TRAINING = 2;
    @Bind(R.id.tv_looped_times)
    TextView tvLoopedTimes;
    @Bind(R.id.tv_step)
    TextView tvStep;
    @Bind(R.id.lv_time_list)
    ListView lvTimeList;
    @Bind(R.id.tv_whole_delay_time)
    TextView tvWholeDelayTime;
    private SequenceSer sequenceSer;
    private SequenceItemListAdapter sequenceItemListAdapter;
    private List<SequenceGroup> sequenceGroupList;
    private Device device;
    //序列编号
    private long sequenceId;
    // 总循环次数、已经循环的次数
    private int totalLoopTimes, loopedTimes;
    //全局延时
    private int wholeDelayTime;
    //当前执行的步骤
    private int step;
    //是否正在训练
    private boolean isTraning = false;
    //是否在检查训练结束
    private boolean isChecking = false;
    private Timer timer;
    private List<IsOnLightInfo> lightInfoList = new ArrayList<>();
    private List<TimeInfo> timeInfoList = new ArrayList<>();
    private RandomTimeAdapter timeListAdapter;
    private MyThread checkingThread;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String data = msg.obj.toString();
            switch (msg.what)
            {
                case Timer.TIMER_FLAG:
                    tvTotalTime.setText(msg.obj.toString());
                    tvStep.setText(step + "");
                    tvLoopedTimes.setText(loopedTimes + "");
                    break;
                //接收到时间数据
                case TIME_RECEIVE:
                    if (data != null && data.length() > 0)
                        analyzeData(data);
                    break;
                case STOP_TRAINING:
                    stopTraining();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_running);
        ButterKnife.bind(this);
        sequenceId = getIntent().getLongExtra("sequenceId", 1);
        sequenceSer = new SequenceSer(((App) getApplication()).getDaoSession());
        initView();
        device = new Device(this);
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connect(this);
            device.initConfig();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        device.disconnect();
    }

    private void initView()
    {
        tvTitle.setText("运行序列");
        sequenceGroupList = sequenceSer.loadSequenceGroups(sequenceId);
        Log.d(Constant.LOG_TAG, sequenceGroupList.size() + "");
        sequenceItemListAdapter = new SequenceItemListAdapter(this, sequenceGroupList);
        lvSequence.setAdapter(sequenceItemListAdapter);
        sequenceItemListAdapter.notifyDataSetChanged();

        //设置seekbar 拖动事件的监听器
        barLoopTimes.setOnSeekBarChangeListener(new MySeekBarListener(tvLoopTimes, 30));
        barWholeDelayTime.setOnSeekBarChangeListener(new MySeekBarListener(tvWholeDelayTime, 60));
        //设置加减按钮的监听事件
        imgLoopTimesSub.setOnTouchListener(new AddOrSubBtnClickListener(barLoopTimes, 0));
        imgLoopTimesAdd.setOnTouchListener(new AddOrSubBtnClickListener(barLoopTimes, 1));
        imgWholeDelayTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barWholeDelayTime, 0));
        imgWholeDelayTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barWholeDelayTime, 1));
    }

    @OnClick({R.id.layout_cancel, R.id.btn_begin})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
            case R.id.btn_begin:
                if (!device.checkDevice(this))
                    return;
                if (isTraning)
                    stopTraining();
                else
                    startTraining();
                break;
        }
    }

    private void startTraining()
    {
        btnBegin.setText("停止");
        isTraning = true;
        step = 0;
        loopedTimes = 0;
        tvLoopedTimes.setText("0");
        tvStep.setText("0");

        lightInfoList.clear();
        tvTotalTime.setText("00:00:00");
        turnOnItems();
        timeInfoList.clear();
        timeListAdapter = new RandomTimeAdapter(this, timeInfoList);
        lvTimeList.setAdapter(timeListAdapter);
        timeListAdapter.notifyDataSetChanged();


        totalLoopTimes = new Integer(tvLoopTimes.getText().toString());
        wholeDelayTime = new Integer(tvWholeDelayTime.getText().toString()) * 1000;

        //清除串口
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();
        //开启接收时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        checkingThread = new MyThread();
        checkingThread.start();
        //计时器
        timer = new Timer(handler);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();
    }

    private void stopTraining()
    {
        btnBegin.setText("开始");
        isTraning = false;
        timer.stopTimer();
        checkingThread.stopThread();
        device.turnOffAllTheLight();
    }

    //开某一步骤的所有灯
    private void turnOnItems()
    {
        SequenceGroup sequenceGroup = sequenceGroupList.get(step);
        for (Light light : sequenceGroup.getLights())
        {
            IsOnLightInfo lightInfo = new IsOnLightInfo();
            lightInfo.light = light;
            lightInfo.beginTime = System.currentTimeMillis();
            lightInfoList.add(lightInfo);
            device.sendOrder((char) light.getDeviceNum(),
                    Order.LightColor.values()[light.getLightColor()],
                    Order.VoiceMode.NONE,
                    Order.BlinkModel.NONE,
                    Order.LightModel.values()[light.getLightMode()],
                    Order.ActionModel.values()[light.getActionMode()],
                    Order.EndVoice.NONE
            );
        }
        Log.d("AAAA", "lightInfoList.size:" + lightInfoList.size());
        step++;
    }

    //关闭一盏灯
    private void turnOffLight(char deviceNum)
    {
        device.sendOrder(deviceNum,
                Order.LightColor.NONE,
                Order.VoiceMode.NONE,
                Order.BlinkModel.NONE,
                Order.LightModel.TURN_OFF,
                Order.ActionModel.TURN_OFF,
                Order.EndVoice.NONE
        );
    }

    //判断是否结束
    private void isTrainingOver()
    {
        isChecking = true;
        //一个步骤的灯全部灭了
        if (lightInfoList.size() == 0)
        {
            //执行到了最后一步
            if (step >= sequenceGroupList.size())
            {
                if (totalLoopTimes > loopedTimes)
                    loopedTimes++;
                //全局循环结束
                if (totalLoopTimes <= loopedTimes)
                {
                    Message msg = handler.obtainMessage();
                    msg.what = STOP_TRAINING;
                    msg.obj = "";
                    handler.sendMessage(msg);
                } else
                {
                    Timer.sleep(wholeDelayTime);
                    step = 0;
                    turnOnItems();
                }
            } else
            {
                Timer.sleep(sequenceGroupList.get(step - 1).getDelayTime());
                turnOnItems();
            }
        }
        isChecking = false;
    }

    private void analyzeData(String data)
    {
        Log.d("AAAA", "lightInfoList.size:" + lightInfoList.size());

        List<TimeInfo> timeInfos = DataAnalyzeUtils.analyzeTimeData(data);
        for (TimeInfo timeInfo : timeInfos)
        {
            timeInfoList.add(timeInfo);
            for (int i = 0; i < lightInfoList.size(); i++)
            {
                IsOnLightInfo lightInfo = lightInfoList.get(i);
                Log.d("AAAA", (char) lightInfo.light.getDeviceNum() + " " + timeInfo.getDeviceNum());
                if (((char) lightInfo.light.getDeviceNum()) == timeInfo.getDeviceNum())
                {
                    lightInfoList.remove(lightInfo);
                    Log.d("AAAA", "remove:" + timeInfo.getDeviceNum());
                    break;
                }
            }
        }
        timeListAdapter.notifyDataSetChanged();
    }

    //已经打开的灯的信息
    private class IsOnLightInfo
    {
        //开灯的时间
        private long beginTime;
        private Light light;
    }

    private class MyThread extends Thread
    {
        private boolean flag = true;

        public void stopThread()
        {
            flag = false;
        }

        @Override
        public void run()
        {
            while (flag)
            {
                for (int i = 0; lightInfoList != null && i < lightInfoList.size(); i++)
                {
                    IsOnLightInfo lightInfo = lightInfoList.get(i);
                    //如果超时
                    if (System.currentTimeMillis() - lightInfo.beginTime > lightInfo.light.getOverTime() * 1000)
                    {
                        TimeInfo timeInfo = new TimeInfo();
                        timeInfo.setDeviceNum((char) lightInfo.light.getDeviceNum());
                        timeInfoList.add(timeInfo);
                        //timeListAdapter.notifyDataSetChanged();

                        turnOffLight((char) lightInfo.light.getDeviceNum());
                        lightInfoList.remove(i);
                        i--;
                        Log.d("AAAA", "超时:移除" + lightInfo.light.getDeviceNum());
                    }
                }
                if (!isChecking)
                {
                    isTrainingOver();
                }
                Timer.sleep(20);
            }
            Log.d("AAAA", "stop thread");
        }
    }

}

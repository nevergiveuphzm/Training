package com.oucb303.training.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.RandomTimeAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.listener.ChangeBarClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.Constant;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.utils.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.oucb303.training.device.Device.DEVICE_LIST;

/**
 * Created by baichangcai on 2016/9/7.
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


    //感应模式和灯光模式集合
    private List<CheckBox> actionModeCheckBoxs, lightModeCheckBoxs;
    //接收到电量信息标志
    private final int POWER_RECEIVE = 1;
    //接收到灭灯时间标志
    private final int TIME_RECEIVE = 2;
    //停止训练标志
    private final int STOP_TRAINING = 3;
    //更新时间
    private final int CHANGE_TIME_LIST = 4;
    private Device device;
    //运行的总次数、当前运行的次数
    private int totalTimes, currentTimes = 0;
    //当前亮的灯
    private char currentLight;
    //延迟时间 单位是毫秒
    private int delayTime;
    //超时时间 单位毫秒
    private int overTime;
    //开灯命令发送后 灯持续亮的时间
    private int durationTime;
    //超时线程
    private OverTimeThread overTimeThread;
    private ArrayList<Map<String, Object>> timeList = new ArrayList<>();
    private RandomTimeAdapter timeAdapter;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case STOP_TRAINING:
                    stopTraining();
                    break;
                case CHANGE_TIME_LIST:
                    if (timeAdapter != null)
                    {
                        timeAdapter.notifyDataSetChanged();
                        lvTimes.setSelection(timeList.size() - 1);
                    }
                    break;
                //获取到电量信息
                case POWER_RECEIVE:
                    DataAnalyzeUtils.analyzePowerData(msg.obj.toString(),
                            RandomTrainingActivity.this);
                    break;
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    //返回数据不为空
                    if (data != null && !data.equals(""))
                    {
                        if (data.length() >= 7)
                        {
                            timeList.addAll(DataAnalyzeUtils.analyzeTimeData(data));
                            timeAdapter.notifyDataSetChanged();
                            lvTimes.setSelection(timeList.size() - 1);
                        }
                        //添加延时
                        try
                        {
                            Thread.sleep(delayTime);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        if (currentTimes < totalTimes)
                            //发送开灯命令
                            turnOnLight();
                        else
                            stopTraining();
                    }
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
        initView();
        device = new Device(RandomTrainingActivity.this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        device.createDeviceList(RandomTrainingActivity.this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connectFunction(RandomTrainingActivity.this);
            device.initConfig();
        }
    }

    @Override
    protected void onPause()
    {
        device.disconnectFunction();
        stopTraining();
        super.onPause();
    }

    public void initView()
    {
        timeAdapter = new RandomTimeAdapter(this, timeList);
        lvTimes.setAdapter(timeAdapter);
        //设置seekbar 拖动事件的监听器
        barTrainingTimes.setOnSeekBarChangeListener(new MySeekBarListener
                (tvTrainingTimes, 10000));
        barDelayTime.setOnSeekBarChangeListener(new MySeekBarListener(tvDelayTime, 10));
        barOverTime.setOnSeekBarChangeListener(new MySeekBarListener(tvOverTime, 30));
        //设置加减按钮的监听事件
        imgTrainingTimesSub.setOnTouchListener(new ChangeBarClickListener
                (barTrainingTimes, 0));
        imgTrainingTimesAdd.setOnTouchListener(new ChangeBarClickListener
                (barTrainingTimes, 1));
        imgDelayTimeSub.setOnTouchListener(new ChangeBarClickListener
                (barDelayTime, 0));
        imgDelayTimeAdd.setOnTouchListener(new ChangeBarClickListener
                (barDelayTime, 1));
        imgOverTimeSub.setOnTouchListener(new ChangeBarClickListener
                (barOverTime, 0));
        imgOverTimeAdd.setOnTouchListener(new ChangeBarClickListener
                (barOverTime, 1));
        //设定感应模式checkBox组合的点击事件
        actionModeCheckBoxs = new ArrayList<>();
        actionModeCheckBoxs.add(new CheckBox(0, true, imgActionModeTouch));
        actionModeCheckBoxs.add(new CheckBox(1, false, imgActionModeLight));
        actionModeCheckBoxs.add(new CheckBox(2, false, imgActionModeTogether));
        new CheckBoxClickListener(actionModeCheckBoxs);
        //设定灯光模式checkBox组合的点击事件
        lightModeCheckBoxs = new ArrayList<>();
        lightModeCheckBoxs.add(new CheckBox(0, true, imgLightModeCenter));
        lightModeCheckBoxs.add(new CheckBox(1, false, imgLightModeAll));
        lightModeCheckBoxs.add(new CheckBox(2, false, imgLightModeBeside));
        new CheckBoxClickListener(lightModeCheckBoxs);
    }

    @OnClick(R.id.btn_begin)
    public void onClick()
    {
        if (device.devCount <= 0)
        {
            Toast.makeText(RandomTrainingActivity.this, "还未插入协调器,请插入协调器!", Toast
                    .LENGTH_SHORT).show();
            return;
        }
        if (DEVICE_LIST.isEmpty() || DEVICE_LIST.size() == 0)
        {
            Toast.makeText(RandomTrainingActivity.this, "未检测到任何设备,请开启设备!", Toast
                    .LENGTH_SHORT).show();
            return;
        }
        String btnBeginText = btnBegin.getText().toString().trim();
        if (btnBeginText.equals("开始"))
        {
            btnBegin.setText("停止");
            totalTimes = new Integer(tvTrainingTimes.getText().toString().trim());
            //totalTimes = 10000;
            delayTime = (int) ((new Double(tvDelayTime.getText().toString().trim())) * 1000);
            overTime = new Integer(tvOverTime.getText().toString().trim());
            Log.d(Constant.LOG_TAG, "系统参数:" + totalTimes + "-" + delayTime + "-" +
                    overTime);
            //数据清空
            currentTimes = 0;
            durationTime = 0;
            timeList.clear();
            timeAdapter.notifyDataSetChanged();
            //发送开灯命令
            turnOnLight();
            //开启超时线程
            overTimeThread = new OverTimeThread();
            overTimeThread.start();
            //开启接收返回灭灯时间线程
            new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD,
                    TIME_RECEIVE).start();
        }
        else if (btnBeginText.equals("停止"))
        {
            stopTraining();
        }
    }

    //获取设备灯编号
    private char getLightNum()
    {
        Map<String, Object> light = Device.DEVICE_LIST.get(RandomUtils.getRandomNum
                (Device.DEVICE_LIST.size()));
        char num = ((char) light.get("deviceNum"));
        currentLight = num;
        Log.d(Constant.LOG_TAG, "turn on :" + num + "-" + currentTimes);
        return num;
    }

    //停止训练
    public void stopTraining()
    {
        btnBegin.setText("开始");
        device.turnOffAll();
        //结束接收返回灭灯时间线程
        ReceiveThread.stopThread();
        if (overTimeThread != null)
            overTimeThread.stopThread();
    }

    //开灯
    private void turnOnLight()
    {
        device.turnOnLight(getLightNum());
        currentTimes++;
        durationTime = 0;
    }

    //关灯
    private void turnOffLight()
    {
        device.turnOffLight(currentLight);
        Map<String, Object> map = new HashMap<>();

        map.put("deviceNum", currentLight);
        map.put("time", overTime * 1000);
        timeList.add(map);
        Message msg = new Message();
        msg.what = CHANGE_TIME_LIST;
        handler.sendMessage(msg);

        if (currentTimes == totalTimes)
        {
            Message msg1 = new Message();
            msg1.what = STOP_TRAINING;
            handler.sendMessage(msg1);
        }
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
                if (durationTime == overTime * 1000)
                {
                    //发送关灯命令
                    turnOffLight();
                    try
                    {
                        Thread.sleep(delayTime);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    if (currentTimes < totalTimes)
                        turnOnLight();
                }
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

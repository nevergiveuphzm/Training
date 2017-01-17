package com.oucb303.training.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.adpter.ShuttleRunAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.SpinnerItemSelectedListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.utils.Constant;
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
 * 折返跑 每组两个设备
 */

public class ShuttleRunActivity extends AppCompatActivity
{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save)
    ImageView imgSave;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
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
    @Bind(R.id.sp_training_times)
    Spinner spTrainingTimes;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.lv_group)
    ListView lvGroup;
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

    private final int TIME_RECEIVE = 1, POWER_RECEIVE = 2, UPDATE_TIMES = 3, STOP_TRAINING = 4;
    //做多分组数目
    private int maxGroupNum;
    //每组所需设备个数
    private final int groupSize = 2;
    //分组数量
    private int groupNum;
    //总的训练次数
    private int totalTrainingTimes;
    private GroupListViewAdapter groupListViewAdapter;
    private ShuttleRunAdapter shuttleRunAdapter;
    private Device device;
    //训练开始标志
    private boolean trainingBeginFlag = false;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox;

    //时间数据
    private ArrayList<TimeInfo>[] timeList;
    //每组已完成的训练次数,和每组完成训练所用的时间
    private int[] completedTimes;
    //每组训练完成的时间
    private int[] finishTime;
    private Timer timer;
    //训练开始时间
    private long startTime;

    private int level;


    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Timer.TIMER_FLAG:
                    String time = msg.obj.toString();
                    tvTotalTime.setText(time);
                    break;
                //接收到返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7)
                    {
                        analyzeTimeData(data);
                    }
                    break;
                case UPDATE_TIMES:
                    shuttleRunAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_shuttle_run);
        ButterKnife.bind(this);
        level = getIntent().getIntExtra("level", 1);
        device = new Device(this);
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connect(this);
            device.initConfig();
        }
        initView();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        device.disconnect();

    }

    public void initView()
    {
        tvTitle.setText("折返跑训练");
        imgHelp.setVisibility(View.VISIBLE);
        imgSave.setVisibility(View.VISIBLE);
        //初始化设备列表

        /*Device.DEVICE_LIST.clear();
        for (int i = 0; i < 10; i++)
        {
            DeviceInfo info = new DeviceInfo();
            info.setDeviceNum((char) ('A' + i * 2));
            Device.DEVICE_LIST.add(info);
        }
        Device.DEVICE_LIST.add(new DeviceInfo('B'));
        Device.DEVICE_LIST.add(new DeviceInfo('F'));*/
        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        switch (level)
        {
            case 1:
                level = 0;
                break;
            case 2:
                level = 3;
                break;
            case 3:
                level = 7;
                break;
        }

        //初始化分组下拉框
        maxGroupNum = Device.DEVICE_LIST.size() / 2;
        String[] groupNumChoose = new String[maxGroupNum + 1];
        groupNumChoose[0] = " ";
        for (int i = 1; i <= maxGroupNum; i++)
            groupNumChoose[i] = (i + " 组");
        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(ShuttleRunActivity.this, spGroupNum, groupNumChoose)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                super.onItemSelected(adapterView, view, i, l);
                groupNum = i;
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });


        //初始化训练强度下拉框
        String[] trainingOptions = new String[10];
        for (int i = 1; i <= 10; i++)
        {
            trainingOptions[i - 1] = "50米 * " + i * 2;
        }
        spTrainingTimes.setOnItemSelectedListener(new SpinnerItemSelectedListener(ShuttleRunActivity.this, spTrainingTimes, trainingOptions)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                totalTrainingTimes = (i + 1) * 2;
            }
        });


        ///初始化分组listView
        groupListViewAdapter = new GroupListViewAdapter(ShuttleRunActivity.this, groupSize);
        lvGroup.setAdapter(groupListViewAdapter);
        //解决listView 与scrollView的滑动冲突
        lvGroup.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                //从listView 抬起时将控制权还给scrollview
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    svContainer.requestDisallowInterceptTouchEvent(false);
                else
                    svContainer.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //初始化
        shuttleRunAdapter = new ShuttleRunAdapter(this);
        lvTimes.setAdapter(shuttleRunAdapter);


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

    @OnClick({R.id.layout_cancel, R.id.img_help, R.id.btn_begin})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
            case R.id.img_help:
                break;
            case R.id.btn_begin:
                if (!device.checkDevice(ShuttleRunActivity.this))
                    return;
                if (trainingBeginFlag)
                    stopTraining();
                else
                    startTraining();
                break;
        }
    }

    //开始训练
    public void startTraining()
    {
        trainingBeginFlag = true;
        timeList = new ArrayList[groupNum];
        for (int i = 0; i < groupNum; i++)
            timeList[i] = new ArrayList<>();
        completedTimes = new int[groupNum];
        finishTime = new int[groupNum];
        shuttleRunAdapter.setTimeList(timeList);
        shuttleRunAdapter.setCompletedTimes(completedTimes);
        shuttleRunAdapter.setFinishTime(finishTime);
        shuttleRunAdapter.notifyDataSetChanged();

        btnBegin.setText("停止");
        //开全灯
        for (int i = 0; i < groupNum * groupSize; i++)
        {
            device.sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                    Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                    Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                    Order.BlinkModel.NONE,
                    Order.LightModel.values()[lightModeCheckBox.getCheckId()],
                    Order.ActionModel.LIGHT,
                    Order.EndVoice.NONE);
        }


        //开启接收设备返回时间的监听线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();
        startTime = System.currentTimeMillis();
        timer = new Timer(handler);
        timer.setBeginTime(startTime);
        timer.start();
    }

    //结束训练
    public void stopTraining()
    {
        trainingBeginFlag = false;
        btnBegin.setText("开始");
        //停止接收线程
        ReceiveThread.stopThread();
        device.turnOffAllTheLight();
        timer.stopTimer();
    }

    public void turnOnLight(char deviceNum)
    {
        device.sendOrder(deviceNum,
                Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                Order.BlinkModel.NONE,
                Order.LightModel.values()[lightModeCheckBox.getCheckId()],
                Order.ActionModel.LIGHT,
                Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
    }

    //解析返回来的时间数据
    public void analyzeTimeData(final String data)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
                for (TimeInfo info : infos)
                {
                    int groupId = findDeviceGroupId(info.getDeviceNum());
                    if (groupId >= groupNum)
                        continue;
                    Log.d(Constant.LOG_TAG, info.getDeviceNum() + " groupId:" + groupId);
                    List<TimeInfo> groupTimes = timeList[groupId];
                    if (groupTimes.size() != 0)
                    {
                        TimeInfo last = groupTimes.get(groupTimes.size() - 1);
                        //本次熄灭的灯和上一次熄灭的灯相同,犯规
                        if (last.getDeviceNum() == info.getDeviceNum())
                        {
                            //时间数据无效
                            info.setValid(false);
                        } else
                        {
                            if (groupTimes.get(0).getDeviceNum() == info.getDeviceNum())
                                completedTimes[groupId] += 1;
                        }
                    }
                    groupTimes.add(info);
                    //该组训练结束
                    if (completedTimes[groupId] == totalTrainingTimes)
                    {
                        finishTime[groupId] = (int) (System.currentTimeMillis() - startTime);
                    } else
                    {
                        turnOnLight(info.getDeviceNum());
                    }
                }

                Message msg = Message.obtain();
                msg.what = UPDATE_TIMES;
                msg.obj = "";
                handler.sendMessage(msg);

                if (isTrainingOver())
                {
                    Message msg1 = Message.obtain();
                    msg1.what = STOP_TRAINING;
                    msg1.obj = "";
                    handler.sendMessage(msg1);
                }
            }
        }).start();
    }


    //查找设备属于第几组
    public int findDeviceGroupId(char deviceNum)
    {
        int position = 0;
        for (int i = 0; i < Device.DEVICE_LIST.size(); i++)
        {
            if (Device.DEVICE_LIST.get(i).getDeviceNum() == deviceNum)
            {
                position = i;
                break;
            }
        }
        return position / groupSize;
    }

    //判断训练是否结束
    public boolean isTrainingOver()
    {
        for (int i = 0; i < groupNum; i++)
        {
            //只要有一组训练没完成 则训练就未结束
            if (completedTimes[i] < totalTrainingTimes)
                return false;
        }
        return true;
    }

}

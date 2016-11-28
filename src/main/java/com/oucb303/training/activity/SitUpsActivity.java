package com.oucb303.training.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.adpter.SitUpsTimeListAdapter;
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
 * 仰卧起坐
 */

public class SitUpsActivity extends AppCompatActivity
{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_training_time)
    TextView tvTrainingTime;
    @Bind(R.id.img_training_time_sub)
    ImageView imgTrainingTimeSub;
    @Bind(R.id.bar_training_time)
    SeekBar barTrainingTime;
    @Bind(R.id.img_training_time_add)
    ImageView imgTrainingTimeAdd;
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
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.img_light_color_blue)
    ImageView imgLightColorBlue;
    @Bind(R.id.img_light_color_red)
    ImageView imgLightColorRed;
    @Bind(R.id.img_light_color_blue_red)
    ImageView imgLightColorBlueRed;
    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;

    private Device device;
    private CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox;
    private final int TIME_RECEIVE = 1, POWER_RECEIVER = 2;
    //训练时间  单位毫秒
    private int trainingTime;
    //计时器
    private Timer timer;
    //是否正在训练标志
    private boolean isTraining = false;
    //每组的设备数量  最大分组数  组数
    private int groupSize = 2, maxGroupNum = 4, groupNum;
    private GroupListViewAdapter groupListViewAdapter;
    private SitUpsTimeListAdapter sitUpsTimeListAdapter;
    //训练成绩
    private int[] scores;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_ups);
        ButterKnife.bind(this);
        initView();
        device = new Device(this);
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connectFunction(this);
            device.initConfig();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        device.disconnectFunction();

    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Timer.TIMER_FLAG:
                    if (timer.time > trainingTime)
                    {
                        stopTraining();
                        return;
                    }
                    tvTotalTime.setText(msg.obj.toString());
                    break;
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7)
                    {
                        analyzeTimeData(DataAnalyzeUtils.analyzeTimeData(data));
                    }
                    break;
            }
        }
    };


    private void initView()
    {
        tvTitle.setText("仰卧起坐训练");
        ///初始化分组listView
        groupListViewAdapter = new GroupListViewAdapter(SitUpsActivity.this, groupSize);
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

        //初始化设备列表
        /*Device.DEVICE_LIST.clear();
        for (int i = 0; i < 10; i++)
        {
            PowerInfo info = new PowerInfo();
            info.setDeviceNum((char) ('A' + i * 2));
            Device.DEVICE_LIST.add(info);
        }
        Device.DEVICE_LIST.add(new PowerInfo('B'));
        Device.DEVICE_LIST.add(new PowerInfo('F'));*/
        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //初始化分组下拉框
        if (maxGroupNum > Device.DEVICE_LIST.size() / 2)
            maxGroupNum = Device.DEVICE_LIST.size() / 2;
        String[] groupNumChoose = new String[maxGroupNum + 1];
        groupNumChoose[0] = " ";
        for (int i = 1; i <= maxGroupNum; i++)
            groupNumChoose[i] = i + " 组";
        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(SitUpsActivity.this, spGroupNum, groupNumChoose)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                groupNum = i;
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });


        //训练时间拖动条初始化
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 5));
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));

        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);
        //设定灯光模式checkBox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgLightModeBeside, imgLightModeCenter, imgLightModeAll,};
        lightModeCheckBox = new CheckBox(1, views1);
        new CheckBoxClickListener(lightModeCheckBox);
        //设定灯光颜色checkBox组合的点击事件
        ImageView[] views2 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorBlueRed};
        lightColorCheckBox = new CheckBox(1, views2);
        new CheckBoxClickListener(lightColorCheckBox);

        sitUpsTimeListAdapter = new SitUpsTimeListAdapter(this);
        lvTimes.setAdapter(sitUpsTimeListAdapter);
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
                if (isTraining)
                    stopTraining();
                else
                    startTraining();
                break;
        }
    }

    private void startTraining()
    {
        btnBegin.setText("停止");
        isTraining = true;
        scores = new int[groupNum];
        sitUpsTimeListAdapter.setScores(scores);
        sitUpsTimeListAdapter.notifyDataSetChanged();
        //训练时间
        trainingTime = (int) (new Double(tvTrainingTime.getText().toString()) * 60 * 1000);

        //亮每组设备的第一个灯
        String lightIds = "";
        for (int i = 0; i < groupNum; i++)
        {
            lightIds += Device.DEVICE_LIST.get(i * 2).getDeviceNum();
        }
        sendOrder(lightIds);

        //开启接受时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        timer = new Timer(handler);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();
    }

    private void stopTraining()
    {
        isTraining = false;
        btnBegin.setText("开始");
        //结束时间线程
        timer.stopTimer();
        //结束接收时间线程
        ReceiveThread.stopThread();
        device.turnOffAllTheLight();
    }

    //解析时间
    private void analyzeTimeData(List<TimeInfo> infos)
    {
        //训练已结束
        if (!isTraining)
            return;
        String lightIds = "";
        for (TimeInfo info : infos)
        {
            int groupId = findDeviceGroupId(info.getDeviceNum());
            char next = Device.DEVICE_LIST.get(groupId * groupSize).getDeviceNum();
            if (next == info.getDeviceNum())
            {
                next = Device.DEVICE_LIST.get(groupId * groupSize + 1).getDeviceNum();
                scores[groupId] += 1;
                sitUpsTimeListAdapter.notifyDataSetChanged();
            }
            lightIds += next;
        }
        sendOrder(lightIds);
    }

    public void sendOrder(String lightIds)
    {
        device.sendOrder(lightIds, Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                Order.BlinkModel.NONE,
                Order.LightModel.values()[lightModeCheckBox.getCheckId()],
                Order.ActionModel.LIGHT,
                Order.EndVoice.NONE);
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
}

package com.oucb303.training.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.oucb303.training.R;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.adpter.ShuttleRunAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.Constant;
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

public class ShuttleRunActivity extends AppCompatActivity
{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.ll_group)
    ListView llGroup;
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


    private final int TIME_RECEIVE = 1;
    private final int POWER_RECEIVE = 2;
    @Bind(R.id.lv_times)
    ListView lvTimes;
    //做多分组数目
    private int maxGroupNum;
    //每组所需设备个数
    private final int groupSize = 2;
    //分组数量
    private int groupNum;
    //总的训练次数
    private int trainingTimes;
    private GroupListViewAdapter groupListViewAdapter;
    private ShuttleRunAdapter shuttleRunAdapter;
    private Device device;
    //训练开始标志
    private boolean trainingBeginFlag = false;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightModeCheckBox;

    //时间数据
    private ArrayList<TimeInfo>[] timeList;
    //每组已完成的训练次数,和每组完成训练所用的时间
    private int[][] groupTrainingTimes;
    private Timer timer;
    //训练开始时间
    private long startTime;


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
                        analyzeTimeData(DataAnalyzeUtils.analyzeTimeData(data));
                    }
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
        test();
        device = new Device(this);
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connectFunction(this);
            device.initConfig();
        }
        initView();
        initSlidingMenu();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        device.disconnectFunction();

    }

    public void initView()
    {
        tvTitle.setText("折返跑训练");
        imgHelp.setVisibility(View.VISIBLE);
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
        maxGroupNum = Device.DEVICE_LIST.size() / 2;
        final List<String> groupNumChoose = new ArrayList<>();
        groupNumChoose.add(" ");
        for (int i = 1; i <= maxGroupNum; i++)
            groupNumChoose.add(i + " 组");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ShuttleRunActivity.this,
                android.R.layout.simple_spinner_item, groupNumChoose);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGroupNum.setAdapter(adapter);
        spGroupNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                groupNum = i;
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        //初始化训练强度下拉框
        String[] trainingOptions = new String[10];
        for (int i = 1; i <= 10; i++)
        {
            trainingOptions[i - 1] = "50米 * " + i * 2;
        }
        ArrayAdapter<String> trainingTimesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, trainingOptions);
        trainingTimesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTrainingTimes.setAdapter(trainingTimesAdapter);
        spTrainingTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                trainingTimes = (i + 1) * 2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        ///初始化分组listView
        groupListViewAdapter = new GroupListViewAdapter(ShuttleRunActivity.this, groupSize);
        llGroup.setAdapter(groupListViewAdapter);
        //解决listView 与scrollView的滑动冲突
        llGroup.setOnTouchListener(new View.OnTouchListener()
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
        ImageView[] views = new ImageView[]{imgActionModeTouch, imgActionModeLight,
                imgActionModeTogether};
        actionModeCheckBox = new CheckBox(0, views);
        new CheckBoxClickListener(actionModeCheckBox);
        //设定灯光模式checkBox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgLightModeCenter, imgLightModeAll,
                imgLightModeBeside};
        lightModeCheckBox = new CheckBox(0, views1);
        new CheckBoxClickListener(lightModeCheckBox);
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
        groupTrainingTimes = new int[groupNum][2];
        shuttleRunAdapter.setTimeList(timeList);
        shuttleRunAdapter.setGroupTrainingTimes(groupTrainingTimes);
        shuttleRunAdapter.notifyDataSetChanged();

        btnBegin.setText("停止");
        device.turnOnAllLight();
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
        device.turnOffAll();
        timer.stopTimer();
    }

    public void turnLight(final char c)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                device.turnOnLight(c);
                Log.d(Constant.LOG_TAG, "turn on light :" + c);
            }
        }).start();

    }

    //解析返回来的时间数据
    public void analyzeTimeData(List<TimeInfo> infos)
    {
        for (TimeInfo info : infos)
        {
            int groupId = findDeviceGroupId(info.getDeviceNum());
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
                }
                else
                {
                    if (groupTimes.get(0).getDeviceNum() == info.getDeviceNum())
                        groupTrainingTimes[groupId][0] += 1;
                }
            }
            groupTimes.add(info);
            //该组训练结束
            if (groupTrainingTimes[groupId][0] == trainingTimes)
            {
                groupTrainingTimes[groupId][1] = (int) (System.currentTimeMillis() - startTime);
            }
            else
                turnLight(info.getDeviceNum());
        }
        shuttleRunAdapter.notifyDataSetChanged();
        if (isTrainingOver())
            stopTraining();
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
            if (groupTrainingTimes[i][0] < trainingTimes)
                return false;
        }
        return true;
    }


    SlidingMenu menu;

    //初始化侧滑菜单
    private void initSlidingMenu()
    {
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //menu.setShadowWidthRes(R.dimen.shadow_width);
        // menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /** * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not. */
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.slidmenu_left);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.ll_left_menu, new FragmentLeftSlidMenu());
        ft.commit();
    }

    public void test()
    {
        int a = 0xFF;
        int b = 0x08;
        String res = (char) a + "" + (char) b;

        String s = res;
        for (int i = 0; i < s.length(); i++)
            Log.d("AAAAA", ((int) s.charAt(i)) + "");
        //Log.d("AAAAA", res);
    }

}

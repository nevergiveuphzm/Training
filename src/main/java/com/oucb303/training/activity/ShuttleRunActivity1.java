package com.oucb303.training.activity;

import android.content.Intent;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.adpter.ShuttleRunAdapter1;
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
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.DataAnalyzeUtils;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 折返跑 每组一个设备
 */
public class ShuttleRunActivity1 extends AppCompatActivity {


    @Bind(R.id.sp_training_times)
    Spinner spTrainingTimes;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save)
    ImageView imgSave;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
    @Bind(R.id.lv_times)
    ListView lvTimes;
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
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
    @Bind(R.id.cb_end_voice)
    android.widget.CheckBox cbEndVoice;
    @Bind(R.id.img_light_color_blue)
    ImageView imgLightColorBlue;
    @Bind(R.id.img_light_color_red)
    ImageView imgLightColorRed;
    @Bind(R.id.img_light_color_blue_red)
    ImageView imgLightColorBlueRed;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.bar_training_time)
    SeekBar barTrainingTime;
    @Bind(R.id.tv_level)
    TextView tvLevel;
    @Bind(R.id.img_training_time_add)
    ImageView imgTrainingTimeAdd;
    @Bind(R.id.img_training_time_sub)
    ImageView imgTrainingTimeSub;

    private Device device;
    //做多分组数目
    private int maxGroupNum;
    //分组数量
    private int groupNum;
    //总的训练次数
    private int trainingTimes;
    //每组所需设备个数为1
    private final int groupSize = 1;
    private GroupListViewAdapter groupListViewAdapter;
    private ShuttleRunAdapter1 shuttleRunAdapter;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox;
    //训练开始标志
    private boolean trainingBeginFlag = false;

    //每组完成的训练次数
    private int[] completedTimes;
    //每组完成训练所用时间
    private int[] finishTimes;
    //总的训练次数
    private int totalTrainingTimes;
    private Timer timer;
    //训练开始时间
    private long startTime;

    private final int TIME_RECEIVE = 1;
    private final int POWER_RECEIVE = 2;
    private final int UPDATE_TIMES = 3;
    private final int STOP_TRAINING = 4;

    private int level=0;


    private Handler handler = new Handler() {
        //处理接收过来的数据的方法
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Timer.TIMER_FLAG:
                    String time = msg.obj.toString();
                    tvTotalTime.setText(time);
                    break;
                //接收到返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7) {
                        //解析数据
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle_run1);
        ButterKnife.bind(this);
//        level = getIntent().getIntExtra("level", 1);
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
        imgSave.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        device.disconnect();

    }

    public void initView() {
        tvTitle.setText("折返跑训练");
        imgHelp.setVisibility(View.VISIBLE);
        imgSave.setVisibility(View.VISIBLE);

        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //初始化分组下拉框
        maxGroupNum = Device.DEVICE_LIST.size();
        if (maxGroupNum > 8)
            maxGroupNum = 8;
        String[] groupNumChoose = new String[maxGroupNum + 1];
        groupNumChoose[0] = "";
        for (int i = 1; i <= maxGroupNum; i++)
            groupNumChoose[i] = (i + " 组");

        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(ShuttleRunActivity1.this, spGroupNum, groupNumChoose) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                groupNum = i;
                finishTimes = new int[groupNum];
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });

        //初始化训练强度下拉框
        final String[] trainingOptions = new String[10];
        for (int i = 1; i <= 10; i++) {
            trainingOptions[i - 1] = "50米 * " + i * 2;
        }

        spTrainingTimes.setOnItemSelectedListener(new SpinnerItemSelectedListener(ShuttleRunActivity1.this, spTrainingTimes, trainingOptions) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //总的训练次数
                totalTrainingTimes = (i + 1) * 2;
            }
        });
        //训练时间拖动条初始化
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(spTrainingTimes,tvLevel, 1));
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));
//        switch (level) {
//            case 1:
//                level = 0;
//                break;
//            case 2:
//                level = 3;
//                break;
//            case 3:
//                level = 7;
//                break;
//        }
        spTrainingTimes.setSelection(level);

        //初始化分组listview
        groupListViewAdapter = new GroupListViewAdapter(ShuttleRunActivity1.this, groupSize);
        lvGroup.setAdapter(groupListViewAdapter);

        //解决listView 与scrollView的滑动冲突
        lvGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                //从listView 抬起时将控制权还给scrollview
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    svContainer.requestDisallowInterceptTouchEvent(false);
                else
                    //requestDisallowInterceptTouchEvent（true）方法是用来子View告诉父容器不要拦截我们的事件的
                    svContainer.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //初始化时间listview
        shuttleRunAdapter = new ShuttleRunAdapter1(this);
        lvTimes.setAdapter(shuttleRunAdapter);


        //设定checkbox组合的点击事件
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


    @OnClick({R.id.layout_cancel, R.id.img_help, R.id.btn_begin, R.id.img_save,R.id.btn_off,R.id.btn_on})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_cancel:
                this.finish();
                device.turnOffAllTheLight();
                break;
            case R.id.img_help:
                Intent intent = new Intent(this, HelpActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);
                break;
            case R.id.img_save:

                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 ...
                bundle.putString("trainingCategory", "1");
                //每组所用时间
                bundle.putIntArray("finishTimes", finishTimes);
                //总次数（强度）
                bundle.putInt("totalTrainingTimes", totalTrainingTimes);
                it.putExtras(bundle);
                startActivity(it);
//                }

                break;
            case R.id.btn_begin:
                if (!device.checkDevice(ShuttleRunActivity1.this))
                    return;
                if (groupNum == 0) {
                    Toast.makeText(this, "请选择训练分组!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (trainingBeginFlag)
                    stopTraining();
                else
                    startTraining();
                break;
            case R.id.btn_on:
                //goupNum组数，1：每组设备个数，0：类型
                device.turnOnButton(groupNum,1,0);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
        }
    }

    //开始训练
    public void startTraining() {
        trainingBeginFlag = true;

        completedTimes = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            completedTimes[i] = -1;
        }

        finishTimes = new int[groupNum];
        shuttleRunAdapter.setCompletedTimes(completedTimes);
        shuttleRunAdapter.setFinishTime(finishTimes);
        shuttleRunAdapter.notifyDataSetChanged();

        btnBegin.setText("停止");
        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();

        //开启接收设备返回时间的监听线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        //开全灯
        for (int i = 0; i < groupNum; i++) {
            device.sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                    Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                    Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                    Order.BlinkModel.NONE,
                    Order.LightModel.values()[lightModeCheckBox.getCheckId()],
                    Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                    Order.EndVoice.NONE);
        }

        //获得当前的系统时间
        startTime = System.currentTimeMillis();
        timer = new Timer(handler);
        timer.setBeginTime(startTime);
        timer.start();
    }

    //结束训 练
    public void stopTraining() {
        trainingBeginFlag = false;
        btnBegin.setText("开始");
        imgSave.setEnabled(true);
        //停止接收线程
        ReceiveThread.stopThread();
        device.turnOffAllTheLight();
        timer.stopTimer();
    }

    public void turnOnLight(final char deviceNum) {
        //实现Runnable接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer.sleep(5000);
                device.sendOrder(deviceNum,
                        Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                        Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                        Order.BlinkModel.NONE,
                        Order.LightModel.values()[lightModeCheckBox.getCheckId()],
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
            }
        }).start();
    }

    //解析返回来的数据
    public void analyzeTimeData(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
                for (TimeInfo info : infos) {
                    int groupId = findDeviceGroupId(info.getDeviceNum());
                    //如果设备组号大于分组数肯定是错误的
                    if (groupId > groupNum)
                        continue;
                    Log.d(Constant.LOG_TAG, info.getDeviceNum() + " groupId " + groupId);
                    completedTimes[groupId] += 1;
                    //该组训练结束
                    if (completedTimes[groupId] == totalTrainingTimes)
                        finishTimes[groupId] = (int) (System.currentTimeMillis() - startTime);
                    else
                        turnOnLight(info.getDeviceNum());
                }

                Message msg = Message.obtain();
                msg.what = UPDATE_TIMES;
                msg.obj = "";
                handler.sendMessage(msg);
                if (isTrainingOver()) {
                    Message msg1 = Message.obtain();
                    msg1.what = STOP_TRAINING;
                    msg1.obj = "";
                    handler.sendMessage(msg1);
                }
            }
        }).start();
    }

    //查找设备属于第几组
    public int findDeviceGroupId(char deviceNum) {
        int position = 0;
        for (int i = 0; i < Device.DEVICE_LIST.size(); i++) {
            if (Device.DEVICE_LIST.get(i).getDeviceNum() == deviceNum) {
                position = i;
                break;
            }
        }
        return position;
    }

    //判断训练是否结束
    public boolean isTrainingOver() {
        for (int i = 0; i < groupNum; i++)
            //只要有一组训练没完成，就没有结束
            if (completedTimes[i] < totalTrainingTimes)
                return false;
        return true;
    }
}

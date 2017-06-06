package com.oucb303.training.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.BoldCautiousAdapter;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.listener.CheckBoxClickListener;
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
 * Created by HP on 2017/3/13.
 * 胆大心细
 */
public class BoldCautiousActivity extends AppCompatActivity {
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
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.img_action_mode_light)
    ImageView imgActionModeLight;
    @Bind(R.id.img_action_mode_touch)
    ImageView imgActionModeTouch;
    @Bind(R.id.img_action_mode_together)
    ImageView imgActionModeTogether;
    @Bind(R.id.img_light_mode_beside)
    ImageView imgLightModeBeside;
    @Bind(R.id.img_light_mode_center)
    ImageView imgLightModeCenter;
    @Bind(R.id.img_light_mode_all)
    ImageView imgLightModeAll;
    @Bind(R.id.img_light_color_blue)
    ImageView imgLightColorBlue;
    @Bind(R.id.img_light_color_red)
    ImageView imgLightColorRed;
    @Bind(R.id.img_light_color_blue_red)
    ImageView imgLightColorBlueRed;
    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
    @Bind(R.id.ll_params)
    LinearLayout llParams;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
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

    private int level;
    private Device device;
    //最大组数，每组数量为2,所选的当前组数
    private int maxGroupNum, groupSize = 2, groupNum;
    private GroupListViewAdapter groupListViewAdapter;
    private BoldCautiousAdapter boldCautiousAdapter;
    private CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox,blinkModeCheckBox;
    //时间列表,设备编号和时间
    private List<TimeInfo> timeList = new ArrayList<>();
    //训练开始标志
    private boolean trainingFlag = false;
    //训练成绩
    private int[] scores;
    //灭灯的设备编号
    private String[] deviceNum;
    private final int TIME_RECEIVE = 1, POWER_RECEIVER = 2, UPDATE_DATA = 3, STOP_TRAINING = 4;
    //计时器
    private Timer timer;
    //每组完成训练所用时间
    private int[] finishTime;
    //训练开始时间
    private long startTime;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Timer.TIMER_FLAG:
                    tvTotalTime.setText(msg.obj.toString());
                    break;
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7)
                        analyzeTimeData(data);
                    break;
                case UPDATE_DATA:
                    boldCautiousAdapter.notifyDataSetChanged();
                    break;
                case STOP_TRAINING:
                    stopTraining();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bold_cautious);
        ButterKnife.bind(this);

        level = getIntent().getIntExtra("level", 1);
        initView();
        device = new Device(this);
        //更新连接设备列表
        device.createDeviceList(this);
        //判断是否插入协调器
        if (device.devCount > 0) {
            device.connect(this);
            device.initConfig();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        device.disconnect();
    }

    public void initView() {
        tvTitle.setText("胆大心细");
        imgHelp.setVisibility(View.VISIBLE);
        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());
        //初始化训练分组下拉框
//        if (maxGroupNum > Device.DEVICE_LIST.size()/2)
        maxGroupNum = Device.DEVICE_LIST.size() / 2;
        String[] groupNumChoose = new String[maxGroupNum + 1];
        groupNumChoose[0] = "";
        for (int i = 1; i <= maxGroupNum; i++)
            groupNumChoose[i] = i + "组";
        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(BoldCautiousActivity.this, spGroupNum, groupNumChoose) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                groupNum = i;
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });
        //初始化左侧listview
        groupListViewAdapter = new GroupListViewAdapter(this, groupSize);
        lvGroup.setAdapter(groupListViewAdapter);
        //解决listView 与scrollView的滑动冲突
        lvGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //从listView 抬起时将控制权还给scrollview
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    svContainer.requestDisallowInterceptTouchEvent(false);
                else
                    svContainer.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
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
        //设定闪烁模式checkbox组合的点击事件
        ImageView[] views3 = new ImageView[]{imgBlinkModeNone, imgBlinkModeSlow, imgBlinkModeFast,};
        blinkModeCheckBox = new CheckBox(1, views3);
        new CheckBoxClickListener(blinkModeCheckBox);

        //初始化右侧listview
        boldCautiousAdapter = new BoldCautiousAdapter(this);
        lvTimes.setAdapter(boldCautiousAdapter);
    }

    @OnClick({R.id.layout_cancel, R.id.btn_begin, R.id.img_help, R.id.btn_on, R.id.btn_off, R.id.img_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_cancel:
                this.finish();
                device.turnOffAllTheLight();
                break;
            case R.id.btn_begin:
                if (!device.checkDevice(this))
                    return;
                if (groupNum == 0) {
                    Toast.makeText(this, "请选择训练分组!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (trainingFlag)
                    stopTraining();
                else
                    startTraining();
                break;
            case R.id.img_help:
                Intent intent = new Intent(this, HelpActivity.class);
                intent.putExtra("flag", 11);
                startActivity(intent);
                break;
            case R.id.img_save:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 6:大课间跑圈，八秒钟跑，胆大心细 ...
                bundle.putString("trainingCategory", "6");
                bundle.putString("trainingName", "胆大心细");//项目名称
                bundle.putInt("totalTimes", 2);//总次数
                bundle.putInt("deviceNum", 2);//设备个数
                bundle.putIntArray("scores", scores);//得分
                bundle.putInt("totalTime", 0);//训练总时间
                bundle.putInt("groupNum", groupNum);//分组数
                it.putExtras(bundle);
                startActivity(it);
                break;
            case R.id.btn_on:
                //groupNum组数，groupSize：每组设备个数，1：类型
                device.turnOnButton(groupNum, groupSize, 1);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                break;
        }
    }

    public void startTraining() {
        btnBegin.setText("停止");
        trainingFlag = true;
        scores = new int[groupNum];
        deviceNum = new String[groupNum];
        finishTime = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            deviceNum[i] = "";
        }
        boldCautiousAdapter.setScore(scores);
        boldCautiousAdapter.setDeviceNum(deviceNum);
        boldCautiousAdapter.setFinishTime(finishTime);

        boldCautiousAdapter.notifyDataSetChanged();
        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();
        //开启接受时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();
        //开灯 感应
        for (int i = 0; i < groupNum * groupSize; i++) {
            device.sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                    Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                    Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                    Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                    Order.LightModel.OUTER,
                    Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                    Order.EndVoice.values()[cbVoice.isChecked() ? 1 : 0]);
        }
        startTime = System.currentTimeMillis();
        timer = new Timer(handler);
        timer.setBeginTime(startTime);
        timer.start();
    }

    //解析时间
    private void analyzeTimeData(final String data) {
        //训练已结束
        if (!trainingFlag)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TimeInfo存的是时间，编号
                List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
                for (TimeInfo info : infos) {
                    //组号
                    int groupId = findDeviceGroupId(info.getDeviceNum()) / groupSize;
                    int num = findDeviceGroupId(info.getDeviceNum());
                    Log.d("设备编号？？？------", "" + groupId);
                    Log.d("设备是几号------", "" + num);
                    if (num % 2 == 0)
                        scores[groupId] += 2;
                    else
                        scores[groupId] += 1;

                    deviceNum[groupId] += info.getDeviceNum() + " ";

                    finishTime[groupId] = (int) (System.currentTimeMillis() - startTime);
                }
                Message msg = Message.obtain();
                msg.obj = "";
                msg.what = UPDATE_DATA;
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

    private void stopTraining() {
        trainingFlag = false;
        btnBegin.setText("开始");
        //结束时间线程
        timer.stopTimer();
        device.turnOffAllTheLight();
        timer.sleep(200);
        //结束接收时间线程
        ReceiveThread.stopThread();
    }

    public boolean isTrainingOver() {
        return false;
    }
}

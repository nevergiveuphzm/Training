package com.oucb303.training.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.adpter.LargeDetailsAdapter;
import com.oucb303.training.adpter.TimeKeeperAdapter;
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
import com.oucb303.training.utils.DataUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2017/3/30.
 * 计时活动基本模块
 */
public class TimeKeeperActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

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
    @Bind(R.id.sp_training_times)
    Spinner spTrainingTimes;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
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
    @Bind(R.id.sp_light_num)
    Spinner spLightNum;
    @Bind(R.id.img_blink_mode_none)
    ImageView imgBlinkModeNone;
    @Bind(R.id.img_blink_mode_slow)
    ImageView imgBlinkModeSlow;
    @Bind(R.id.img_blink_mode_fast)
    ImageView imgBlinkModeFast;


    private int level;
    private Device device;
    //最大分组数目
    private int maxGroupNum;
    //当前所选择的组数是
    private int groupNum;
    //    //当前所选择的灯的个数
//    private int lightNum;
    //每组最终完成所有次数所用时间
    private int[] finishTime;
    //当前所选择的总的训练次数
    private int totalTrainingTimes;
    //每组所需设备个数
    private int groupSize = 1;
    //训练开始标志
    private boolean trainingFlag = false;
    //每组最终完成训练次数
    private int[] completeTimes;
    //上一次完成训练的时间点
    private int[] currentTime;
    //map里存的是：每组完成每次训练所用时间，组号和相应组号每次的时间
    //list 里存的是：所有解析过来的map集合
    private List<Map<Integer, Integer>> list_finishTime = new ArrayList<>();
    //训练开始时间
    private int startTime;
    //用于训练次数为1的情况下，记录组号
    private Integer[] ids;
    //记录ids里的编号
    private int positionIds;
    //训练总时间
    private int totalTime = 0;
    //i记录行号，j记录列号  例如:
    //0  A  B
    //1  C  D
    private char[][] deviceNum;

    private GroupListViewAdapter groupListViewAdapter;
    private TimeKeeperAdapter timeKeeperAdapter;
    private LargeDetailsAdapter largeDetailsAdapter;
    private Timer timer;

    private CheckBox actionModeCheckBox, lightColorCheckBox,blinkModeCheckBox;
    private final int TIME_RECEIVE = 1;
    private final int UPDATE_TIMES = 2;
    private final int STOP_TRAINING = 3;

    Handler handler = new Handler() {
        //处理接收过来的数据的方法

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Timer.TIMER_FLAG:
                    //更新计时
                    tvTotalTime.setText(msg.obj.toString());
                    break;
                //接收到返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7)
                        analyzeTimeData(data);
                    break;
                //更新完成次数
                case UPDATE_TIMES:
                    timeKeeperAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_time_keeper);
        ButterKnife.bind(this);

        level = getIntent().getIntExtra("level", 0);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        device.turnOffAllTheLight();
        ReceiveThread.stopThread();
        if (device.devCount > 0)
            device.disconnect();
    }

    public void initView() {
        tvTitle.setText("计时活动");
        imgSave.setVisibility(View.VISIBLE);
        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //初始化训练次数下拉框
        final String[] trainingOptions = new String[10];
        for (int i = 1; i <= 10; i++) {
            trainingOptions[i - 1] = i + " 次";
        }
        spTrainingTimes.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spTrainingTimes, trainingOptions) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                totalTrainingTimes = i + 1;
//                if (totalTrainingTimes != 1) {
//                    spLightNum.setSelection(0);
//                    spLightNum.setEnabled(false);
//                    groupSize = 1;
//                    groupListViewAdapter.setGroupSize(groupSize);
//                    groupListViewAdapter.notifyDataSetChanged();
//                } else {
//                    spLightNum.setEnabled(true);
//                }
            }
        });

        //初始化每组设备个数下拉框
        final String[] lightNumChoose = new String[2];
        for (int i = 0; i < 2; i++) {
            lightNumChoose[i] = (i + 1) + "个";
        }
        spLightNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spLightNum, lightNumChoose) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                groupSize = i + 1;
                if (Device.DEVICE_LIST.size() / groupSize < groupNum) {
                    Toast.makeText(TimeKeeperActivity.this, "当前设备数量为" + Device.DEVICE_LIST.size() + ",不能分成" + groupNum + "组!",
                            Toast.LENGTH_LONG).show();
                    spGroupNum.setSelection(0);
                    groupNum = 0;
                }

                groupListViewAdapter.setGroupNum(groupNum);
                groupListViewAdapter.setGroupSize(groupSize);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });

        //初始化分组下拉框
        maxGroupNum = Device.DEVICE_LIST.size();
        String[] groupNumChoose = new String[maxGroupNum + 1];
        groupNumChoose[0] = " ";
        for (int i = 1; i <= maxGroupNum; i++) {
            groupNumChoose[i] = i + "组";
        }
        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spGroupNum, groupNumChoose) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                groupNum = i;
                if (Device.DEVICE_LIST.size() / groupSize < groupNum) {
                    Toast.makeText(TimeKeeperActivity.this, "当前设备数量为" + Device.DEVICE_LIST.size() + ",不能分成" + groupNum + "组!",
                            Toast.LENGTH_LONG).show();
                    spGroupNum.setSelection(0);
                    groupNum = 0;
                }
                finishTime = new int[groupNum];
                groupListViewAdapter.setGroupNum(groupNum);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });


        //初始化左侧listview
        groupListViewAdapter = new GroupListViewAdapter(this);
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

        //初始化右侧listview
        timeKeeperAdapter = new TimeKeeperAdapter(this);
        lvTimes.setAdapter(timeKeeperAdapter);
        lvTimes.setOnItemClickListener(TimeKeeperActivity.this);

        //设定checkbox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);

        //设定灯光颜色checkBox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorBlueRed};
        lightColorCheckBox = new CheckBox(1, views1);
        new CheckBoxClickListener(lightColorCheckBox);

        //设定闪烁模式checkbox组合的点击事件
        ImageView[] views2 = new ImageView[]{imgBlinkModeNone, imgBlinkModeSlow, imgBlinkModeFast,};
        blinkModeCheckBox = new CheckBox(1, views2);
        new CheckBoxClickListener(blinkModeCheckBox);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
        listDialog.setTitle("\t\t\t\t\t\t\t\t\t\t\t\t\t第" + (position + 1) + "组训练详情");

        List<Integer> time = new ArrayList<>();

        Log.i("list_finishTime", "----------" + list_finishTime);

        for (int i = 0; i < list_finishTime.size(); i++) {
            if (list_finishTime.get(i).get(position) != null) {
                time.add(list_finishTime.get(i).get(position));
                Log.i("time", "----------" + time);
            }
//            Log.i("aaaa","aaa---"+list_finishTime.get(i).toString());
        }

        largeDetailsAdapter = new LargeDetailsAdapter(this, time, position);

        listDialog.setAdapter(largeDetailsAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        listDialog.show();
    }

    @OnClick({R.id.layout_cancel, R.id.img_help, R.id.btn_begin, R.id.img_save, R.id.btn_off, R.id.btn_on})
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
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 6:大课间跑圈，八秒钟跑,羽毛球训练，限时活动，计时活动 ...
                bundle.putString("trainingCategory", "6");
                bundle.putString("trainingName", "计时活动");//项目名称
                bundle.putInt("totalTimes", totalTrainingTimes);//总次数
                bundle.putInt("deviceNum", 1);//设备个数
                int[] scores = new int[groupNum];
                int[] avgScores = new int[groupNum];
                for (int j = 0; j < groupNum; j++) {
                    for (int i = 0; i < list_finishTime.size(); i++) {
                        if (list_finishTime.get(i).get(j + 1) != null)
                            scores[i] += list_finishTime.get(i).get(j + 1);
                    }
                    avgScores[j] = DataUtils.getAvg(scores);
                }
                bundle.putIntArray("scores", avgScores);//得分
                bundle.putInt("totalTime", totalTime);//训练总时间
                bundle.putInt("groupNum", groupNum);//分组数
                it.putExtras(bundle);
                startActivity(it);
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
            case R.id.btn_on:
                //goupNum组数，1：每组设备个数，0：类型
                device.turnOnButton(groupNum, groupSize, 0);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
        }
    }

    public void startTraining() {

        trainingFlag = true;
        btnBegin.setText("停止");

        completeTimes = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            completeTimes[i] = 0;
        }

        ids = new Integer[groupSize * groupNum];
        for (int j = 0; j < ids.length; j++)
            ids[j] = -1;
        positionIds = 0;

        //i记录行号，j记录列号  例如:
        //0  A  B
        //1  C  D
        deviceNum = new char[groupNum][groupSize];

        list_finishTime.clear();

        timeKeeperAdapter.setCompletedTimes(completeTimes);
        timeKeeperAdapter.setFinishTime(finishTime);
        timeKeeperAdapter.notifyDataSetChanged();

        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();
        //开启接收设备返回时间的监听线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        //开全灯
        int count = 0;
        for (int i = 0; i < groupNum; i++) {
            for (int j = 0; j < groupSize; j++) {
                device.sendOrder(Device.DEVICE_LIST.get(count).getDeviceNum(),
                        Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                        Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                        Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                        Order.LightModel.OUTER,
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.NONE);
                //i记录行号，j记录列号  例如:
                //0  A  B
                //1  C  D
                deviceNum[i][j] = Device.DEVICE_LIST.get(count).getDeviceNum();
                count++;
            }


        }

        currentTime = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            currentTime[i] = (int) (System.currentTimeMillis());
        }

        //获得当前的系统时间
        startTime = (int) (System.currentTimeMillis());
        timer = new Timer(handler);
        timer.setBeginTime(startTime);
        timer.start();
    }

    //结束训练
    public void stopTraining() {
        trainingFlag = false;
        btnBegin.setText("开始");
        imgSave.setEnabled(true);
        //结束时间线程
        timer.stopTimer();
        //结束接收返回时间线程
        ReceiveThread.stopThread();
        device.turnOffAllTheLight();
    }
    //开灯
    public void turnOnLight(final char deviceNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer.sleep(5000);

                if (!trainingFlag)
                    return;
                device.sendOrder(deviceNum,
                        Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                        Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                        Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                        Order.LightModel.OUTER,
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
            }
        }).start();
    }

    public void analyzeTimeData(final String data) {
        Log.i("解析数据执行几次", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //设备编号和时间
                List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
                Log.i("TimeInfo里都有什么", "" + infos);

                for (TimeInfo info : infos) {
                    Log.i("Info是什么", "" + info);
                    //得到组号
                    int groupId = findDeviceGroupId(info.getDeviceNum());

                    if (totalTrainingTimes == 1 && groupSize == 2) {
                        for (int j = 0; j < groupSize; j++) {
                            if (info.getDeviceNum() == deviceNum[groupId][j]) {
                                if (j == 1)
                                    turnOffLight(deviceNum[groupId][j - 1]);
                                else
                                    turnOffLight(deviceNum[groupId][j + 1]);
                            }
                        }
                        int flag = 0;
                        //寻找组号是否有相同的，如果有，跳出循环
                        for (int j = 0; j < ids.length; j++) {
                            if (ids[j] == groupId) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 1)
                            continue;
                        else {
                            ids[positionIds] = groupId;
                            positionIds++;
                        }
                    }
                    //如果设备组号大于分组数肯定是错误的
                    if (groupId > groupNum)
                        continue;
                    completeTimes[groupId] += 1;
                    Log.d("completeTimes[groupId]：", "" + groupId + "---" + completeTimes[groupId]);
                    //map_finishTime存的是组号，和这个组这一次所用的时间
                    Map<Integer, Integer> map_finishTime = new HashMap<Integer, Integer>();
                    map_finishTime.put(groupId, (int) (System.currentTimeMillis()) - currentTime[groupId]);

                    list_finishTime.add(map_finishTime);
                    //此次完成训练的时间点
                    currentTime[groupId] = (int) (System.currentTimeMillis());

                    if (completeTimes[groupId] == totalTrainingTimes) {
                        finishTime[groupId] = (int) System.currentTimeMillis() - startTime;
                    } else {
                        turnOnLight(info.getDeviceNum());
                    }

                }
                //解析完数据就要更新完成次数
                Message msg = Message.obtain();
                msg.what = UPDATE_TIMES;
                msg.obj = "";
                handler.sendMessage(msg);
                //如果结束
                if (isTrainingOver()) {
                    Message msg1 = Message.obtain();
                    msg1.what = STOP_TRAINING;
                    msg1.obj = "";
                    handler.sendMessage(msg1);
                }
            }
        }).start();
    }

    //关灯
    public void turnOffLight(char deviceNum) {
        device.sendOrder(deviceNum,
                Order.LightColor.NONE,
                Order.VoiceMode.NONE,
                Order.BlinkModel.NONE,
                Order.LightModel.TURN_OFF,
                Order.ActionModel.TURN_OFF,
                Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
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
        return position / groupSize;
    }

    //判断训练是否结束
    public boolean isTrainingOver() {
        for (int i = 0; i < groupNum; i++) {
            //只要有一组训练没完成，就没有结束,返回假，如果结束返回真
            if (completeTimes[i] < totalTrainingTimes)
                return false;
        }
        return true;
    }
}

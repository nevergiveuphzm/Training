package com.oucb303.training.activity;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.DGroupListViewAdapter;
import com.oucb303.training.adpter.GroupResistAdapter;
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
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2017/4/11.
 * 分组对抗基本模块
 */
public class GroupResistActivity extends AppCompatActivity {
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
    @Bind(R.id.sp_dev_num)
    Spinner spDevNum;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.tv_device_list)
    TextView tvDeviceList;
    @Bind(R.id.sp_light_num)
    Spinner spLightNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.img_action_mode_light)
    ImageView imgActionModeLight;
    @Bind(R.id.img_action_mode_touch)
    ImageView imgActionModeTouch;
    @Bind(R.id.img_action_mode_together)
    ImageView imgActionModeTogether;
    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
    @Bind(R.id.cb_end_voice)
    android.widget.CheckBox cbEndVoice;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.ll_params)
    LinearLayout llParams;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.lv_scores)
    ListView lvScores;
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
    private Context context;

    private GroupResistAdapter groupResistAdapter;
    private DGroupListViewAdapter dGroupListViewAdapter;
    //当前所选设备总个数
    private int totalNum;
    //当前所选分组数
    private int groupNum;
    //当前所选每组每次亮灯个数
    private int lightEveryNum;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox,blinkModeCheckBox;
    //训练开始标志
    private boolean trainningFlag = false;
    //训练总时间,延迟时间,超时时间  单位是毫秒
    private int trainingTime, delayTime, overTime;
    //成绩统计
    private int[] scores;
    //随机数存放序列
    private ArrayList<Integer> listRand = new ArrayList<>();
    private final int TIME_RECEIVE = 1;
    private final int UPDATE_SCORES = 2;
    private final int STOP_TRAINING = 4;
    //训练开始时间
    private long startTime;
    //计时器
    private Timer timer;
    //超时线程
    private OverTimeThread overTimeThread;
    //行表示组号，列数表示每组每次开几个灯,存放的是设备编号
    private char[][] deviceNums;
    //每组设备灯亮起的时间
//    private Map<Character,Integer> overTimeMap;
    //每组设备灯亮起的时间
    private long[] duration;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String data = msg.obj.toString();
            //比赛结束则不接收任何数据
            if (!trainningFlag)
                return;
            switch (msg.what) {
                //更新成绩
                case UPDATE_SCORES:
                    groupResistAdapter.notifyDataSetChanged();
                    break;
                //更新计时
                case Timer.TIMER_FLAG:
                    tvTotalTime.setText(data);
                    //判断是否结束
                    if (timer.time >= trainingTime) {
                        Message message = Message.obtain();
                        message.what = STOP_TRAINING;
                        message.obj = "";
                        handler.sendMessage(message);
                    }
                    break;
                case STOP_TRAINING:
                    stopTraining();
                    break;
                //接收到返回的时间线程
                case TIME_RECEIVE:
                    if (data != null && data.length() > 7)
                        analyseData(data);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_resist);
        ButterKnife.bind(this);
        level = getIntent().getIntExtra("level", 0);
        context = this;

        device = new Device(this);
        //更新设备连接列表，当重新打开程序或是熄灭屏幕之后重新打开都会执行此方法，应该次列表的设备数量一般情况下为1
        device.createDeviceList(this);
        //判断协调器是否插入
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
        imgSave.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (device.devCount > 0)
            device.disconnect();
    }

    private void initView() {
        tvTitle.setText("分组对抗");

        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //训练时间
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 10));
        //0为减，1为加
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));
        //设置延时和超时的 seekbar 拖动事件的监听器
        barDelayTime.setOnSeekBarChangeListener(new MySeekBarListener(tvDelayTime, 10));
        barOverTime.setOnSeekBarChangeListener(new MySeekBarListener(tvOverTime, 28, 2));
        //设置加减按钮的监听事件
        imgDelayTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barDelayTime, 1));
        imgDelayTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barDelayTime, 0));

        imgOverTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barOverTime, 1));
        imgOverTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barOverTime, 0));
        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);

        //设定闪烁模式checkbox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgBlinkModeNone, imgBlinkModeSlow, imgBlinkModeFast,};
        blinkModeCheckBox = new CheckBox(1, views1);
        new CheckBoxClickListener(blinkModeCheckBox);

        //选择设备个数spinner
        String[] num = new String[Device.DEVICE_LIST.size()];
        for (int i = 0; i < num.length; i++)
            num[i] = (i + 1) + "个";

        spDevNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spDevNum, num) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                totalNum = i + 1;
                String str = "";
                for (int j = 0; j < totalNum; j++)
                    str += Device.DEVICE_LIST.get(j).getDeviceNum() + "  ";
                tvDeviceList.setText(str);

                //每次亮灯个数spinner
                String[] everyTimeNum = new String[totalNum];
                for (int j = 0; j < everyTimeNum.length; j++)
                    everyTimeNum[j] = (j + 1) + "个";

                ArrayAdapter<String> adapterEveryNum = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, everyTimeNum);
                adapterEveryNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLightNum.setAdapter(adapterEveryNum);

                //初始化每次亮灯个数spinner,设置动作
                spLightNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        lightEveryNum = i + 1;

                        //训练分组
                        int maxGoupNum = totalNum / lightEveryNum;
                        if (maxGoupNum > 7)
                            maxGoupNum = 7;
                        String[] groupCount = new String[maxGoupNum];
                        for (int j = 0; j < groupCount.length; j++)
                            groupCount[j] = (j + 1) + "组";
                        ArrayAdapter<String> adapterGroupCount = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, groupCount);
                        adapterGroupCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spGroupNum.setAdapter(adapterGroupCount);

                        spGroupNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //当前选择的分组数
                                groupNum = position + 1;

                                dGroupListViewAdapter.setGroupNum(groupNum);
                                dGroupListViewAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        //初始化左侧listView
        dGroupListViewAdapter = new DGroupListViewAdapter(this);
        lvGroup.setAdapter(dGroupListViewAdapter);

        //解决listview与scrollview的滑动冲突
        lvGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                //从listView 抬起时将控制权还给scrollview
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    svContainer.requestDisallowInterceptTouchEvent(false);
                    //requestDisallowInterceptTouchEvent（true）方法是用来子View告诉父容器不要拦截我们的事件的
                else
                    svContainer.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        //初始化右侧listView
        groupResistAdapter = new GroupResistAdapter(this);
        lvScores.setAdapter(groupResistAdapter);
    }

    @OnClick({R.id.layout_cancel, R.id.btn_begin, R.id.img_help, R.id.btn_on, R.id.btn_off, R.id.img_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_cancel:
                this.finish();
                device.turnOffAllTheLight();
                break;
            case R.id.btn_begin:
                //检测设备
                if (!device.checkDevice(this))
                    return;
                if (groupNum <= 0) {
                    Toast.makeText(this, "未选择分组,不能开始!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (trainningFlag)
                    stopTraining();
                else
                    startTraining();
                break;
            case R.id.img_help:
                Intent intent = new Intent(this, HelpActivity.class);
                if (level == 4)
                    intent.putExtra("flag", 6);
                else
                    intent.putExtra("flag", 5);
                startActivity(intent);
                break;
            case R.id.btn_on:
                //totalNum组数，lightEveryNum：每组设备个数，1：类型
                device.turnOnButton(totalNum, 1, 0);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                break;
            case R.id.img_save:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 5:运球比赛、多人混战、分组对抗 ...
                bundle.putString("trainingCategory", "5");
                bundle.putString("trainingName", "分组对抗");
                //训练总时间
                bundle.putInt("trainingTime", trainingTime);
                //设备个数
                bundle.putInt("DeviceNum", totalNum);
                //每组得分
                bundle.putIntArray("scores", scores);
                it.putExtras(bundle);
                startActivity(it);
                break;
        }
    }

    //开始训练
    public void startTraining() {
        trainningFlag = true;
        btnBegin.setText("停止");

        trainingTime = (int) (new Double(tvTrainingTime.getText().toString()) * 60 * 1000);
        delayTime = new Integer(tvDelayTime.getText().toString()) * 1000;
        overTime = new Integer(tvOverTime.getText().toString()) * 1000;

        scores = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            scores[i] = 0;
        }
        listRand.clear();

        deviceNums = new char[groupNum][lightEveryNum];
        for (int i = 0; i < groupNum; i++) {
            for (int j = 0; j < lightEveryNum; j++) {
                deviceNums[i][j] = '\0';
            }
        }

        //每组设备灯亮起的时间
        duration = new long[groupNum * lightEveryNum];

        groupResistAdapter.setScores(scores);
        groupResistAdapter.notifyDataSetChanged();
        //创建随机队列
        createRandomNumber();
        Log.i("随机序列", "" + listRand);
        //开启全部灯
        int position = 0;
        int t = 1;
        for (int i = 0; i < groupNum; i++) {
            for (int j = 0; j < lightEveryNum; j++) {
                device.sendOrder(Device.DEVICE_LIST.get(listRand.get(position)).getDeviceNum(),
                        Order.LightColor.values()[t],
                        Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                        Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                        Order.LightModel.OUTER,
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);

                deviceNums[i][j] = Device.DEVICE_LIST.get(listRand.get(position)).getDeviceNum();

                //每组设备灯亮起的当前时间
                duration[position] = System.currentTimeMillis();

                position++;
            }
            t++;
        }
        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();

        //开启接收设备返回时间的监听线程,返回时间和编号
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        //开启超时线程
        overTimeThread = new OverTimeThread();
        overTimeThread.start();

        startTime = System.currentTimeMillis();
        //开启计时器
        timer = new Timer(handler, trainingTime);
        timer.setBeginTime(startTime);
        timer.start();
    }

    //生成随机数
    private void createRandomNumber() {
        Random random = new Random();
        for (int i = 0; i < groupNum; i++) {
            for (int j = 0; j < lightEveryNum; j++) {
                int randomInt = random.nextInt(totalNum);
                //如果没有重复值，则加入
                if (!listRand.contains(randomInt))
                    listRand.add(randomInt);
                else
                    j--;
            }
        }
    }

    //解析数据
    public void analyseData(final String data) {
        //存放编号和时间
        List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);

        if (infos.size() == totalNum) {
            listRand.clear();
            //重新创建随机队列
            createRandomNumber();
            Log.i("随机序列", "" + listRand);
            //开启全部灯
            int position = 0;
            int t = 1;
            for (int i = 0; i < groupNum; i++) {
                for (int j = 0; j < lightEveryNum; j++) {
                    device.sendOrder(Device.DEVICE_LIST.get(listRand.get(position)).getDeviceNum(),
                            Order.LightColor.values()[t],
                            Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                            Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                            Order.LightModel.OUTER,
                            Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                            Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);

                    deviceNums[i][j] = Device.DEVICE_LIST.get(listRand.get(position)).getDeviceNum();
                    position++;
                }
                scores[i]++;
                t++;
            }
        } else {
            for (TimeInfo info : infos) {
                int[] Id = findDeviceGroupId(info.getDeviceNum());
                //组号
                int groupId = Id[0];
                //该设备所在的列号
                int everyId = Id[1];
                Log.i("进来的是什么灯", "" + info + "--" + groupId + "---" + everyId);
                scores[groupId]++;
                turnOnLight(groupId, everyId, info.getDeviceNum());
            }
        }

        //更新成绩
        Message msg = Message.obtain();
        msg.what = UPDATE_SCORES;
        msg.obj = "";
        handler.sendMessage(msg);
    }

    //开某一组的任意灯
    private void turnOnLight(final int groupId, final int everyId, char deviceNum) {
        int lightNum = 0;
        for (int i = 0; i < Device.DEVICE_LIST.size(); i++) {
            if (deviceNum == Device.DEVICE_LIST.get(i).getDeviceNum()) {
                //找到了这个设备对应的编号
                lightNum = i;
                break;
            }
        }
        Log.i("这个设备对应的编号", "" + lightNum);
        int listNum = 0;//这个设备在随机队列里的序号
        for (int j = 0; j < listRand.size(); j++) {
            //如果随机队列里包含这个编号，就找到了这个设备在随机队列里的序号,并且移除
            if (listRand.get(j) == lightNum) {
                listNum = j;
                listRand.remove(j);
                break;
            }
        }

        Random random = new Random();
        while (listRand.size() < groupNum * lightEveryNum) {
            //rand对应的是在设备列表里的设备序号
            int rand = random.nextInt(totalNum);
            if (!listRand.contains(rand)) {
                //将指定的元素插入此列表中的指定位置。
                listRand.add(listNum, rand);
                deviceNums[groupId][everyId] = Device.DEVICE_LIST.get(rand).getDeviceNum();
            }
        }
        Log.i("现在的随机序列是什么", "" + listRand);
        //亮起这一组的新的一盏灯
        final int finalListNum = listNum;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer.sleep(delayTime);
                //若训练结束则返回
                if (!trainningFlag)
                    return;
                device.sendOrder(Device.DEVICE_LIST.get(listRand.get(finalListNum)).getDeviceNum(),
                        Order.LightColor.values()[groupId + 1],
                        Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                        Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                        Order.LightModel.OUTER,
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);

                //记录这个灯亮起的时间编号
//                overTimeMap.put(Device.DEVICE_LIST.get(listRand.get(finalListNum)).getDeviceNum(),(int)System.currentTimeMillis());

                //记录这个灯亮起的实时时间
                duration[finalListNum] = System.currentTimeMillis();

            }
        }).start();
    }

    //查找设备的组号和所在的列
    private int[] findDeviceGroupId(char deviceNum) {
        //遍历deviceNums数组与deviceNum作比较
        int flag = 0;
        int[] Id = new int[2];//0表示组号，1表示该设备所在的列号
        for (int i = 0; i < groupNum; i++) {
            for (int j = 0; j < lightEveryNum; j++) {
                if (deviceNum == deviceNums[i][j]) {
                    //i 就是组号
                    Id[0] = i;
                    //j 就是该设备所在的列号
                    Id[1] = j;
                    flag = 1;//找到了
                    break;
                }
            }
            if (flag == 1)
                break;
        }
        return Id;
    }

    //停止训练
    public void stopTraining() {
        timer.stopTimer();
        btnBegin.setText("开始");
        btnBegin.setEnabled(false);
        imgSave.setEnabled(true);
        trainningFlag = false;

        if (overTimeThread != null)
            overTimeThread.stopThread();

        for (int i = 0; i < groupNum * lightEveryNum; i++)
            turnOffLight(Device.DEVICE_LIST.get(listRand.get(i)).getDeviceNum());

        ReceiveThread.stopThread();
        Timer.sleep(100);
        btnBegin.setEnabled(true);

    }

    //关灯
    private void turnOffLight(final char deviceNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                device.sendOrder(deviceNum,
                        Order.LightColor.NONE,
                        Order.VoiceMode.NONE,
                        Order.BlinkModel.NONE,
                        Order.LightModel.TURN_OFF,
                        Order.ActionModel.TURN_OFF,
                        Order.EndVoice.NONE);
            }
        }).start();
    }

    class OverTimeThread extends Thread {
        private boolean stop = false;

        public void stopThread() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                for (int i = 0; i < groupNum * lightEveryNum; i++) {
                    if (duration[i] != 0 && System.currentTimeMillis() - duration[i] > overTime) {

                        char deviceNum = Device.DEVICE_LIST.get(listRand.get(i)).getDeviceNum();
                        Log.i("此时超时的是：", "" + deviceNum);
                        duration[i] = 0;
                        turnOffLight(deviceNum);
                        int[] Id = findDeviceGroupId(deviceNum);
                        turnOnLight(Id[0], Id[1], deviceNum);
                    }
                }
                Timer.sleep(100);
            }
        }
    }

}

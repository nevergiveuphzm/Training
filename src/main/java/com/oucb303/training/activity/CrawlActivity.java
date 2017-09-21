package com.oucb303.training.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.oucb303.training.adpter.GroupListViewAdapter;
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
import com.oucb303.training.utils.DataUtils;
import com.oucb303.training.utils.DialogUtils;
import com.oucb303.training.utils.OperateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bai ChangCai on 2017/9/3.
 * 爬行：固定时间内拍灭的次数
 */
public class CrawlActivity extends AppCompatActivity{
    @Bind(R.id.bt_distance_cancel)
    ImageView btDistanceCancel;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save_new)
    ImageView imgSaveNew;
    @Bind(R.id.tv_training_time)
    TextView tvTrainingTime;
    @Bind(R.id.img_training_time_sub)
    ImageView imgTrainingTimeSub;
    @Bind(R.id.bar_training_time)
    SeekBar barTrainingTime;
    @Bind(R.id.img_training_time_add)
    ImageView imgTrainingTimeAdd;
//    @Bind(R.id.training_time)
//    LinearLayout TrainingTime;



//    @Bind(R.id.tv_training_times)
//    TextView tvTrainingTimes;
//    @Bind(R.id.training_times)
//    LinearLayout TrainingTimes;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.sp_dev_num)
    Spinner spDevNum;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.tv_device_list)
    TextView tvDeviceList;
//    @Bind(R.id.img_action_mode_light)
//    ImageView imgActionModeLight;
//    @Bind(R.id.img_action_mode_touch)
//    ImageView imgActionModeTouch;
//    @Bind(R.id.img_action_mode_together)
//    ImageView imgActionModeTogether;
//    @Bind(R.id.img_light_color_blue)
//    ImageView imgLightColorBlue;
//    @Bind(R.id.img_light_color_red)
//    ImageView imgLightColorRed;
//    @Bind(R.id.img_light_color_blue_red)
//    ImageView imgLightColorBlueRed;
//    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
//    @Bind(R.id.cb_end_voice)
    android.widget.CheckBox cbEndVoice;
  //  @Bind(R.id.cb_over_time_voice)
    android.widget.CheckBox cbOverTimeVoice;
//    @Bind(R.id.ll_params)
//    LinearLayout llParams;
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
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.img_set)
    ImageView imgSet;
//    @Bind(R.id.img_blink_mode_none)
//    ImageView imgBlinkModeNone;
//    @Bind(R.id.img_blink_mode_slow)
//    ImageView imgBlinkModeSlow;
//    @Bind(R.id.img_blink_mode_fast)
//    ImageView imgBlinkModeFast;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
    private int level;
    private Device device;
    //当前选用的设备个数
    private int totalNum;
    //当前选择的每次亮灯个数
    private int everyLightNum=1;
    //训练开始的标志
    private boolean trainingFlag = false;
    //运行的总次数、当前运行的次数、遗漏次数、训练的总时间
    private int totalTimes, currentTimes[], lostTimes, trainingTime;
    //延迟时间 单位是毫秒
    private int delayTime=0;
    //超时时间 单位毫秒
    private int overTime=30000;
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
    //每次开灯的设备编号
    private char[][] deviceNums;
    //每组设备灯亮起的时间
    private long[][] duration;
    private int groupSize, groupNum = 1;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox, blinkModeCheckBox;


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
    //存放随机数的list
    private List<List<Integer>> listRands = new ArrayList<>();
    private int[] everyGroupTotalTime;
    private GroupListViewAdapter groupListViewAdapter;
    private int colorNum = 1;//颜色数
    private Dialog set_dialog;

    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == timer.TIMER_FLAG) {
                //如果是时间随机，并且当前时间减去开始时间的时间差超过训练总时间
                if (timer.time >= trainingTime) {
                    timer.stopTimer();
                    stopTraining();
                }
                //开始到现在持续的时间
//                tvTotalTime.setText(msg.obj.toString());
            }
            if(msg.what == timer.TIMER_DOWN){
                tvTotalTime.setText("倒计时："+msg.obj.toString());
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                //接收到返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    //返回数据不为空
                    if (data != null && data.length() >= 4) {
                        analyseDatas(data);
                        if (randomTimesModuleAdapter != null) {
                            randomTimesModuleAdapter.notifyDataSetChanged();
                            //将列表移动到最后的位置
                            lvTimes.setSelection(timeList.size() - 1);
                        }
                        tvCurrentTimes.setText(DataUtils.getSum(currentTimes) + "");
//                        isTrainingOver();
                    }

                    break;
                case LOST_TIME:
                    tvLostTimes.setText(lostTimes + "");
                    tvCurrentTimes.setText(DataUtils.getSum(currentTimes) + "");

                    break;
                //更新完成次数
                case UPDATE_TIMES:
                    randomTimesModuleAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充屏幕的UI
        setContentView(R.layout.activity_crawl1);
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
        imgSaveNew.setEnabled(false);
        imgSaveNew.setVisibility(View.VISIBLE);
        imgHelp.setVisibility(View.VISIBLE);
        set_dialog = createLightSetDialog();
    }

    @Override
    protected void onDestroy() {
        if (trainingFlag)
            stopTraining();
        device.disconnect();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (trainingFlag) {
            Toast.makeText(CrawlActivity.this, "请先停止训练后再退出!", Toast
                    .LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    public void initView() {
        tvTitle.setText(R.string.navigation_climbing_item3);

        randomTimesModuleAdapter = new RandomTimesModuleAdapter(this, timeList);
        lvTimes.setAdapter(randomTimesModuleAdapter);

        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

//        TrainingTimes.setVisibility(View.GONE);
//        TrainingTime.setVisibility(View.VISIBLE);

        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 10));
        //0为减，1为加
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));

        //初始化设备个数spinner
        String[] num = new String[Device.DEVICE_LIST.size()];
        for (int i = 0; i < num.length; i++)
            num[i] = (i + 1) + "个";

        adapterDeviceNum = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, num);
        spDevNum.setAdapter(adapterDeviceNum);

        spDevNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spDevNum, num) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                totalNum = i + 1;
                String str = "";
                for (int j = 0; j < totalNum; j++) {
                    str += Device.DEVICE_LIST.get(j).getDeviceNum() + "  ";
                }
                tvDeviceList.setText(str);

                //初始化分组下拉框
                String[] groupNumChoose = new String[totalNum + 1];
                groupNumChoose[0] = " ";
                for (int j = 1; j <= totalNum; j++)
                    groupNumChoose[j] = j + " 组";
                //分组数的下拉框
                spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(CrawlActivity.this, spGroupNum, groupNumChoose) {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        groupNum = i;
                        currentTimes = new int[groupNum];
                        if (groupNum == 0)
                            groupNum = 1;
                        groupSize = totalNum / groupNum;
                        ///初始化分组listView
                        groupListViewAdapter = new GroupListViewAdapter(CrawlActivity.this, totalNum / groupNum);
                        lvGroup.setAdapter(groupListViewAdapter);
                        groupListViewAdapter.setGroupNum(i);
                        groupListViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

//        //设定感应模式checkBox组合的点击事件
//        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
//        actionModeCheckBox = new CheckBox(1, views);
//        //设定灯光颜色checkBox组合的点击事件
//        ImageView[] views2 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorBlueRed};
//        lightColorCheckBox = new CheckBox(1, views2);
//        new CheckBoxClickListener(lightColorCheckBox);
//        ImageView[] view3 = new ImageView[]{imgBlinkModeNone,imgBlinkModeSlow,imgBlinkModeFast};
//        blinkModeCheckBox = new CheckBox(1,view3);
//        new CheckBoxClickListener(blinkModeCheckBox);
//        //解决listView 与scrollView的滑动冲突
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
    }

    @OnClick({R.id.btn_stop,R.id.btn_begin, R.id.layout_cancel, R.id.img_help, R.id.btn_on, R.id.btn_off, R.id.img_save_new,R.id.img_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_set:
                set_dialog = createLightSetDialog();
                OperateUtils.setScreenWidth(this, set_dialog, 0.95, 0.7);
                set_dialog.show();
                break;
            case R.id.btn_begin:
                if (!device.checkDevice(this))
                    return;
                if (!trainingFlag) {
                    startTraining();
                    btnOn.setClickable(false);
                    btnOff.setClickable(false);
                }
                else
                    stopTraining();
                break;
            case R.id.layout_cancel:
                finish();
                device.turnOffAllTheLight();
                break;
            case R.id.btn_on:
                //totalNum组数，1：每组设备个数，0：类型
                device.turnOnButton(totalNum, 1, 0);
                btnOn.setClickable(false);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                btnOn.setClickable(true);
                break;
            case R.id.img_save_new:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 4:换物跑、时间随机、次数随机 ...
                bundle.putString("trainingCategory", "6");
                bundle.putString("trainingName", "爬行");
                //训练总时间
                bundle.putInt("totalTime", trainingTime);
                //每组设备个数
                bundle.putInt("deviceNum", groupSize);
                //分组数
                bundle.putInt("groupNum", groupNum);
                //每组得分
                bundle.putIntArray("scores", currentTimes);
                //总次数
                bundle.putInt("totalTimes", DataUtils.getSum(currentTimes));
                it.putExtras(bundle);
                startActivity(it);
                break;
            case R.id.img_help:
                List<Integer> list = new ArrayList<>();
                list.add(R.string.crawl_training_method);
                list.add(R.string.crawl_training_standard);
                Dialog dialog_help = DialogUtils.createHelpDialog(CrawlActivity.this,list);
                OperateUtils.setScreenWidth(this, dialog_help, 0.95, 0.7);
                dialog_help.show();
                break;
            case R.id.btn_stop:
                if(trainingFlag){
                    stopTraining();
                    btnOn.setClickable(true);
                    btnOff.setClickable(true);
                }
                break;
        }
    }

    //开始训练
    public void startTraining() {
        trainingFlag = true;

        //运行的总次数
//        totalTimes = new Integer(tvTrainingTimes.getText().toString().trim());
        //训练总时间
        trainingTime = (int) ((new Double(tvTrainingTime.getText().toString().trim())) * 60 * 1000);
        //数据清空
        listRands.clear();
        currentTimes = new int[groupNum];
        everyGroupTotalTime = new int[groupNum];
        lostTimes = 0;

        timeList.clear();
        //每次开灯的设备编号
        deviceNums = new char[groupNum][everyLightNum];
        for (int i = 0; i < groupNum; i++) {
            for (int j = 0; j < everyLightNum; j++) {
                deviceNums[i][j] = '\0';
            }
        }
        //每组设备灯亮起的时间
        duration = new long[groupNum][everyLightNum];
        //平均时间和遗漏次数
        tvAverageTime.setText("---");
        tvLostTimes.setText("---");
        tvCurrentTimes.setText("---");

        randomTimesModuleAdapter.notifyDataSetChanged();

        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();

        //创建随机序列
        createMoreRandomList();
        //发送开灯命令,随机亮起某几个灯
        for (int j = 0; j < groupNum; j++) {
            for (int i = 0; i < everyLightNum; i++) {
                Random random = new Random();
                int color = random.nextInt(colorNum) + 1;
                device.sendOrder(Device.DEVICE_LIST.get(listRands.get(j).get(i)).getDeviceNum(),
                        Order.LightColor.values()[color],
                        Order.VoiceMode.values()[ 0],
                        Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                        Order.LightModel.values()[1],
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.values()[ 0]);
                //每组设备灯亮起的当前时间
                duration[j][i] = System.currentTimeMillis();
                //每次开灯的设备编号
                deviceNums[j][i] = Device.DEVICE_LIST.get(listRands.get(j).get(i)).getDeviceNum();
            }
        }

        //开启超时线程
        overTimeThread = new OverTimeThread();
        overTimeThread.start();

        //开启接收返回灭灯时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        //开启计时线程
        beginTime = System.currentTimeMillis();
        timer = new Timer(timerHandler, trainingTime);
        timer.setBeginTime(beginTime);
        timer.start();
    }

    //停止训练
    public void stopTraining() {
        trainingFlag = false;
        btnBegin.setEnabled(false);
        imgSaveNew.setEnabled(true);
        if (overTimeThread != null)
            overTimeThread.stopThread();
        if (timer != null)
            timer.stopTimer();
        device.turnOffAllTheLight();

        //计算平均时间
        int totalTime = 0;
        for (TimeInfo info : timeList) {
            totalTime += info.getTime();
        }
        totalTime += lostTimes * overTime;
        tvAverageTime.setText(new DecimalFormat("0.00").format((totalTime * 1.0 / timeList.size())) + "毫秒");

        //暂停0.5秒,等全部数据返回,结束接收线程
        Timer.sleep(500);
        //结束接收返回灭灯时间线程
        ReceiveThread.stopThread();
        btnBegin.setEnabled(true);
        showResultDialog();
    }

    //判断训练是否结束
    public void showResultDialog() {

        List<Integer> list = new ArrayList<>();
        for (int j = 0; j < currentTimes.length; j++) {
            list.add(currentTimes[j]);
        }
        Dialog dialog = DialogUtils.createRankDialog(context, list, trainingTime + "", 0, true);
        OperateUtils.setScreenWidth(this, dialog, 0.7, 0.5);
        dialog.show();
    }

    //查找设备的组号和所在的列
    private int[] findDeviceGroupId(char deviceNum) {
        //遍历deviceNums数组与deviceNum作比较
        int flag = 0;
        int[] Id = new int[2];//0表示组号，1表示该设备所在的列号
        for (int i = 0; i < groupNum; i++) {
            for (int j = 0; j < everyLightNum; j++) {
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

    //分析数据
    public void analyseDatas(final String data) {
        List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
        outterLoop:
        for (TimeInfo info : infos) {
            int[] Id = findDeviceGroupId(info.getDeviceNum());
            //组号
            int groupId = Id[0];
            //该设备所在的列号
            int everyId = Id[1];
            Log.i("进来的是什么灯", "" + info + "--" + groupId + "---" + everyId);
            currentTimes[groupId]++;
            everyGroupTotalTime[groupId] = (int) (System.currentTimeMillis() - beginTime);
            Log.d("everyGroupTotalTime", groupId + "i" + everyGroupTotalTime[groupId]);
            timeList.add(info);
            if (!trainingFlag)
                break outterLoop;
            if (trainingFlag) {
                turnOnLight(groupId, everyId, info.getDeviceNum());
            }


        }
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
        final int[] listNumGroup = new int[2];//这个设备在随机队列里的序号
        for (int j = 0; j < listRands.size(); j++) {
            //如果随机队列里包含这个编号，就找到了这个设备在随机队列里的序号,并且移除
            for (int k = 0; k < listRands.get(j).size(); k++) {
                if (listRands.get(j).get(k) == lightNum) {
                    listNumGroup[0] = j;
                    listNumGroup[1] = k;
                    listRands.get(j).remove(k);
                    break;
                }
            }

        }

        Random random = new Random();
        for (int i = 0; i < groupNum; i++) {
            while (listRands.get(i).size() < everyLightNum) {
                //rand对应的是在设备列表里的设备序号
                int rand = random.nextInt(groupSize) + i * groupSize;
                if (!listRands.get(i).contains(rand)) {
                    //将指定的元素插入此列表中的指定位置。
                    listRands.get(i).add(listNumGroup[1], rand);
                    deviceNums[groupId][everyId] = Device.DEVICE_LIST.get(rand).getDeviceNum();
                }
            }
        }

        //亮起这一组的新的一盏灯
        final int finalListNum = listNumGroup[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer.sleep(delayTime);
                //若训练结束则返回
                if (!trainingFlag)
                    return;
                Random random = new Random();
                int color = random.nextInt(colorNum) + 1;
                device.sendOrder(Device.DEVICE_LIST.get(listRands.get(listNumGroup[0]).get(finalListNum)).getDeviceNum(),
                        Order.LightColor.values()[color],
                        Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                        Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                        Order.LightModel.values()[1],
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);

                //记录这个灯亮起的时间编号
//                overTimeMap.put(Device.DEVICE_LIST.get(listRand.get(finalListNum)).getDeviceNum(),(int)System.currentTimeMillis());

                //记录这个灯亮起的实时时间
                duration[listNumGroup[0]][finalListNum] = System.currentTimeMillis();

            }
        }).start();
    }

    //如果超时了就要关灯
    public void turnOffLight(char deviceNum) {
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

    //生成随机序列
    private void createMoreRandomList() {
        for (int i = 0; i < groupNum; i++) {
            List<Integer> list = new ArrayList<>();
            while (list.size() < everyLightNum) {
                Random random = new Random();
                int randonInt = random.nextInt(groupSize) + i * groupSize;
                if (!list.contains(randonInt)) {
                    list.add(randonInt);
                }
            }
            listRands.add(list);
        }
    }

    //超时线程,从开始训练就开始跑
    class OverTimeThread extends Thread {
        private boolean stop = false;

        public void stopThread() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                for (int j = 0; j < groupNum; j++) {
                    for (int i = 0; i < everyLightNum; i++) {
                        if (duration[j][i] != 0 && System.currentTimeMillis() - duration[j][i] > overTime) {

                            char deviceNum = Device.DEVICE_LIST.get(listRands.get(j).get(i)).getDeviceNum();
                            Log.i("此时超时的是：", "" + deviceNum);
                            duration[j][i] = 0;
                            turnOffLight(deviceNum);
                            if (currentTimes[j] < totalTimes)
                                turnOnLight(j, i, deviceNum);
                            lostTimes++;
//                            currentTimes[j]++;
                            //更新遗漏次数
                            Message msg = handler.obtainMessage();
                            msg.what = LOST_TIME;
                            handler.sendMessage(msg);
                        }
                    }
                }

            }
        }
    }
    public Dialog createLightSetDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.layout_dialog_lightset, null);// 得到加载view

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_light_set);
        ImageView imgActionModeTouch = (ImageView) layout.findViewById(R.id.img_action_mode_touch);
        ImageView imgActionModeLight = (ImageView) layout.findViewById(R.id.img_action_mode_light);
        ImageView imgActionModeTogether = (ImageView) layout.findViewById(R.id.img_action_mode_together);
        ImageView imgLightColorBlue = (ImageView) layout.findViewById(R.id.img_light_color_blue);
        ImageView imgLightColorRed = (ImageView) layout.findViewById(R.id.img_light_color_red);
        ImageView imgLightColorBlueRed = (ImageView) layout.findViewById(R.id.img_light_color_blue_red);
        ImageView imgBlinkModeNone = (ImageView) layout.findViewById(R.id.img_blink_mode_none);
        ImageView imgBlinkModeSlow = (ImageView) layout.findViewById(R.id.img_blink_mode_slow);
        ImageView imgBlinkModeFast = (ImageView) layout.findViewById(R.id.img_blink_mode_fast);
        cbVoice = (android.widget.CheckBox) layout.findViewById(R.id.cb_voice);
        cbEndVoice = (android.widget.CheckBox)layout.findViewById(R.id.cb_endvoice);
        Button btnOk = (Button) layout.findViewById(R.id.btn_ok);
        Button btnCloseSet = (Button) layout.findViewById(R.id.btn_close_set);
        final Dialog dialog = new Dialog(this, R.style.dialog_rank);

        dialog.setContentView(layout);

        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);
        //设定灯光颜色checkBox组合的点击事件
        ImageView[] views2 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorBlueRed};
        lightColorCheckBox = new CheckBox(1, views2);
        new CheckBoxClickListener(lightColorCheckBox);
        //设定闪烁模式checkbox组合的点击事件
        ImageView[] views3 = new ImageView[]{imgBlinkModeNone, imgBlinkModeSlow, imgBlinkModeFast};
        blinkModeCheckBox = new CheckBox(1, views3);
        new CheckBoxClickListener(blinkModeCheckBox);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCloseSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
}

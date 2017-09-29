package com.oucb303.training.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.adpter.RandomTimesModuleAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.dialugue.CustomDialog;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
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

public class QuickSR_Upgrade1Activity extends AppCompatActivity {

    @Bind(R.id.bt_distance_cancel)
    ImageView btDistanceCancel;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save_new)
    ImageView imgSave;
    @Bind(R.id.tv_training_times)
    TextView tvTrainingTimes;
    @Bind(R.id.img_training_times_sub)
    ImageView imgTrainingTimesSub;
    @Bind(R.id.bar_training_times)
    SeekBar barTrainingTimes;
    @Bind(R.id.img_training_times_add)
    ImageView imgTrainingTimesAdd;

    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.sp_dev_num)
    Spinner spDevNum;
    @Bind(R.id.btn_off)
    Button btnOff;

    android.widget.CheckBox cbVoice;
    android.widget.CheckBox cbEndVoice;
    @Bind(R.id.ll_params)
    LinearLayout llParams;
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


    private int level;
    private Device device;
    //当前选用的设备个数
    private int totalNum;
    //当前选择的每次亮灯个数
    private int everyLightNum;
    //成绩统计
    private int[] scores;
    //训练开始的标志
    private boolean trainingFlag = false;
    //运行的总次数、当前运行的次数、遗漏次数、训练的总时间，最终的运行次数
    private int totalTimes, currentTimes[], lostTimes, trainingTime, currentTimes1;
    //最大分组数、实际选择组数
    private int maxGroupNum, groupSize, groupNum = 1;
    //延迟时间 单位是毫秒
    private int delayTime;
    //超时时间 单位毫秒
    private int overTime;
    //开灯命令发送后 灯持续亮的时间,单位毫秒
    private int durationTime;
    //开始时间
    private long beginTime, endTime;
    //编号和时间的列表
    private ArrayList<TimeInfo> timeList = new ArrayList<>();
    //编号和时间的列表
    private ArrayList<TimeInfo> overTimeList = new ArrayList<>();
    //超时线程
    private OverTimeThread overTimeThread;
    //计时线程
    private Timer timer;
    //存放随机数的list
    //private List<Integer> listRand = new ArrayList<>();
    //存放随机数的list
    private List<List<Integer>> listRands = new ArrayList<>();
    private GroupListViewAdapter groupListViewAdapter;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightColorCheckBox, blinkModeCheckBox;

    //设置返回数据
    int[] Setting_return_data = new int[5];
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

    //每次开灯的设备编号
//    private char[] deviceNums;
    //每组设备灯亮起的时间
    private long[][] duration;
    private char[][] deviceNums;
    private int[] everyGroupTotalTime;
    private int colorNum = 1;//颜色数

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
                        isTrainingOver();
                        Log.d("currentTimes", DataUtils.getSum(currentTimes) + "");
                        tvCurrentTimes.setText(DataUtils.getSum(currentTimes) + "");
                    }
                    //Log.i("最终显示运行次数：",""+currentTimes);
                    break;
                case LOST_TIME:
                    tvLostTimes.setText(lostTimes + "");
                    tvCurrentTimes.setText(DataUtils.getSum(currentTimes) + "");
                    isTrainingOver();
                    break;
                //更新完成次数
                case UPDATE_TIMES:
                    randomTimesModuleAdapter.notifyDataSetChanged();
                    break;
                case Timer.TIMER_FLAG:
                    //开始到现在持续的时间
                    tvTotalTime.setText(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充屏幕的UI
        setContentView(R.layout.activity_quick_sr__upgrade1);
        //返回xml中定义的视图或组件的ID
        ButterKnife.bind(this);
        level = getIntent().getIntExtra("level", 0);
        context = this;
        device = new Device(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        //更新连接设备列表
        device.createDeviceList(this);
        //判断是否插入协调器
        if (device.devCount > 0) {
            device.connect(this);
            device.initConfig();
        }
        initView();
        imgSave.setEnabled(false);
        imgSave.setVisibility(View.VISIBLE);
        Setting_return_data[0]=1;
        Setting_return_data[1]=1;
        Setting_return_data[2]=0;
        Setting_return_data[3]=0;
        Setting_return_data[4]=0;

    }

    @Override
    protected void onPause() {
        super.onPause();
        device.disconnect();
        stopTraining();
        ReceiveThread.stopThread();
    }

    @Override
    public void onBackPressed() {
        if (trainingFlag) {
            Toast.makeText(QuickSR_Upgrade1Activity.this, "请先停止训练后再退出!", Toast
                    .LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    public void initView() {
        tvTitle.setText("快速启动跑(升级跑1)");

        randomTimesModuleAdapter = new RandomTimesModuleAdapter(this, timeList);
        lvTimes.setAdapter(randomTimesModuleAdapter);

        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());


        barTrainingTimes.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTimes, 100));
        //0为减，1为加
        imgTrainingTimesAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTimes, 1));
        imgTrainingTimesSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTimes, 0));



        //初始化设备个数spinner
        String[] num = new String[3];
        for (int i = 0; i < 3; i++)
            num[i] = (i + 3) + "个";
        everyLightNum = 1;
                adapterDeviceNum = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, num);
        spDevNum.setAdapter(adapterDeviceNum);

        spDevNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spDevNum, num) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                totalNum = i + 3;

                Log.i("-------------------",totalNum+"");

                //初始化分组下拉框
                maxGroupNum = Device.DEVICE_LIST.size()/totalNum;

                String[] groupNumChoose = new String[maxGroupNum + 1];
                groupNumChoose[0] = " ";
                for (int j = 1; j <= maxGroupNum; j++)
                    groupNumChoose[j] = j + " 组";

                //分组数的下拉框
                spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(QuickSR_Upgrade1Activity.this, spGroupNum, groupNumChoose) {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        groupNum = i;
                        currentTimes = new int[groupNum];
                        if (groupNum == 0)
                            groupNum = 1;
                        groupSize = totalNum ;
                        ///初始化分组listView
                        groupListViewAdapter = new GroupListViewAdapter(QuickSR_Upgrade1Activity.this,totalNum);
                        lvGroup.setAdapter(groupListViewAdapter);
                        groupListViewAdapter.setGroupNum(i);
                        groupListViewAdapter.notifyDataSetChanged();
                        //每次亮灯个数spinner
                        String[] everyTimeNum = new String[groupSize];
                        for (int j = 0; j < everyTimeNum.length; j++)
                            everyTimeNum[j] = (j + 1) + "个";

                        ArrayAdapter<String> adapterEveryNum = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, everyTimeNum);
                        adapterEveryNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //spLightNum.setAdapter(adapterEveryNum);
                    }
                });

            }
        });



    }

    @OnClick({R.id.btn_stop,R.id.btn_begin, R.id.layout_cancel, R.id.img_help, R.id.btn_on, R.id.btn_off, R.id.img_save_new,R.id.img_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_begin:
                if (!device.checkDevice(this))
                    return;
                if (!trainingFlag)
                    startTraining();
                else
                    stopTraining();
                break;
            case R.id.btn_stop:
                if(trainingFlag){
                    stopTraining();
                }
                break;
            case R.id.img_help:
                List<Integer> list = new ArrayList<>();
                list.add(R.string.situp_training_method);
                list.add(R.string.situp_training_standard);
                Dialog dialog_help = DialogUtils.createHelpDialog(QuickSR_Upgrade1Activity.this,list);
                OperateUtils.setScreenWidth(this, dialog_help, 0.95, 0.7);
                dialog_help.show();
                break;
            case R.id.layout_cancel:
                device.turnOffAllTheLight();
//                stopTraining();
                finish();
                break;
            case R.id.btn_on:
                //totalNum组数，1：每组设备个
                // +数，0：类型
                device.turnOnButton(totalNum, everyLightNum, 0);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                break;
            case R.id.img_save_new:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 4:换物跑、时间随机、次数随机 ...
                bundle.putString("trainingCategory", "6");
                bundle.putString("trainingName", "次数随机");
                trainingTime = (int) (endTime - beginTime);
                //训练总时间
                bundle.putInt("totalTime", trainingTime);
                //每组设备个数
                bundle.putInt("deviceNum", groupSize);
                //总次数
                bundle.putInt("totalTimes", totalTimes);
                //每组完成时间
                bundle.putIntArray("scores", everyGroupTotalTime);
                //分组数
                bundle.putInt("groupNum", groupNum);
                it.putExtras(bundle);
                startActivity(it);
                break;
            case R.id.img_set:
                CustomDialog dialog = new  CustomDialog(QuickSR_Upgrade1Activity.this,"From btn 2",new CustomDialog.ICustomDialogEventListener() {
                    @Override
                    public void customDialogEvent(int id) {
                        // TextView imageView = (TextView)findViewById(R.id.main_image);

                        int aaa = id;
                        String a  =String.valueOf(aaa);
                        for(int i=0;i<5;i++){
                            Setting_return_data[i] = aaa % 10;
                            aaa = aaa/10;
                            Log.i("----------",i+"   "+Setting_return_data[i]+"");
                            //imageView.append(Setting_shuju[i]+"   ");
                        }
                    }
                },R.style.dialog_rank);

                dialog.show();
                OperateUtils operateUtils = new OperateUtils();
                operateUtils.setScreenWidth(QuickSR_Upgrade1Activity.this,dialog,0.95,0.7);
                break;
        }
    }

    //开始训练
    public void startTraining() {
        btnOn.setClickable(false);
        btnOff.setClickable(false);
        trainingFlag = true;
        //运行的总次数
        totalTimes = new Integer(tvTrainingTimes.getText().toString().trim());
        //延迟时间
        delayTime = 0;
        //超时时间
        overTime = 10000;
        //训练总时间
//        trainingTime = (int) ((new Double(tvTrainingTime.getText().toString().trim())) * 60 * 1000);
        //数据清空
        //listRand.clear();
        listRands.clear();
        currentTimes = new int[groupNum];
        everyGroupTotalTime = new int[groupNum];

        lostTimes = 0;
        //开灯命令发送后 灯持续亮的时间,单位毫秒
        durationTime = 0;
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
        //初始化成绩
        scores = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            scores[i] = 0;
        }

        //平均时间和遗漏次数
        tvAverageTime.setText("---");
        tvLostTimes.setText("---");
        tvCurrentTimes.setText("---");

        randomTimesModuleAdapter.notifyDataSetChanged();

        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();
        //创建随机序列
        createMoreRandomList();
//        Log.i("刚开始的随机序列：", "" + listRand);
        //发送开灯命令,随机亮起某几个灯
        for (int j = 0; j < groupNum; j++) {
            for (int i = 0; i < everyLightNum; i++) {
//                Random random = new Random();
//                int color = random.nextInt(colorNum) + 1;

                device.sendOrder(Device.DEVICE_LIST.get(listRands.get(j).get(i)).getDeviceNum(),
                        Order.LightColor.values()[Setting_return_data[1]],
                        Order.VoiceMode.values()[Setting_return_data[3]],
                        Order.BlinkModel.values()[Setting_return_data[2]],
                        Order.LightModel.OUTER,
                        Order.ActionModel.values()[Setting_return_data[0]],
                        Order.EndVoice.values()[Setting_return_data[4]]);
                //每组设备灯亮起的当前时间
                duration[j][i] = System.currentTimeMillis();
                //每次开灯的设备编号
                deviceNums[j][i] = Device.DEVICE_LIST.get(listRands.get(j).get(i)).getDeviceNum();
                //开灯命令发送后 灯持续亮的时间
                durationTime = 0;
            }
        }

        //开启超时线程
        overTimeThread = new OverTimeThread();
        overTimeThread.start();

        //开启接收返回灭灯时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        //开启计时线程
        beginTime = System.currentTimeMillis();
        timer = new Timer(handler);
        timer.setBeginTime(beginTime);
        timer.start();
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

    //停止训练
    public void stopTraining() {
        btnOn.setClickable(true);
        btnOff.setClickable(true);
        trainingFlag = false;
        btnBegin.setEnabled(false);
        imgSave.setEnabled(true);
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
        endTime = System.currentTimeMillis();


    }

    //判断训练是否结束
    public boolean isTrainingOver() {
        //如果结束了
        if (!trainingFlag)
            return true;
        int i = 0;
        for (; i < currentTimes.length; ++i) {
            if (currentTimes[i] < totalTimes)
                break;
        }

        if (i == groupNum) {
            stopTraining();
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < everyGroupTotalTime.length; j++) {
                list.add(everyGroupTotalTime[j]);
            }
            Dialog dialog = DialogUtils.createRankDialog(context, list, totalTimes + "", 1, true);
            OperateUtils.setScreenWidth(this, dialog, 0.7, 0.5);
            dialog.show();
            return true;
        }
        return false;
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
            scores[groupId]++;
            currentTimes[groupId]++;
            everyGroupTotalTime[groupId] = (int) (System.currentTimeMillis() - beginTime);
            Log.d("everyGroupTotalTime", groupId + "i" + everyGroupTotalTime[groupId]);
            timeList.add(info);
            if (isTrainingOver())
                break outterLoop;
            if (currentTimes[groupId] < totalTimes) {
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

//        Log.i("现在的随机序列是什么",""+listRand);
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
                        Order.LightColor.values()[Setting_return_data[1]],
                        Order.VoiceMode.values()[Setting_return_data[3]],
                        Order.BlinkModel.values()[Setting_return_data[2]],
                        Order.LightModel.OUTER,
                        Order.ActionModel.values()[Setting_return_data[0]],
                        Order.EndVoice.values()[Setting_return_data[4]]);


                //记录这个灯亮起的实时时间
                duration[listNumGroup[0]][finalListNum] = System.currentTimeMillis();

            }
        }).start();
    }

    //如果超时了就要关灯
    public void turnOffLight(char deviceNum) {
        device.sendOrder(deviceNum,
                Order.LightColor.NONE,
                Order.VoiceMode.values()[0],
                Order.BlinkModel.NONE,
                Order.LightModel.TURN_OFF,
                Order.ActionModel.TURN_OFF,
                Order.EndVoice.NONE);

        //超时了运行次数也要加1

        //如果超时了，也要放到timeList里，然后放到adapter里，显示在右侧的listView之中，更新listView的数据
        TimeInfo info = new TimeInfo();
        info.setDeviceNum(deviceNum);
        timeList.add(info);

        Message msg = handler.obtainMessage();
        msg.what = UPDATE_TIMES;
        msg.obj = "";
        handler.sendMessage(msg);
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

    //超时线程,从开始训练就开始跑,一直到结束训练才结束
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
                            currentTimes[j]++;
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
}

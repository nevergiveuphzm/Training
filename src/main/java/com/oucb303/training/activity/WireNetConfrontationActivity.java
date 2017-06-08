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
import com.oucb303.training.adpter.WireNetConfrontationAdapter;
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
 * Created by HP on 2017/5/25.
 */
public class WireNetConfrontationActivity extends AppCompatActivity
{
    @Bind(R.id.bt_distance_cancel)
    ImageView btDistanceCancel;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
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
    @Bind(R.id.btn_on)
    Button btnOn;
    //每组设备个数
    @Bind(R.id.sp_device_num)
    Spinner spDeviceNum;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.sp_light_num)
    Spinner spLightNum;
    @Bind(R.id.img_action_mode_light)
    ImageView imgActionModeLight;
    @Bind(R.id.img_action_mode_touch)
    ImageView imgActionModeTouch;
    @Bind(R.id.img_action_mode_together)
    ImageView imgActionModeTogether;
    @Bind(R.id.img_blink_mode_none)
    ImageView imgBlinkModeNone;
    @Bind(R.id.img_blink_mode_slow)
    ImageView imgBlinkModeSlow;
    @Bind(R.id.img_blink_mode_fast)
    ImageView imgBlinkModeFast;
    @Bind(R.id.img_light_color_blue)
    ImageView imgLightColorBlue;
    @Bind(R.id.img_light_color_red)
    ImageView imgLightColorRed;
    @Bind(R.id.img_light_color_green)
    ImageView imgLightColorGreen;
    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
    @Bind(R.id.cb_end_voice)
    android.widget.CheckBox cbEndVoice;
    @Bind(R.id.cb_over_time_voice)
    android.widget.CheckBox cbOverTimeVoice;
    @Bind(R.id.ll_params)
    LinearLayout llParams;
    @Bind(R.id.tv_current_times)
    TextView tvCurrentTimes;
    @Bind(R.id.tv_lost_times)
    TextView tvLostTimes;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.lv_scores)
    ListView lvScores;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
    @Bind(R.id.img_save)
    ImageView imgSave;


    private Device device;
    //所选择的设备个数，分组数，每组设备个数，每组每次亮灯个数
    private int totalNum, groupNum, everyNum, lightNum;
    private GroupListViewAdapter groupListViewAdapter;
    private WireNetConfrontationAdapter wireNetConfrontationAdapter;
    private int level;
    //感应模式，闪烁模式，灯光颜色
    private CheckBox actionModeCheckBox, lightModeCheckBox, blinkModeCheckBox;
    //是否训练标志
    private boolean trainningFlag = false;
    //训练总时间,超时时间  单位是毫秒
    private int trainingTime, overTime;
    //运行总次数,遗漏次数
    private int totalTimes, lostTimes;
    //成绩统计
    private int[] scores;
    //存放没有亮灯的设备编号  奇数
    private ArrayList<ArrayList<Integer>> oddListRand = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> listRand1 = new ArrayList<>();

    //存放偶数随机队列
    private ArrayList<ArrayList<Integer>> evenListRand = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> listRand2 = new ArrayList<>();

    private final int TIME_RECEIVE = 1;
    private final int UPDATE_SCORES = 2;
    private final int STOP_TRAINING = 3;
    private final int LOST_TIME = 4;
    //训练开始时间
    private long startTime;
    //计时器
    private Timer timer;
    //每次开灯组号和设备编号
    private char[] deviceNums;
    //记录上一次第一组开的灯的编号
    private int light2 = 0;
    //每组设备灯亮起的时间
    private Object[][] duration;
    //超时线程
    private OverTimeThread overTimeThread;
    //此时超时的设备
    private char overTimeDeviceNum;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String data = msg.obj.toString();
            if (!trainningFlag)
                return;
            switch (msg.what)
            {
                case UPDATE_SCORES:
                    //更新成绩
                    wireNetConfrontationAdapter.notifyDataSetChanged();
                    break;
                case Timer.TIMER_FLAG:
                    //判断是否结束
                    if (timer.time >= trainingTime)
                    {
                        Message message = Message.obtain();
                        message.what = STOP_TRAINING;
                        message.obj = "";
                        handler.sendMessage(message);
                    }
                    //开始到现在持续的时间
                    tvTotalTime.setText(msg.obj.toString());
                    break;
                case TIME_RECEIVE:
                    //接受返回的时间线程
                    if (data != null && data.length() > 7)
                    {
                        analyseData(data);
                        tvCurrentTimes.setText(totalTimes + "");
                    }
                    break;
                case STOP_TRAINING:
                    stopTraining();
                    break;
                case LOST_TIME:
                    tvLostTimes.setText(lostTimes + "");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wirenetconfrontation);
        ButterKnife.bind(this);
        level = getIntent().getIntExtra("level", 1);

        device = new Device(this);
        //更新设备连接列表
        device.createDeviceList(this);
        //判断协调器是否插入
        if (device.devCount > 0)
        {
            //连接
            device.connect(this);
            //设备初始化
            device.initConfig();
        }
        initView();
    }

    public void initView()
    {
        tvTitle.setText("隔网对抗");
        imgSave.setVisibility(View.VISIBLE);
        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //初始化训练时间拖动条
        //拖动进度条的事件监听  第一个：显示时间tectview，第二个：最大值
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 10));
        //直接在触摸屏进行按住和松开事件的操作
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));
        //初始化超时时间拖动条
        barOverTime.setOnSeekBarChangeListener(new MySeekBarListener(tvOverTime, 28, 2));
        imgOverTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barOverTime, 1));
        imgOverTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barOverTime, 0));

        //选择设备个数spinner
        if (Device.DEVICE_LIST.size() >= 4)
        {
            String[] deviceNum = new String[Device.DEVICE_LIST.size() - 2];
            deviceNum[0] = "";
            for (int i = 1; i < deviceNum.length; i++)
            {
                deviceNum[i] = (i + 3) + "个";
            }
            spDevNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spDevNum, deviceNum)
            {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int poision, long l)
                {
                    super.onItemSelected(adapterView, view, poision, l);
                    totalNum = poision + 3;
                    Toast.makeText(WireNetConfrontationActivity.this, "当前选择总设备个数是" + totalNum, Toast.LENGTH_SHORT).show();

                    //每组设备个数spinner
                    final String[] everyNums = new String[totalNum / 2];
                    everyNums[0] = "";
                    for (int i = 1; i < everyNums.length; i++)
                    {
                        everyNums[i] = (i + 1) + "个";
                    }
                    ArrayAdapter<String> adapterEveryNum = new ArrayAdapter<String>(WireNetConfrontationActivity.this, android.R.layout.simple_spinner_item, everyNums);
                    adapterEveryNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDeviceNum.setAdapter(adapterEveryNum);

                    spDeviceNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            everyNum = i + 1;
//                        Toast.makeText(WireNetConfrontationActivity.this, "当前选择每组设备个数是" + everyNum, Toast.LENGTH_SHORT).show();
//                        Log.i("------------------","-----------------"+everyNum);
                            groupNum = totalNum / everyNum;
                            if (groupNum % 2 != 0)
                            {
                                //组数为奇数，不符合规定
                                groupNum = groupNum - 1;
                            }
//                        Toast.makeText(WireNetConfrontationActivity.this, "当前分组是" + groupNum, Toast.LENGTH_SHORT).show();
                            groupListViewAdapter.setGroupNum(groupNum);
                            groupListViewAdapter.setGroupSize(everyNum);
                            groupListViewAdapter.notifyDataSetChanged();


                            //初始化每次亮灯个数spinner
                            String[] lightNums = new String[everyNum + 1];
                            lightNums[0] = "";
                            for (int j = 1; j < lightNums.length; j++)
                            {
                                lightNums[j] = j + "个";
                            }
                            ArrayAdapter<String> adapterLightNum = new ArrayAdapter<String>(WireNetConfrontationActivity.this, android.R.layout.simple_spinner_item, lightNums);
                            adapterLightNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spLightNum.setAdapter(adapterLightNum);
                            spLightNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                            {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                                {
                                    lightNum = i;
//                                Toast.makeText(WireNetConfrontationActivity.this, "当前每次亮灯个数是" + lightNum, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView)
                                {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView)
                        {

                        }
                    });

                }
            });


            groupListViewAdapter = new GroupListViewAdapter(this);
            lvGroup.setAdapter(groupListViewAdapter);
        } else
            Toast.makeText(this, "设备个数不足，不能运行!", Toast.LENGTH_SHORT).show();

        //解决listView 与scrollView的滑动冲突
        lvGroup.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent)
            {
                //从listView 抬起时将控制权还给scrollview
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    svContainer.requestDisallowInterceptTouchEvent(false);
                else
                    //requestDisallowInterceptTouchEvent（true）方法是用来子View告诉父容器不要拦截我们的事件的
                    svContainer.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //感应模式
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);
        //闪烁模式
        ImageView[] views1 = new ImageView[]{imgBlinkModeNone, imgBlinkModeSlow, imgBlinkModeFast};
        blinkModeCheckBox = new CheckBox(1, views1);
        new CheckBoxClickListener(blinkModeCheckBox);
        //灯光颜色
        ImageView[] views2 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorGreen};
        lightModeCheckBox = new CheckBox(1, views2);
        new CheckBoxClickListener(lightModeCheckBox);

        //初始化右侧成绩listview
        wireNetConfrontationAdapter = new WireNetConfrontationAdapter(this);
        lvScores.setAdapter(wireNetConfrontationAdapter);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        device.turnOffAllTheLight();
        ReceiveThread.stopThread();
        if (device.devCount > 0)
            device.disconnect();
    }

    @OnClick({R.id.layout_cancel, R.id.btn_begin, R.id.img_help, R.id.btn_on, R.id.btn_off, R.id.img_save})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                device.turnOffAllTheLight();
                break;
            case R.id.btn_begin:
                //检测设备
                if (!device.checkDevice(this))
                    return;
                if (groupNum <= 0)
                {
                    Toast.makeText(this, "未选择分组,不能开始!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (trainningFlag)
                    stopTraining();
                else
                    startTraining();
                break;
            case R.id.btn_on:
                device.turnOnButton(totalNum, 1, 0);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                break;
            case R.id.img_save:
                Intent intent = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 5:运球比赛、多人混战、分组对抗 ...
                bundle.putString("trainingCategory", "7");
                bundle.putString("trainingName", "隔网对抗");
                //训练总时间
                bundle.putInt("trainingTime", trainingTime);
                //设备个数
                bundle.putInt("totalNum", totalNum);
                //每组得分
                bundle.putIntArray("scores", scores);
                //分组数
                bundle.putInt("groupNum", groupNum);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    public void startTraining()
    {
        btnBegin.setText("停止");
        trainningFlag = true;
        Log.i("分组数是：", "---------" + groupNum);
        //初始化
        scores = new int[groupNum];
        for (int i = 0; i < groupNum; i++)
        {
            scores[i] = 0;
        }
        //每组设备灯亮起的时间
        duration = new Object[groupNum * everyNum][2];
        overTimeDeviceNum = '\0';

        listRand1.clear();
        listRand2.clear();
        oddListRand.clear();
        evenListRand.clear();
        totalTimes = 0;
        lostTimes = 0;
        wireNetConfrontationAdapter.setScores(scores);
        wireNetConfrontationAdapter.notifyDataSetChanged();
        //创建随机队列
        createRandomNumber(groupNum);
        for (int i = 0; i < oddListRand.size(); i++)
        {
            Log.i("奇数组的随机队列都有什么？", "=======" + oddListRand.get(i) + "\n");
        }
//        for (int j = 0;j < oddListRand.size();j++)
//        {
//            for (int i = 0;i< lightNum;i++)
//            {
//                Device.DEVICE_LIST.get(oddListRand.get(j).get(0));
//                //开那个灯就把这个灯给删掉
//                oddListRand.get(j).remove(0);
//            }
//
//        }
        for (int j = 0; j < oddListRand.size(); j++)
        {
            for (int i = 0; i < lightNum; i++)
            {
                //第一次开奇数组的灯
                device.sendOrder(Device.DEVICE_LIST.get(oddListRand.get(j).get(0)).getDeviceNum(),
                        Order.LightColor.values()[1],
                        Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                        Order.BlinkModel.values()[blinkModeCheckBox.getCheckId() - 1],
                        Order.LightModel.OUTER,
                        Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                        Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);

                //每组设备灯亮起的当前时间
                duration[oddListRand.get(j).get(0)][0] = System.currentTimeMillis();
                duration[oddListRand.get(j).get(0)][1] = 1;

                //开那个灯就把这个灯给删掉
                oddListRand.get(j).remove(0);
            }
//            oddListRand.remove(0);
        }

        Log.i("rand2:----------", "--sssssssssssss------" + evenListRand);
        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();
        //开启接收设备返回时间的监听线程,返回时间和编号
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();
        //开启超时线程
        overTimeThread = new OverTimeThread();
        overTimeThread.start();

        startTime = System.currentTimeMillis();
        trainingTime = (int) (new Double(tvTrainingTime.getText().toString()) * 60 * 1000);
        overTime = new Integer(tvOverTime.getText().toString()) * 1000;
        //开启计时器
        timer = new Timer(handler, trainingTime);
        timer.setBeginTime(startTime);
        timer.start();
    }

    //解析数据
    public void analyseData(final String data)
    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        //infos里有设备编号和返回时间
        List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
        Log.i("infos里面有什么", "<<<<<<>>>>>><<<<>>>>>" + infos);
        for (int t = 0; t < infos.size(); t++)
        {
            char deviceNum = infos.get(t).getDeviceNum();

            int groupId = findDeviceGroupId(infos.get(t).getDeviceNum());
            int lineNum = findDeviceGroupId(overTimeDeviceNum);
            //如果发现进来的是超时的那个设备，就不解析直接结束本次循环
            Log.i("此时超时的设备是overTimeDNum", "===11111=====" + overTimeDeviceNum);
            if ((deviceNum == overTimeDeviceNum) && ((int) (duration[lineNum][1]) == 0))
            {
                Log.i("哈哈哈啊哈哈哈", "我结束本次循环了");
                overTimeDeviceNum = '\0';
                continue;
            }
            scores[groupId] += 1;
            if (groupId % 2 == 0)
            {
                Log.i("evenListRand:----------", "------000000000------" + evenListRand);
                //奇数组，开groupId + 1 组的灯
                turnOnEvenLight(groupId / 2);
                Log.i("evenListRan:----------", "------111111111------" + evenListRand);
                //把谁灭了就再把谁给加进去

//                        for (int j = 0;j < Device.DEVICE_LIST.size(); j++){
//                            if (deviceNum == Device.DEVICE_LIST.get(j).getDeviceNum()){
//                                oddListRand.get(groupId/2).add(j);
//                                Log.i("duration的行号是：","====aaaaaaaa======="+j);
//                                duration[j][1] = 0;
//                                break;
//                            }
//                        }
                addRand(deviceNum, groupId);
                Log.i("oddListRand:----------", "--ccccccccccccccccc------" + oddListRand);
            } else
            {
                //开groupId - 1组的灯
                Log.i("evenListRand:----------", "--222222222222------" + evenListRand);
//                        char deviceNum = infos.get(t).getDeviceNum();
//                        for (int j = 0;j < Device.DEVICE_LIST.size(); j++){
//                            if (deviceNum == Device.DEVICE_LIST.get(j).getDeviceNum()){
//                                evenListRand.get(groupId/2).add(j);
//                                Log.i("duration的行号是：","====bbbbbbbbbb======="+j);
//                                duration[j][1] = 0;
//                                break;
//                            }
//                        }
                addRand(deviceNum, groupId);
                Log.i("evenListRand:----------", "--33333333333------" + evenListRand);
                Log.i("oddListRand:----------", "--aaaaaaaaaaaaa------" + oddListRand);
                turnOnOddLight(groupId / 2);
                Log.i("oddListRand:----------", "--bbbbbbbbbbbbbb------" + oddListRand);
            }
            totalTimes++;
//                    //毁灭一个灯，第二组灯才会亮一个
//                    if (groupId == 0) {
//                        //把这个灯从listrand1里移除
////                for (int i = 0; i < listRand1.size(); i++) {
////                    if (infos.get(t).getDeviceNum() == Device.DEVICE_LIST.get(listRand1.get(i)).getDeviceNum()) {
////                        listRand1.remove(i);
////                        record1[t] = i;
////                        while (listRand1.size() < everyNum)
////                        {
////                            int randomInt1 = (int) (2 * Math.random());
////                            //如果随机list里不包含产生的这个随机数，则将产生的这个随机数加入到随机list中
////                            if (!listRand1.contains(randomInt1)) {
////                                listRand1.add(i,randomInt1);
////                            } else {
////                                System.out.println("该数字已经被添加,不能重复添加");
////                            }
////                        }
////                        break;
////                    }
////                }
//                        Log.i("rand2:----------","------000000------"+listRand2);
//                        Log.i("开的是哪个灯","=============="+Device.DEVICE_LIST.get(listRand2.get(0)).getDeviceNum());
//                        turnOnEvenLight();
//
//                        listRand2.remove(0);
//                        Log.i("此时的rand2:----------","--111111111------"+listRand2);
//                        char deviceNum = infos.get(t).getDeviceNum();
//                        for (int j = 0;j < Device.DEVICE_LIST.size(); j++){
//                            if (deviceNum == Device.DEVICE_LIST.get(j).getDeviceNum()){
//                                listRand1.add(j);
//                                break;
//                            }
//                        }
//                        Log.i("rand1:----------","--ccccccccccccccccc------"+listRand1);
////                light2++;
////                light2++;
////                while (listRand2.size() < infos.size()) {
////                        Log.i("执行了几次？","-------------------------");
////                        int random2 = (int) (2 * Math.random() + 2);
////                        //如果随机list里不包含产生的这个随机数，则将产生的这个随机数加入到随机list中
////                        if (!listRand2.contains(random2)) {
////                            listRand2.add(random2);
////                            light2 = random2;
////                            turnOnSecondLight(listRand2, random2);
////                        } else {
////                            System.out.println("该数字已经被添加,不能重复添加");
////                        }
////                }
//                    } else if (groupId == 1){
//                        Log.i("此时的rand2:----------","--222222222------"+listRand2);
//                        char deviceNum = infos.get(t).getDeviceNum();
//                        for (int j = 0;j < Device.DEVICE_LIST.size(); j++){
//                            if (deviceNum == Device.DEVICE_LIST.get(j).getDeviceNum())
//                                listRand2.add(j);
//                        }
//                        Log.i("加上以后的rand2:----------","--33333333------"+listRand2);
//                        Log.i("rand1:----------","--aaaaaaaaaaaaa------"+listRand1);
//                        //再一次开灯的时候，开的是rand1里新加入的那个号
//                        turnOnFirstLight(groupId/2);
//                        listRand1.remove(0);
//                        Log.i("rand1:----------","--bbbbbbbbbbbbbb------"+listRand1);
//                    }
        }
        //更新成绩
        Message msg = Message.obtain();
        msg.what = UPDATE_SCORES;
        msg.obj = "";
        handler.sendMessage(msg);
//            }
//        }).start();
    }

    public void addRand(char deviceNum, int groupId)
    {
        for (int j = 0; j < Device.DEVICE_LIST.size(); j++)
        {
            if (deviceNum == Device.DEVICE_LIST.get(j).getDeviceNum())
            {
                if (groupId % 2 == 0)
                {
                    //j就是要加到oddListRand里的那个编号
                    if (checkRepeat(oddListRand, j))
                        oddListRand.get(groupId / 2).add(j);
                } else
                {
                    if (checkRepeat(evenListRand, j))
                        evenListRand.get(groupId / 2).add(j);
                }
                Log.i("duration的行号是：", "====aaaaaaaa=======" + j);
                duration[j][1] = 0;
                break;
            }
        }
    }

    public boolean checkRepeat(ArrayList<ArrayList<Integer>> listRand, int id)
    {
        for (int i = 0; i < listRand.size(); i++)
        {
            for (int j = 0; j < listRand.get(i).size(); j++)
            {
                if (id == listRand.get(i).get(j))
                    return false;
            }
        }
        return true;
    }

    private void turnOnOddLight(int num)
    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Timer.sleep(2000);
        device.sendOrder(Device.DEVICE_LIST.get(oddListRand.get(num).get(0)).getDeviceNum(),
                Order.LightColor.values()[1],
                Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                Order.BlinkModel.values()[blinkModeCheckBox.getCheckId() - 1],
                Order.LightModel.OUTER,
                Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
        Log.i("开的是哪个灯", "999999999999999" + Device.DEVICE_LIST.get(oddListRand.get(num).get(0)).getDeviceNum());
        duration[oddListRand.get(num).get(0)][0] = System.currentTimeMillis();
        duration[oddListRand.get(num).get(0)][1] = 1;
        oddListRand.get(num).remove(0);
//            }
//        }).start();
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
        return position / everyNum;
    }

    //生成随机数
    private void createRandomNumber(int groupNum)
    {
        for (int i = 0, j = 1; (i < groupNum) && (j < groupNum); i += 2, j += 2)
        {
            listRand1 = new ArrayList<>();
            listRand2 = new ArrayList<>();
            //lightNum是每次亮灯个数，everyNum是每组设备个数
            while (listRand1.size() < everyNum)
            {
                int randomInt1 = (int) (everyNum * Math.random() + everyNum * i);
                //如果随机list里不包含产生的这个随机数，则将产生的这个随机数加入到随机list中
                if (!listRand1.contains(randomInt1))
                {
                    listRand1.add(randomInt1);
                    duration[randomInt1][0] = System.currentTimeMillis();
                    duration[randomInt1][1] = 0;
                } else
                {
                    System.out.println("该数字已经被添加,不能重复添加i");
                }
            }
            oddListRand.add(listRand1);
            //lightNum是每次亮灯个数，everyNum是每组设备个数
            while (listRand2.size() < everyNum)
            {
                int randomInt2 = (int) (everyNum * Math.random() + everyNum * j);
                //如果随机list里不包含产生的这个随机数，则将产生的这个随机数加入到随机list中
                if (!listRand2.contains(randomInt2))
                {
                    listRand2.add(randomInt2);
                    duration[randomInt2][0] = System.currentTimeMillis();
                    duration[randomInt2][1] = 0;
                } else
                {
                    System.out.println("该数字已经被添加,不能重复添加j");
                }
            }
            evenListRand.add(listRand2);
        }
    }

    //开第二组的一个灯
    public void turnOnEvenLight(int num)
    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Timer.sleep(2000);
        device.sendOrder(Device.DEVICE_LIST.get(evenListRand.get(num).get(0)).getDeviceNum(),
                Order.LightColor.values()[1],
                Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                Order.BlinkModel.values()[blinkModeCheckBox.getCheckId() - 1],
                Order.LightModel.OUTER,
                Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
        duration[evenListRand.get(num).get(0)][0] = System.currentTimeMillis();
        duration[evenListRand.get(num).get(0)][1] = 1;
        Log.i("开的是哪个灯", "999999999999999" + Device.DEVICE_LIST.get(evenListRand.get(num).get(0)).getDeviceNum());
        evenListRand.get(num).remove(0);
//            }
//        }).start();
    }

    //停止训练
    private void stopTraining()
    {
        timer.stopTimer();
        btnBegin.setText("开始");
        btnBegin.setEnabled(false);
        trainningFlag = false;
        if (overTimeThread != null)
            overTimeThread.stopThread();
        device.turnOffAllTheLight();
        ReceiveThread.stopThread();
        Timer.sleep(100);
        btnBegin.setEnabled(true);
    }

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
                for (int i = 0; i < duration.length; i++)
                {
                    String countString = String.valueOf(duration[i][0]);
                    Long count = Long.valueOf(countString);
                    if ((System.currentTimeMillis() - count >= overTime) && ((int)duration[i][1] == 1) && ((int)duration[i][0] != 0))
                    {

                        //这个i就是设备的编号
                        int groupId = i / everyNum;
                        char deviceNum = Device.DEVICE_LIST.get(i).getDeviceNum();
                        overTimeDeviceNum = deviceNum;
                        Log.i("此时超时的是：", "" + deviceNum);
                        duration[i][0] = System.currentTimeMillis();
                        duration[i][1] = 0;
                        //关灯
                        turnOffLight(deviceNum);
                        Timer.sleep(10);
                        if (groupId % 2 == 0)
                        {
                            turnOnEvenLight(groupId / 2);
                            Log.i("aadfjkdjfkdsjfkdj", "dfkdjfkdsjfkdsfksdj");
                            if (checkRepeat(oddListRand, i))
                                oddListRand.get(groupId / 2).add(i);
//                            for (int j = 0;j < Device.DEVICE_LIST.size(); j++){
//                                if (deviceNum == Device.DEVICE_LIST.get(j).getDeviceNum()){
//                                    oddListRand.get(groupId/2).add(j);
//                                    break;
//                                }
//                            }
//                            addRand(deviceNum,groupId);
                        } else
                        {
                            if (checkRepeat(evenListRand, i))
                                evenListRand.get(groupId / 2).add(i);
//                            for (int j = 0;j < Device.DEVICE_LIST.size(); j++){
//                                if (deviceNum == Device.DEVICE_LIST.get(j).getDeviceNum()){
//                                    evenListRand.get(groupId/2).add(j);
//                                    break;
//                                }
//                            }
//                            addRand(deviceNum,groupId);
                            turnOnOddLight(groupId / 2);
                            Log.i("aadfjkdjfkdsjfkdj", "dfkdjfkdsjfkdsfksdj");
                        }
                        lostTimes++;
                        Message msg = new Message();
                        msg.what = LOST_TIME;
                        msg.obj = "";
                        handler.sendMessage(msg);
                    }
                }
                Timer.sleep(100);
            }
        }
    }

    private void turnOffLight(final char deviceNum)
    {
        device.sendOrder(deviceNum,
                Order.LightColor.NONE,
                Order.VoiceMode.NONE,
                Order.BlinkModel.NONE,
                Order.LightModel.TURN_OFF,
                Order.ActionModel.TURN_OFF,
                Order.EndVoice.NONE);
    }
}

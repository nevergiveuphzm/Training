package com.oucb303.training.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.ConfrontationAdapter;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.SpinnerItemSelectedListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.model.TimeInfo;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.widget.HorizontalListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 双人对抗赛
 */

public class GroupConfrontationActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.sp_group_device_num)
    Spinner spGroupDeviceNum;

    private final int TIME_RECEIVE = 1;
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
    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.hlv_group1)
    HorizontalListView hlvGroup1;
    @Bind(R.id.hlv_group2)
    HorizontalListView hlvGroup2;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_blink_mode_none)
    ImageView imgBlinkModeNone;
    @Bind(R.id.img_blink_mode_slow)
    ImageView imgBlinkModeSlow;
    @Bind(R.id.img_blink_mode_fast)
    ImageView imgBlinkModeFast;


    private Device device;
    private GroupListViewAdapter groupListViewAdapter;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightModeCheckBox,blinkModeCheckBox;

    //每组设备个数
    private int groupSize;
    // 训练是否正在进行的标志
    private boolean trainingFlag = false;

    // 0:表示该设备还未亮  1:表示该设备亮正常颜色  2:表示该设备是对方将我方设备亮起品红色 3:表示不能挥灭
    private ArrayList<GroupLightInfo>[] lightInfos = new ArrayList[2];
    private ConfrontationAdapter groupOneAdapter, groupTwoAdapter;
    private Timer timer;
    private int delay = 1000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String data = msg.obj.toString();
            switch (msg.what) {
                case TIME_RECEIVE:
                    if (data != null && data.length() > 0)
                        analyzeData(data);
                    break;
                case Timer.TIMER_FLAG:
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < groupSize; j++) {
                            GroupLightInfo info = lightInfos[i].get(j);
                            if (info.lightFlag == 2 && timer.time - info.time > delay) {
                                //把原来的感应和灯关了
                                device.sendOrder(Device.DEVICE_LIST.get(i * groupSize + j).getDeviceNum(),
                                        Order.LightColor.NONE,
                                        Order.VoiceMode.NONE,
                                        Order.BlinkModel.NONE,
                                        Order.LightModel.TURN_OFF,
                                        Order.ActionModel.TURN_OFF,
                                        Order.EndVoice.NONE);
                                Timer.sleep(20);
                                info.lightFlag = 3;
                                turnOnLight(j, i, 3);
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_confrontation);
        ButterKnife.bind(this);

        initView();
        device = new Device(this);
        //更新设备连接列表
        device.createDeviceList(this);
        //判断协调器是否插入
        if (device.devCount > 0) {
            //连接
            device.connect(this);
            //设备初始化
            device.initConfig();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (device.devCount > 0)
            device.disconnect();
    }

    private void initView() {
        tvTitle.setText("双人对抗");

        imgHelp.setVisibility(View.VISIBLE);

        //初始化分组listview
        groupListViewAdapter = new GroupListViewAdapter(this, 0);
        groupListViewAdapter.setGroupNum(2);
        lvGroup.setAdapter(groupListViewAdapter);

        //选择设备个数spinner
        String[] num = new String[]{" ", "3个", "4个"};
        spGroupDeviceNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spGroupDeviceNum, num) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                int totalNum = 0;
                if (i > 0)
                    totalNum = (i + 2) * 2;
                if (totalNum > Device.DEVICE_LIST.size()) {
                    Toast.makeText(GroupConfrontationActivity.this, "当前设备不足!", Toast.LENGTH_SHORT).show();
                    totalNum = 0;
                }
                groupSize = totalNum / 2;
                groupListViewAdapter.setGroupSize(groupSize);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });

        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);
        //设定灯光模式checkBox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgLightModeBeside, imgLightModeCenter, imgLightModeAll};
        lightModeCheckBox = new CheckBox(1, views1);
        new CheckBoxClickListener(lightModeCheckBox);

        //设定闪烁模式checkbox组合的点击事件
        ImageView[] views2 = new ImageView[]{imgBlinkModeNone, imgBlinkModeSlow, imgBlinkModeFast,};
        blinkModeCheckBox = new CheckBox(1, views2);
        new CheckBoxClickListener(blinkModeCheckBox);
    }


    @OnClick({R.id.layout_cancel, R.id.img_help, R.id.btn_begin})
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
                if (groupSize <= 0) {
                    Toast.makeText(this, "未选择分组,不能开始!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (trainingFlag)
                    stopTraining();
                else
                    startTraining();
                break;
            case R.id.img_help:
                Intent intent = new Intent(this, HelpActivity.class);
                intent.putExtra("flag", 7);
                startActivity(intent);
                break;
        }
    }

    private void startTraining() {
        btnBegin.setText("停止");
        trainingFlag = true;

        //初始化每组的设备列表
        for (int i = 0; i < 2; i++) {
            lightInfos[i] = new ArrayList<>();
            for (int j = 0; j < groupSize; j++) {
                GroupLightInfo lightInfo = new GroupLightInfo();
                //设备编号
                lightInfo.deviceInfo = Device.DEVICE_LIST.get(i * groupSize + j);
                //0表示该是还未亮起
                lightInfo.lightFlag = 0;
                lightInfos[i].add(lightInfo);
            }
        }

        groupOneAdapter = new ConfrontationAdapter(this, 0, lightInfos[0]);
        hlvGroup1.setAdapter(groupOneAdapter);

        groupTwoAdapter = new ConfrontationAdapter(this, 1, lightInfos[1]);
        hlvGroup2.setAdapter(groupTwoAdapter);

        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();

        //开启接收时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        int position1 = createRandomNum(0);//从第一组还未亮起的灯中随机抽取一个
        GroupLightInfo temp = lightInfos[0].get(position1);
        //lightFlag为1表示设备亮正常颜色
        temp.lightFlag = 1;
        //亮蓝灯
        turnOnLight(position1, 0, 1);

        int position2 = createRandomNum(0);
        temp = lightInfos[1].get(position2);
        temp.lightFlag = 1;
        turnOnLight(position2, 1, 1);

        timer = new Timer(handler);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();

        groupOneAdapter.notifyDataSetChanged();
        groupOneAdapter.notifyDataSetChanged();
    }

    private void stopTraining() {
        btnBegin.setText("开始");
        trainingFlag = false;
        ReceiveThread.stopThread();
        device.turnOffAllTheLight();
        timer.stopTimer();
    }

    //从每组中还未亮起灯的设备中随机抽取一个
    public int createRandomNum(int groupNum) {
        Random random = new Random();
        while (true) {
            int rand = random.nextInt(100) % groupSize;//0-groupSize-1

            int lightFlg = lightInfos[groupNum].get(rand).lightFlag;
            if (lightFlg == 0)
                return rand;
        }
    }

    private void turnOnLight(int position, int groupId, int lightFlag) {

        position = groupId * groupSize + position;
        int color = 0;
        if (lightFlag == 2)
            color = 3; //品红
        else if (lightFlag == 3) {
            //不能挥灭的灯
            if (groupId == 0)
                color = 2;
            else
                color = 1;

        } else    //第一组亮蓝色  第二组亮红色
            color = groupId + 1;


        if (lightFlag == 3)
            device.sendOrder(Device.DEVICE_LIST.get(position).getDeviceNum(),
                    Order.LightColor.values()[color],
                    Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                    Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                    Order.LightModel.OUTER,
                    Order.ActionModel.TURN_OFF,
                    Order.EndVoice.NONE);
        else
            device.sendOrder(Device.DEVICE_LIST.get(position).getDeviceNum(),
                    Order.LightColor.values()[color],
                    Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                    Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                    Order.LightModel.OUTER,
                    Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                    Order.EndVoice.NONE);
    }

    private void analyzeData(String data) {
        List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
        for (TimeInfo info : infos) {
            //该设备属于哪一组,并且在该组的位置
            int position = findPosition(info.getDeviceNum());
            int groupId = position / groupSize;
            position = position - groupId * groupSize;
            Log.d(Constant.LOG_TAG, position + "");
            //找到了设备在每组中的具体位置
            if (lightInfos[groupId].get(position).deviceInfo.getDeviceNum() == info.getDeviceNum()) {
                int flag = lightInfos[groupId].get(position).lightFlag;
                lightInfos[groupId].get(position).lightFlag = 0;
                //2 表示该灯是对方将我方的灯点亮的
                //1 系统随机点亮的
                if (flag == 1) {
                    //判断另一组设备灯是否全部都亮了
                    if (isGroupLightsOn((groupId + 1) % 2)) {
                        stopTraining();
                        //全亮 表示另一组输了
                        endingMovie(groupId);
                        break;
                    } else {
                        int rand1 = createRandomNum(groupId);
                        lightInfos[groupId].get(rand1).lightFlag = 1;
                        turnOnLight(rand1, groupId, 1);

                        int groupId2 = (groupId + 1) % 2;
                        int rand2 = createRandomNum(groupId2);
                        lightInfos[groupId2].get(rand2).lightFlag = 2;
                        lightInfos[groupId2].get(rand2).time = timer.time;
                        turnOnLight(rand2, groupId2, 2);
                    }
                }
                groupOneAdapter.notifyDataSetChanged();
                groupTwoAdapter.notifyDataSetChanged();

            }

        }

    }

    //查找设备在哪个组
    private int findPosition(char num) {
        for (int i = 0; i < Device.DEVICE_LIST.size(); i++)
            if (Device.DEVICE_LIST.get(i).getDeviceNum() == num)
                return i;
        return 0;
    }

    //判断一组设备是否全部设备都已经亮了
    private boolean isGroupLightsOn(int groupId) {
        for (int i = 0; i < groupSize; i++) {
            if (lightInfos[groupId].get(i).lightFlag == 0)
                return false;
        }
        return true;
    }

    //结束动画
    private void endingMovie(int groupId) {
        int temp = 0;
        while (temp < 5) {
            for (int i = 0; i < groupSize * 2; i++)
                device.sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                        Order.LightColor.values()[groupId + 1],
                        Order.VoiceMode.NONE,
                        Order.BlinkModel.NONE,
                        Order.LightModel.OUTER,
                        Order.ActionModel.NONE,
                        Order.EndVoice.NONE);

            Timer.sleep(100);
            for (int i = 0; i < groupSize * 2; i++)
                device.sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                        Order.LightColor.NONE,
                        Order.VoiceMode.NONE,
                        Order.BlinkModel.NONE,
                        Order.LightModel.TURN_OFF,
                        Order.ActionModel.NONE,
                        Order.EndVoice.NONE);
            Timer.sleep(100);
            temp++;
        }
    }

    public class GroupLightInfo {
        public int lightFlag;
        public int time;
        public DeviceInfo deviceInfo;

    }

}

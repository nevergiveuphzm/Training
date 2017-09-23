package com.oucb303.training.activity;

import android.app.Dialog;
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
import com.oucb303.training.adpter.LargeRecessAdapter;
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
import com.oucb303.training.utils.Battery;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.utils.DialogUtils;
import com.oucb303.training.utils.OperateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bai ChangCai on 2017/8/24.
 * 自抛自接：
 *      固定时间内扫灭的次数
 */
public class CourbetteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


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
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    android.widget.CheckBox cbVoice;
    android.widget.CheckBox cbEndVoice;
    @Bind(R.id.img_set)
    ImageView imgSet;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.btn_stop)
    Button btnStop;
    @Bind(R.id.img_start)
    TextView imgSatart;

    private Dialog set_dialog;
    private Device device;
    private int level=2;
    private int maxGroupNum;//做多分组数目
    private int goupNum;//分组数量
    private int trainingTime;//训练时间 单位毫秒
    private Timer timer;//计时器
    private final int groupSize = 1;//每组所需设备个数
    private GroupListViewAdapter groupListViewAdapter;
    private LargeRecessAdapter largeRecessAdapter;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightColorCheckBox, lightModeCheckBox,blinkModeCheckBox;
    private boolean isTraining = false;//是否正在训练标志
    //每组在规定时间内，完成的训练次数
    private int[] completedTimes;
    //存放所有的map,map存的是：key值是组号，value值是每次灭灯所用时间
    private List<Map<String, Integer>> list_finishTime = new ArrayList<>();

    //上一次完成训练的时间
    private int[] currentTime;

    private final int TIME_RECEIVE = 1;
    private final int POWER_RECEIVE = 2;
    private final int UPDATE_TIMES = 3;
    private final int STOP_TRAINING = 4;
    Battery battery;

    private Handler handler = new Handler() {
        //处理接收过来的数据的方法
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //更新计时
                case Timer.TIMER_FLAG:
//                    tvTotalTime.setText(msg.obj.toString());
                    //判断结束
                    if (timer.time >= trainingTime) {
                        stopTraining();
                        return;
                    }
                    break;
                //接收到返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7)
                        analyzeTimeData(data);
                    break;
                //更新完成次数
                case UPDATE_TIMES:
                    largeRecessAdapter.notifyDataSetChanged();
                    break;
                case Timer.TIMER_DOWN:
                    tvTotalTime.setText("倒计时："+msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courbette1);
        ButterKnife.bind(this);
        initView();
        device = new Device(this);
        //更新连接设备列表
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if (device.devCount > 0) {
            device.connect(this);
            device.initConfig();
        }
        battery = new Battery(device,imgSatart,handler_battery );
    }

    @Override
    protected void onStart() {
        super.onStart();
        imgSaveNew.setEnabled(false);
        set_dialog = createLightSetDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        device.disconnect();
    }

    private void initView() {
        tvTitle.setText(R.string.navigation_juggling_item1);
        imgHelp.setVisibility(View.VISIBLE);
        imgSaveNew.setVisibility(View.VISIBLE);

        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //初始化训练分组下拉框
        maxGroupNum = Device.DEVICE_LIST.size();

        String[] groupNumChoose = new String[maxGroupNum + 1];
        groupNumChoose[0] = "";
        for (int i = 1; i <= maxGroupNum; i++) {
            groupNumChoose[i] = (i + "组");
        }
        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(CourbetteActivity.this, spGroupNum, groupNumChoose) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                goupNum = i;
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });

        barTrainingTime.setProgress(level);

        //训练时间拖动条初始化
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 10));
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));

        //初始化左侧分组listView
        groupListViewAdapter = new GroupListViewAdapter(CourbetteActivity.this, groupSize);
        lvGroup.setAdapter(groupListViewAdapter);



        //初始化右侧listView
        largeRecessAdapter = new LargeRecessAdapter(this);
        lvTimes.setAdapter(largeRecessAdapter);
        lvTimes.setOnItemClickListener(this);


    }


    //    position:用户当前点击列表项传过来的位置
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("flag", 1);

        Bundle bundle = new Bundle();
        //传的值是所有组号的map
        bundle.putSerializable("list_detail", (Serializable) list_finishTime);
        //点击的组号
        bundle.putInt("groupId", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @OnClick({R.id.layout_cancel, R.id.img_help, R.id.btn_begin, R.id.img_save_new, R.id.btn_on,
            R.id.btn_off,R.id.img_set,R.id.btn_stop,R.id.img_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_set:
                set_dialog = createLightSetDialog();
                OperateUtils.setScreenWidth(this, set_dialog, 0.95, 0.7);
                set_dialog.show();
                break;
            case R.id.layout_cancel:
                this.finish();
                device.turnOffAllTheLight();
                break;
            case R.id.img_help:
                List<Integer> list = new ArrayList<>();
                list.add(R.string.courbette_training_method);
                list.add(R.string.courbette_training_standard);
                Dialog dialog_help = DialogUtils.createHelpDialog(CourbetteActivity.this,list);
                OperateUtils.setScreenWidth(this, dialog_help, 0.95, 0.7);
                dialog_help.show();
                break;
            case R.id.img_save_new:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 6:大课间跑圈，八秒钟跑 ...
                bundle.putString("trainingCategory", "6");
                bundle.putString("trainingName", "自抛自接");//项目名称
                bundle.putInt("totalTimes", 0);//总次数
                bundle.putInt("deviceNum", goupNum);//设备个数
                bundle.putIntArray("scores", completedTimes);//得分
                bundle.putInt("totalTime", trainingTime);//训练总时间
                bundle.putInt("groupNum", goupNum);//分组数
                it.putExtras(bundle);
                startActivity(it);
                break;
            case R.id.btn_stop:
                if(isTraining){
                    stopTraining();

                }
                break;
            case R.id.btn_begin:
                if (!device.checkDevice(CourbetteActivity.this))
                    return;
                if (goupNum == 0) {
                    Toast.makeText(this, "请选择训练分组", Toast.LENGTH_LONG).show();
                    return;
                }
                if (isTraining) {
                    stopTraining();


                }
                else
                    starTraining();
                break;
            case R.id.btn_on:
                //goupNum组数，1：每组设备个数，0：类型
                device.turnOnButton(goupNum, 1, 0);
                btnOn.setClickable(false);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                btnOn.setClickable(true);
                break;
            case R.id.img_start:
                battery.initDevice();
                break;

        }
    }

    //开始训练
    public void starTraining() {
        isTraining = true;
        //finishTime = new ArrayList<Integer>();
        btnOn.setClickable(false);
        btnOff.setClickable(false);
        currentTime = new int[goupNum];

        completedTimes = new int[goupNum];
        for (int i = 0; i < goupNum; i++) {
            completedTimes[i] = 0;
        }
        for (int i = 0; i < goupNum; i++) {
            currentTime[i] = (int) System.currentTimeMillis();
        }
        list_finishTime.clear();
        largeRecessAdapter.setCompletedTimes(completedTimes);
        largeRecessAdapter.notifyDataSetChanged();


        //训练时间
        trainingTime = (int) (new Double(tvTrainingTime.getText().toString()) * 60 * 1000);
        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();
        //开启接收设备返回时间的监听线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();
        //开全灯
        for (int i = 0; i < goupNum; i++) {
          sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum());
        }
        timer = new Timer(handler, trainingTime);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();
    }

    //结束训练
    public void stopTraining() {
        isTraining = false;

        imgSaveNew.setEnabled(true);
        //结束时间线程
        timer.stopTimer();
        //结束接收返回时间线程
        ReceiveThread.stopThread();
        device.turnOffAllTheLight();
        btnOn.setClickable(true);
        btnOff.setClickable(true);
    }

    public void turnOnLight(final char deviceNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer.sleep(500);
                if (!isTraining)
                    return;
               sendOrder(deviceNum);
            }
        }).start();
    }

    public void sendOrder(char deviceNum) {
        device.sendOrder(deviceNum, Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                Order.BlinkModel.values()[blinkModeCheckBox.getCheckId() - 1],
                Order.LightModel.OUTER,
                Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                Order.EndVoice.NONE);
    }
    //解析饭回来的数据,灭一次解析一次，如果同时灭多盏灯，就是解析一次,infos里存放同时灭灯个数
    public void analyzeTimeData(final String data) {
        if (!isTraining)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //编号和时间
                List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
//                Log.i("mmmmmmmmmmmmmmmmmmm", "所点击的灭灯次数---" + infos.size());
                for (TimeInfo info : infos) {
                    int groupId = findDeviceGroupId(info.getDeviceNum());
                    //如果设备组号大于分组数肯定是错误的
                    if (groupId > goupNum)
                        continue;
                    Log.d(Constant.LOG_TAG, info.getDeviceNum() + " groupId " + groupId);
                    completedTimes[groupId] += 1;

                    //map_finishTime存的是组号，和这个组这一次所用的时间
                    Map<String, Integer> map_finishTime = new HashMap<>();
                    map_finishTime.put(String.valueOf(groupId), (int) (System.currentTimeMillis() - currentTime[groupId]));

                    list_finishTime.add(map_finishTime);

                    //上一次的当前时间
                    currentTime[groupId] = (int) (System.currentTimeMillis());
                    turnOnLight(info.getDeviceNum());
                }
                Message msg = Message.obtain();
                msg.what = UPDATE_TIMES;
                msg.obj = "";
                handler.sendMessage(msg);
            }
        }).start();
    }

    //查找设备属于第几组
    //DEVICE_LIST存放DeviceInfo，DeviceInfo存放编号，电量，短地址
    public int findDeviceGroupId(char deviceNum) {
        int position = 0;
        //遍历
        for (int i = 0; i < Device.DEVICE_LIST.size(); i++) {
            if (Device.DEVICE_LIST.get(i).getDeviceNum() == deviceNum) {
                position = i;
                break;
            }
        }
        return position;
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
        ImageView imgLightModeBeside=(ImageView)layout.findViewById(R.id.img_light_mode_beside);
        ImageView imgLightModeCenter=(ImageView)layout.findViewById(R.id.img_light_mode_center);
        ImageView imgLightModeAll=(ImageView)layout.findViewById(R.id.img_light_mode_all);
        cbVoice = (android.widget.CheckBox) layout.findViewById(R.id.cb_voice);
        Button btnOk = (Button) layout.findViewById(R.id.btn_ok);
        Button btnCloseSet = (Button) layout.findViewById(R.id.btn_close_set);
        final Dialog dialog = new Dialog(this, R.style.dialog_rank);

        dialog.setContentView(layout);

        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new CheckBoxClickListener(actionModeCheckBox);
        ImageView[] views1 = new ImageView[]{imgLightModeBeside, imgLightModeCenter, imgLightModeAll};
        lightModeCheckBox = new CheckBox(1, views1);
        new CheckBoxClickListener(lightModeCheckBox);
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

    Handler handler_battery = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    maxGroupNum = Device.DEVICE_LIST.size();

                    String[] groupNumChoose = new String[maxGroupNum + 1];
                    groupNumChoose[0] = "";
                    for (int i = 1; i <= maxGroupNum; i++) {
                        groupNumChoose[i] = (i + "组");
                    }
                    spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(CourbetteActivity.this, spGroupNum, groupNumChoose) {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            super.onItemSelected(adapterView, view, i, l);
                            goupNum = i;
                            groupListViewAdapter.setGroupNum(i);
                            groupListViewAdapter.notifyDataSetChanged();
                        }
                    });

                    break;
                default:
                    break;
            }
        }
    };
}

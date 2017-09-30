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
import com.oucb303.training.adpter.SitUpsTimeListAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.dialugue.CustomDialog;
import com.oucb303.training.entity.SitUps;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.listener.SpinnerItemSelectedListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.model.PowerInfoComparetor;
import com.oucb303.training.model.TimeInfo;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.Battery;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.utils.DialogUtils;
import com.oucb303.training.utils.OperateUtils;

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
 * 快速蹲起
 */

public class QuickSquatActivity extends AppCompatActivity {

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
    @Bind(R.id.img_start)
    TextView imgSatart;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    //    @Bind(R.id.tv_deviceNum)
//    TextView tvDeviceNum;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.sp_dev_num)
    Spinner spDevNum;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;

    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.img_set)
    ImageView imgSet;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.img_save_new)
    ImageView imgSaveNew;
    @Bind(R.id.btn_result)
    Button btnResult;

//    @Bind(R.id.btn_history_result)
//    Button btnHistoryResult;

    private Device device;
    private final int TIME_RECEIVE = 1, POWER_RECEIVER = 2, UPDATE_DATA = 3;
    //训练时间  单位毫秒
    private int trainingTime;
    //计时器
    private Timer timer;
    //是否正在训练标志
    private boolean isTraining = false;
    //每组的设备数量  最大分组数  组数
    private int groupSize=2 , maxGroupNum, groupNum;
    private GroupListViewAdapter groupListViewAdapter;
    private SitUpsTimeListAdapter sitUpsTimeListAdapter;
    //训练成绩
    private int[] scores;
    private int floag=1;
    private ArrayAdapter<String> adapterDeviceNum;
    //key : 组号 value： 成绩
    private Map<Integer, Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;
    //设置返回数据
    int[] Setting_return_data = new int[5];
    private int type = 1;
    private Context context;
    private int level=2;
    Battery battery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quicksquat);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 1);//当获取不到值的时候是type= 1
        initView();
        device = new Device(this);
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if (device.devCount > 0) {
            device.connect(this);
            device.initConfig();
        }
        battery = new Battery(device,imgSatart,handler_battery );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        device.disconnect();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Timer.TIMER_FLAG:
                    if (timer.time >= trainingTime) {
                        stopTraining();
                        return;
                    }
                    break;
                case Timer.TIMER_DOWN:
                    tvTotalTime.setText("倒计时："+msg.obj.toString());
                    break;
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7)
                        analyzeTimeData(data);
                    break;
                case UPDATE_DATA:
                    sortTime(timeMap);
                    sitUpsTimeListAdapter.setTimeMap(timeMap, keyId);
                    sitUpsTimeListAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };


    private void initView() {
        tvTitle.setText("快速蹲起");
//        tvDeviceNum.setText(groupSize+"个");

        imgHelp.setVisibility(View.VISIBLE);
        imgSaveNew.setVisibility(View.VISIBLE);
        ///初始化分组listView
        groupListViewAdapter = new GroupListViewAdapter(QuickSquatActivity.this, groupSize);
        lvGroup.setAdapter(groupListViewAdapter);


        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());


        String[] lightNum = new String[2];

        for (int i = 0; i < 2; i++) {
            lightNum[i] = (i * 2 + 2) + "个";

        }
        adapterDeviceNum = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lightNum);
        spDevNum.setAdapter(adapterDeviceNum);
        spDevNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(this, spDevNum, lightNum) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groupSize=i*2+2;
                int totalGroupNum = Device.DEVICE_LIST.size() / groupSize;
                String[] trainGroupNum = new String[totalGroupNum + 1];
                trainGroupNum[0] = "";
                for (int j = 1; j <= totalGroupNum; j++)
                    trainGroupNum[j] = j + "组";
                spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(QuickSquatActivity.this,spGroupNum,trainGroupNum) {
                                                         @Override
                                                         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                             super.onItemSelected(adapterView, view, i, l);
                                                             groupNum = i;

                                                             groupListViewAdapter.setGroupSize(groupSize);
                                                             groupListViewAdapter.setGroupNum(groupNum);
                                                             groupListViewAdapter.notifyDataSetChanged();
                                                         }
                                                     }
                );
//                ArrayAdapter<String> adapterGroupNum = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, trainGroupNum);
//                adapterGroupNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spGroupNum.setAdapter(adapterGroupNum);
            }
        });
//        spGroupNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                groupNum = position;
//
//                groupListViewAdapter.setGroupSize(groupSize);
//                groupListViewAdapter.setGroupNum(groupNum);
//                groupListViewAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

//        maxGroupNum = Device.DEVICE_LIST.size() / groupSize;
//        String[] groupNumChoose = new String[maxGroupNum + 1];
//        groupNumChoose[0] = " ";
//        for (int i = 1; i <= maxGroupNum; i++)
//            groupNumChoose[i] = i + " 组";
//        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(QuickSquatActivity.this, spGroupNum, groupNumChoose) {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                groupNum = i;
//                groupListViewAdapter.setGroupNum(i);
//                groupListViewAdapter.notifyDataSetChanged();
//            }
//        });







        //训练时间拖动条初始化
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 10));
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));


        //初始化右侧listview
        sitUpsTimeListAdapter = new SitUpsTimeListAdapter(this);
        lvTimes.setAdapter(sitUpsTimeListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        imgSaveNew.setEnabled(false);
        Setting_return_data[0]=0;
        Setting_return_data[1]=0;
        Setting_return_data[2]=0;
        Setting_return_data[3]=1;
        Setting_return_data[4]=1;

    }

    @OnClick({R.id.layout_cancel, R.id.btn_begin, R.id.btn_stop, R.id.img_help, R.id.btn_on,R.id.img_start,
            R.id.btn_off, R.id.img_save_new, R.id.img_set, R.id.btn_result})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_set:
                CustomDialog dialog = new  CustomDialog(QuickSquatActivity.this,"From btn 2",new CustomDialog.ICustomDialogEventListener() {
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
                operateUtils.setScreenWidth(QuickSquatActivity.this,dialog, 0.95, 0.7);
                break;
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
                if (isTraining)
                    stopTraining();
                else
                    startTraining();
                break;
            case R.id.btn_stop:
                if(isTraining)
                    stopTraining();
                break;
            case R.id.img_help:
                List<Integer> list = new ArrayList<>();
                list.add(R.string.quicksquat_training_method);
                list.add(R.string.quicksquat_training_standard);
                Dialog dialog_help = DialogUtils.createHelpDialog(QuickSquatActivity.this,list);
                OperateUtils.setScreenWidth(this, dialog_help, 0.95, 0.7);
                dialog_help.show();
                break;
            case R.id.btn_on:
                //groupNum组数，groupSize：每组设备个数，1：类型
                device.turnOnButton(groupNum, groupSize, 1);
                btnOn.setClickable(false);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                btnOn.setClickable(true);
                break;
            case R.id.img_save_new:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐、交替活动 ...
                bundle.putString("trainingName", "快速蹲起");
                bundle.putString("trainingCategory", "3");
                //训练总时间
                bundle.putInt("trainingTime", trainingTime);
                //次数
                bundle.putIntArray("scores", scores);
                it.putExtras(bundle);
                startActivity(it);
                break;
            case R.id.btn_result:
                lvTimes.setVisibility(View.VISIBLE);
                btnResult.setTextColor(this.getResources().getColor(R.color.ui_green));

                break;

            case R.id.img_start:
                battery.initDevice();
                break;
        }
    }

    private void startTraining() {
        btnOn.setClickable(false);
        btnOff.setClickable(false);
//        imgSave.setEnabled(true);
        isTraining = true;

        scores = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            timeMap.put(i, 0);
        }
        keyId = new int[groupNum];

        sitUpsTimeListAdapter.setScores(scores);
        sitUpsTimeListAdapter.setTimeMap(timeMap, keyId);
        sitUpsTimeListAdapter.notifyDataSetChanged();
        //训练时间
        trainingTime = (int) (new Double(tvTrainingTime.getText().toString()) * 60 * 1000);

        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();

        //开启接受时间线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        //亮每组设备的第一个灯
        if (groupSize==2) {
            for (int i = 0; i < groupNum; i++) {
                sendOrder(Device.DEVICE_LIST.get(i * 2).getDeviceNum());
            }
        }
        else {
            for (int i = 0; i < groupNum; i++) {
                sendOrder(Device.DEVICE_LIST.get(i * 4).getDeviceNum());
                sendOrder(Device.DEVICE_LIST.get(i * 4 + 1).getDeviceNum());
            }
        }

        timer = new Timer(handler, trainingTime);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();
    }

    private void stopTraining() {
        btnOn.setClickable(true);
        btnOff.setClickable(true);
        isTraining = false;
        imgSaveNew.setEnabled(true);
        //结束时间线程
        timer.stopTimer();
        device.turnOffAllTheLight();
        timer.sleep(200);
        //结束接收时间线程
        ReceiveThread.stopThread();
    }

    //解析时间
    private void analyzeTimeData(final String data) {
        //训练已结束
        if (!isTraining)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
                if (groupSize==2) {
                    for (TimeInfo info : infos) {
                        int groupId = findDeviceGroupId(info.getDeviceNum());
                        Log.d("getDeviceNum----------", "" + info.getDeviceNum());
                        Log.d("groupID---------------", "" + groupId);
                        char next = Device.DEVICE_LIST.get(groupId * groupSize).getDeviceNum();
                        if (next == info.getDeviceNum()) {
                            next = Device.DEVICE_LIST.get(groupId * groupSize + 1).getDeviceNum();
                            scores[groupId] += 1;
                            timeMap.put(groupId, scores[groupId]);
                        }
                        sendOrder(next);
//                    sendOrder(Device.DEVICE_LIST.get(groupId*groupSize+2).getDeviceNum());
                    }
                }
                else {
                    for (TimeInfo info : infos) {
                        int groupId = findDeviceGroupId(info.getDeviceNum());
                        Log.d("getDeviceNum----------", "" + info.getDeviceNum());
                        Log.d("groupID---------------", "" + groupId);
                        char next = Device.DEVICE_LIST.get(groupId * groupSize).getDeviceNum();
                        char next1 = Device.DEVICE_LIST.get(groupId * groupSize + 1).getDeviceNum();
                        if (next == info.getDeviceNum() || next1 == info.getDeviceNum()) {

                            next = Device.DEVICE_LIST.get(groupId * groupSize + 2).getDeviceNum();
                            next1 = Device.DEVICE_LIST.get(groupId * groupSize + 3).getDeviceNum();
                            scores[groupId] += 1;
                            timeMap.put(groupId, scores[groupId]);
                        }
                        device.turnOffAllTheLight();

                        sendOrder(next);
                        sendOrder(next1);
                    }
                }

                Message msg = Message.obtain();
                msg.obj = "";
                msg.what = UPDATE_DATA;
                handler.sendMessage(msg);

            }
        }).start();

    }

    public void sendOrder(char deviceNum) {
        device.sendOrder(deviceNum, Order.LightColor.values()[Setting_return_data[3]],
                Order.VoiceMode.NONE,
                Order.BlinkModel.NONE,
                Order.LightModel.OUTER,
                Order.ActionModel.values()[Setting_return_data[4]],
                Order.EndVoice.values()[Setting_return_data[0]]);

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

    //排序
    public void sortTime(Map<Integer, Integer> timeMap) {
        List<Map.Entry<Integer, Integer>> list = new ArrayList<Map.Entry<Integer, Integer>>(timeMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            //升序排序
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int i = 0;
        for (Map.Entry<Integer, Integer> mapping : list) {
            Log.i("============", "" + mapping.getKey() + ":" + mapping.getValue());
//            System.out.println(mapping.getKey()+":"+mapping.getValue());
            keyId[i] = mapping.getKey();
            i++;
        }
    }
    Handler handler_battery = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Device.DEVICE_LIST =(ArrayList<DeviceInfo>)msg.obj;
                    Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

                    //初始化分组下拉框
                    maxGroupNum = Device.DEVICE_LIST.size() / groupSize;
                    String[] groupNumChoose = new String[maxGroupNum + 1];
                    groupNumChoose[0] = " ";
                    for (int i = 1; i <= maxGroupNum; i++)
                        groupNumChoose[i] = i + " 组";
                    spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(QuickSquatActivity.this, spGroupNum, groupNumChoose) {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            groupNum = i;
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
package com.oucb303.training.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
 * 仰卧起坐
 */

public class SitUpsActivity extends AppCompatActivity {

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
    //    @Bind(R.id.img_action_mode_touch)
//    ImageView imgActionModeTouch;
//    @Bind(R.id.img_action_mode_light)
//    ImageView imgActionModeLight;
//    @Bind(R.id.img_action_mode_together)
//    ImageView imgActionModeTogether;
//    @Bind(R.id.img_light_mode_center)
//    ImageView imgLightModeCenter;
//    @Bind(R.id.img_light_mode_all)
//    ImageView imgLightModeAll;
//    @Bind(R.id.img_light_mode_beside)
//    ImageView imgLightModeBeside;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    //    @Bind(R.id.sv_container)
//    ScrollView svContainer;
    @Bind(R.id.lv_times)
    ListView lvTimes;
    //    @Bind(R.id.img_light_color_blue)
//    ImageView imgLightColorBlue;
//    @Bind(R.id.img_light_color_red)
//    ImageView imgLightColorRed;
//    @Bind(R.id.img_light_color_blue_red)
//    ImageView imgLightColorBlueRed;
//    @Bind(R.id.cb_voice)
    android.widget.CheckBox cbVoice;
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
    @Bind(R.id.btn_history_result)
    Button btnHistoryResult;


//    @Bind(R.id.lightMode_checkBox)
//    LinearLayout lightModecheckBox;
//    @Bind(R.id.img_level_sub)
//    ImageView imgLevelsub;
//    @Bind(R.id.img_level_add)
//    ImageView imgLevelAdd;
//    @Bind(R.id.bar_level)
//    SeekBar barLevel;
//    @Bind(R.id.tv_level)
//    TextView tvLevel;
//    @Bind(R.id.ll_level)
//    LinearLayout lLevel;

//    @Bind(R.id.blinkMode_checkBox)
//    LinearLayout blinkModecheckBox;
//    @Bind(R.id.ll_params)
//    LinearLayout llParams;
//    @Bind(R.id.img_blink_mode_none)
//    ImageView imgBlinkModeNone;
//    @Bind(R.id.img_blink_mode_slow)
//    ImageView imgBlinkModeSlow;
//    @Bind(R.id.img_blink_mode_fast)
//    ImageView imgBlinkModeFast;

    private Device device;
    private final int TIME_RECEIVE = 1, POWER_RECEIVER = 2, UPDATE_DATA = 3;
    //训练时间  单位毫秒
    private int trainingTime;
    //计时器
    private Timer timer;
    //是否正在训练标志
    private boolean isTraining = false;
    //每组的设备数量  最大分组数  组数
    private int groupSize = 2, maxGroupNum, groupNum;
    private GroupListViewAdapter groupListViewAdapter;
    private SitUpsTimeListAdapter sitUpsTimeListAdapter;
    //训练成绩
    private int[] scores;
    //key : 组号 value： 成绩
    private Map<Integer, Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;
    //设置返回数据
    int[] Setting_return_data = new int[5];
    private int type = 1;

    private int level=2;
    private Dialog set_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situps);
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
//                    tvTotalTime.setText("训练总时间："+msg.obj.toString());

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
        if (type == 0) {
            tvTitle.setText("交替活动");
//            lightModecheckBox.setVisibility(View.GONE);
//            lLevel.setVisibility(View.GONE);
        } else {
            tvTitle.setText("仰卧起坐训练");
//            lLevel.setVisibility(View.VISIBLE);
        }

        imgHelp.setVisibility(View.VISIBLE);
        imgSaveNew.setVisibility(View.VISIBLE);
        ///初始化分组listView
        groupListViewAdapter = new GroupListViewAdapter(SitUpsActivity.this, groupSize);
        lvGroup.setAdapter(groupListViewAdapter);
        //解决listView 与scrollView的滑动冲突
//        lvGroup.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                //从listView 抬起时将控制权还给scrollview
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
//                    svContainer.requestDisallowInterceptTouchEvent(false);
//                else
//                    svContainer.requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //初始化分组下拉框
        maxGroupNum = Device.DEVICE_LIST.size() / 2;
        String[] groupNumChoose = new String[maxGroupNum + 1];
        groupNumChoose[0] = " ";
        for (int i = 1; i <= maxGroupNum; i++)
            groupNumChoose[i] = i + " 组";
        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(SitUpsActivity.this, spGroupNum, groupNumChoose) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groupNum = i;
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });
        //初始化训练强度拖动条
//        barLevel.setOnSeekBarChangeListener(new MySeekBarListener(barTrainingTime,tvLevel, 2));
//        imgLevelsub.setOnTouchListener(new AddOrSubBtnClickListener(barLevel, 0));
//        imgLevelAdd.setOnTouchListener(new AddOrSubBtnClickListener(barLevel, 1));
        //训练时间拖动条初始化
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 10));
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));
//        switch (level) {
//            case 1:
//                level = 2;
//                break;
//            case 2:
//                level = 4;
//                break;
//            case 3:
//                level = 10;
//                break;
//        }
//        Log.d(Constant.LOG_TAG, level + "ddd");
//        barTrainingTime.setProgress(level);

        //初始化右侧listview
        sitUpsTimeListAdapter = new SitUpsTimeListAdapter(this);
        lvTimes.setAdapter(sitUpsTimeListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        imgSaveNew.setEnabled(false);
//        imgSave.setEnabled(false);
        for(int i=0;i<3;i++)
          Setting_return_data[i]=1;
        Setting_return_data[4]=0;
        Setting_return_data[3]=0;
    }

    @OnClick({R.id.layout_cancel, R.id.btn_begin, R.id.btn_stop, R.id.img_help, R.id.btn_on,
            R.id.btn_off, R.id.img_save_new, R.id.img_set, R.id.btn_result, R.id.btn_history_result})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_set:
                CustomDialog dialog = new  CustomDialog(SitUpsActivity.this,"From btn 2",new CustomDialog.ICustomDialogEventListener() {
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
                operateUtils.setScreenWidth(SitUpsActivity.this,dialog,0.97,0.8);
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
                stopTraining();
                break;
            case R.id.img_help:
                List<Integer> list = new ArrayList<>();
                list.add(R.string.situp_training_method);
                list.add(R.string.situp_training_standard);
                Dialog dialog_help = DialogUtils.createHelpDialog(SitUpsActivity.this,list);
                OperateUtils.setScreenWidth(this, dialog_help, 0.95, 0.7);
                dialog_help.show();
                break;
            case R.id.btn_on:
                //groupNum组数，groupSize：每组设备个数，1：类型
                device.turnOnButton(groupNum, groupSize, 1);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                break;
            case R.id.img_save_new:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐、交替活动 ...
                if (type == 0) {
                    bundle.putString("trainingName", "交替活动");
                } else {
                    bundle.putString("trainingName", "仰卧起坐");
                }
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
                btnHistoryResult.setTextColor(this.getResources().getColor(R.color.white));
                break;
            case R.id.btn_history_result:
                lvTimes.setVisibility(View.INVISIBLE);
                btnResult.setTextColor(this.getResources().getColor(R.color.white));
                btnHistoryResult.setTextColor(this.getResources().getColor(R.color.ui_green));
                break;
        }
    }

    private void startTraining() {
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
        for (int i = 0; i < groupNum; i++) {
            sendOrder(Device.DEVICE_LIST.get(i * 2).getDeviceNum());
        }
        timer = new Timer(handler, trainingTime);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();
    }

    private void stopTraining() {
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
                }
                Message msg = Message.obtain();
                msg.obj = "";
                msg.what = UPDATE_DATA;
                handler.sendMessage(msg);

            }
        }).start();

    }

    public void sendOrder(char deviceNum) {
        device.sendOrder(deviceNum, Order.LightColor.values()[Setting_return_data[2]],
                Order.VoiceMode.values()[Setting_return_data[4]],
                Order.BlinkModel.values()[Setting_return_data[1]],
                Order.LightModel.OUTER,
                Order.ActionModel.values()[Setting_return_data[2]],
                Order.EndVoice.NONE);
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


}

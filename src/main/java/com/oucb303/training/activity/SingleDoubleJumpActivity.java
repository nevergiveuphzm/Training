package com.oucb303.training.activity;

import android.app.Dialog;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.adpter.SingleDoubleJumpAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.dialugue.CustomDialog;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.listener.SpinnerItemSelectedListener;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.model.PowerInfoComparetor;
import com.oucb303.training.model.TimeInfo;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.threads.Timer;
import com.oucb303.training.utils.Battery;
import com.oucb303.training.utils.Constant;
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
 * Created by john on 2017/9/25.
 */

public class SingleDoubleJumpActivity extends AppCompatActivity {

    @Bind(R.id.img_set)
    ImageView imgSet;
    @Bind(R.id.tv_training_time)
    TextView tvTrainingTime;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save_new)
    ImageView imgSaveNew;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.sp_training_times)
    Spinner spTrainingTimes;
    @Bind(R.id.lv_times)
    ListView lvTimes;


    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;


    android.widget.CheckBox cbVoice;
    android.widget.CheckBox cbEndVoice;

    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.img_start)
    TextView imgSatart;



    @Bind(R.id.btn_result)
    Button btnResult;

    private Device device;
    //最多分组数目
    private int maxGroupNum;
    //分组数量
    private int groupNum;
    //总的训练次数
    private int trainingTimes;
    //每组所需设备个数为1
    private int groupSize=6;
    private GroupListViewAdapter groupListViewAdapter;
    private SingleDoubleJumpAdapter singleDoubleJumpAdapter;

    //训练开始标志
    private boolean trainingBeginFlag = false;

    //每组完成的训练次数
    private int[] completedTimes;
    //每组完成训练所用时间
    private int[] finishTimes;
    //
    int[] Setting_return_data = new int[5];
    //key : 组号 value： 时间
    private Map<Integer,Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;
    //总的训练次数
    private int totalTrainingTimes;
    private Timer timer;
    //训练开始时间
    private long startTime;

    private final int TIME_RECEIVE = 1;
    private final int POWER_RECEIVE = 2;
    private final int UPDATE_TIMES = 3;
    private final int STOP_TRAINING = 4;
    //电池信息磊
    Battery battery;
    private int level=0;



    private Handler handler = new Handler() {
        //处理接收过来的数据的方法
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Timer.TIMER_FLAG:
                    String time = msg.obj.toString();
                    tvTotalTime.setText("总时间："+time);
                    break;
                //接收到返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7) {
                        //解析数据
                        analyzeTimeData(data);
                    }
                    break;
                case UPDATE_TIMES:
                    sortTime(timeMap);
                    singleDoubleJumpAdapter.setTimeMap(timeMap,keyId);
                    singleDoubleJumpAdapter.notifyDataSetChanged();
                    break;
                case STOP_TRAINING:
                    stopTraining();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singledoublejump);
        ButterKnife.bind(this);
        device = new Device(this);
        //更新连接设备列表
        device.createDeviceList(this);
        //判断是否插入协调器
        if (device.devCount > 0) {
            device.connect(this);
            device.initConfig();
        }
        initView();
        battery = new Battery(device,imgSatart,handler_battery );
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

    @Override
    protected void onPause() {
        super.onPause();
        device.turnOffAllTheLight();
        ReceiveThread.stopThread();
        if (device.devCount > 0)
            device.disconnect();
    }

    public void initView() {
        tvTitle.setText("单双脚连续跳训练");
        imgHelp.setVisibility(View.VISIBLE);
        imgSaveNew.setVisibility(View.VISIBLE);

        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //初始化分组下拉框
        int deviceNum;
        int maxGroupNum=2;
        deviceNum= Device.DEVICE_LIST.size();
        if(deviceNum<6) {
            Toast.makeText(SingleDoubleJumpActivity.this,"设备数不足,至少6个",Toast.LENGTH_SHORT).show();
            return;
        }

        String[] groupNumChoose = new String[deviceNum/6 + 1];
        groupNumChoose[0] = "";
        for (int i = 1; i <= deviceNum/6; i++)
            groupNumChoose[i] = (i + " 组");

        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(SingleDoubleJumpActivity.this, spGroupNum, groupNumChoose) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                groupNum = i;
                finishTimes = new int[groupNum];
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });

        //初始化训练强度下拉框
        final String[] trainingOptions = new String[deviceNum-6+1];
        for (int i = 6; i <= deviceNum; i++) {
            trainingOptions[i - 6] = i +"个";
        }
        //初始化分组listview
        groupListViewAdapter = new GroupListViewAdapter(SingleDoubleJumpActivity.this, groupSize);
        lvGroup.setAdapter(groupListViewAdapter);
        Log.i("=================","groupSize"+groupSize);


        spTrainingTimes.setOnItemSelectedListener(new SpinnerItemSelectedListener(SingleDoubleJumpActivity.this, spTrainingTimes, trainingOptions) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //一组的亮灯数
                groupSize=i+6;
                totalTrainingTimes = i+6;
                groupListViewAdapter.setGroupSize(groupSize);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });

        //初始化时间listview
        singleDoubleJumpAdapter = new SingleDoubleJumpAdapter(this);
        lvTimes.setAdapter(singleDoubleJumpAdapter);


    }


    @OnClick({R.id.layout_cancel, R.id.img_help, R.id.btn_begin,R.id.img_set, R.id.img_save_new, R.id.btn_off,
            R.id.btn_stop,R.id.btn_on , R.id.btn_result,R.id.img_start})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_stop:
                if(trainingBeginFlag)
                    stopTraining();
                break;
            case R.id.img_set:
                CustomDialog dialog = new  CustomDialog(SingleDoubleJumpActivity.this,"From btn 2",new CustomDialog.ICustomDialogEventListener() {
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
                operateUtils.setScreenWidth(SingleDoubleJumpActivity.this,dialog, 0.95, 0.7);
                break;
            case R.id.layout_cancel:
                this.finish();
                device.turnOffAllTheLight();
                break;
            case R.id.img_help:
                List<Integer> list = new ArrayList<>();
                list.add(R.string.situp_training_method);
                list.add(R.string.situp_training_standard);
                Dialog dialog_help = DialogUtils.createHelpDialog(SingleDoubleJumpActivity.this,list);
                OperateUtils.setScreenWidth(this, dialog_help, 0.95, 0.7);
                dialog_help.show();
                break;
            case R.id.img_save_new:

                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐 ...
                bundle.putString("trainingCategory", "1");
                //每组所用时间
                bundle.putIntArray("finishTimes", finishTimes);
                //总次数（强度）
                bundle.putInt("totalTrainingTimes", totalTrainingTimes);
                it.putExtras(bundle);
                startActivity(it);
//                }

                break;
            case R.id.btn_begin:
                if (!device.checkDevice(SingleDoubleJumpActivity.this))
                    return;
                if (groupNum == 0) {
                    Toast.makeText(this, "请选择训练分组!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (trainingBeginFlag)
                    stopTraining();
                else
                    startTraining();
                break;
            case R.id.btn_on:
                //goupNum组数，1：每组设备个数，0：类型
                device.turnOnButton(groupNum, groupSize, 1);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
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

    //开始训练
    public void startTraining() {
        trainingBeginFlag = true;
        btnOn.setClickable(false);
        btnOff.setClickable(false);
        completedTimes = new int[groupNum];
        keyId = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            completedTimes[i] = -1;
            keyId[i] = 0;
        }
        finishTimes = new int[groupNum];
        for (int i=0;i<groupNum;i++){
            timeMap.put(i,0);
        }
        singleDoubleJumpAdapter.setCompletedTimes(completedTimes);
        singleDoubleJumpAdapter.setFinishTime(finishTimes);
        singleDoubleJumpAdapter.setTimeMap(timeMap,keyId);
        singleDoubleJumpAdapter.notifyDataSetChanged();

        //清除串口数据
        new ReceiveThread(handler, device.ftDev, ReceiveThread.CLEAR_DATA_THREAD, 0).start();

        //开启接收设备返回时间的监听线程
        new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, TIME_RECEIVE).start();

        //开全灯
        for (int i = 0; i < groupNum*groupSize; i++) {
            device.sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                    Order.LightColor.values()[Setting_return_data[3]],
                    Order.VoiceMode.values()[Setting_return_data[1]],
                    Order.BlinkModel.values()[Setting_return_data[2]],
                    Order.LightModel.OUTER,
                    Order.ActionModel.values()[Setting_return_data[4]],
                    Order.EndVoice.values()[Setting_return_data[0]]);

        }

        //获得当前的系统时间
        startTime = System.currentTimeMillis();
        timer = new Timer(handler);
        timer.setBeginTime(startTime);
        timer.start();
    }


    //结束训 练
    public void stopTraining() {
        trainingBeginFlag = false;
        btnOn.setClickable(true);
        btnOff.setClickable(true);
        imgSaveNew.setEnabled(true);
        //停止接收线程
        ReceiveThread.stopThread();
        device.turnOffAllTheLight();
        timer.stopTimer();
    }


    public void turnOnLight(final char deviceNum) {
        //实现Runnable接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer.sleep(5000);
                if (!trainingBeginFlag)
                    return;
                device.sendOrder(deviceNum,
                        Order.LightColor.values()[Setting_return_data[3]],
                        Order.VoiceMode.values()[Setting_return_data[1]],
                        Order.BlinkModel.values()[Setting_return_data[2]],
                        Order.LightModel.OUTER,
                        Order.ActionModel.values()[Setting_return_data[4]],
                        Order.EndVoice.values()[Setting_return_data[0]]);
            }
        }).start();
    }

    //解析返回来的数据
    public void analyzeTimeData(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
                for (TimeInfo info : infos) {
                    int groupId = findDeviceGroupId(info.getDeviceNum());
                    //如果设备组号大于分组数肯定是错误的
                    if (groupId > groupNum)
                        continue;
                    Log.d(Constant.LOG_TAG, info.getDeviceNum() + " groupId " + groupId);
                    completedTimes[groupId] += 1;
                    //该组训练结束
                    if (completedTimes[groupId] == totalTrainingTimes-1) {
                        finishTimes[groupId] = (int) (System.currentTimeMillis() - startTime);
                        timeMap.put(groupId,finishTimes[groupId]);
                    }
//                    else
//                        turnOnLight(info.getDeviceNum());
                }

                Message msg = Message.obtain();
                msg.what = UPDATE_TIMES;
                msg.obj = "";
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
        return position / groupSize;
    }

    //判断训练是否结束
    public boolean isTrainingOver() {
        for (int i = 0; i < groupNum; i++)
            //只要有一组训练没完成，就没有结束
            if (completedTimes[i] < totalTrainingTimes-1)
                return false;
        return true;
    }
    public void sortTime(Map<Integer,Integer> timeMap){
        List<Map.Entry<Integer,Integer>> list = new ArrayList<Map.Entry<Integer,Integer>>(timeMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
            //升序排序
            public int compare(Map.Entry<Integer,Integer> o1,
                               Map.Entry<Integer,Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        int i=0;
        for(Map.Entry<Integer,Integer> mapping:list){
            Log.i("============",""+mapping.getKey()+":"+mapping.getValue());
//            System.out.println(mapping.getKey()+":"+mapping.getValue());
            keyId[i]=mapping.getKey();
            i++;
        }
    }




    //这个Handler作用是刷新Devices以及spanner
    Handler handler_battery = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Device.DEVICE_LIST =(ArrayList<DeviceInfo>)msg.obj;
                    //设备排序
                    Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

                    //初始化分组下拉框
                    maxGroupNum = Device.DEVICE_LIST.size();
                    if (maxGroupNum > 8)
                        maxGroupNum = 8;
                    String[] groupNumChoose = new String[maxGroupNum + 1];
                    groupNumChoose[0] = "";
                    for (int i = 1; i <= maxGroupNum; i++)
                        groupNumChoose[i] = (i + " 组");

                    spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(SingleDoubleJumpActivity.this, spGroupNum, groupNumChoose) {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            super.onItemSelected(adapterView, view, i, l);
                            groupNum = i;
                            finishTimes = new int[groupNum];
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

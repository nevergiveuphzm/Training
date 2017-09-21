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
import com.oucb303.training.adpter.DribblingGameAdapter;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.adpter.LargeRecessAdapter;
import com.oucb303.training.adpter.OrientRunAdapter;
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
 * Created by HP on 2017/8/24.
 */
public class SingleFootJumpActivity extends AppCompatActivity {
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save_new)
    ImageView imgSaveNew;
    @Bind(R.id.img_set)
    ImageView imgSet;
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
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.tv_down_time)
    TextView tvDownTime;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.btn_pause)
    Button btnPause;
    @Bind(R.id.btn_stop)
    Button btnStop;
    @Bind(R.id.sv_container)
    ScrollView svContainer;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.lv_scores)
    ListView lvScores;
    android.widget.CheckBox cbVoice;
    android.widget.CheckBox cbEndVoice;
    //---------------------------------------------------------------------------------------------
    private Device device;
    private int maxGroupNum;//最大分组数
    private int groupNum;//当前选择的分组数
    private final int groupSize = 1;//每组所需设备个数为1
    private GroupListViewAdapter groupListViewAdapter;
    private OrientRunAdapter orientRunAdapter;

    private int[] scores;//记录每组的得分
    private int trainingTime;//训练时间 单位毫秒
    private final int TIME_RECEIVE = 1;
    private final int UPDATE_TIMES = 2;
    private final int STOP_TRAINING = 3;
    private Timer timer;//计时器
    private CheckBox actionModeCheckBox, lightColorCheckBox, blinkModeCheckBox;
    private Dialog set_dialog;
    //key : 组号 value： 成绩
    private Map<Integer, Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;


    private Handler handler = new Handler(){
        //处理接收过来的数据的方法
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //更新计时
                case Timer.TIMER_FLAG:
                    tvTotalTime.setText(msg.obj.toString());
                    //判断结束
                    if (timer.time >= trainingTime){
                        stopTraining();
                        return;
                    }
                    break;
                //倒计时
                case Timer.TIMER_DOWN:
                    tvDownTime.setText("倒计时："+msg.obj.toString());
                    break;
                //接收返回的时间
                case TIME_RECEIVE:
                    String data = msg.obj.toString();
                    if (data.length() > 7){
                        analyzeTimeData(data);
                    }
                    break;
                //更新完成次数
                case UPDATE_TIMES:
                    sortTime(timeMap);
                    orientRunAdapter.setTimeMap(timeMap,keyId);
                    orientRunAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_foot_jump);
        ButterKnife.bind(this);
        device = new Device(this);
        // 更新连接设备列表
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
        set_dialog = createSetDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        device.disconnect();
    }

    private void initView() {
        tvTitle.setText("原地单双脚连续跳");
        //设备排序
        Collections.sort(Device.DEVICE_LIST,new PowerInfoComparetor());
        //初始化训练时间seekbar   //拖动进度条的事件监听  第一个：显示时间tectview，第二个：最大值
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime,10));
        //直接在触摸屏进行按住和松开事件的操作
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime,1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime,0));

        //训练分组spinner监听事件
        maxGroupNum = Device.DEVICE_LIST.size();
        String[] groupChoose = new String[maxGroupNum+1];
        groupChoose[0] = "";
        for (int i=1;i<groupChoose.length;i++)
        {
            groupChoose[i] = i +" 组";
        }
        spGroupNum.setOnItemSelectedListener(new SpinnerItemSelectedListener(SingleFootJumpActivity.this,spGroupNum,groupChoose) {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                super.onItemSelected(adapterView, view, i, l);
                groupNum = i;
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }
        });
        //初始化分组listview
        groupListViewAdapter = new GroupListViewAdapter(this,groupSize);
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

        //初始化成绩的分listview
        orientRunAdapter = new OrientRunAdapter(this);
        lvScores.setAdapter(orientRunAdapter);
    }

    @OnClick({R.id.layout_cancel,R.id.btn_on,R.id.btn_off,R.id.btn_begin,R.id.btn_stop,R.id.img_help,R.id.img_save_new,R.id.img_set})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.layout_cancel:
                this.finish();
                device.turnOffAllTheLight();
                break;
            case R.id.btn_on:
                device.turnOnButton(groupNum,groupSize,0);
                btnOn.setClickable(false);
                break;
            case R.id.btn_off:
                device.turnOffAllTheLight();
                btnOn.setClickable(true);
                break;
            case R.id.btn_begin:
                if (!device.checkDevice(this))
                    return;
                if (groupNum == 0){
                    Toast.makeText(this,"请先选择分组",Toast.LENGTH_SHORT).show();
                    return;
                }
                startTraining();
                btnOn.setClickable(false);
                btnOff.setClickable(false);
                break;
            case R.id.btn_stop: {
                stopTraining();
                btnOn.setClickable(true);
                btnOff.setClickable(true);
            }
                break;
            case R.id.img_set:
                set_dialog = createSetDialog();
                OperateUtils.setScreenWidth(this, set_dialog, 0.95, 0.7);
                set_dialog.show();
                break;
            case R.id.img_save_new:
                Intent it = new Intent(this, SaveActivity.class);
                Bundle bundle = new Bundle();
                //trainingCategory 1:折返跑 2:纵跳摸高 3:仰卧起坐、交替活动 ...
                bundle.putString("trainingName", "原地单双脚连续跳");
                bundle.putString("trainingCategory", "6");
                bundle.putInt("totalTimes", 0);//总次数
                bundle.putInt("deviceNum", groupNum);//设备个数
                bundle.putIntArray("scores", scores);//得分
                bundle.putInt("totalTime", trainingTime);//训练总时间
                bundle.putInt("groupNum", groupNum);//分组数
                it.putExtras(bundle);
                startActivity(it);
                break;
            case R.id.img_help:
                List<Integer> list = new ArrayList<>();
                list.add(R.string.singlefootjump_training_method);
                list.add(R.string.singlefootjump_training_standard);
                Dialog dialog_help = DialogUtils.createHelpDialog(SingleFootJumpActivity.this,list);
                OperateUtils.setScreenWidth(this, dialog_help, 0.95, 0.7);
                dialog_help.show();
                break;
                    
        }
    }

    public void startTraining() {
        scores = new int[groupNum];
        for (int i = 0; i < groupNum; i++) {
            scores[i] = 0;
        }
        //排序用的
        for (int i = 0; i < groupNum; i++) {
            timeMap.put(i, 0);
        }
        keyId = new int[groupNum];

        orientRunAdapter.setScores(scores);
        orientRunAdapter.setTimeMap(timeMap,keyId);
        orientRunAdapter.notifyDataSetChanged();
        //训练时间
        trainingTime = (int) (new Double(tvTrainingTime.getText().toString()) * 60 * 1000);
        //清除串口数据
        new ReceiveThread(handler,device.ftDev,ReceiveThread.CLEAR_DATA_THREAD,0).start();
        //开启接收设备返回时间的监听线程
        new ReceiveThread(handler,device.ftDev,ReceiveThread.TIME_RECEIVE_THREAD,TIME_RECEIVE).start();
        //第一次开所选组数的灯
        for (int i=0;i<groupNum;i++){
            device.sendOrder(Device.DEVICE_LIST.get(i).getDeviceNum(),
                    Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                    Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                    Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                    Order.LightModel.OUTER,
                    Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                    Order.EndVoice.values()[cbEndVoice.isChecked()?1:0]);
        }
        timer = new Timer(handler,trainingTime);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();
    }
    public void stopTraining(){
        timer.stopTimer();
        ReceiveThread.stopThread();
        device.turnOffAllTheLight();
    }
    //解析饭回来的数据,灭一次解析一次，如果同时灭多盏灯，就是解析一次,infos里存放同时灭灯个数
    public void analyzeTimeData(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TimeInfo里面是：编号和时间
                List<TimeInfo> infos = DataAnalyzeUtils.analyzeTimeData(data);
                for (TimeInfo info:infos) {
                    //找到灭灯的所属组号
                    int groupId = findDeviceGroupId(info.getDeviceNum());
                    Log.i("groupId:","---------------"+groupId);
                    if (groupId > groupNum)
                        continue;
                    scores[groupId]++;
                    Log.i("scores[groupId]:","======================="+scores[groupId]);
//                    dribblingGameAdapter.setScores(scores);
                    timeMap.put(groupId,scores[groupId]);
                    //开下一次灯
                    turnOnLight(info.getDeviceNum());
                }
                Message msg = Message.obtain();
                msg.what = UPDATE_TIMES;
                msg.obj = "";
                handler.sendMessage(msg);
            }
        }).start();

    }
    //开下一次灯
    private void turnOnLight(char deviceNum) {
        Timer.sleep(500);
        device.sendOrder(deviceNum,
                Order.LightColor.values()[lightColorCheckBox.getCheckId()],
                Order.VoiceMode.values()[cbVoice.isChecked() ? 1 : 0],
                Order.BlinkModel.values()[blinkModeCheckBox.getCheckId()-1],
                Order.LightModel.OUTER,
                Order.ActionModel.values()[actionModeCheckBox.getCheckId()],
                Order.EndVoice.values()[cbEndVoice.isChecked()?1:0]);
    }

    //查找设备所属组号
    public int findDeviceGroupId(char device){
        int groupId = 0;
        for(int i=0;i<groupNum;i++){
            if (Device.DEVICE_LIST.get(i).getDeviceNum() == device){
                groupId = i;
                break;
            }
        }
        return groupId;
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

    public Dialog createSetDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);//获取实例
        View v = inflater.inflate(R.layout.layout_dialog_lightset,null);//加载布局
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
        cbEndVoice = (android.widget.CheckBox) layout.findViewById(R.id.cb_endvoice);
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
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCloseSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
}

package com.oucb303.training.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.GroupListViewAdapter;
import com.oucb303.training.device.Device;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.PowerInfo;
import com.oucb303.training.model.PowerInfoComparetor;
import com.oucb303.training.threads.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 仰卧起坐
 */

public class SitUpsActivity extends AppCompatActivity
{

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
    @Bind(R.id.img_action_mode_touch)
    ImageView imgActionModeTouch;
    @Bind(R.id.img_action_mode_light)
    ImageView imgActionModeLight;
    @Bind(R.id.img_action_mode_together)
    ImageView imgActionModeTogether;
    @Bind(R.id.img_light_mode_center)
    ImageView imgLightModeCenter;
    @Bind(R.id.img_light_mode_all)
    ImageView imgLightModeAll;
    @Bind(R.id.img_light_mode_beside)
    ImageView imgLightModeBeside;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.btn_begin)
    Button btnBegin;
    @Bind(R.id.sp_group_num)
    Spinner spGroupNum;
    @Bind(R.id.lv_group)
    ListView lvGroup;
    @Bind(R.id.sv_container)
    ScrollView svContainer;

    private CheckBox actionModeCheckBox, lightModeCheckBox;
    //训练时间  单位毫秒
    private int trainingTime;
    //计时器
    private Timer timer;
    //是否正在训练标志
    private boolean isTraining = false;
    //每组的设备数量  最大分组数  组数
    private int groupSize = 2, maxGroupNum = 4, groupNum;
    private GroupListViewAdapter groupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sit_ups);
        ButterKnife.bind(this);
        initView();
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Timer.TIMER_FLAG:
                    tvTotalTime.setText(msg.obj.toString());
                    if (timer.time >= trainingTime)
                        stopTraining();
                    break;
            }
        }
    };


    private void initView()
    {
        tvTitle.setText("仰卧起坐训练");
        ///初始化分组listView
        groupListViewAdapter = new GroupListViewAdapter(SitUpsActivity.this, groupSize);
        lvGroup.setAdapter(groupListViewAdapter);
        //解决listView 与scrollView的滑动冲突
        lvGroup.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                //从listView 抬起时将控制权还给scrollview
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    svContainer.requestDisallowInterceptTouchEvent(false);
                else
                    svContainer.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //初始化设备列表
        Device.DEVICE_LIST.clear();
        for (int i = 0; i < 10; i++)
        {
            PowerInfo info = new PowerInfo();
            info.setDeviceNum((char) ('A' + i * 2));
            Device.DEVICE_LIST.add(info);
        }
        Device.DEVICE_LIST.add(new PowerInfo('B'));
        Device.DEVICE_LIST.add(new PowerInfo('F'));
        //设备排序
        Collections.sort(Device.DEVICE_LIST, new PowerInfoComparetor());

        //初始化分组下拉框
        if (maxGroupNum > Device.DEVICE_LIST.size() / 2)
            maxGroupNum = Device.DEVICE_LIST.size() / 2;
        final List<String> groupNumChoose = new ArrayList<>();
        groupNumChoose.add(" ");
        for (int i = 1; i <= maxGroupNum; i++)
            groupNumChoose.add(i + " 组");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SitUpsActivity.this,
                android.R.layout.simple_spinner_item, groupNumChoose);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGroupNum.setAdapter(adapter);
        spGroupNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                groupNum = i;
                groupListViewAdapter.setGroupNum(i);
                groupListViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });


        //训练时间拖动条初始化
        barTrainingTime.setOnSeekBarChangeListener(new MySeekBarListener(tvTrainingTime, 5));
        imgTrainingTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 1));
        imgTrainingTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barTrainingTime, 0));

        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeTouch, imgActionModeLight,
                imgActionModeTogether};
        actionModeCheckBox = new CheckBox(0, views);
        new CheckBoxClickListener(actionModeCheckBox);
        //设定灯光模式checkBox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgLightModeCenter, imgLightModeAll,
                imgLightModeBeside};
        lightModeCheckBox = new CheckBox(0, views1);
        new CheckBoxClickListener(lightModeCheckBox);
    }

    @OnClick({R.id.layout_cancel, R.id.btn_begin})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
            case R.id.btn_begin:
                if (isTraining)
                    stopTraining();
                else
                    startTraining();
                break;
        }
    }

    private void startTraining()
    {
        btnBegin.setText("停止");
        isTraining = true;
        //训练时间
        trainingTime = (int) (new Double(tvTrainingTime.getText().toString()) * 60 * 1000);
        timer = new Timer(handler);
        timer.setBeginTime(System.currentTimeMillis());
        timer.start();
    }

    private void stopTraining()
    {
        isTraining = false;
        btnBegin.setText("开始");
        timer.stopTimer();
    }
}

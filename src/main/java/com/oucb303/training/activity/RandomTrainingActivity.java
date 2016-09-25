package com.oucb303.training.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

<<<<<<< HEAD
=======
import com.oucb303.training.listener.ChangeBarClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
>>>>>>> 2094065eaaf13cad7322012c0f92a1b37b47794b
import com.oucb303.training.R;
import com.oucb303.training.device.Device;
import com.oucb303.training.listener.ChangeBarClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.utils.DataAnalyzeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baichangcai on 2016/9/7.
 */
public class RandomTrainingActivity extends Activity
{
    @Bind(R.id.bt_distance_cancel)
    ImageView btDistanceCancel;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_training_times)
    TextView tvTrainingTimes;
    @Bind(R.id.img_training_times_sub)
    ImageView imgTrainingTimesSub;
    @Bind(R.id.bar_training_times)
    SeekBar barTrainingTimes;
    @Bind(R.id.img_training_times_add)
    ImageView imgTrainingTimesAdd;
    @Bind(R.id.tv_delay_time)
    TextView tvDelayTime;
    @Bind(R.id.img_delay_time_sub)
    ImageView imgDelayTimeSub;
    @Bind(R.id.bar_delay_time)
    SeekBar barDelayTime;
    @Bind(R.id.img_delay_time_add)
    ImageView imgDelayTimeAdd;
    @Bind(R.id.tv_over_time)
    TextView tvOverTime;
    @Bind(R.id.img_over_time_sub)
    ImageView imgOverTimeSub;
    @Bind(R.id.bar_over_time)
    SeekBar barOverTime;
    @Bind(R.id.img_over_time_add)
    ImageView imgOverTimeAdd;
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
    @Bind(R.id.btn_begin)
    Button btnBegin;

    //感应模式和灯光模式集合
    private List<CheckBox> actionModeCheckBoxs, lightModeCheckBoxs;
    private final int POWER_RECEIVE = 1;
    private Device device;
    //运行的总次数
    private int totalTimes;
    //延迟时间 单位是毫秒
    private int delayTime;
    //超时时间 单位毫秒
    private int overTime;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                //获取到电量信息
                case POWER_RECEIVE:
                    DataAnalyzeUtils.analyzePowerData(msg.obj.toString(),
                            RandomTrainingActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomtraining);
        ButterKnife.bind(this);
        initView();
        device = Device.getInstance(RandomTrainingActivity.this);
    }

    public void initView()
    {
        //设置seekbar 拖动事件的监听器
        barTrainingTimes.setOnSeekBarChangeListener(new MySeekBarListener
                (tvTrainingTimes, 500));
        barDelayTime.setOnSeekBarChangeListener(new MySeekBarListener(tvDelayTime, 10));
        barOverTime.setOnSeekBarChangeListener(new MySeekBarListener(tvOverTime, 30));
        //设置加减按钮的监听事件
        imgTrainingTimesSub.setOnTouchListener(new ChangeBarClickListener
                (barTrainingTimes, 0));
        imgTrainingTimesAdd.setOnTouchListener(new ChangeBarClickListener
                (barTrainingTimes, 1));
        imgDelayTimeSub.setOnTouchListener(new ChangeBarClickListener
                (barDelayTime, 0));
        imgDelayTimeAdd.setOnTouchListener(new ChangeBarClickListener
                (barDelayTime, 1));
        imgOverTimeSub.setOnTouchListener(new ChangeBarClickListener
                (barOverTime, 0));
        imgOverTimeAdd.setOnTouchListener(new ChangeBarClickListener
                (barOverTime, 1));
        //设定感应模式checkBox组合的点击事件
        actionModeCheckBoxs = new ArrayList<>();
        actionModeCheckBoxs.add(new CheckBox(0, true, imgActionModeTouch));
        actionModeCheckBoxs.add(new CheckBox(1, false, imgActionModeLight));
        actionModeCheckBoxs.add(new CheckBox(2, false, imgActionModeTogether));
        new CheckBoxClickListener(actionModeCheckBoxs);
        //设定灯光模式checkBox组合的点击事件
        lightModeCheckBoxs = new ArrayList<>();
        lightModeCheckBoxs.add(new CheckBox(0, true, imgLightModeCenter));
        lightModeCheckBoxs.add(new CheckBox(1, false, imgLightModeAll));
        lightModeCheckBoxs.add(new CheckBox(2, false, imgLightModeBeside));
        new CheckBoxClickListener(lightModeCheckBoxs);
    }

    @OnClick(R.id.btn_begin)
    public void onClick()
    {
        totalTimes = new Integer(tvTrainingTimes.getText().toString().trim());

    }
}

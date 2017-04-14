package com.oucb303.training.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.device.Device;
import com.oucb303.training.model.PowerInfoComparetor;

import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HP on 2017/4/7.
 */
public class RandomTimesModuleActivity extends AppCompatActivity {

    @Bind(R.id.bt_distance_cancel)
    ImageView btDistanceCancel;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save)
    ImageView imgSave;
    @Bind(R.id.tv_training_time)
    TextView tvTrainingTime;
    @Bind(R.id.img_training_time_sub)
    ImageView imgTrainingTimeSub;
    @Bind(R.id.bar_training_time)
    SeekBar barTrainingTime;
    @Bind(R.id.img_training_time_add)
    ImageView imgTrainingTimeAdd;
    @Bind(R.id.ll_training_time)
    LinearLayout llTrainingTime;
    @Bind(R.id.tv_training_times)
    TextView tvTrainingTimes;
    @Bind(R.id.img_training_times_sub)
    ImageView imgTrainingTimesSub;
    @Bind(R.id.bar_training_times)
    SeekBar barTrainingTimes;
    @Bind(R.id.img_training_times_add)
    ImageView imgTrainingTimesAdd;
    @Bind(R.id.ll_training_times)
    LinearLayout llTrainingTimes;
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
    @Bind(R.id.sp_light_num)
    Spinner spLightNum;
    @Bind(R.id.btn_on)
    Button btnOn;
    @Bind(R.id.sp_dev_num)
    Spinner spDevNum;
    @Bind(R.id.btn_off)
    Button btnOff;
    @Bind(R.id.tv_device_list)
    TextView tvDeviceList;
    @Bind(R.id.img_action_mode_light)
    ImageView imgActionModeLight;
    @Bind(R.id.img_action_mode_touch)
    ImageView imgActionModeTouch;
    @Bind(R.id.img_action_mode_together)
    ImageView imgActionModeTogether;
    @Bind(R.id.img_light_color_blue)
    ImageView imgLightColorBlue;
    @Bind(R.id.img_light_color_red)
    ImageView imgLightColorRed;
    @Bind(R.id.img_light_color_blue_red)
    ImageView imgLightColorBlueRed;
    @Bind(R.id.cb_voice)
    CheckBox cbVoice;
    @Bind(R.id.cb_end_voice)
    CheckBox cbEndVoice;
    @Bind(R.id.cb_over_time_voice)
    CheckBox cbOverTimeVoice;
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
    private int level;
    private Device device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充屏幕的UI
        setContentView(R.layout.activity_random_train);
        //返回xml中定义的视图或组件的ID
        ButterKnife.bind(this);

        level = getIntent().getIntExtra("level", 0);

        device = new Device(this);
        //更新连接设备列表
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        device.disconnect();
    }

    public void initView()
    {
        tvTitle.setText("随机模块");
        //设备排序
        Collections.sort(Device.DEVICE_LIST,new PowerInfoComparetor());
    }
}

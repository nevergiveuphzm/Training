package com.oucb303.training.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.HorizonListViewAdapter;
import com.oucb303.training.adpter.LightGridViewAdapter;
import com.oucb303.training.adpter.SequenceSetListViewAdapter;
import com.oucb303.training.listener.ChangeBarClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.Constant;
import com.oucb303.training.model.Light;
import com.oucb303.training.utils.OperateUtils;
import com.oucb303.training.widget.SequenceSetListview;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baichangcai on 2016/9/18.
 */
public class SequenceTrainingActivity extends Activity {
    private SequenceTrainingActivity sequenceSetActivity;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.tv_delay_time)
    TextView tvDelayTime;
    @Bind(R.id.tv_over_time)
    TextView tvOverTime;
    @Bind(R.id.bar_over_time)
    SeekBar barOverTime;
    @Bind(R.id.bar_delay_time)
    SeekBar barDelayTime;
    @Bind(R.id.bar_distance)
    SeekBar barDistance;
    @Bind(R.id.iv_distance_sub)
    ImageView imgDistanceSub;
    @Bind(R.id.iv_distance_plus)
    ImageView imgDistanceAdd;
    @Bind(R.id.iv_delaytime_sub)
    ImageView imgDelayTimeSub;
    @Bind(R.id.img_delay_time_add)
    ImageView imgDelayTimeAdd;
    @Bind(R.id.img_over_time_sub)
    ImageView imgOverTimeSub;
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
    private GridView gridView;//显示灯的网格布局
    private HorizonListViewAdapter horizonListViewAdapter;//横向ListView布局适配器
    private SequenceSetListViewAdapter sequenceSetListViewAdapter;//纵向ListView布局适配器
    private LightGridViewAdapter lightGridViewAdapter;//网格布局适配器
    private List<Light> list_light_noCheck = new ArrayList<>();;//初始化时，还没有被选中的灯
    private List<Light>  list_light_check = new ArrayList<>();;//被选中的灯
    private Light lights;//灯
    private Context mcontext;
    private String[] str = {"步骤一", "12"};//步骤名称
    @Bind(R.id.lv_sequenceSet)
    SequenceSetListview sequenceSetListview;
    //感应模式和灯光模式集合
    private List<CheckBox> actionModeCheckBoxs, lightModeCheckBoxs;
    /**
     * 实现类，响应按钮点击事件
     * 点击Dialog中的每个灯，设置灯的状态
     */
    private LightGridViewAdapter.ChangeLightClickListener changeLightClickListener = new LightGridViewAdapter.ChangeLightClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            //改变等的选中与未选中状态
            if (!list_light_noCheck.get(position).isChecked()) {
                list_light_noCheck.get(position).setChecked(true);
                list_light_check.add(list_light_noCheck.get(position));
            } else {
                list_light_noCheck.get(position).setChecked(false);
                list_light_check.remove(list_light_noCheck.get(position));
            }
            lightGridViewAdapter.notifyDataSetChanged();
        }
    };
    /**
     * 实现类，响应按钮点击事件
     * 点击“添加”，设置灯的数量
     */
    private SequenceSetListViewAdapter.AddLightClickListener addLightClickListener = new SequenceSetListViewAdapter.AddLightClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            switch (v.getId()) {
                //添加灯
                case R.id.tv_add:
                    AlertDialog.Builder builder = new AlertDialog.Builder(sequenceSetActivity);
                    View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_dialog_addlight, null);
                    builder.setView(view);
                    gridView = (GridView) view.findViewById(R.id.gv_light);
                    Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
                    Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                    //如果有16个设备，那默认的加进去16个灯
                    list_light_noCheck = new ArrayList<>();
                    for (int i = 0; i < 16; i++) {
                        Light light = new Light(i, false, R.drawable.iv_circle);
                        list_light_noCheck.add(light);
                    }
                    lightGridViewAdapter = new LightGridViewAdapter(mcontext, list_light_noCheck, changeLightClickListener);
                    gridView.setAdapter(lightGridViewAdapter);
                    final AlertDialog alertDialog = builder.create();
                    //确定
                    btn_sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //将被选中的灯装入到横向ListView中
                            horizonListViewAdapter = new HorizonListViewAdapter(mcontext, list_light_check);
                            sequenceSetListViewAdapter = new SequenceSetListViewAdapter(mcontext,str,horizonListViewAdapter,addLightClickListener);
                            sequenceSetListview.setAdapter(sequenceSetListViewAdapter);
                            sequenceSetListViewAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                    });
                    //取消
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    //设置占屏比
                    OperateUtils.setScreenWidth(sequenceSetActivity, alertDialog, Constant.SCREEN_WIDTH16, Constant.SCREEN_HEIGHT);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_training);
        ButterKnife.bind(this);
        sequenceSetActivity = this;
        mcontext = this.getApplicationContext();
       //此Activity标题
        tv_title.setText("序列编程");
        //初始化右侧ListView
        initListView();


    }

    private void initListView() {
        //设置seekbar 拖动事件的监听器
        barDistance.setOnSeekBarChangeListener(new MySeekBarListener
                (tvDistance, 80, "厘米"));
        barDelayTime.setOnSeekBarChangeListener(new MySeekBarListener(tvDelayTime, 10,
                "秒"));
        barOverTime.setOnSeekBarChangeListener(new MySeekBarListener(tvOverTime, 30,
                "秒"));
        //设置加减按钮的监听事件
        imgDistanceSub.setOnTouchListener(new ChangeBarClickListener
                (barDistance, 0));
        imgDistanceAdd.setOnTouchListener(new ChangeBarClickListener
                (barDistance, 1));
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
        //纵向的ListView适配器
        horizonListViewAdapter = new HorizonListViewAdapter(mcontext,list_light_noCheck);
        //初始化纵向布局适配器
        sequenceSetListViewAdapter = new SequenceSetListViewAdapter(mcontext, str,horizonListViewAdapter, addLightClickListener);
        sequenceSetListview.setAdapter(sequenceSetListViewAdapter);
    }
    @OnClick(R.id.layout_cancel)
    public void onClick(View view) {

        switch (view.getId()) {
            //头部返回按钮
            case R.id.layout_cancel:
                finish();
                break;
        }
    }

}

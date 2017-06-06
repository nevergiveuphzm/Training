package com.oucb303.training.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.App;
import com.oucb303.training.R;
import com.oucb303.training.adpter.HorizonListViewAdapter;
import com.oucb303.training.adpter.LightGridViewAdapter;
import com.oucb303.training.adpter.SequenceSettingListViewAdapter;
import com.oucb303.training.daoservice.SequenceSer;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.model.Light;
import com.oucb303.training.utils.ConfigParamUtils;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.widget.SequenceSetListview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设定序列  序列编程
 */
public class SequenceTrainingActivity extends Activity {
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_delay_time)
    TextView tvDelayTime;
    @Bind(R.id.tv_over_time)
    TextView tvOverTime;
    @Bind(R.id.bar_over_time)
    SeekBar barOverTime;
    @Bind(R.id.bar_delay_time)
    SeekBar barDelayTime;
    ImageView imgDistanceAdd;
    @Bind(R.id.iv_delaytime_sub)
    ImageView imgDelayTimeSub;
    @Bind(R.id.img_delay_time_add)
    ImageView imgDelayTimeAdd;
    @Bind(R.id.img_over_time_sub)
    ImageView imgOverTimeSub;
    @Bind(R.id.img_over_time_add)
    ImageView imgOverTimeAdd;
    @Bind(R.id.lv_sequenceSet)
    SequenceSetListview sequenceSetListview;
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
    @Bind(R.id.img_light_color_blue)
    ImageView imgLightColorBlue;
    @Bind(R.id.img_light_color_red)
    ImageView imgLightColorRed;
    @Bind(R.id.img_light_color_blue_red)
    ImageView imgLightColorBlueRed;


    List<Light> list_light_noCheck;
    @Bind(R.id.img_blink_mode_none)
    ImageView imgBlinkModeNone;
    @Bind(R.id.img_blink_mode_slow)
    ImageView imgBlinkModeSlow;
    @Bind(R.id.img_blink_mode_fast)
    ImageView imgBlinkModeFast;
    private SequenceTrainingActivity sequenceSetActivity;
    private GridView gridView;//显示灯的网格布局
    private HorizonListViewAdapter horizonListViewAdapter;//横向ListView布局适配器
    private SequenceSettingListViewAdapter sequenceSetListViewAdapter;//纵向ListView布局适配器
    private LightGridViewAdapter lightGridViewAdapter;//网格布局适配器
    //步骤详细信息
    private List<Map<String, Object>> list_sequence = new ArrayList<>();
    private List<HorizonListViewAdapter> list_adepter = new ArrayList<>();
    private Light lights;//灯
    private Context mcontext;
    //感应模式和灯光模式集合
    private CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox,blinkModeCheckBox;
    //选中的灯
    private Point choseLight = new Point();
    private SequenceSer sequenceSer;
    //纵向listView 滑动到的位置
    private int scrolledX, scrolledY;
    //一套设备默认个数
    private int defaultDeviceNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_training);
        ButterKnife.bind(this);
        sequenceSetActivity = this;
        mcontext = this.getApplicationContext();
        sequenceSer = new SequenceSer(((App) getApplication()).getDaoSession());
        //此Activity标题
        tv_title.setText("序列编程");
        //初始化右侧ListView
        initListView();

        //纵向Listview的点击事件
        sequenceSetListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(Constant.LOG_TAG, position + "--" + list_adepter.size());
                //如果是最后一个
                if (position == list_adepter.size() - 1) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("list_light", null);
                    map.put("delay_time", 0.00);
                    list_sequence.add(list_sequence.size() - 1, map);
                    //Log.d(Constant.LOG_TAG, list_adepter.size() + "");
                    HorizonListViewAdapter horizonListViewAdapter = new HorizonListViewAdapter(mcontext, null);
                    list_adepter.add(list_adepter.size() - 1, horizonListViewAdapter);
                    sequenceSetListViewAdapter.notifyDataSetChanged();
                    sequenceSetListview.setSelection(list_sequence.size() - 1);
                }
            }
        });

        //记录下纵向listView的滑动位置
        sequenceSetListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    scrolledX = sequenceSetListview.getScrollX();
                    scrolledY = sequenceSetListview.getScrollY();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

    }

    private void initListView() {
        //设置seekbar 拖动事件的监听器
        barDelayTime.setOnSeekBarChangeListener(new SequenceSeekBarChangeListener(tvDelayTime, 10,0));
        barOverTime.setOnSeekBarChangeListener(new SequenceSeekBarChangeListener(tvOverTime, 98,2));
        //设置加减按钮的监听事件
        imgDelayTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barDelayTime, 0));
        imgDelayTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barDelayTime, 1));
        imgOverTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barOverTime, 0));
        imgOverTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barOverTime, 1));
        //设定感应模式checkBox组合的点击事件
        ImageView[] views = new ImageView[]{imgActionModeLight, imgActionModeTouch, imgActionModeTogether};
        actionModeCheckBox = new CheckBox(1, views);
        new SequenceCheckBoxListener(actionModeCheckBox, 0);
        //设定灯光模式checkBox组合的点击事件
        ImageView[] views1 = new ImageView[]{imgLightModeBeside, imgLightModeCenter, imgLightModeAll};
        lightModeCheckBox = new CheckBox(1, views1);
        new SequenceCheckBoxListener(lightModeCheckBox, 1);
        //设定灯光颜色checkBox组合的点击事件
        ImageView[] views2 = new ImageView[]{imgLightColorBlue, imgLightColorRed, imgLightColorBlueRed};
        lightColorCheckBox = new CheckBox(1, views2);
        new SequenceCheckBoxListener(lightColorCheckBox, 2);
        //设定闪烁模式checkbox组合的点击事件
        ImageView[] views3 = new ImageView[]{imgBlinkModeNone, imgBlinkModeSlow, imgBlinkModeFast,};
        blinkModeCheckBox = new CheckBox(1, views3);
        new CheckBoxClickListener(blinkModeCheckBox);

        list_adepter.add(null);
        list_sequence.add(null);

        //初始化纵向布局适配器
        sequenceSetListViewAdapter = new SequenceSettingListViewAdapter(mcontext,
                list_sequence, list_adepter, addLightClickListener, lightsItemClickListener);
        sequenceSetListview.setAdapter(sequenceSetListViewAdapter);
        changeWidgetState(false);

        //获取一套设备的默认个数
        defaultDeviceNum = ConfigParamUtils.getDefaultDeviceNum(this);
    }

    @OnClick({R.id.layout_cancel, R.id.btn_save, R.id.btn_random})
    public void onClick(View view) {
        switch (view.getId()) {
            //头部返回按钮
            case R.id.layout_cancel:
                finish();
                break;
            //保存
            case R.id.btn_save:
                AlertDialog.Builder builder = new AlertDialog.Builder(sequenceSetActivity);
                View v = LayoutInflater.from(mcontext).inflate(R.layout.dialog_add_sequence, null);
                builder.setView(v);
                Button btnSave = (Button) v.findViewById(R.id.btn_save);
                Button btnCancel = (Button) v.findViewById(R.id.btn_cancel);
                final EditText etTitle = (EditText) v.findViewById(R.id.et_title);
                final AlertDialog dialog = builder.create();
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = etTitle.getText().toString();
                        long res = sequenceSer.addSequence(list_sequence, title);
                        Log.d(Constant.LOG_TAG, "save result:" + res);
                        if (res > 0)
                            Toast.makeText(SequenceTrainingActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                        else if (res == -1)
                            Toast.makeText(SequenceTrainingActivity.this, "序列名称已存在,保存失败!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SequenceTrainingActivity.this, "添加失败!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            //随机生成序列
            case R.id.btn_random:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                View v1 = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null);
                builder1.setView(v1);
                Button btnSave1 = (Button) v1.findViewById(R.id.btn_save);
                Button btnCancel1 = (Button) v1.findViewById(R.id.btn_cancel);
                TextView tvTitle = (TextView) v1.findViewById(R.id.tv_edit_title);
                tvTitle.setText("随机生成序列个数");
                final EditText etDeviceNum = (EditText) v1.findViewById(R.id.et_edit_value);
                final AlertDialog dialog1 = builder1.create();
                btnSave1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String temp = etDeviceNum.getText().toString();
                        if (!temp.matches("[1-9][0-9]*")) {
                            Toast.makeText(SequenceTrainingActivity.this, "输入不合法!(格式:1、2、3)", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int num = new Integer(temp);
                        createSequence(num);
                        dialog1.dismiss();
                    }
                });

                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
        }
    }

    //生成随机序列
    private void createSequence(int num) {
        ArrayList<Integer> numList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            numList.clear();
            ArrayList<Light> lights = new ArrayList<>();
            int n = random.nextInt(100);
            n = n % 8 + 1;
            while (numList.size() < n) {
                int temp = random.nextInt(1000) % defaultDeviceNum + 1;
                if (!numList.contains(temp)) {
                    numList.add(temp);
                }
            }
            Collections.sort(numList);
            for (int j = 0; j < numList.size(); j++) {
                lights.add(new Light(numList.get(j), true));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list_light", lights);
            map.put("delay_time", 0.00);
            list_sequence.add(list_sequence.size() - 1, map);

            HorizonListViewAdapter horizonListViewAdapter = new HorizonListViewAdapter(mcontext, lights);
            list_adepter.add(list_adepter.size() - 1, horizonListViewAdapter);
        }
        sequenceSetListViewAdapter.notifyDataSetChanged();
        sequenceSetListview.setSelection(list_sequence.size() - 1);
    }


    //横向灯的点击事件
    private AdapterView.OnItemClickListener lightsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            lightItemClick((Integer) (adapterView.getTag()), i);
        }

        public void lightItemClick(int xPosition, int yPosition) {
            Log.d("AAAA", "x:" + xPosition + "  y:" + yPosition);
            List<Light> lights = (List<Light>) list_sequence.get(choseLight.x).get("list_light");
            Light light = lights.get(choseLight.y);
            light.setChoosed(false);
            list_adepter.get(choseLight.x).notifyDataSetChanged();

            choseLight.set(xPosition, yPosition);

            lights = (List<Light>) list_sequence.get(xPosition).get("list_light");
            double delayTime = new Double(list_sequence.get(xPosition).get("delay_time").toString());
            light = lights.get(yPosition);
            light.setChoosed(true);
            list_adepter.get(xPosition).notifyDataSetChanged();

            barDelayTime.setProgress((int) (delayTime * 200 / 10));
            int overTime = (light.getOverTime()-2);
            Log.d(Constant.LOG_TAG, overTime + "");
            barOverTime.setProgress(overTime);
            //模拟点击事件
            actionModeCheckBox.getViews()[light.getActionMode() - 1].performClick();
            lightModeCheckBox.getViews()[light.getLightMode() - 1].performClick();
            lightColorCheckBox.getViews()[light.getLightColor() - 1].performClick();
            changeWidgetState(true);

        }
    };

    /**
     * 实现类，响应按钮点击事件
     * 点击Dialog中的每个灯，设置灯的状态
     */
    private LightGridViewAdapter.ChangeLightClickListener changeLightClickListener = new LightGridViewAdapter.ChangeLightClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            if (list_light_noCheck.get(position).isChecked())
                list_light_noCheck.get(position).setChecked(false);
            else
                list_light_noCheck.get(position).setChecked(true);

            lightGridViewAdapter.notifyDataSetChanged();
        }
    };
    /**
     * 实现类，响应按钮点击事件
     * 点击“添加”，设置灯的数量
     */
    private SequenceSettingListViewAdapter.AddLightClickListener addLightClickListener = new SequenceSettingListViewAdapter.AddLightClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(sequenceSetActivity);
            View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_addlight, null);
            builder.setView(view);
            gridView = (GridView) view.findViewById(R.id.gv_light);
            Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
            Button btn_del = (Button) view.findViewById(R.id.btn_del);
            Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
            final List<Light> list_light = (List<Light>) list_sequence.get(position).get("list_light");
            list_light_noCheck = new ArrayList<>();
            for (int i = 1; i <= defaultDeviceNum; i++) {
                Light light = new Light(i, false);
                list_light_noCheck.add(light);
            }
            //之前没有选中过设备
            if (list_light != null && list_light.size() != 0) {
                //如果有，装进去之前的，用于回显
                for (int j = 0; j < list_light.size(); j++) {
                    list_light_noCheck.get(list_light.get(j).getNum() - 1).setChecked(true);
                }
            }

            lightGridViewAdapter = new LightGridViewAdapter(mcontext, list_light_noCheck, changeLightClickListener);
            gridView.setAdapter(lightGridViewAdapter);
            final AlertDialog alertDialog = builder.create();
            //确定
            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //将被选中的灯装入到横向ListView中
                    List<Light> list_light = (List<Light>) list_sequence.get(position).get("list_light");
                    if (list_light == null) {
                        list_light = new ArrayList<Light>();
                    }
                    list_light.clear();
                    for (Light light : list_light_noCheck) {
                        if (light.isChecked()) {
                            list_light.add(light);
                        }
                    }
                    list_sequence.get(position).put("list_light", list_light);
                    list_adepter.get(position).setList(list_light);

                    sequenceSetListViewAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                    sequenceSetListview.scrollTo(scrolledX, scrolledY);
                }
            });
            //取消
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            //删除
            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scrolledX = sequenceSetListview.getScrollX();
                    scrolledY = sequenceSetListview.getScrollY();
                    list_adepter.remove(position);
                    list_sequence.remove(position);
                    sequenceSetListViewAdapter.notifyDataSetChanged();
                    sequenceSetListview.scrollTo(scrolledX, scrolledY);
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
    };


    //更改控件的状态
    private void changeWidgetState(boolean state) {
        barDelayTime.setEnabled(state);
        barOverTime.setEnabled(state);
        imgDelayTimeAdd.setEnabled(state);
        imgDelayTimeSub.setEnabled(state);
        imgOverTimeAdd.setEnabled(state);
        imgOverTimeSub.setEnabled(state);
        imgActionModeLight.setEnabled(state);
        imgActionModeTogether.setEnabled(state);
        imgActionModeTouch.setEnabled(state);
        imgLightModeAll.setEnabled(state);
        imgLightModeBeside.setEnabled(state);
        imgLightModeCenter.setEnabled(state);
    }

    //seekbar 更新事件监听器
    private class SequenceSeekBarChangeListener extends MySeekBarListener {
        public SequenceSeekBarChangeListener(TextView textView, int maxValue,int minValue) {
            super(textView, maxValue,minValue);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            super.onProgressChanged(seekBar, i, b);
            Light l = ((List<Light>) (list_sequence.get(choseLight.x).get("list_light"))).get(choseLight.y);
            switch (getTextView().getId()) {
                case R.id.tv_delay_time:
                    list_sequence.get(choseLight.x).put("delay_time", new Double(tvDelayTime.getText().toString()));
                    sequenceSetListViewAdapter.notifyDataSetChanged();
                    sequenceSetListview.setSelection(choseLight.x);
                    break;
                case R.id.tv_over_time:
                    l.setOverTime(new Integer(tvOverTime.getText().toString()));
                    break;
            }
        }
    }

    //checkbox 更新监听事件
    private class SequenceCheckBoxListener extends CheckBoxClickListener {
        private int type;

        public SequenceCheckBoxListener(CheckBox checkBox, int type) {
            super(checkBox);
            this.type = type;
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            Light l = ((List<Light>) (list_sequence.get(choseLight.x).get("list_light"))).get(choseLight.y);
            switch (type) {
                case 0:
                    l.setActionMode(actionModeCheckBox.getCheckId());
                    break;
                case 1:
                    l.setLightMode(lightModeCheckBox.getCheckId());
                    break;
                case 2:
                    l.setLightColor(lightColorCheckBox.getCheckId());
                    break;
            }
        }
    }
}

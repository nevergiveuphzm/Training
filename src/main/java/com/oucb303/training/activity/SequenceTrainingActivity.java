package com.oucb303.training.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.HorizonListViewAdapter;
import com.oucb303.training.adpter.SequenceSetListViewAdapter;
import com.oucb303.training.device.Light;
import com.oucb303.training.widget.SequenceSetListview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baichangcai on 2016/9/18.
 */
public class SequenceTrainingActivity extends Activity {
    private SequenceTrainingActivity sequenceSetActivity;
    private TextView tv_title;
    private SeekBar seekBar;

    private HorizonListViewAdapter mAdapter;
    private List<Light> list_light;
    private Light lights;
    private Context mcontext;
    private String[] str={"步骤一","12"};
    private SequenceSetListview sequenceSetListview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_training);
        sequenceSetActivity = this;
        mcontext = this.getApplicationContext();
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("序列编程");
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setMax(100);
        seekBar.setProgress(20);
        initView();

    }
    private void initView() {
        //纵向的ListView
        sequenceSetListview = (SequenceSetListview) findViewById(R.id.mylist);
        list_light = new ArrayList<Light>();
        lights = new Light(R.drawable.phone);
        list_light.add(lights);
        mAdapter = new HorizonListViewAdapter(this, list_light);
        //纵向的ListView适配器
        SequenceSetListViewAdapter sequenceSetListViewAdapter = new SequenceSetListViewAdapter(mcontext,str,mAdapter,addLightClickListener);
        sequenceSetListview.setAdapter(sequenceSetListViewAdapter);
    }
    /**
     * 实现类，响应按钮点击事件
     * 点击“添加”，设置灯的数量
     */
    private SequenceSetListViewAdapter.AddLightClickListener addLightClickListener = new SequenceSetListViewAdapter.AddLightClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            switch (v.getId()) {
                case R.id.tv_add:
                    AlertDialog.Builder builder = new AlertDialog.Builder(sequenceSetActivity);
                    View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_dialog_addlight,null);
                    Toast.makeText(mcontext,"添加",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };


}

package com.oucb303.training.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.HorizonListViewAdapter;
import com.oucb303.training.adpter.LightGridViewAdapter;
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
    private String[] str = {"步骤一", "12"};
    private SequenceSetListview sequenceSetListview;
    //32个灯的图片
    public int[] images =  new int[]{
            R.drawable.aerow_winter,R.drawable.aerow_winter,R.drawable.aerow_winter,R.drawable.aerow_winter,
            R.drawable.aerow_winter,R.drawable.aerow_winter,R.drawable.aerow_winter,R.drawable.aerow_winter,

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

                    GridView gridView = (GridView) view.findViewById(R.id.gv_light);
                    Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
                    Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
                    LightGridViewAdapter lightGridViewAdapter = new LightGridViewAdapter(mcontext,images);
                    gridView.setAdapter(lightGridViewAdapter);
                    final AlertDialog alertDialog = builder.create();
                    btn_sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                    SetScreenWidth(alertDialog);
                    Toast.makeText(mcontext, "添加", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

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
        SequenceSetListViewAdapter sequenceSetListViewAdapter = new SequenceSetListViewAdapter(mcontext, str, mAdapter, addLightClickListener);
        sequenceSetListview.setAdapter(sequenceSetListViewAdapter);
    }
    public void SetScreenWidth(Dialog dialog) {

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow()
                .getAttributes(); // 获取对话框当前的参数值

        p.height = (int) (d.getHeight() * 0.28); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.64); // 宽度设置为屏幕的0.5

        dialog.getWindow().setAttributes(p); // 设置生效
    }

}

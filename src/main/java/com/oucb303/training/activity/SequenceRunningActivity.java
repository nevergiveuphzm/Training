package com.oucb303.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oucb303.training.App;
import com.oucb303.training.R;
import com.oucb303.training.adpter.SequenceItemListAdapter;
import com.oucb303.training.daoservice.SequenceSer;
import com.oucb303.training.entity.SequenceGroup;
import com.oucb303.training.listener.AddOrSubBtnClickListener;
import com.oucb303.training.listener.MySeekBarListener;
import com.oucb303.training.utils.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 运行设定的序列
 */
public class SequenceRunningActivity extends AppCompatActivity
{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.lv_sequence)
    ListView lvSequence;
    @Bind(R.id.tv_loop_times)
    TextView tvLoopTimes;
    @Bind(R.id.img_loop_times_sub)
    ImageView imgLoopTimesSub;
    @Bind(R.id.bar_loop_times)
    SeekBar barLoopTimes;
    @Bind(R.id.img_loop_times_add)
    ImageView imgLoopTimesAdd;
    @Bind(R.id.tv_whole_delay_time)
    TextView tvWholeDelayTime;
    @Bind(R.id.img_whole_delay_time_sub)
    ImageView imgWholeDelayTimeSub;
    @Bind(R.id.bar_whole_delay_time)
    SeekBar barWholeDelayTime;
    @Bind(R.id.img_whole_delay_time_add)
    ImageView imgWholeDelayTimeAdd;


    private SequenceSer sequenceSer;
    private SequenceItemListAdapter sequenceItemListAdapter;
    private List<SequenceGroup> sequenceGroupList;
    //序列编号
    private long sequenceId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_running);
        ButterKnife.bind(this);
        sequenceId = getIntent().getLongExtra("sequenceId", 1);
        sequenceSer = new SequenceSer(((App) getApplication()).getDaoSession());
        initView();
    }

    private void initView()
    {
        tvTitle.setText("运行序列");
        sequenceGroupList = sequenceSer.loadSequenceGroups(sequenceId);
        Log.d(Constant.LOG_TAG, sequenceGroupList.size() + "");
        sequenceItemListAdapter = new SequenceItemListAdapter(this, sequenceGroupList);
        lvSequence.setAdapter(sequenceItemListAdapter);
        sequenceItemListAdapter.notifyDataSetChanged();

        //设置seekbar 拖动事件的监听器
        barLoopTimes.setOnSeekBarChangeListener(new MySeekBarListener(tvLoopTimes, 30));
        barWholeDelayTime.setOnSeekBarChangeListener(new MySeekBarListener(tvWholeDelayTime, 60));
        //设置加减按钮的监听事件
        imgLoopTimesSub.setOnTouchListener(new AddOrSubBtnClickListener(barLoopTimes, 0));
        imgLoopTimesAdd.setOnTouchListener(new AddOrSubBtnClickListener(barLoopTimes, 1));
        imgWholeDelayTimeSub.setOnTouchListener(new AddOrSubBtnClickListener(barWholeDelayTime, 0));
        imgWholeDelayTimeAdd.setOnTouchListener(new AddOrSubBtnClickListener(barWholeDelayTime, 1));


    }

    @OnClick(R.id.layout_cancel)
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
        }
    }

}

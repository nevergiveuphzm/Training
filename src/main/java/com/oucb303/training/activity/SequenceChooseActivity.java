package com.oucb303.training.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.oucb303.training.App;
import com.oucb303.training.R;
import com.oucb303.training.adpter.SequenceListAdapter;
import com.oucb303.training.daoservice.SequenceSer;
import com.oucb303.training.entity.Sequence;
import com.oucb303.training.utils.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SequenceChooseActivity extends AppCompatActivity
{

    @Bind(R.id.lv_sequence_list)
    ListView lvSequenceList;
    @Bind(R.id.tv_title)
    TextView tvTitle;


    private SequenceSer sequenceSer;
    private List<Sequence> sequenceList;
    private SequenceListAdapter sequenceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_choose);
        ButterKnife.bind(this);
        sequenceSer = new SequenceSer(((App) getApplication()).getDaoSession());

        initView();
    }

    private void initView()
    {
        tvTitle.setText("选择序列");
        sequenceList = sequenceSer.loadSequences();
        sequenceListAdapter = new SequenceListAdapter(this, sequenceList, sequenceSer);
        lvSequenceList.setAdapter(sequenceListAdapter);
        lvSequenceList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                long sequenceId = (long) view.getTag();
                Intent intent = new Intent(SequenceChooseActivity.this, SequenceRunningActivity.class);
                intent.putExtra("sequenceId", sequenceId);
                startActivity(intent);
            }
        });
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

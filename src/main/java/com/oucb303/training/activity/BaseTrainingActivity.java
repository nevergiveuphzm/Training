package com.oucb303.training.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oucb303.training.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baichangcai on 2016/9/14.
 */
public class BaseTrainingActivity extends Activity
{
    @Bind(R.id.btn_times_random)
    Button btnTimesRandom;
    @Bind(R.id.btn_time_random)
    Button btnTimeRandom;
    @Bind(R.id.btn_sequence_training)
    Button btnSequenceTraining;
    @Bind(R.id.btn_sequences)
    Button btnSequences;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basetraining);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_times_random, R.id.btn_time_random, R.id.btn_sequence_training, R
            .id.btn_sequences,R.id.layout_cancel})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_times_random:
                break;
            case R.id.btn_time_random:
                break;
            case R.id.btn_sequence_training:
                Intent intent = new Intent(this, SequenceTrainingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sequences:
                break;
            case R.id.layout_cancel:
                finish();
                break;
        }
    }
}

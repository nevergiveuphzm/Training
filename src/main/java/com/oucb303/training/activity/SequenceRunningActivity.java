package com.oucb303.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.oucb303.training.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_running);
        ButterKnife.bind(this);

        initView();
    }

    private void initView()
    {
        tvTitle.setText("运行序列");
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

package com.oucb303.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.oucb303.training.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends AppCompatActivity
{

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        flag = getIntent().getIntExtra("flag", 0);
        initView();
    }

    private void initView()
    {
        switch (flag)
        {
            //折返跑
            case 1:
                //imgHelpContent.setImageResource(R.drawable.help_shuttle_run);
                break;
        }

    }


    @OnClick(R.id.layout_cancel)
    public void onClick()
    {
        this.finish();
    }
}

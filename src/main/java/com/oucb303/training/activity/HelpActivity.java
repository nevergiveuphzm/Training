package com.oucb303.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.oucb303.training.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpActivity extends AppCompatActivity
{

    @Bind(R.id.img_help_content)
    ImageView imgHelpContent;
    @Bind(R.id.tv_title)
    TextView tvTitle;
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
        tvTitle.setText("帮助");
        switch (flag)
        {
            //折返跑
            case 1:
                imgHelpContent.setImageResource(R.drawable.help_shuttle_run);
                break;
            //总跳莫高
            case 2:
                imgHelpContent.setImageResource(R.drawable.help_jump_high);
                break;
            //仰卧起坐
            case 3:
                imgHelpContent.setImageResource(R.drawable.help_setups);
                break;
            //总跳莫高
            case 4:
                break;
            //运球比赛
            case 5:
                imgHelpContent.setImageResource(R.drawable.help_dribbling_game);
                break;
            //多人混战
            case 6:
                imgHelpContent.setImageResource(R.drawable.help_multi_match);
                break;
            //双人对抗
            case 7:
                imgHelpContent.setImageResource(R.drawable.help_group_confrontation);
                break;
            //随机
            case 8:
                imgHelpContent.setImageResource(R.drawable.help_romdom);
                break;
            //大课间活动
            case 9:
                imgHelpContent.setImageResource(R.drawable.help_large_recess);
                break;
            //八秒钟跑
            case 10:
                imgHelpContent.setImageResource(R.drawable.help_eight_run);
                break;
            //胆大心细
            case 11:
                imgHelpContent.setImageResource(R.drawable.help_bold_cautious);
                break;
            //羽毛球步法训练
            case 12:
                imgHelpContent.setImageResource(R.drawable.help_badminton_training);
                break;
        }
    }


    @OnClick(R.id.layout_cancel)
    public void onClick()
    {
        this.finish();
    }
}

package com.b303.saflight.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.b303.saflight.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BaiChangCai on 2017/7/1.
 * 创客空间界面
 */
public class HackerSpaceActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.layout_cancel)
    LinearLayout llCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_create)
    ImageView ivCreate;
    @Bind(R.id.iv_div)
    ImageView ivDiv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackerspace);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("创客空间");
        ivCreate.setOnClickListener(this);
        ivDiv.setOnClickListener(this);
        llCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_create:
                Toast.makeText(HackerSpaceActivity.this, "创作课程", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.iv_div:
                Toast.makeText(HackerSpaceActivity.this, "DIY课程", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.layout_cancel:
                finish();
                break;
        }
    }
}

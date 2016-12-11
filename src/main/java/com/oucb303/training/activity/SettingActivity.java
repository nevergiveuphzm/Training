package com.oucb303.training.activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.utils.VersionUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity
{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_version)
    TextView tvVersion;

    private PackageInfo packageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        packageInfo = VersionUtils.getAppVersion(this);
        initView();
    }

    private void initView()
    {
        tvTitle.setText("设置");
        tvVersion.setText("当前版本:" + packageInfo.versionName);
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

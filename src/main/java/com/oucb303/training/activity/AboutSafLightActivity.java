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

/**
 * 关于SafLight
 */
public class AboutSafLightActivity extends AppCompatActivity
{

    @Bind(R.id.tv_app_name)
    TextView tvAppName;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    private PackageInfo packageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_saf_light);
        ButterKnife.bind(this);

        packageInfo = VersionUtils.getAppVersion(this);
        initView();
    }

    private void initView()
    {
        tvAppName.setText("SafLight v" + packageInfo.versionName);
        tvTitle.setText("关于SafLight");
    }

    @OnClick({R.id.layout_cancel, R.id.tv_check_update})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
            case R.id.tv_check_update:

                break;
        }

    }
}

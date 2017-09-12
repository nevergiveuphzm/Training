package com.b303.saflight.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b303.saflight.R;
import com.b303.saflight.adapter.CombinedFragmentAdapter;
import com.b303.saflight.fragment.ApplicationToolsFragmentFirst;
import com.b303.saflight.fragment.ApplicationToolsFragmentSecond;
import com.b303.saflight.fragment.CombinedFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BaiChangCai on 2017/7/1.
 * 应用工具界面
 */
public class ApplicationToolsActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.layout_cancel)
    LinearLayout llCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.vp_combined)
    ViewPager vpCombined;
    @Bind(R.id.ll_left)
    LinearLayout llLeft;
    @Bind(R.id.ll_right)
    LinearLayout llRight;
    private List<Fragment> fragmentList;
    private CombinedFragmentAdapter combinedFragmentAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_tools);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("应用工具");
        //创建fragment
        ApplicationToolsFragmentFirst fragment_first = new ApplicationToolsFragmentFirst();
        ApplicationToolsFragmentSecond fragment_second = new ApplicationToolsFragmentSecond();
        fragmentList = new ArrayList<>();
        fragmentList.add(fragment_first);
        fragmentList.add(fragment_second);
        combinedFragmentAdapter = new CombinedFragmentAdapter(getSupportFragmentManager(),fragmentList);
        vpCombined.setAdapter(combinedFragmentAdapter);
        llLeft.setOnClickListener(this);
        llRight.setOnClickListener(this);
        llCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_left:
                vpCombined.setCurrentItem(0,true);
                break;
            case R.id.ll_right:
                vpCombined.setCurrentItem(1,true);
                break;
            case R.id.layout_cancel:
                finish();
                break;
        }
    }
}

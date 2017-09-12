package com.b303.saflight.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b303.saflight.R;
import com.b303.saflight.adapter.CombinedFragmentAdapter;
import com.b303.saflight.fragment.CombinedFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 组合练习
 */
public class CombinedTrainingActivity extends AppCompatActivity implements View.OnClickListener
{

    @Bind(R.id.layout_cancel)
    LinearLayout llCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.vp_combined)
    ViewPager vpCombined;
    private List<Fragment> fragmentList;
    private CombinedFragmentAdapter combinedFragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combined_training);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("组合练习");
        //创建fragment
        CombinedFragment fragment_first = new CombinedFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(fragment_first);
        combinedFragmentAdapter = new CombinedFragmentAdapter(getSupportFragmentManager(),fragmentList);
        vpCombined.setAdapter(combinedFragmentAdapter);
        llCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_cancel:
                finish();
                break;
        }
    }
}

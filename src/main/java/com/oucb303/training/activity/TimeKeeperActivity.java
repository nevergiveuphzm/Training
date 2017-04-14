package com.oucb303.training.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.oucb303.training.R;

import butterknife.ButterKnife;

/**
 * Created by HP on 2017/3/30.
 */
public class TimeKeeperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        ButterKnife.bind(this);
    }
}

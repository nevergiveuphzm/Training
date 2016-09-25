package com.oucb303.training.listener;

import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by huzhiming on 16/9/24.
 * Description：
 */

public class MySeekBarListener implements SeekBar.OnSeekBarChangeListener
{
    //显示控件
    private TextView textView;
    //单位
    private String suffix;
    //显示的最大值
    private int maxValue;

    public MySeekBarListener(TextView textView, int maxValue, String suffix)
    {
        this.textView = textView;
        this.maxValue = maxValue;
        this.suffix = suffix;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {
        int progress = seekBar.getProgress();
        if ((progress * maxValue) % seekBar.getMax() != 0)
        {
            double value = 1.0 * (progress * maxValue) / seekBar.getMax();
            textView.setText(value + suffix);
        }
        else
        {
            int value = (progress * maxValue) / seekBar.getMax();
            textView.setText(value + suffix);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }
}

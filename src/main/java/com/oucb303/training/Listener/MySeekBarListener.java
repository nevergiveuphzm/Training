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
    //显示的最大值
    private int maxValue;

    public MySeekBarListener(TextView textView, int maxValue)
    {
        this.textView = textView;
        this.maxValue = maxValue;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {
        int progress = seekBar.getProgress();
        if ((progress * maxValue) % seekBar.getMax() != 0)
        {
            double value = 1.0 * (progress * maxValue) / seekBar.getMax();
            textView.setText(value + "");
        }
        else
        {
            int value = (progress * maxValue) / seekBar.getMax();
            textView.setText(value);
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

package com.oucb303.training.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.oucb303.training.threads.Timer;

/**
 * Created by huzhiming on 16/9/24.
 * Description：
 */

public class AddOrSubBtnClickListener implements View.OnTouchListener
{
    private SeekBar seekBar;
    //按钮类型 0: 减 1:加
    private int type;
    //单位
    private String suffix;
    //最小值
    private int minValue = 0;
    //线程停止标志
    private boolean threadStopFlag = false;

    public AddOrSubBtnClickListener(SeekBar seekBar, int type)
    {
        this.seekBar = seekBar;
        this.type = type;
    }

    public AddOrSubBtnClickListener(SeekBar seekBar, int type, int minValue)
    {
        this(seekBar, type);
        this.minValue = minValue;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            threadStopFlag = false;
            Log.i("AAAA", "down");
            new MyThread().start();
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_MOVE)
        {
            threadStopFlag = true;
            Log.i("AAAA", "up");
        }
        return true;
    }

    private class MyThread extends Thread
    {
        @Override
        public void run()
        {
            //如果控件一直按着
            while (!threadStopFlag)
            {
                //当前进度
                int progress = seekBar.getProgress();
                //最大刻度
                int max = seekBar.getMax();
                //减
                if (type == 0)
                {
                    if (progress > minValue)
                    {
                        progress--;
                        seekBar.setProgress(progress);
                        Log.i("AAAA", progress + "");
                    }
                } else
                {
                    if (progress != max)
                    {
                        progress++;
                        seekBar.setProgress(progress);
                    }
                }
                Timer.sleep(100);
            }
        }
    }

}

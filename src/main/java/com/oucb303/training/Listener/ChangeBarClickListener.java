package com.oucb303.training.Listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by huzhiming on 16/9/24.
 * Description：
 */

public class ChangeBarClickListener implements View.OnTouchListener
{
    private TextView textView;
    private SeekBar seekBar;
    //按钮类型 0:加 1:减
    private int type;
    //单位
    private String suffix;
    //线程停止标志
    private boolean threadStopFlag = false;

    public ChangeBarClickListener(SeekBar seekBar, int type)
    {
        this.seekBar = seekBar;
        this.type = type;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            threadStopFlag = false;
            Log.i("AAAA", "down");
            new Thread(new MyThread()).start();
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_MOVE)
        {
            threadStopFlag = true;
            Log.i("AAAA", "up");
        }
        return true;
    }

    private class MyThread implements Runnable
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
                    if (progress != 0)
                    {
                        progress--;
                        seekBar.setProgress(progress);
                        Log.i("AAAA", progress + "");
                    }
                }
                else
                {
                    if (progress != max)
                    {
                        progress++;
                        seekBar.setProgress(progress);
                    }
                }
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}

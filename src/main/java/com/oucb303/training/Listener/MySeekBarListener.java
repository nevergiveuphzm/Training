package com.oucb303.training.listener;

import android.widget.SeekBar;
import android.widget.Spinner;
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
    //显示的最小值
    private int minValue;

    private int mFlag=0;//0:表示正常拖动条；1：训练强度拖动条，改变下边Spinner;2:训练强度拖动条，改变下边的SeekBar
    private Spinner mSpinner;
    private SeekBar mSeekbar;

    public MySeekBarListener(Spinner spinner,TextView textView,int flag){
        this.textView = textView;
        this.mFlag = flag;
        this.mSpinner=spinner;
    }
    public MySeekBarListener(SeekBar seekBar,TextView textView,int flag){
        this.textView = textView;
        this.mFlag = flag;
        this.mSeekbar=seekBar;
    }
    public MySeekBarListener(SeekBar seekBar,TextView textView){
        this.textView = textView;
        this.mSeekbar=seekBar;
    }
    public MySeekBarListener(TextView textView, int maxValue)
    {
        this.textView = textView;
        this.maxValue = maxValue;
    }

    public MySeekBarListener(TextView textView, int maxValue, int minValue)
    {
        this(textView,maxValue);
        this.minValue = minValue;
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {
        int progress = seekBar.getProgress();
        if(mFlag==1){
            //如果是训练强度拖动条,同时改变的是下边的Spinner
            switch (progress){
                case 0:
                    textView.setText("水平一");
                    mSpinner.setSelection(0);
                    break;
                case 1:
                    textView.setText("水平二");
                    mSpinner.setSelection(1);
                    break;
                case 2:
                    textView.setText("水平三");
                    mSpinner.setSelection(2);
                    break;
                case 3:
                    textView.setText("水平四");
                    mSpinner.setSelection(3);
                    break;
                case 4:
                    textView.setText("水平五");
                    mSpinner.setSelection(4);
                    break;
                case 5:
                    textView.setText("水平六");
                    mSpinner.setSelection(5);
                    break;
                case 6:
                    textView.setText("自定义");
                    mSpinner.setSelection(0);
                    break;
            }
        }else if(mFlag==2){
            //如果是训练强度拖动条,同时改变的是下边的SeekBar
                    switch (progress){
                        case 0:
                            textView.setText("水平一");
                            mSeekbar.setProgress(2);
                            break;
                        case 1:
                            textView.setText("水平二");
                            mSeekbar.setProgress(3);
                            break;
                        case 2:
                            textView.setText("水平三");
                            mSeekbar.setProgress(4);
                            break;
                        case 3:
                            textView.setText("水平四");
                            mSeekbar.setProgress(8);
                            break;
                        case 4:
                            textView.setText("水平五");
                            mSeekbar.setProgress(12);
                            break;
                        case 5:
                            textView.setText("水平六");
                            mSeekbar.setProgress(16);
                    break;
                case 6:
                    textView.setText("自定义");
                    mSeekbar.setProgress(0);
                    break;
            }
        }else if(mFlag==3){
            //如果是训练强度拖动条,同时改变的是下边的SeekBar
            switch (progress){
                case 0:
                    textView.setText("水平一");
                    mSeekbar.setProgress(20);
                    break;
                case 1:
                    textView.setText("水平二");
                    mSeekbar.setProgress(40);
                    break;
                case 2:
                    textView.setText("水平三");
                    mSeekbar.setProgress(60);
                    break;
                case 3:
                    textView.setText("水平四");
                    mSeekbar.setProgress(80);
                    break;
                case 4:
                    textView.setText("水平五");
                    mSeekbar.setProgress(100);
                    break;
                case 5:
                    textView.setText("水平六");
                    mSeekbar.setProgress(120);
                    break;
                case 6:
                    textView.setText("自定义");
                    mSeekbar.setProgress(0);
                    break;
            }
        }else {
            if ((progress * maxValue) % seekBar.getMax() != 0)
            {
                double value = 1.0 * (progress * maxValue) / seekBar.getMax();
                value+=minValue;
                textView.setText(value + "");
            }
            else
            {
                int value = (progress * maxValue) / seekBar.getMax();
                value+=minValue;
                textView.setText(value + "");
            }
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

    public TextView getTextView()
    {
        return textView;
    }
}

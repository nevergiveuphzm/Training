package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.model.TimeInfo;

import java.util.List;

/**
 * Created by HP on 2017/3/13.
 */
public class BoldCautiousAdapter extends BaseAdapter {

    private Context context;
    //成绩
    private int[] scores;
    //设备编号
    private String[] deviceNum;
    //每组所用时间
    private int[] finishTime;

    public BoldCautiousAdapter(Context context)
    {
        this.context = context;
    }
    public void setScore(int[] scores)
    {
        this.scores = scores;
    }
    public void setDeviceNum(String[] deviceNum)
    {
        this.deviceNum = deviceNum;
    }
    public void setFinishTime(int[] finishTime)
    {
        this.finishTime = finishTime;
    }
    @Override
    public int getCount() {
        if (scores != null)
            return scores.length;
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_bold_cautious,null);
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_group_num);
        TextView tvScore = (TextView) view.findViewById(R.id.tv_score);
        TextView tvDeviceNum = (TextView) view.findViewById(R.id.tv_device_num);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);

        tvGroupId.setText("第"+(i+1)+"组");
        tvScore.setText(scores[i]+"");
        tvDeviceNum.setText(deviceNum[i]);

        if (finishTime[i] != 0)
        {
            int time = finishTime[i];
            int minute = time / (1000 * 60);
            int second = (time / 1000) % 60;
            int msec = time % 1000;
            String res = "";
            res += minute == 0 ? "" : minute + ":";
            res += second + ":" + msec / 10;
            tvTime.setText(res);
        } else
            tvTime.setText("---");
        return view;
    }
}

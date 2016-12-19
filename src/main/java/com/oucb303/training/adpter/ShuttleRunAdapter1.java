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
 * Created by huzhiming on 16/10/13.
 * Description：
 */

public class ShuttleRunAdapter1 extends BaseAdapter
{

    private Context context;
    private int[] completedTimes;
    private int[] finishTime;

    public ShuttleRunAdapter1(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount()
    {
        if (completedTimes == null)
            return 0;
        return completedTimes.length;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_shuttle_run_group_time1, null);
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_group_id);
        TextView tvFinishTimes = (TextView) view.findViewById(R.id.tv_group_finish_times);

        TextView tvGroupTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
        tvGroupId.setText("第 " + (position + 1) + " 组");
        tvFinishTimes.setText(completedTimes[position] + "");
       if (completedTimes[position]<0)
           tvFinishTimes.setText("0");
        if (finishTime[position] != 0)
        {
            int time = finishTime[position];
            int minute = time / (1000 * 60);
            int second = (time / 1000) % 60;
            int msec = time % 1000;
            String res = "";
            res += minute == 0 ? "" : minute + ":";
            res += second + ":" + msec / 10;
            tvGroupTotalTime.setText(res);
        } else
            tvGroupTotalTime.setText("---");

        return view;
    }



    public void setFinishTime(int[] finishTime)
    {
        this.finishTime = finishTime;
    }

    public void setCompletedTimes(int[] completedTimes)
    {
        this.completedTimes = completedTimes;
    }
}

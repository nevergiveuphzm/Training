package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oucb303.training.R;

/**
 * Created by HP on 2017/3/30.
 */
public class TimeKeeperAdapter extends BaseAdapter {
    private Context context;
    private int[] completedTimes;//每组完成次数
    //每组最终完成所有次数所用时间
    private int[] finishTime;

    public TimeKeeperAdapter(Context context)
    {
        this.context = context;
    }

    public void setCompletedTimes(int[] completedTimes){
        this.completedTimes = completedTimes;
    }
    public void setFinishTime(int[] finishTime)
    {
        this.finishTime = finishTime;
    }
    @Override
    public int getCount() {
        if (completedTimes == null)
            return 0;
        return completedTimes.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.item_time_keeper,null);
        }
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_gruopId);
        TextView tvFinishTimes = (TextView) view.findViewById(R.id.tv_finish_times);
//        TextView tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
        TextView tvNote = (TextView) view.findViewById(R.id.tv_note);


        tvGroupId.setText("第"+(i+1)+"组");
        tvFinishTimes.setText(completedTimes[i]+"次");

//        if (finishTime[i] != 0)
//        {
//            int time = finishTime[i];
//            int minute = time / (1000 * 60);
//            int second = (time / 1000) % 60;
//            int msec = time % 1000;
//            String res = "";
//            res += minute == 0 ? "" : minute + ":";
//            res += second + ":" + msec / 10;
//            tvTotalTime.setText(res);
//        } else
//            tvTotalTime.setText("---");
        tvNote.setText("点击查看");
        return view;
    }
}

package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;

/**
 * Created by huzhiming on 2016/11/7.
 */

public class SitUpsTimeListAdapter extends BaseAdapter
{
    //每组成绩
    private int[] scores;
    private Context context;

    public SitUpsTimeListAdapter(Context context)
    {
        this.context = context;
    }

    public void setScores(int[] scores)
    {
        this.scores = scores;
    }

    @Override
    public int getCount()
    {
        if (scores != null)
            return scores.length;
        return 0;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_situps_score_list, null);

        TextView tvGroupNum = (TextView) view.findViewById(R.id.tv_group_num);
        TextView tvTimes = (TextView) view.findViewById(R.id.tv_times);
        tvGroupNum.setText("第" + (i + 1) + "组");
        tvTimes.setText(scores[i] + "");
        return view;
    }

}

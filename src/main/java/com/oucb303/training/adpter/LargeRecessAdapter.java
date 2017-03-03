package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;

/**
 * Created by HP on 2017/2/27.
 */
public class LargeRecessAdapter extends BaseAdapter {

    private Context context;
    private int[] completedTimes;//规定时间内的完成次数

    public LargeRecessAdapter(Context context){
        this.context = context;
    }

    public void setCompletedTimes(int[] completedTimes){
        this.completedTimes = completedTimes;
    }


    @Override
    public int getCount() {
        if (completedTimes == null)
            return 0;
        return completedTimes.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_largerecess_completedtimes_list,null);
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_group_id);
        TextView tvTimes = (TextView) view.findViewById(R.id.tv_times);

        tvGroupId.setText("第"+(i+1)+"组");
        tvTimes.setText(completedTimes[i]+"次");
        if (completedTimes[i]<0)
            tvTimes.setText("0");
        return view;
    }
}

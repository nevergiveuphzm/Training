package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;

import java.util.List;
import java.util.Map;

/**
 * Created by huzhiming on 16/9/26.
 * Description：
 */

public class RandomTimeAdapter extends BaseAdapter
{
    private List<Map<String, Object>> timeList;
    private Context context;
    private LayoutInflater inflater;

    public RandomTimeAdapter(Context context, List<Map<String, Object>> list)
    {
        this.timeList = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        if (timeList == null)
            return 0;
        else
            return timeList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return timeList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View v = inflater.inflate(R.layout.sample_time, null);
        TextView num = (TextView) v.findViewById(R.id.tv_num);
        TextView time = (TextView) v.findViewById(R.id.tv_time);
        num.setText((i + 1) + "");
        time.setText(timeList.get(i).get("time").toString() + "毫秒");
        return v;
    }
}

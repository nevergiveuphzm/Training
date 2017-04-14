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
 * Created by HP on 2017/4/7.
 */
public class RandomTimesModuleAdapter extends BaseAdapter {

    //TimeInfo里包括：设备编号，返回的时间，数据是否有效
    private List<TimeInfo> timeList;
    private Context context;
    private LayoutInflater inflater;

    public RandomTimesModuleAdapter(Context context,List<TimeInfo> list)
    {
        this.context = context;
        this.timeList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (timeList == null)
            return 0;
        else
            return timeList.size();
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
        View v = inflater.inflate(R.layout.item_statistics_time1, null);

        TextView num = (TextView) v.findViewById(R.id.tv_num);
        TextView time = (TextView) v.findViewById(R.id.tv_time);
        TextView note = (TextView) v.findViewById(R.id.tv_note);
        TextView lightNum = (TextView) v.findViewById(R.id.tv_light_num);

        num.setText((i + 1) + "");
        if (timeList.get(i).getTime() == 0)
        {
            time.setText("---");
            note.setText("超时");
        }
        else
        {
            time.setText(timeList.get(i).getTime()+"毫秒");
            note.setText("---");
        }
        lightNum.setText(timeList.get(i).getDeviceNum() + "");
        return v;
    }
}

package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.device.Device;

/**
 * Created by HP on 2016/12/9.
 */
public class DGroupListViewAdapter extends BaseAdapter {

    String[] color = new String[]{"蓝色","红色","品红"};

//    分组数量
    private int groupNum;
    private Context context;
    public DGroupListViewAdapter(Context context)
    {
        this.context = context;

    }
    public void setGroupNum(int groupNum)
    {
        this.groupNum = groupNum;
    }
    @Override
    public int getCount() {
        return groupNum;
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
    public View getView(int position , View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_dribbling_game, null);
        TextView tvGroupNum = (TextView) view.findViewById(R.id.tv_group_num);
        TextView tvGroupColor = (TextView) view.findViewById(R.id.tv_group_color);
        tvGroupNum.setText("第" + (position + 1) + "组");
        tvGroupColor.setText(color[position]);
        return view;
    }
}

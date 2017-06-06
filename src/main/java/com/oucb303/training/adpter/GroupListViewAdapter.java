package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.device.Device;

import io.vov.vitamio.utils.Log;

/**
 * Created by huzhiming on 16/10/12.
 * Description：
 */

public class GroupListViewAdapter extends BaseAdapter
{
    private Context context;

    private int groupSize;
    //分组数量
    private int groupNum;
    //最大分组数
//    private int maxGroupNum;

    public GroupListViewAdapter(Context context)
    {
        this.context = context;
    }

    public GroupListViewAdapter(Context context, int groupSize)
    {
        this.context = context;
        this.groupSize = groupSize;
    }

    public void setGroupNum(int groupNum)
    {
        this.groupNum = groupNum;
    }

    public void setGroupSize(int groupSize)
    {
        this.groupSize = groupSize;
    }

    @Override
    public int getCount()
    {
        return groupNum;
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
        Log.i("执行几次","----------------------");
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_group_divide, null);
        TextView tvGroupNum = (TextView) view.findViewById(R.id.tv_group_num);
        TextView tvGroupDevice = (TextView) view.findViewById(R.id.tv_group_device);
        tvGroupNum.setText("第" + (position + 1) + "组");
        String deviceStr = "";
        for (int i = 0; i < groupSize; i++)
        {
            int n = position * groupSize + i;
            //设置设备灯的分组
            //Device.DEVICE_LIST.get(n).setGroupNum(position);
            deviceStr += Device.DEVICE_LIST.get(n).getDeviceNum() + " ";
        }
        tvGroupDevice.setText(deviceStr);
        return view;
    }
}

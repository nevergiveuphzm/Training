package com.oucb303.training.adpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.device.Device;
import com.oucb303.training.model.DeviceAndPower;
import com.oucb303.training.model.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzhiming on 16/9/23.
 * Description：
 */

public class PowerAdapter extends BaseAdapter
{
    private LayoutInflater inflater = null;
    private int num;

    private List<DeviceInfo> currentList = new ArrayList<>();

    public PowerAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }

    public PowerAdapter(Context context,int num)
    {
        this.inflater = LayoutInflater.from(context);
        this.num = num;
    }

    public void setCurrentList(List<DeviceInfo> currentList) {
        this.currentList = currentList;
    }

    @Override
    public int getCount()
    {
        return num;
    }

    @Override
    public Object getItem(int position)
    {
        return Device.DEVICE_LIST.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View view1 = inflater.inflate(R.layout.item_power, null);
        TextView tvDeviceNum = (TextView) view1.findViewById(R.id.tv_device_num);

        ImageView imgPower = (ImageView) view1.findViewById(R.id.img_power);

//        DeviceInfo info = Device.DEVICE_LIST.get(i);

        DeviceInfo info = currentList.get(i);
        tvDeviceNum.setText("" + info.getDeviceNum()+"   ");

        switch (info.getPower())
        {
            case 0:
                imgPower.setImageResource(R.drawable.stat_power_empyt);
                tvDeviceNum.setTextColor(Color.argb(200, 105, 105, 105));
                break;
            case 1:
            case 2:
            case 3:
                imgPower.setImageResource(R.drawable.stat_power_01);
                break;
            case 4:
            case 5:
            case 6:
                imgPower.setImageResource(R.drawable.stat_power_03);
                break;
            case 7:
            case 8:
            case 9:
                imgPower.setImageResource(R.drawable.stat_power_04);
                break;
            case 10:
                imgPower.setImageResource(R.drawable.stat_power_full);
                break;
        }
        return view1;
    }

}

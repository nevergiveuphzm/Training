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
            case -1:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim0);
                tvDeviceNum.setTextColor(Color.argb(200, 105, 105, 105));
                break;
            case 0:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim8);
                break;
            case 1:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim15);
                break;
            case 2:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim25);
                break;
            case 3:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim35);
                break;
            case 4:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim45);
                break;
            case 5:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim55);
                break;
            case 6:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim65);
                break;
            case 7:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim75);
                break;
            case 8:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim85);
                break;
            case 9:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim95);
                break;
            case 10:
                imgPower.setImageResource(R.drawable.stat_sys_battery_charge_anim100);
                break;
        }
        return view1;
    }

}

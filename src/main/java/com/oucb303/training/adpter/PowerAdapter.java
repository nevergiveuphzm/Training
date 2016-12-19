package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.device.Device;
import com.oucb303.training.model.DeviceInfo;

import java.util.List;

/**
 * Created by huzhiming on 16/9/23.
 * Description：
 */

public class PowerAdapter extends BaseAdapter
{
    private LayoutInflater inflater = null;

    public PowerAdapter(Context context)
    {
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount()
    {
        if (Device.DEVICE_LIST == null)
            return 0;
        return Device.DEVICE_LIST.size();
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
        TextView tvPower = (TextView) view1.findViewById(R.id.tv_power);
        ImageView imgPower = (ImageView) view1.findViewById(R.id.img_power);

        DeviceInfo info = Device.DEVICE_LIST.get(i);

        tvDeviceNum.setText("设备" + info.getDeviceNum());
//        if (info.getPower() == 0)
//            tvPower.setText("5%");
//        else
//            tvPower.setText(info.getPower() + "0%");
        switch (info.getPower())
        {
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

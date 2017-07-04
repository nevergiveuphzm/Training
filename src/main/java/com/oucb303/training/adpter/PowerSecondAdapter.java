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

/**
 * Created by HP on 2017/7/2.
 * 当设备数量大于8时，用到这个adapter
 */
public class PowerSecondAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;

    public PowerSecondAdapter(Context context)
    {
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount()
    {
        if (Device.DEVICE_LIST == null)
            return 0;
//        if (Device.DEVICE_LIST.size() > 4)
//            return Device.DEVICE_LIST.size()-4;
//        else
//            return 0;
        return Device.DEVICE_LIST.size()-8;
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

        DeviceInfo info = Device.DEVICE_LIST.get(i+8);

        tvDeviceNum.setText( ""+info.getDeviceNum()+"   ");

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

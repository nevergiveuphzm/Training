package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oucb303.training.R;

import java.util.List;
import java.util.Map;

/**
 * Created by huzhiming on 16/9/23.
 * Description：
 */

public class PowerAdapter extends BaseAdapter
{
    private List<Map<String, Object>> powerInfos;
    private LayoutInflater inflater = null;

    public PowerAdapter(Context context, List<Map<String, Object>> powerInfos)
    {
        this.powerInfos = powerInfos;
        this.inflater = LayoutInflater.from(context);
    }

    public List<Map<String, Object>> getPowerInfos()
    {
        return powerInfos;
    }

    public void setPowerInfos(List<Map<String, Object>> powerInfos)
    {
        this.powerInfos = powerInfos;
    }

    @Override
    public int getCount()
    {
        if (powerInfos == null)
            return 0;
        return powerInfos.size();
    }

    @Override
    public Object getItem(int position)
    {
        return powerInfos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View view1 = inflater.inflate(R.layout.sample_power, null);
        TextView tvDeviceNum = (TextView) view1.findViewById(R.id.tv_device_num);
        TextView tvPower = (TextView) view1.findViewById(R.id.tv_power);
        ImageView imgPower = (ImageView) view1.findViewById(R.id.img_power);

        tvDeviceNum.setText("设备" + powerInfos.get(i).get("deviceNum"));
        tvPower.setText(powerInfos.get(i).get("power") + "0%");
        switch ((int) powerInfos.get(i).get("power"))
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

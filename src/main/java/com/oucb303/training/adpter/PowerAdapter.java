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

    @Override
    public int getCount()
    {
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
        tvPower.setText(powerInfos.get(i).get("power") + "%");
        switch ((int)powerInfos.get(i).get("power"))
        {
            case 8:
                break;
        }
        return view1;
    }

}

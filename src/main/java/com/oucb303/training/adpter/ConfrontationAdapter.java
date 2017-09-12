package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.activity.GroupConfrontationActivity;

import java.util.ArrayList;

/**
 * Created by huzhiming on 2016/12/19.
 */

public class ConfrontationAdapter extends BaseAdapter
{
    private Context context;
    private int groupId;
    private ArrayList<GroupConfrontationActivity.GroupLightInfo> lightInfos;

    public ConfrontationAdapter(Context context, int groupId, ArrayList<GroupConfrontationActivity.GroupLightInfo> lightInfos)
    {
        this.context = context;
        this.lightInfos = lightInfos;
        this.groupId = groupId;
    }

    @Override
    public int getCount()
    {
        return lightInfos.size();
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        GroupConfrontationActivity.GroupLightInfo info = lightInfos.get(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_sequenceset_lv_horizontal, null);
        TextView tvNum = (TextView) view.findViewById(R.id.tv_light_num);
        LinearLayout llLight = (LinearLayout) view.findViewById(R.id.ll_image);

        tvNum.setText(info.deviceInfo.getDeviceNum() + "");
        if (info.lightFlag == 0)
            llLight.setBackgroundResource(R.drawable.light_default);
        else if (info.lightFlag == 1)
        {
            if (groupId == 0)
                llLight.setBackgroundResource(R.drawable.light_green);
            else
                llLight.setBackgroundResource(R.drawable.light_red);
        } else if (info.lightFlag == 2)
            llLight.setBackgroundResource(R.drawable.light_blue_red);
        return view;
    }
}

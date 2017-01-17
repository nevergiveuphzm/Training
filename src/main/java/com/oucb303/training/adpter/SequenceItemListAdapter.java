package com.oucb303.training.adpter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.entity.Light;
import com.oucb303.training.entity.SequenceGroup;
import com.oucb303.training.utils.Constant;

import java.util.List;

/**
 * Created by huzhiming on 2017/1/4.
 */

public class SequenceItemListAdapter extends BaseAdapter
{
    private Context context;
    private List<SequenceGroup> sequenceGroupList;

    public SequenceItemListAdapter(Context context, List<SequenceGroup> sequenceGroups)
    {
        this.context = context;
        this.sequenceGroupList = sequenceGroups;
    }

    @Override
    public int getCount()
    {
        if (sequenceGroupList == null)
            return 0;
        return sequenceGroupList.size();
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
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        SequenceGroup sequenceGroup = sequenceGroupList.get(position);
        List<Light> lights = sequenceGroup.getLights();

        view = LayoutInflater.from(context).inflate(R.layout.item_sequence_item_list, null);
        TextView tvStep = (TextView) view.findViewById(R.id.tv_item_step);
        LinearLayout llItemList = (LinearLayout) view.findViewById(R.id.ll_item_list);
        int num = (lights.size() + 7) / 8;
        for (int i = 0; i < num; i++)
        {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 8; j++)
            {
                int p = i * 8 + j;
                if (p >= lights.size())
                    break;
                Light light = lights.get(p);
                View lightView = LayoutInflater.from(context).inflate(R.layout.item_sequence_running_light, null);
                LinearLayout llImage = (LinearLayout) lightView.findViewById(R.id.ll_image);
                TextView tvNum = (TextView) lightView.findViewById(R.id.tv_light_num);


                tvNum.setText((char) light.getDeviceNum() + "");
                linearLayout.addView(lightView);
            }
            llItemList.addView(linearLayout);
        }

        tvStep.setText("步骤" + (position + 1));
        return view;
    }
}

package com.oucb303.training.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.daoservice.SequenceSer;
import com.oucb303.training.entity.Light;
import com.oucb303.training.entity.Sequence;
import com.oucb303.training.entity.SequenceGroup;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.DateUtil;

import java.util.List;

/**
 * Created by huzhiming on 2016/12/29.
 */

public class SequenceListAdapter extends BaseAdapter
{
    private List<Sequence> sequenceList;
    private Context context;
    private SequenceSer sequenceSer;

    public SequenceListAdapter(Context context, List<Sequence> sequenceList, SequenceSer sequenceSer)
    {
        this.context = context;
        this.sequenceList = sequenceList;
        this.sequenceSer = sequenceSer;
//        for (Sequence sequence : sequenceList)
//        {
//            Log.d(Constant.LOG_TAG, "sequence:" + sequence.toString());
//            for (SequenceGroup group : sequence.getGroups())
//            {
//                Log.d(Constant.LOG_TAG, "Items:" + group.toString());
//                for (Light light : group.getLights())
//                {
//                    Log.d(Constant.LOG_TAG, "light:" + light.toString());
//                }
//            }
//        }
    }

    @Override
    public int getCount()
    {
        if (sequenceList == null)
            return 0;
        return sequenceList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return sequenceList.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        Sequence sequence = sequenceList.get(i);
        view = LayoutInflater.from(context).inflate(R.layout.item_sequence_list, null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_sequence_name);
        TextView tvCreateTime = (TextView) view.findViewById(R.id.tv_sequence_create_time);
        TextView tvDel = (TextView) view.findViewById(R.id.tv_del);

        tvName.setText(sequence.getName());
        tvCreateTime.setText(DateUtil.DateToString(sequence.getCreateTime(), "yyyy/MM/dd HH:mm:ss"));
        view.setTag(sequence.getId());
        tvDel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sequenceSer.delSequence(sequenceList.get(i).getId());
                sequenceList.remove(i);
                SequenceListAdapter.this.notifyDataSetChanged();
            }
        });
        return view;
    }
}

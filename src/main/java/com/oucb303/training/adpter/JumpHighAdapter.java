package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huzhiming on 2016/11/29.
 */

public class JumpHighAdapter extends BaseAdapter
{
    private List<HashMap<String, Object>> scores;
    private Context context;

    public JumpHighAdapter(Context context, List<HashMap<String, Object>> scores)
    {
        this.scores = scores;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        if (scores == null)
            return 0;
        return scores.size();
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
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = LayoutInflater.from(context).inflate(R.layout.item_statistics_time, null);

        Map<String, Object> map = scores.get(i);
        TextView tvGroupNum = (TextView) view.findViewById(R.id.tv_num);
        TextView tvLast = (TextView) view.findViewById(R.id.tv_time);
        TextView tvScore = (TextView) view.findViewById(R.id.tv_note);

        tvGroupNum.setText("第" + (i + 1) + "组");
        if (!map.isEmpty())
        {
            tvLast.setText(map.get("lights").toString());
            tvScore.setText(map.get("score") + "");
        }
        return view;
    }
}

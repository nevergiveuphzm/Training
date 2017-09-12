package com.oucb303.training.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oucb303.training.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by HP on 2016/12/5.
 */
public class DribblingGameAdapter extends BaseAdapter {

    private int[] scores;
    private Context context;

    public DribblingGameAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (scores == null)
            return 0;
        return scores.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = LayoutInflater.from(context).inflate(R.layout.item_shuttle_run_group_time1, null);
        //组号
        TextView tvGroupNum = (TextView) view.findViewById(R.id.tv_group_id);
        //图标
        ImageView imgGroupId = (ImageView) view.findViewById(R.id.img_group);
        //得分
        TextView tvScore = (TextView) view.findViewById(R.id.tv_group_finish_times);
        //备注
        TextView tvNote = (TextView) view.findViewById(R.id.tv_total_time);

        tvNote.setVisibility(View.INVISIBLE);

        tvGroupNum.setText("第"+ (i+1) + "组");
        tvScore.setText(scores[i] + "");
        if (scores[i]<0)
            tvScore.setText("0");

        return view;
    }
    public void setScores(int[] scores)
    {
        this.scores = scores;
    }

}

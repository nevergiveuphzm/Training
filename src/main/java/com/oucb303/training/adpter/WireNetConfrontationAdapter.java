package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;

/**
 * Created by HP on 2017/5/25.
 */
public class WireNetConfrontationAdapter extends BaseAdapter {

    private int[] scores;
    private Context context;

    public WireNetConfrontationAdapter(Context context){
        this.context = context;
    }
    public void setScores(int[] scores){
        this.scores = scores;
    }

    @Override
    public int getCount() {
        if (scores == null)
            return 0;
        return scores.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_dribbling_scores, null);
        TextView tvGroupNum = (TextView) view.findViewById(R.id.tv_group_id);
        TextView tvLightNum = (TextView) view.findViewById(R.id.tv_finish_times);

        tvGroupNum.setText("第"+ (i+1) + "组");
        tvLightNum.setText(scores[i] + "");
        if (scores[i]<0)
            tvLightNum.setText("0");

        return view;
    }


}

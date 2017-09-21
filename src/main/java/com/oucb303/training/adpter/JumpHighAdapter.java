package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        view = LayoutInflater.from(context).inflate(R.layout.item_statistics_time2, null);

        Map<String, Object> map = scores.get(i);
       TextView tvGroupNum = (TextView) view.findViewById(R.id.tv_num);
        ImageView imgGroupId = (ImageView) view.findViewById(R.id.img_group);
        TextView tvLast = (TextView) view.findViewById(R.id.tv_time);
        TextView tvScore = (TextView) view.findViewById(R.id.tv_note);


        tvGroupNum.setText("第" + (i + 1) + "组");
        imgGroupId.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
        if (i<3){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(168,85,80,0);//4个参数按顺序分别是左上右下
            tvLast.setLayoutParams(layoutParams);

            layoutParams.setMargins(170,85,80,0);
            tvScore.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams paramTest = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            paramTest.leftMargin = 10;
            paramTest.topMargin=5;
            imgGroupId.setLayoutParams(paramTest);
            imgGroupId.setImageResource(imgId[i]);
        }else{
            RelativeLayout.LayoutParams paramImg = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(170,55,80,0);
            tvLast.setLayoutParams(layoutParams);
            paramImg.leftMargin = 80;
            paramImg.topMargin=38;
            imgGroupId.setLayoutParams(paramImg);
            imgGroupId.setImageResource(imgId[3]);
        }
        if (!map.isEmpty())
        {
            tvLast.setText(map.get("lights").toString());
            tvScore.setText(map.get("score") + "");
        }
        return view;
    }
}

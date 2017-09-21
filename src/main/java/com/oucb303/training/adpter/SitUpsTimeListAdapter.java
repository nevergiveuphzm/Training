package com.oucb303.training.adpter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
import java.util.Map;

/**
 * Created by huzhiming on 2016/11/7.
 */

public class SitUpsTimeListAdapter extends BaseAdapter
{
    //每组成绩
    private int[] scores;
    private Context context;
    //key:groupId   value:scores
    private Map<Integer,Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;

    public SitUpsTimeListAdapter(Context context)
    {
        this.context = context;
    }

    public void setScores(int[] scores)
    {
        this.scores = scores;
    }

    @Override
    public int getCount()
    {
        if (scores != null)
            return scores.length;
        return 0;
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
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_shuttle_run_group_time1, null);

        TextView tvGroupNum = (TextView) view.findViewById(R.id.tv_group_id);
        ImageView imgGroupId = (ImageView) view.findViewById(R.id.img_group);
        TextView tvFinishTimes = (TextView) view.findViewById(R.id.tv_group_finish_times);
        TextView tvNote = (TextView) view.findViewById(R.id.tv_total_time);

        tvGroupNum.setText((i + 1) + "组");
        tvFinishTimes.setText(scores[i] + "");

        imgGroupId.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
        if (i<3){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(168,85,80,0);//4个参数按顺序分别是左上右下
            tvFinishTimes.setLayoutParams(layoutParams);

            layoutParams.setMargins(170,85,80,0);
            tvNote.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams paramTest = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            paramTest.leftMargin = 10;
            paramTest.topMargin=5;
            imgGroupId.setLayoutParams(paramTest);
            imgGroupId.setImageResource(imgId[i]);
        }else{
            RelativeLayout.LayoutParams paramImg = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(170,55,80,0);
            tvFinishTimes.setLayoutParams(layoutParams);
            paramImg.leftMargin = 80;
            paramImg.topMargin=38;
            imgGroupId.setLayoutParams(paramImg);
            imgGroupId.setImageResource(imgId[3]);
        }
        int Id = keyId[i];
        if (timeMap.get(Id) != 0){
            tvGroupNum.setText((Id+1) +" 组");
            tvFinishTimes.setText(timeMap.get(Id)+"");
        }else {
            tvGroupNum.setText((i + 1) + "组");
            tvFinishTimes.setText(0 + "");
        }
        return view;
    }
    public void setTimeMap(Map<Integer, Integer> timeMap, int[] keyId) {
        this.timeMap = timeMap;
        this.keyId = keyId;
        Iterator iter = timeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            int val = (int) entry.getValue();
            Log.i("timeMap里有什么",""+key+"---"+val);
        }
    }

}

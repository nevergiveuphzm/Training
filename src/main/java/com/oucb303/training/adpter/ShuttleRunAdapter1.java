package com.oucb303.training.adpter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.model.TimeInfo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by huzhiming on 16/10/13.
 * Description：
 */

public class ShuttleRunAdapter1 extends BaseAdapter
{

    private Context context;
    private int[] completedTimes;
    private int[] finishTime;
    private Map<Integer,Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;

    public ShuttleRunAdapter1(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount()
    {
        if (completedTimes == null)
            return 0;
        return completedTimes.length;
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
    public View getView(int position, View view, ViewGroup viewGroup)
    {
//            list_time.add(position,finishTime[position]);
//            Log.i("finish_time里面有什么",""+finishTime[position]);
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_shuttle_run_group_time1, null);
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_group_id);
        ImageView imgGroupId = (ImageView) view.findViewById(R.id.img_group);
        TextView tvFinishTimes = (TextView) view.findViewById(R.id.tv_group_finish_times);
        TextView tvGroupTotalTime = (TextView) view.findViewById(R.id.tv_total_time);

        tvGroupId.setText((position + 1) + " 组");
        tvFinishTimes.setText(completedTimes[position] + "");


        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
        if (position<3){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(168,85,80,0);//4个参数按顺序分别是左上右下
            tvFinishTimes.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams paramTest = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            paramTest.leftMargin = 20;
            paramTest.topMargin=5;
            imgGroupId.setLayoutParams(paramTest);
            imgGroupId.setImageResource(imgId[position]);
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


       if (completedTimes[position]<0)
           tvFinishTimes.setText("0");
//        if (finishTime[position] != 0)
//        {
//            int time = finishTime[position];
////                Log.i("此时的时间是：",""+time);
//            int minute = time / (1000 * 60);
//            int second = (time / 1000) % 60;
//            int msec = time % 1000;
//            String res = "";
//            res += minute == 0 ? "" : minute + ":";
//            res += second + ":" + msec / 10;
//            tvGroupTotalTime.setText(res);
//        }
        int Id = keyId[position];
        if (timeMap.get(Id) != 0)
        {
            int time = timeMap.get(Id);
//                Log.i("此时的时间是：",""+time);
            int minute = time / (1000 * 60);
            int second = (time / 1000) % 60;
            int msec = time % 1000;
            String res = "";
            res += minute == 0 ? "" : minute + ":";
            res += second + ":" + msec / 10;
            tvGroupTotalTime.setText(res);
            tvGroupId.setText((Id + 1)+ " 组");
        }
        else
            tvGroupTotalTime.setText("---");

        return view;
    }

    public void setFinishTime(int[] finishTime)
    {
        this.finishTime = finishTime;
    }

    public void setCompletedTimes(int[] completedTimes)
    {
        this.completedTimes = completedTimes;
    }

    public void setTimeMap(Map<Integer, Integer> timeMap,int[] keyId) {
        this.timeMap = timeMap;
        this.keyId = keyId;
//        Iterator iter = timeMap.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            int key = (int) entry.getKey();
//            int val = (int) entry.getValue();
//            Log.i("timeMap里有什么",""+key+"---"+val);
//        }
        for (int i=0;i<keyId.length;i++)
            Log.i("keyId里有什么",""+keyId[i]);
    }
}

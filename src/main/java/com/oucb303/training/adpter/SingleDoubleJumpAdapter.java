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
import java.util.Map;

/**
 * Created by john on 2017/9/26.
 */

public class SingleDoubleJumpAdapter extends BaseAdapter {
    private Context context;
    private int[] completedTimes;
    private int[] finishTime;
    private Map<Integer,Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;

    public SingleDoubleJumpAdapter(Context context)
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

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_single_double_jump, null);
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_group_id);
        ImageView imgGroupId = (ImageView) view.findViewById(R.id.img_group);
        TextView tvAverageTime = (TextView) view.findViewById(R.id.tv_group_average_time);
        TextView tvGroupTotalTime = (TextView) view.findViewById(R.id.tv_total_time);

        tvGroupId.setText((position + 1) + " 组");
       // tvFinishTimes.setText(completedTimes[position] + "");
        tvAverageTime.setText("---");

        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
        if (position<3){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(168,85,80,0);//4个参数按顺序分别是左上右下
            tvAverageTime.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams paramTest = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            paramTest.leftMargin = 20;
            paramTest.topMargin=5;
            imgGroupId.setLayoutParams(paramTest);
            imgGroupId.setImageResource(imgId[position]);
        }else{
            RelativeLayout.LayoutParams paramImg = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(170,55,80,0);
            tvAverageTime.setLayoutParams(layoutParams);
            paramImg.leftMargin = 80;
            paramImg.topMargin=38;
            imgGroupId.setLayoutParams(paramImg);
            imgGroupId.setImageResource(imgId[3]);
        }


        if (completedTimes[position]<0)
            tvAverageTime.setText("0");

        int Id = keyId[position];
        if (timeMap.get(Id) != 0)
        {
            int time = timeMap.get(Id);
//                Log.i("此时的时间是：",""+time);
//            int minute = time / (1000 * 60);
//            int second = (time / 1000) % 60;
//            int msec = time % 1000;
//            String res = "";
//            res += minute == 0 ? "" : minute + ":";
//            res += second + ":" + msec / 10;
            Log.i("============", "time=" + time);
            tvGroupTotalTime.setText(time+"ms");
            tvAverageTime.setText(time/(completedTimes[position]+1)+"ms");
            tvGroupId.setText((Id + 1)+ " 组");

        }
        else {
            tvGroupTotalTime.setText("---");
            tvAverageTime.setText("---");
        }

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

        for (int i=0;i<keyId.length;i++)
            Log.i("keyId里有什么",""+keyId[i]);
    }
}

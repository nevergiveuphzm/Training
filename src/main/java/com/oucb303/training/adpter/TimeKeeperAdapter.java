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

/**
 * Created by HP on 2017/3/30.
 */
public class TimeKeeperAdapter extends BaseAdapter {
    private Context context;
    private int[] completedTimes;//每组完成次数
    //每组最终完成所有次数所用时间
    private int[] finishTime;


    public TimeKeeperAdapter(Context context)
    {
        this.context = context;
    }

    public void setCompletedTimes(int[] completedTimes){
        this.completedTimes = completedTimes;
    }
    public void setFinishTime(int[] finishTime)
    {
        this.finishTime = finishTime;
    }
    @Override
    public int getCount() {
        if (completedTimes == null)
            return 0;
        return completedTimes.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.item_time_keeper,null);
        }
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_group_id);
        TextView tvFinishTimes = (TextView) view.findViewById(R.id.tv_finish_times);
        ImageView imgGroupId = (ImageView) view.findViewById(R.id.img_group);
//        TextView tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
        TextView tvNote = (TextView) view.findViewById(R.id.tv_note);

        tvGroupId.setText((i + 1) + "组");
        tvFinishTimes.setText(completedTimes[i] + "");

        imgGroupId.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
        if (i<3){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(168,85,80,0);//4个参数按顺序分别是左上右下
            tvFinishTimes.setLayoutParams(layoutParams);

            layoutParams.setMargins(140,65,100,0);
            tvNote.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams paramTest = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            paramTest.leftMargin = 10;
            paramTest.topMargin=5;
            imgGroupId.setLayoutParams(paramTest);
            imgGroupId.setImageResource(imgId[i]);
        }else{
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(165,55,85,0);
            tvFinishTimes.setLayoutParams(layoutParams);
            layoutParams.setMargins(140,65,100,0);
            tvNote.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams paramImg = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            paramImg.leftMargin = 70;
            paramImg.topMargin=38;
            imgGroupId.setLayoutParams(paramImg);
            imgGroupId.setImageResource(imgId[3]);
        }

//        if (finishTime[i] != 0)
//        {
//            int time = finishTime[i];
//            int minute = time / (1000 * 60);
//            int second = (time / 1000) % 60;
//            int msec = time % 1000;
//            String res = "";
//            res += minute == 0 ? "" : minute + ":";
//            res += second + ":" + msec / 10;
//            tvTotalTime.setText(res);
//        } else
//            tvTotalTime.setText("---");
        tvNote.setText("点击查看");
        return view;
    }
}

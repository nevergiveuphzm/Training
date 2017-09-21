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
import com.oucb303.training.model.TimeInfo;

import java.util.List;

/**
 * Created by HP on 2017/3/30.
 */
public class TimingModuleAdapter extends BaseAdapter {

    //TimeInfo里包括：设备编号，返回的时间，数据是否有效
    private List<TimeInfo> timeList;
    private Context context;
    private int trainingTime;

    public TimingModuleAdapter(Context context,List<TimeInfo> timeList)
    {
        this.context = context;
        this.timeList = timeList;
    }

    public void setTrainingTime(int trainingTime)
    {
        this.trainingTime = trainingTime;
    }
    @Override
    public int getCount() {
        if (timeList == null)
            return 0;
        else
            return timeList.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.item_time_module,null);
        TextView tvNum = (TextView) view.findViewById(R.id.tv_num);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
//        TextView tvFinishTimes = (TextView) view.findViewById(R.id.tv_finish_times);
        TextView tvLightNum = (TextView) view.findViewById(R.id.tv_light_num);
        TextView tvNote = (TextView) view.findViewById(R.id.tv_note);
        ImageView imgGroupId=(ImageView)view.findViewById(R.id.img_group);
        tvNum.setText((i+1)+"");
//        Log.i("trainingTime:------",""+trainingTime);
        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
        if (i<3){
            imgGroupId.setImageResource(imgId[i]);
        }
        else{
            RelativeLayout.LayoutParams paramImg = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(170,55,80,0);
            paramImg.leftMargin = 80;
            paramImg.topMargin=38;
            imgGroupId.setLayoutParams(paramImg);
            imgGroupId.setImageResource(imgId[3]);
//            layoutParams.setMargins(170,85,80,0);
//            tvTime.setLayoutParams(layoutParams);
        }
        if ((timeList.get(i).getTime() == 0)||(timeList.get(i).getTime() >= trainingTime))
        {
            tvTime.setText("---");
//            tvFinishTimes.setText("---");
            tvNote.setText("超时");
        }
        else
        {
            tvTime.setText(timeList.get(i).getTime()+" 毫秒");
            tvNote.setText("---");
        }
        tvLightNum.setText(timeList.get(i).getDeviceNum()+"");

        return view;
    }
}

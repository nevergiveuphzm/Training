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
import com.oucb303.training.model.TimeInfo;

import java.util.List;

/**
 * Created by HP on 2017/4/7.
 */
public class RandomTimesModuleAdapter extends BaseAdapter {

    //TimeInfo里包括：设备编号，返回的时间，数据是否有效
    private List<TimeInfo> timeList;
    private Context context;
    private LayoutInflater inflater;

    public RandomTimesModuleAdapter(Context context,List<TimeInfo> list)
    {
        this.context = context;
        this.timeList = list;
        inflater = LayoutInflater.from(context);
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
       View v = inflater.inflate(R.layout.item_statistics_time3, null);

        TextView num = (TextView) v.findViewById(R.id.tv_num);
        ImageView imgGroupId = (ImageView) v.findViewById(R.id.img_group);
        TextView time = (TextView) v.findViewById(R.id.tv_time);
        TextView note = (TextView) v.findViewById(R.id.tv_note);
        TextView lightNum = (TextView) v.findViewById(R.id.tv_light_num);

        num.setText((i + 1) + "");
        imgGroupId.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
//        if (i<3){
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(168,85,80,0);//4个参数按顺序分别是左上右下
//            time.setLayoutParams(layoutParams);
//
//            layoutParams.setMargins(170,85,80,0);
//            note.setLayoutParams(layoutParams);
//
//            RelativeLayout.LayoutParams paramTest = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
//            paramTest.leftMargin = 10;
//            paramTest.topMargin=5;
//            imgGroupId.setLayoutParams(paramTest);
//            imgGroupId.setImageResource(imgId[i]);
//        }
//        else {
//            RelativeLayout.LayoutParams paramImg = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(170, 55, 80, 0);
//            time.setLayoutParams(layoutParams);
//            paramImg.leftMargin = 80;
//            paramImg.topMargin = 38;
//            imgGroupId.setLayoutParams(paramImg);
//            imgGroupId.setImageResource(imgId[3]);
//        }
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
    }


            if (timeList.get(i).getTime() == 0)
        {
            time.setText("---");
            note.setText("超时");
        }
        else
        {
            time.setText(timeList.get(i).getTime()+"毫秒");
            note.setText("---");
        }
        lightNum.setText(timeList.get(i).getDeviceNum() + "");
        return v;
    }
}

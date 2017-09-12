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
import java.util.Map;

/**
 * Created by HP on 2017/9/3.
 */
public class OrientRunAdapter extends BaseAdapter {
    private int[] scores;
    private Context context;
    //key:groupId   value:scores
    private Map<Integer,Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;

    public OrientRunAdapter(Context context)
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

        imgGroupId.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
        if (i<3){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(310,85,80,0);//4个参数按顺序分别是左上右下
            tvScore.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams paramTest = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            paramTest.leftMargin = 20;
            paramTest.topMargin=5;
            imgGroupId.setLayoutParams(paramTest);
            imgGroupId.setImageResource(imgId[i]);
        }else{
            RelativeLayout.LayoutParams paramImg = (RelativeLayout.LayoutParams) imgGroupId.getLayoutParams();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(310,55,80,0);
            tvScore.setLayoutParams(layoutParams);
            paramImg.leftMargin = 80;
            paramImg.topMargin=38;
            imgGroupId.setLayoutParams(paramImg);
            imgGroupId.setImageResource(imgId[3]);
        }

//        tvGroupNum.setText("第"+ (i+1) + "组");
//        tvScore.setText(scores[i] + "");
//        if (scores[i]<0)
//            tvScore.setText("0");

        int Id = keyId[i];
        tvGroupNum.setText((Id+1) +" 组");
        tvScore.setText(timeMap.get(Id)+"");

        return view;
    }
    public void setScores(int[] scores)
    {
        this.scores = scores;
    }

    public void setTimeMap(Map<Integer, Integer> timeMap, int[] keyId) {
        this.timeMap = timeMap;
        this.keyId = keyId;
        Iterator iter = timeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            int val = (int) entry.getValue();
            Log.i("timeMap里有什么", "" + key + "---" + val);
        }
    }
}

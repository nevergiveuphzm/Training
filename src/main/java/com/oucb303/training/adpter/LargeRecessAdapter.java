
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
 * Created by HP on 2017/2/27.
 */
public class LargeRecessAdapter extends BaseAdapter {

    private Context context;
    private int[] completedTimes;//规定时间内的完成次数
    //key:groupId   value:scores
    private Map<Integer,Integer> timeMap = new HashMap<Integer, Integer>();
    //存放排序后的key值
    private int[] keyId;


    public LargeRecessAdapter(Context context){
        this.context = context;
    }

    public void setCompletedTimes(int[] completedTimes){
        this.completedTimes = completedTimes;
//        for(int i=0;i<completedTimes.length;i++)
//            Log.i("completedTimes是多少？",""+completedTimes[i]);
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

    @Override
    public int getCount() {
        if (completedTimes == null)
            return 0;
        return completedTimes.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_largerecess_completedtimes_list1,null);
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_group_id);
        ImageView imgGroupId = (ImageView) view.findViewById(R.id.img_group);
        TextView tvTimes = (TextView) view.findViewById(R.id.tv_times);
       TextView tvNote = (TextView) view.findViewById(R.id.tv_note);
//        int Id = keyId[i];
//        tvGroupId.setText((Id+1) +" 组");
//        tvTimes.setText(timeMap.get(Id)+"");
        Log.i("completedTimes[i]是什么",""+completedTimes[i]);
        tvGroupId.setText("第"+(i+1)+"组");
        tvTimes.setText(completedTimes[i]+"");

        imgGroupId.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int[] imgId = new int[]{R.drawable.champion,R.drawable.silver,R.drawable.bronze,R.drawable.other};
        if (i<3){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(168,85,80,0);//4个参数按顺序分别是左上右下
            tvTimes.setLayoutParams(layoutParams);

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
            tvTimes.setLayoutParams(layoutParams);
            paramImg.leftMargin = 80;
            paramImg.topMargin=38;
            imgGroupId.setLayoutParams(paramImg);
            imgGroupId.setImageResource(imgId[3]);
        }
        if (completedTimes[i]<0)
            tvTimes.setText("0");
        return view;
    }
}
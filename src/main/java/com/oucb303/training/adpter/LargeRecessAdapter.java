package com.oucb303.training.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_largerecess_completedtimes_list,null);
        TextView tvGroupId = (TextView) view.findViewById(R.id.tv_group_id);
        TextView tvTimes = (TextView) view.findViewById(R.id.tv_times);

//        int Id = keyId[i];
//        tvGroupId.setText((Id+1) +" 组");
//        tvTimes.setText(timeMap.get(Id)+"");
        Log.i("completedTimes[i]是什么",""+completedTimes[i]);
        tvGroupId.setText("第"+(i+1)+"组");
        tvTimes.setText(completedTimes[i]+"");
        if (completedTimes[i]<0)
            tvTimes.setText("0");
        return view;
    }
}

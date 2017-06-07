package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by baichangcai on 2017/5/19.
 * 训练完毕后的排名适配器
 */
public class RankingListViewAdapter extends BaseAdapter {
    private List<Integer> mList = new ArrayList<>();
    private Context mContext;
    private String mTotal;
    private int mType;//0:表示时间随机，1：表示次数随机
    public RankingListViewAdapter(Context context,List<Integer> list,String total,int type){
        this.mContext = context;
        this.mList = list;
        this.mTotal = total;
        this.mType = type;
    }
    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group_divide,null);
        TextView tvGroupNum = (TextView) convertView.findViewById(R.id.tv_group_num);
        TextView tvTimes = (TextView) convertView.findViewById(R.id.tv_group_device);
        TextView tvAllTime = (TextView) convertView.findViewById(R.id.tv_allTime);

//        Collections.sort(mList);
        List<Score> listScore = new ArrayList<>();
        for(int i=0;i<mList.size();i++){
            Score score = new Score();
            score.groupNum = i;
            score.time = mList.get(i);
            listScore.add(score);
        }

        if(mType==0){
            Collections.sort(listScore, new Comparator<Score>() {
                @Override
                public int compare(Score lhs, Score rhs) {
                    int time0 = lhs.time;
                    int time1 = rhs.time;
                    if(time1>time0)
                        return 1;
                    else if(time1==time0)
                        return 0;
                    else
                        return -1;
                }
            });
            tvGroupNum.setText("第" + (listScore.get(position).groupNum + 1) + "组");
            //时间随机，填充次数
            tvTimes.setText(listScore.get(position).time+"次");
            if(listScore.get(position).time!=0){
                tvAllTime.setText(Integer.valueOf(mTotal)/listScore.get(position).time+"毫秒");
            }else {
                tvAllTime.setText(0+"毫秒");
            }

        }else {
            Collections.sort(listScore, new Comparator<Score>() {
                @Override
                public int compare(Score lhs, Score rhs) {
                    int time0 = lhs.time;
                    int time1 = rhs.time;
                    if(time1<time0)
                        return 1;
                    else if(time1==time0)
                        return 0;
                    else
                        return -1;
                }
            });
            tvGroupNum.setText("第" + (listScore.get(position).groupNum + 1) + "组");
            //次数随机，填充时间
            tvAllTime.setText((listScore.get(position).time/Integer.valueOf(mTotal))+"毫秒");
            tvTimes.setText(mTotal);
        }
        return convertView;
    }
   static class Score{
        int groupNum;
        int time;
    }
}

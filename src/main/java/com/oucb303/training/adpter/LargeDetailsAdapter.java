package com.oucb303.training.adpter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017/2/28.
 */
public class LargeDetailsAdapter extends BaseAdapter {

    private Context context;
    private int groupId;
    private int[] completedTimes;//规定时间内的完成次数
    private List<Integer> finishTime;//每次完成所用时间

    public LargeDetailsAdapter(Context context,List<Integer> finishTime,int groupId){
        this.context = context;
        this.finishTime = finishTime;
        this.groupId = groupId;
    }

    //getCount决定了listview一共有多少个item
    @Override
    public int getCount() {

        return finishTime.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_largerecess_completedtimes_list, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        //关于finishTime的逻辑写道这里
            holder.tvGroupId.setText("第"+(groupId+1)+"组");
            holder.tvTimes.setText("第"+ (position+1) +"次");
//            Log.i("tag","组号= "+(groupId+1)+" 次数" +
//                    "= "+(position+1)+ "每次所用时间= "+finishTime.get(position));

        int time = finishTime.get(position);
        int minute = time / (1000 * 60);//分
        int second = (time / 1000) % 60;//秒
        int msec = time % 1000;
        String res = "";

//        res += minute == 0 ? "00:" : minute + ":";
        if (minute!=0)
            res =minute + ":";
        else
            res="00:";
//        Log.i("res---------------",""+res+"minute-----------"+minute);
        res += second + ":" ;
        if (second<=9){
            if (minute<=9)
                 res="0"+minute + ":"+"0"+second+":";
            else
                res = minute + ":"+"0"+second+":";
        }
        if ((msec/10)<=9) {
           res += "0" + msec / 10;
        }else
            res  += msec / 10;

        holder.tvNote.setText(res);

        return view;
    }

    class ViewHolder {
        private TextView tvGroupId;
        private TextView tvTimes;
        private  TextView tvNote;

        public ViewHolder(View view){
            tvGroupId = (TextView) view.findViewById(R.id.tv_group_id);//组号
            tvTimes = (TextView) view.findViewById(R.id.tv_times);//次数
            tvNote = (TextView) view.findViewById(R.id.tv_note);//每次所用时间
        }
    }
}







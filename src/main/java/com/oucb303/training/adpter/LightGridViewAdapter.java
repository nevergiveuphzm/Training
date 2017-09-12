package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.model.Light;

import java.util.List;

/**
 * Created by baichangcai on 2016/9/23.
 */
public class LightGridViewAdapter extends BaseAdapter
{
    private LayoutInflater inflater = null;
    private List<Light> list_light;
    private ChangeLightClickListener mListener;

    public LightGridViewAdapter(Context context, List<Light> list, ChangeLightClickListener listener)
    {
        this.mListener = listener;
        //接收灯的数量，初始化
        this.list_light = list;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount()
    {
        return list_light.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list_light.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sequenceset_lv_horizontal, null);
            holder.ll_image = (LinearLayout) convertView.findViewById(R.id.ll_image);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_light_num);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        //如果被选中，选择亮图片
        if (list_light.get(position).isChecked())
            holder.ll_image.setBackgroundResource(R.drawable.light_green);
        else
            holder.ll_image.setBackgroundResource(R.drawable.lostfocus_light);

        char num = 'A';
        if (list_light.get(position).getNum() <= 26)
            num = (char) (list_light.get(position).getNum() + 'A' - 1);
        else
            num = (char) (list_light.get(position).getNum() + 'a' - 27);
        holder.tv_num.setText(num + "");
        holder.ll_image.setOnClickListener(mListener);
        holder.ll_image.setTag(position);

        return convertView;
    }

    class ViewHolder
    {

        TextView tv_num;
        LinearLayout ll_image;
    }

    //* 用于回调的抽象类
    public static abstract class ChangeLightClickListener implements View.OnClickListener
    {
        /**
         * 基类的onClick方法
         */
        public void onClick(View v)
        {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}

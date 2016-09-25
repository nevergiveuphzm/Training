package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.model.Light;

import java.util.List;

/**
 * Created by baichangcai on 2016/9/23.
 */
public class LightGridViewAdapter extends BaseAdapter{
    private LayoutInflater inflater = null;
    private List<Light> list_light;
    private ChangeLightClickListener mListener;
    public LightGridViewAdapter(Context context,List<Light> list,ChangeLightClickListener listener) {
        this.mListener = listener;
        //接收灯的数量，初始化
        this.list_light = list;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return list_light.size();
    }

    @Override
    public Object getItem(int position) {
        return list_light.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_sequenceset_lv_horizontal, null);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_light);
            holder.tv_light_num = (TextView) convertView.findViewById(R.id.tv_light_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //如果被选中，选择亮图片
        if(list_light.get(position).isChecked()){
            holder.image.setImageResource(R.drawable.aerow_winter);
        }else {
            holder.image.setImageResource(R.drawable.iv_circle);
        }
        holder.image.setOnClickListener(mListener);
        holder.image.setTag(position);

        return convertView;
    }
    class ViewHolder {

        TextView tv_light_num;
        ImageView image;
    }
    //* 用于回调的抽象类
    public static abstract class ChangeLightClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }
}

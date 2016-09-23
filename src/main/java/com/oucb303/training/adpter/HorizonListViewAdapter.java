package com.oucb303.training.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.device.Light;


/**
 * Created by baichangcai on 2016/9/20.
 */
public class HorizonListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private List<Light> list_light = new ArrayList<>();

    public HorizonListViewAdapter(Context context, List<Light> list) {
        this.inflater = LayoutInflater.from(context);
        this.list_light = list;
    }

    @Override
    public int getCount() {
        //如果goods是空的话，返回1，因为要把“添加”按钮加到最后
        if (list_light.size() == 0) {
            return 1;
        } else {
            return list_light.size();
        }
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
            holder.image = (ImageView) convertView.findViewById(R.id.iv_history_bill);
            holder.item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            holder.image.setImageResource(list_light.get(position).getImageId());


        return convertView;
    }

    class ViewHolder {
        LinearLayout item;
        ImageView image;
    }

}


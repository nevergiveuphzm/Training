package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.oucb303.training.R;
import com.oucb303.training.device.Light;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baichangcai on 2016/9/23.
 */
public class LightGridViewAdapter extends BaseAdapter{
    private LayoutInflater inflater = null;
    private List<Light> list_light;
    public LightGridViewAdapter(Context context,int[] images) {
        inflater = LayoutInflater.from(context);

        list_light = new ArrayList<>();

        for (int i = 0; i < images.length; i++)
        {
            Light light = new Light(images[i]);
            list_light.add(light);
        }
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageResource(list_light.get(position).getImageId());

        return convertView;
    }
    class ViewHolder {

        ImageView image;
    }
}

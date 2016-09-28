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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by baichangcai on 2016/9/20.
 */
public class HorizonListViewAdapter extends BaseAdapter
{

    private LayoutInflater inflater = null;
    private List<Light> list_light = new ArrayList<>();

    public HorizonListViewAdapter(Context context, List<Light> list)
    {
        this.inflater = LayoutInflater.from(context);
        this.list_light = list;
    }

    public void setList(List<Light> list)
    {
        this.list_light = list;
    }

    @Override
    public int getCount()
    {
        if (list_light == null)
        {
            return 0;
        }
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
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list_light.get(position).isChecked())
        {
            holder.ll_image.setBackgroundResource(R.drawable.aerow_winter);

            holder.tv_num.setText("" + list_light.get(position).getNum());
        }
        else
        {
            holder.ll_image.setBackgroundResource(R.drawable.iv_circle);
            holder.tv_num.setText("" + list_light.get(position).getNum());
        }
        holder.ll_image.setTag(position);
        return convertView;
    }

    class ViewHolder
    {
        LinearLayout ll_image;
        TextView tv_num;
    }

}


package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oucb303.training.R;

import java.util.List;

/**
 * Created by huzhiming on 16/10/9.
 * Description：
 */

public class TrainingListAdapter extends BaseAdapter
{
    private Context context;
    private List<Object> items;

    public TrainingListAdapter(Context context, List<Object> items)
    {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int i)
    {
        return items.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        Object[] item = (Object[]) items.get(i);
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_training_list, null);
        view.setTag(item[3]);
        ImageView img = (ImageView) view.findViewById(R.id.img_item_icon);
        TextView tv = (TextView) view.findViewById(R.id.tv_item_title);
        img.setImageResource((int) item[2]);
        tv.setText(item[0].toString());
        return view;
    }
}

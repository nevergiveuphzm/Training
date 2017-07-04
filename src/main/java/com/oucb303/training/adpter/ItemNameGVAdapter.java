package com.oucb303.training.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oucb303.training.R;


/**
 * Created by huzhiming on 2017/7/1.
 */

public class ItemNameGVAdapter extends BaseAdapter
{
    private Context context;
    private int[][] nameStringIds;
    int itemSelected = 0;


    public ItemNameGVAdapter(Context context, int[][] nameStringIds)
    {
        this.context = context;
        this.nameStringIds = nameStringIds;
    }

    public void changeContent(int id)
    {
        itemSelected = id;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return nameStringIds[itemSelected].length;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.item_navigation_item_name, null);
        TextView textView = (TextView) v.findViewById(R.id.tv_item_name);
        textView.setText(nameStringIds[itemSelected][i]);
        return v;
    }
}

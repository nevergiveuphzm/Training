package com.b303.saflight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.b303.saflight.R;

/**
 * Created by huzhiming on 2017/7/1.
 * 导航栏底部横向ListView 适配器
 */

public class NavigationItemsHLVAdapter extends BaseAdapter
{
    private Context context;
    //底部的图片
    private int[][] imgIds;
    //底部的项目名称
    private int[][] nameStringIds;
    private int itemSelected = 0;

    public NavigationItemsHLVAdapter(Context context, int[][] imgIds, int[][] nameStringIds)
    {
        this.context = context;
        this.imgIds = imgIds;
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
        return imgIds[itemSelected].length;
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
        View v = LayoutInflater.from(context).inflate(R.layout.item_navigation_items, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.iv_item);
        TextView textView = (TextView) v.findViewById(R.id.tv_item_name);
        imageView.setImageResource(imgIds[itemSelected][i]);
        textView.setText(nameStringIds[itemSelected][i]);
        return v;
    }
}

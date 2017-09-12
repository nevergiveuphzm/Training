package com.b303.saflight.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.b303.saflight.R;
import com.b303.saflight.activity.SitUpsActivity;
import com.b303.saflight.adapter.ItemNameGVAdapter;
import com.b303.saflight.adapter.NavigationItemsHLVAdapter;
import com.b303.saflight.widget.HorizontalListView;

import java.util.ArrayList;

/**
 * Created by huzhiming on 2017/6/30.
 */

public class NavigationBuilder implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private Context context;
    private int[][] imgSourceId;
    private ArrayList<ImageView> imageViewList = new ArrayList<>();
    private Category category;
    private TextView tvCategoryName;
    //中间具体项目
    private GridView gridView;
    private ItemNameGVAdapter gvAdapter;
    //中间的布局
    private LinearLayout linearLayout;
    //底部的横向Listview
    private HorizontalListView bottomListView;
    private NavigationItemsHLVAdapter hlvAdapter;
    //导航栏选中编号
    private int selectedCategory = 0;

    public NavigationBuilder(Context context, Category category, GridView gridView, TextView textView, HorizontalListView hlv, LinearLayout layout)
    {
        this.category = category;
        this.imgSourceId = category.getTopImgIds();
        this.gridView = gridView;
        this.context = context;
        this.tvCategoryName = textView;
        this.bottomListView = hlv;
        this.linearLayout = layout;
    }

    //初始化整个页面动态生成顶部的导航栏分类
    public LinearLayout initCategory()
    {
        //初始化顶部的导航栏
        LinearLayout linearLayout = new LinearLayout(context);
        for (int i = 0; i < imgSourceId[0].length; i++)
        {
            View category = LayoutInflater.from(context).inflate(R.layout.item_navigation_category, null);
            ImageView view = (ImageView) category.findViewById(R.id.img_category);
            if (i == 0)
                view.setImageResource(imgSourceId[1][i]);
            else
                view.setImageResource(imgSourceId[0][i]);
            view.setTag(i);
            imageViewList.add(view);
            view.setOnClickListener(this);
            linearLayout.addView(category);
        }
        //初始化中间的gridView
        gvAdapter = new ItemNameGVAdapter(context, category.getCenterNameStringIds());
        gridView.setAdapter(gvAdapter);
        //初始化底部的Listview
        hlvAdapter = new NavigationItemsHLVAdapter(context, category.getBottomImgIds(), category.getCenterNameStringIds());
        bottomListView.setAdapter(hlvAdapter);
        //添加点击事件监听器
        bottomListView.setOnItemClickListener(this);

        return linearLayout;
    }

    //导航栏点击事件,点击图片更换
    @Override
    public void onClick(View view)
    {
        int chooseId = (int) view.getTag();
        selectedCategory = chooseId;
        for (int i = 0; i < imageViewList.size(); i++)
        {
            if (i == (int) view.getTag())
                imageViewList.get(i).setImageResource(imgSourceId[1][i]);
            else
                imageViewList.get(i).setImageResource(imgSourceId[0][i]);
        }
        gvAdapter.changeContent(chooseId);
        hlvAdapter.changeContent(chooseId);
        linearLayout.setBackgroundResource(category.getCenterImgIds()[chooseId]);
        tvCategoryName.setText(category.getCategoryNamesId()[chooseId]);
    }

    //底部Listview的点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Toast.makeText(context, category.getCenterNameStringIds()[selectedCategory][i], Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent();
        //context.startActivity(intent);
        if(selectedCategory==4&&i==0){
            Intent intent = new Intent();
            intent.setClass(context, SitUpsActivity.class);
            context.startActivity(intent);
        }
    }
}

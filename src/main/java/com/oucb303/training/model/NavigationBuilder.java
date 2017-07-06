package com.oucb303.training.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.oucb303.training.R;
import com.oucb303.training.activity.LargeRecessActivity;
import com.oucb303.training.activity.RandomTrainingActivity;
import com.oucb303.training.activity.ShuttleRunActivity1;
import com.oucb303.training.adpter.ItemNameGVAdapter;
import com.oucb303.training.adpter.NavigationItemsHLVAdapter;
import com.oucb303.training.widget.HorizontalListView;

import java.util.ArrayList;
import java.util.Objects;

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

    private int type;

    Category c1 = new CategoryExerciseAbility();
    String[][] targetString1 = c1.getTargetString();
    Category c2 = new MotorTechnique();
    String[][] targetString2 = c2.getTargetString();


    public NavigationBuilder(Context context, Category category, GridView gridView, TextView textView, HorizontalListView hlv, LinearLayout layout,int type)
    {
        this.category = category;
        this.imgSourceId = category.getTopImgIds();
        this.gridView = gridView;
        this.context = context;
        this.tvCategoryName = textView;
        this.bottomListView = hlv;
        this.linearLayout = layout;
        this.type = type;
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
//        if (type == 0){
            switch (selectedCategory){
                case 0:
                    target(selectedCategory,i,type);
                    break;
                case 1:
                    target(selectedCategory,i,type);
                    break;
                case 2:
                    target(selectedCategory,i,type);
                    break;
                case 3:
                    target(selectedCategory,i,type);
                    break;
                case 4:
                    target(selectedCategory,i,type);
                    break;
                case 5:
                    target(selectedCategory,i,type);
                    break;
                default:
                    break;
            }
//        }
//        else {
//            switch (selectedCategory){
//                case 0:
//                    target(selectedCategory,i);
//                    break;
//                case 1:
//                    target(selectedCategory,i);
//                    break;
//                case 2:
//                    target(selectedCategory,i);
//                    break;
//                case 3:
//                    target(selectedCategory,i);
//                    break;
//                case 4:
//                    target(selectedCategory,i);
//                    break;
//                case 5:
//                    target(selectedCategory,i);
//                    break;
//                default:
//                    break;
//            }
//        }

    }
    public void target(int position1,int position2,int type){
        Intent intent = new Intent();
        String str = null;
        if (type == 0){
            str = targetString1[position1][position2];
        }else{
            str = targetString2[position1][position2];
        }
        if (str.equals(""))
            Toast.makeText(context,"正在研发中。。。" , Toast.LENGTH_SHORT).show();
        else{
            intent.setAction(str);
            context.startActivity(intent);
        }
    }
//    category.getCenterNameStringIds()[position1][position2]
}

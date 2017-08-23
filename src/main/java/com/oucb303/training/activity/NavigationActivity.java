package com.oucb303.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.oucb303.training.R;
import com.oucb303.training.model.Category;
import com.oucb303.training.model.CategoryExerciseAbility;
import com.oucb303.training.model.MotorTechnique;
import com.oucb303.training.model.NavigationBuilder;
import com.oucb303.training.widget.HorizontalListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationActivity extends AppCompatActivity
{


    @Bind(R.id.ll_category)
    LinearLayout llCategory;
    @Bind(R.id.gv_item_name)
    GridView gvItemName;
    @Bind(R.id.tv_category_name)
    TextView tvCategoryName;
    @Bind(R.id.tv_module_name)
    TextView tvModuleName;
    @Bind(R.id.hlv_items)
    HorizontalListView hlvItems;
    @Bind(R.id.ll_center)
    LinearLayout llCenter;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
       type = getIntent().getIntExtra("type",1);
        init();
    }

    private void init()
    {
        Category c1 = new CategoryExerciseAbility();
        Category c2 = new MotorTechnique();
        if (type == 0){
            tvModuleName.setText(c1.getTitle());
            NavigationBuilder categoryGroup = new NavigationBuilder(this, c1, gvItemName, tvCategoryName, hlvItems, llCenter,0);
            llCategory.addView(categoryGroup.initCategory());
        }else{
            tvModuleName.setText(c2.getTitle());
            NavigationBuilder categoryGroup2 = new NavigationBuilder(this, c2, gvItemName, tvCategoryName, hlvItems, llCenter,1);
            llCategory.addView(categoryGroup2.initCategory());
        }

    }

    @OnClick({R.id.ll_back})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_back:
                this.finish();
                break;
        }
    }
}

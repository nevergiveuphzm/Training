package com.b303.saflight.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.b303.saflight.R;
import com.b303.saflight.model.Category;
import com.b303.saflight.model.CategoryExerciseAbility;
import com.b303.saflight.model.NavigationBuilder;
import com.b303.saflight.widget.HorizontalListView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        Category c1 = new CategoryExerciseAbility();
        tvModuleName.setText(c1.getTitle());
        NavigationBuilder categoryGroup = new NavigationBuilder(this, c1, gvItemName, tvCategoryName, hlvItems, llCenter);
        llCategory.addView(categoryGroup.initCategory());
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

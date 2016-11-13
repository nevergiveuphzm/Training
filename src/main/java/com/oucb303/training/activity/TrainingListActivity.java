package com.oucb303.training.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.TrainingListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrainingListActivity extends Activity
{
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.gv_training_list)
    GridView gvTrainingList;

    //水平级别
    private int level;
    private String[] levelName = {"一", "二", "三", "四"};
    private TrainingListAdapter adapter;

    private Object[][] items = {
            //项目名称、项目描述、项目图标、项目id、项目所属水平等级
            {"折返跑", "描述1", R.drawable.run, 1, "1 2"},
            {"纵跳摸高", "描述1", R.drawable.jump, 2, "1 2 3 4"},
            {"仰卧起坐", "描述1", R.drawable.ywqz, 3, "1 2 3"},
            {"换物跑", "描述1", R.drawable.bwp, 4, "1 3 4"},
            {"运球比赛", "描述1", R.drawable.ball, 5, "1 2 3"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_list);
        ButterKnife.bind(this);
        level = getIntent().getIntExtra("level", 1);
        initView();
    }

    public void initView()
    {

        adapter = new TrainingListAdapter(TrainingListActivity.this, initItems(items));
        gvTrainingList.setAdapter(adapter);
        tvTitle.setText("水平" + levelName[level - 1] + "训练项目");
        gvTrainingList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                int itemId = (int) view.getTag();
                Intent intent = new Intent();
                switch (itemId)
                {
                    case 1://折返跑
                        intent.setClass(TrainingListActivity.this, ShuttleRunActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        intent.setClass(TrainingListActivity.this, SitUpsActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(TrainingListActivity.this, RandomTrainingActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    public List initItems(Object[][] items)
    {
        List<Object> res = new ArrayList<>();
        for (Object[] item : items)
        {
            if ((item[4].toString()).contains("" + level))
                res.add(item);
        }
        return res;
    }

    @OnClick({R.id.layout_cancel})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
        }
    }
}

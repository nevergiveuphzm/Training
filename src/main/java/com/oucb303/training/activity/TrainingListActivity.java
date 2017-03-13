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

/**
 * 水平训练列表
 */
public class TrainingListActivity extends Activity
{
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.gv_training_list)
    GridView gvTrainingList;

    //水平级别
    private int level;
    private String[] levelName = {"初级", "中级", "高级", "竞技"};
    private TrainingListAdapter adapter;

    private Object[][] items = {
            //项目名称、项目描述、项目图标、项目id、项目所属分类 1-初级 2-中级 3-高级  4-竞技
            {"折返跑", "描述1", R.drawable.run, 1, "1 2 3 "},
            {"纵跳摸高", "描述1", R.drawable.jump, 2, "1 2 3 "},
            {"仰卧起坐", "描述1", R.drawable.ywqz, 3, "1 2 3"},
            {"换物跑", "描述1", R.drawable.bwp, 4, "1 2 3"},
            {"运球比赛", "描述1", R.drawable.ball, 5, "1 2 3"},
            {"大课间活动","描述1",R.drawable.run,8,"1 2 3"},
            {"八分钟跑","描述1",R.drawable.run,9,"1 2 3"},
            {"多人混战", "描述1", R.drawable.ball, 6, "4"},
            {"双人对抗", "描述1", R.drawable.srdk, 7, "4"}
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
        tvTitle.setText(levelName[level - 1] + "项目");
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
                        intent.setClass(TrainingListActivity.this, ShuttleRunActivity1.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 2://纵跳摸高
                        intent.setClass(TrainingListActivity.this, JumpHighActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 3://仰卧起坐
                        intent.setClass(TrainingListActivity.this, SitUpsActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 4://换物跑
                        intent.setClass(TrainingListActivity.this, RandomTrainingActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 5://运球比赛
                        intent.setClass(TrainingListActivity.this, DribblingGameActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 6:
                        intent.setClass(TrainingListActivity.this, DribblingGameActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 7:
                        intent.setClass(TrainingListActivity.this, GroupConfrontationActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //大课间活动
                    case 8:
                        intent.setClass(TrainingListActivity.this, LargeRecessActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //八分钟跑
                    case 9:
                        intent.setClass(TrainingListActivity.this, EightSecondRunActivity.class);
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

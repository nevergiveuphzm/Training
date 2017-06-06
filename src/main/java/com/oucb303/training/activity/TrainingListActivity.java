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
import com.oucb303.training.utils.ConfigParamUtils;

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
            {"大课间活动","描述1",R.drawable.large_recess,8,"1 2 3"},
            {"八秒钟跑","描述1",R.drawable.eight,9,"1 2 3"},
            {"胆大心细","描述1",R.drawable.bold_cautious,10,"1 2 3"},
            {"多人混战", "描述1", R.drawable.ball, 6, "4"},
            {"双人对抗", "描述1", R.drawable.srdk, 7, "4"},
            {"交替", "描述1", R.drawable.ywqz, 11, "4"},//交替
            {"限时","描述1",R.drawable.eight,12,"4"},//限时
            {"计时","描述1",R.drawable.time_keeper,13,"4"},//计时
            {"次数随机","描述1",R.drawable.random_times_module,14,"4"},//次数随机
            {"时间随机","描述1",R.drawable.random_time,15,"4"},//时间随机
            {"分组对抗","描述1",R.drawable.group_resist,16,"4"},//分组对抗
            {"羽毛球步法训练","描述1",R.drawable.badminton_training,17,"1 2 3"},
            {"隔网对抗", "描述1", R.drawable.run, 18, "1 2 3 "}//隔网对抗
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
                    //分组对抗赛
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
                    //八秒钟跑
                    case 9:
                        intent.setClass(TrainingListActivity.this, EightSecondRunActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //胆大心细
                    case 10:
                        intent.setClass(TrainingListActivity.this, BoldCautiousActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    case 11://仰卧起坐---交替
                        intent.setClass(TrainingListActivity.this, SitUpsActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //八秒钟跑---限时活动
                    case 12:
                        intent.setClass(TrainingListActivity.this, TimingModuleActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //计时活动
                    case 13:
                        intent.setClass(TrainingListActivity.this, TimeKeeperActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //次数随机
                    case 14:
                        intent.setClass(TrainingListActivity.this, RandomTimesModuleActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //时间随机
                    case 15:
                        intent.setClass(TrainingListActivity.this, RandomTimeActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //分组对抗
                    case 16:
                        intent.setClass(TrainingListActivity.this, GroupResistActivity.class);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        break;
                    //羽毛球步法训练
                    case 17:
                        intent.setClass(TrainingListActivity.this, BadmintonActivity.class);
                        intent.putExtra("level", level);
                        intent.putExtra("randomMode", 0);
                        startActivity(intent);
                        break;
                    case 18:
                        intent.setClass(TrainingListActivity.this, WireNetConfrontationActivity.class);
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
            if( ConfigParamUtils.getDefaultDeviceNum(this)<12&&(item[0].toString()).contains("羽毛球步法训练"))
                res.remove(item);
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

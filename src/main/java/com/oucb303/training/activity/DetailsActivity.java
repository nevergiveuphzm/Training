package com.oucb303.training.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.adpter.LargeDetailsAdapter;
import com.oucb303.training.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HP on 2017/2/28  .
 */
public class DetailsActivity extends AppCompatActivity {

    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.bt_distance_cancel)
    ImageView btDistanceCancel;
    @Bind(R.id.layout_cancel)
    LinearLayout layoutCancel;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_help)
    ImageView imgHelp;
    @Bind(R.id.img_save)
    ImageView imgSave;

    private LargeDetailsAdapter largeDetailsAdapter;
    private int flag;
    private int groupId;//点击的组号


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        ButterKnife.bind(this);
        flag = getIntent().getIntExtra("flag", 0);
        initData();
    }

    private void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        groupId = bundle.getInt("groupId");
//        Log.i("groupId","groupId---"+String.valueOf(groupId));

        List<Map<String,Integer>> list_finishTime = (ArrayList) bundle.getSerializable("list_detail");
        List<Integer> time = new ArrayList<>();

//        Log.i("mmmm","所点击的灭灯次数---"+list_finishTime.get(groupId).size());

        for(int i=0;i<list_finishTime.size();i++){
            if(list_finishTime.get(i).get(String.valueOf(groupId))!=null){
               time.add(list_finishTime.get(i).get(String.valueOf(groupId)));
            }
            Log.i("aaaa","aaa---"+list_finishTime.get(i).toString());
        }

        tvTitle.setText("每组训练详情");

        largeDetailsAdapter = new LargeDetailsAdapter(this,time,groupId);
        lvTimes.setAdapter(largeDetailsAdapter);
        largeDetailsAdapter.notifyDataSetChanged();

    }
    @OnClick({R.id.layout_cancel})
    public void onClick(View view) {
        this.finish();
    }
}

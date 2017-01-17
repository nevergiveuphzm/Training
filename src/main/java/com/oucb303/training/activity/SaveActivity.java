package com.oucb303.training.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.App;
import com.oucb303.training.R;
import com.oucb303.training.adpter.SaveListViewAdapter;
import com.oucb303.training.daoservice.JumpHighSer;
import com.oucb303.training.daoservice.ShuttleRunSer;
import com.oucb303.training.entity.JumpHigh;
import com.oucb303.training.entity.ShuttleRun;
import com.oucb303.training.entity.ShuttleRunDao;
import com.oucb303.training.utils.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by BaiChangCai on 2017/1/8.
 * Description:保存运动数据
 */
public class SaveActivity extends AppCompatActivity{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.lv_student)
    ListView lvStudent;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    @Bind(R.id.tv_allData)
    TextView tvAllData;

    private SaveListViewAdapter adapter;
    //分组数量
    private int groupNum;
    //每组设备个数
    private int groupDeviceNum;
    //每组完成时间
    private int[] finishTimes;
    //总的训练次数
    private int totalTrainingTimes;
    //每组得分
    private List<Map<String,Object>> scoreList = new ArrayList<>();
    //训练内容 1:折返跑  2:纵跳摸高 ...
    private String trainingCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);

        initData();
        initView();

    }

    private void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        trainingCategory = bundle.getString("trainingCategory");
        //折返跑
        if(!trainingCategory.equals("")&&!trainingCategory.equals(null)&&trainingCategory.equals("1")){
             finishTimes = bundle.getIntArray("finishTimes");
             groupNum = finishTimes.length;
             totalTrainingTimes = bundle.getInt("totalTrainingTimes");
        }else if(!trainingCategory.equals("")&&!trainingCategory.equals(null)&&trainingCategory.equals("2")){
            //纵跳摸高
            totalTrainingTimes = bundle.getInt("trainingTime");
            groupDeviceNum = bundle.getInt("groupDeviceNum");
            ArrayList scores=bundle.getParcelableArrayList("scores");
            if(scores.size()>0){
                scoreList = (List<Map<String,Object>>) scores.get(0);
            }
            groupNum = scoreList.size();
        }
    }

    private void initView() {
        tvTitle.setText("保存数据");

        List<String> list = new ArrayList<>();
        for(int i=0;i<groupNum;i++){
            list.add("第"+(i+1)+"组");
        }
        adapter = new SaveListViewAdapter(SaveActivity.this,list);
        lvStudent.setAdapter(adapter);

    }
    @OnClick({R.id.layout_cancel,R.id.btn_save,R.id.btn_select,R.id.btn_delete})
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.layout_cancel:
                this.finish();
                break;
            case R.id.btn_save:
                //获取输入的学生的编号
                String[] studentNum = adapter.getStudentNum();
                //判空
                boolean isEmpty = false;
                for(int i=0;i<studentNum.length;i++){
                    if(studentNum[i]==""||studentNum[i]==null){
                        isEmpty = true;
                    }
                }
                if(isEmpty){
                    Toast.makeText(SaveActivity.this, "请输入全部学生编号!", Toast.LENGTH_SHORT).show();
                }else {
                    saveData(studentNum);
                }
                //显示获取的学生编号
                List<String> list = new ArrayList<>();
                for(int i=0;i<studentNum.length;i++){
                   list.add(studentNum[i]);
                }
                tvDetail.setText(list.toString());
                break;
            case R.id.btn_select:
                StringBuffer sb;
                switch (Integer.valueOf(trainingCategory)) {
                    case 1:
                        List<ShuttleRun> shuList = new ShuttleRunSer(((App) getApplication()).getDaoSession()).loadAllShuttleRun();
                        sb = new StringBuffer();
                        for(ShuttleRun shuttleRuns:shuList){
                            sb.append(shuttleRuns.toString());
                        }
                        tvAllData.setText(sb.toString());
                        break;
                    case 2:
                        List<JumpHigh> jumpList = new JumpHighSer(((App)getApplication()).getDaoSession()).loadAllShuttleRun();
                        sb = new StringBuffer();
                        for(JumpHigh jumpHigh:jumpList){
                            sb.append(jumpHigh.toString());
                        }
                        tvAllData.setText(sb.toString());
                        break;
                }
                break;
            case R.id.btn_delete:
                switch (Integer.valueOf(trainingCategory)) {
                    case 1:
                        new ShuttleRunSer(((App) getApplication()).getDaoSession()).deleteAll();
                        Toast.makeText(SaveActivity.this, "清空成功!", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        new JumpHighSer(((App) getApplication()).getDaoSession()).deleteAll();
                        Toast.makeText(SaveActivity.this, "清空成功!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }

    }

    private void saveData(String[] studentNum) {
        boolean FLAG_SUCCESS = true;
        switch (Integer.valueOf(trainingCategory)){
            case 1:
                ShuttleRunSer shuttleRunSer = new ShuttleRunSer(((App) getApplication()).getDaoSession());
                for(int i=0;i<studentNum.length;i++){
                    ShuttleRun shuttleRun = new ShuttleRun(null,studentNum[i],totalTrainingTimes,finishTimes[i],new Date(),false);
                    long res = shuttleRunSer.addShuttleRun(shuttleRun);
                    Log.d(Constant.LOG_TAG, "save shuttle:" + res);
                    if (res<=0){
                        Toast.makeText(SaveActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
                        FLAG_SUCCESS  = false;
                        break;
                    }
                }
                if(FLAG_SUCCESS){
                    Toast.makeText(SaveActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();

                   // this.finish();
                }
                break;
            case 2:
                JumpHighSer jumpHighSer = new JumpHighSer(((App)getApplication()).getDaoSession());
                for(int i=0;i<studentNum.length;i++){
                    JumpHigh jumpHigh = new JumpHigh(null,studentNum[i],Integer.valueOf(scoreList.get(i).get("score").toString()),groupDeviceNum,totalTrainingTimes,new Date(),false);
                    long res = jumpHighSer.addJumpHigh(jumpHigh);
                    Log.i(Constant.LOG_TAG, "save jumpHigh:" + res);
                    if(res<0){
                        FLAG_SUCCESS = false;
                        break;
                    }
                }
                if(FLAG_SUCCESS){
                    Toast.makeText(SaveActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
//                    this.finish();
                }
                break;
        }
    }
}

package com.oucb303.training.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oucb303.training.App;
import com.oucb303.training.R;
import com.oucb303.training.adpter.SaveListViewAdapter;
import com.oucb303.training.daoservice.BaseTrainingDataSer;
import com.oucb303.training.daoservice.DribblingGameSer;
import com.oucb303.training.daoservice.JumpHighSer;
import com.oucb303.training.daoservice.RandomTrainingSer;
import com.oucb303.training.daoservice.ShuttleRunSer;
import com.oucb303.training.daoservice.SitUpsSer;
import com.oucb303.training.daoservice.WireNetConfrontSer;
import com.oucb303.training.entity.BaseTrainingData;
import com.oucb303.training.entity.DribblingGame;
import com.oucb303.training.entity.JumpHigh;
import com.oucb303.training.entity.RandomTraining;
import com.oucb303.training.entity.ShuttleRun;
import com.oucb303.training.entity.SitUps;
import com.oucb303.training.entity.WireNetConfront;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.DateUtil;
import com.oucb303.training.utils.OperateUtils;

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
public class SaveActivity extends Activity {
    @Bind(R.id.lv_student)
    ListView lvStudent;

//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//    @Bind(R.id.lv_student)
//    ListView lvStudent;
//    @Bind(R.id.tv_detail)
//    TextView tvDetail;
//    @Bind(R.id.tv_allData)
//    TextView tvAllData;

    private SaveListViewAdapter adapter;
    //分组数量
    private int groupNum;
    //每组设备个数
    private int groupDeviceNum = 0;
    //每组完成时间
    private int[] finishTimes;
    //总的训练次数
    private int totalTrainingTimes = 0;
    //总的训练时间
    private int totalTime;
    //每组得分
    private List<Map<String, Object>> scoreList = new ArrayList<>();
    //训练内容 1:折返跑  2:纵跳摸高 ...
    private String trainingCategory;
    //每组的成绩
    private int[] scores;
    //每组在规定时间内，完成的训练次数
    private int[] completedTimes;
    //设备个数
    private int totalNum;
    //项目名称
    private String trainingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_save);
        setContentView(R.layout.layout_dialog_save);
        ButterKnife.bind(this);
        OperateUtils.setScreenWidth1(this, SaveActivity.this, 0.95, 0.6);
        initData();
        initView();

    }

    private void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        trainingCategory = bundle.getString("trainingCategory");
        //折返跑
        if (!trainingCategory.equals("") && !trainingCategory.equals(null) && trainingCategory.equals("1")) {
            finishTimes = bundle.getIntArray("finishTimes");
            groupNum = finishTimes.length;
            totalTrainingTimes = bundle.getInt("totalTrainingTimes");
        } else if (!trainingCategory.equals("") && !trainingCategory.equals(null) && trainingCategory.equals("2")) {
            //纵跳摸高
            totalTrainingTimes = bundle.getInt("trainingTime");
            Log.d("TRAINING_CATEGORY", trainingCategory);
            groupDeviceNum = bundle.getInt("groupDeviceNum");
            ArrayList scores = bundle.getParcelableArrayList("scores");
            if (scores.size() > 0) {
                scoreList = (List<Map<String, Object>>) scores.get(0);
            }
            groupNum = scoreList.size();
        } else if (!trainingCategory.equals("") && !trainingCategory.equals(null) && trainingCategory.equals("3")) {
            //仰卧起坐、交替活动
            trainingName = bundle.getString("trainingName");
            totalTime = bundle.getInt("trainingTime");
            scores = bundle.getIntArray("scores");
            groupNum = scores.length;
            System.out.println("groupNum" + groupNum);
        } else if (!trainingCategory.equals("") && !trainingCategory.equals(null) && trainingCategory.equals("4")) {
            //换物跑、时间随机、次数随机
            trainingName = bundle.getString("trainingName");
            totalTime = bundle.getInt("trainingTime");
            groupDeviceNum = bundle.getInt("groupDeviceNum");
            totalTrainingTimes = bundle.getInt("totalTimes");
            groupNum = 1;
        } else if (!trainingCategory.equals("") && !trainingCategory.equals(null) && trainingCategory.equals("5")) {
            //运球比赛、多人混战
            totalTime = bundle.getInt("trainingTime");
            trainingName = bundle.getString("trainingName");//项目名称
            totalNum = bundle.getInt("DeviceNum");
            scores = bundle.getIntArray("scores");
            groupNum = scores.length;
        } else if (!trainingCategory.equals("") && !trainingCategory.equals(null) && trainingCategory.equals("6")) {
            //大课间跑圈、八秒钟跑、羽毛球步法训练、计时活动、时间随机、次数随机基本模块
            totalTime = bundle.getInt("totalTime");//总时间
            trainingName = bundle.getString("trainingName");//项目名称
            groupNum = bundle.getInt("groupNum");//分组数
            scores = bundle.getIntArray("scores");//得分
            groupDeviceNum = bundle.getInt("deviceNum");//设备个数
            totalTrainingTimes = bundle.getInt("totalTimes");//总次数
        } else if (!trainingCategory.equals("") && !trainingCategory.equals(null) && trainingCategory.equals("7")) {
            //隔网对抗
            totalTime = bundle.getInt("trainingTime");//总时间
            trainingName = bundle.getString("trainingName");//项目名称
            totalNum = bundle.getInt("totalNum");//设备个数
            groupNum = bundle.getInt("groupNum");//分组数
            scores = bundle.getIntArray("scores");//得分
        }
    }
//
    private void initView() {
//        tvTitle.setText("保存数据");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < groupNum; i++) {
            list.add("第" + (i + 1) + "组");
        }
        adapter = new SaveListViewAdapter(SaveActivity.this, list);
        lvStudent.setAdapter(adapter);
    }
    @OnClick({R.id.btn_close_save, R.id.btn_save, R.id.btn_select, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close_save:
                this.finish();
                break;
            case R.id.btn_save:
                //获取输入的学生的编号
                String[] studentNum = adapter.getStudentNum();
                //判空
                boolean isEmpty = false;
                for (int i = 0; i < studentNum.length; i++) {
                    if (studentNum[i] == "" || studentNum[i] == null) {
                        isEmpty = true;
                    }
                }
                if (isEmpty) {
                    Toast.makeText(SaveActivity.this, "请输入全部学生编号!", Toast.LENGTH_SHORT).show();
                } else {
                    saveData(studentNum);
                }
                //显示获取的学生编号
                List<String> list = new ArrayList<>();
                for (int i = 0; i < studentNum.length; i++) {
                    list.add(studentNum[i]);
                }
//                tvDetail.setText(list.toString());
                break;
            case R.id.btn_select:
                StringBuffer sb;
                switch (Integer.valueOf(trainingCategory)) {
                    case 1:
                        List<ShuttleRun> shuList = new ShuttleRunSer(((App) getApplication()).getDaoSession()).loadAllShuttleRun();
                        sb = new StringBuffer();
                        for (ShuttleRun shuttleRuns : shuList) {
                            sb.append(shuttleRuns.toString());
                        }
//                        tvAllData.setText(sb.toString());
                        break;
                    case 2:
                        List<JumpHigh> jumpList = new JumpHighSer(((App) getApplication()).getDaoSession()).loadAllShuttleRun();
                        sb = new StringBuffer();
                        for (JumpHigh jumpHigh : jumpList) {
                            sb.append(jumpHigh.toString());
                        }
//                        tvAllData.setText(sb.toString());
                        break;
                    case 3:
                        List<SitUps> sitList = new SitUpsSer(((App) getApplication()).getDaoSession()).loadAllSitUps();
                        sb = new StringBuffer();
                        for (SitUps sitUps : sitList) {
                            sb.append(sitUps.toString());
                        }
//                        tvAllData.setText(sb.toString());
                        break;
                    case 4:
                        List<RandomTraining> randomList = new RandomTrainingSer(((App) getApplication()).getDaoSession()).loadAllRandomTraining();
                        sb = new StringBuffer();
                        for (RandomTraining randomTraining : randomList) {
                            sb.append(randomTraining.toString());
                        }
//                        tvAllData.setText(sb.toString());
                        break;
                    case 5:
                        List<DribblingGame> dribblingList = new DribblingGameSer(((App) getApplication()).getDaoSession()).loadAllDribblingGame();
                        sb = new StringBuffer();
                        for (DribblingGame dribblingGame : dribblingList) {
                            sb.append(dribblingGame.toString());
                        }
//                        tvAllData.setText(sb.toString());
                        break;
                    case 6:
                        List<BaseTrainingData> baseTrainingDataList = new BaseTrainingDataSer(((App) getApplication()).getDaoSession()).loadAllBaseTrainingData();
                        sb = new StringBuffer();
                        for (BaseTrainingData baseTrainingData : baseTrainingDataList) {
                            sb.append(baseTrainingData.toString());
                        }
//                        tvAllData.setText(sb.toString());
                        break;
                    case 7:
                        List<WireNetConfront> wireNetConfrontList = new WireNetConfrontSer(((App) getApplication()).getDaoSession()).loadAllWireNetConfront();
                        sb = new StringBuffer();
                        for (WireNetConfront wireNetConfront : wireNetConfrontList) {
                            sb.append(wireNetConfront.toString());
                        }
//                        tvAllData.setText(sb.toString());
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
                    case 3:
                        new SitUpsSer(((App) getApplication()).getDaoSession()).deleteAll();
                        Toast.makeText(SaveActivity.this, "清空成功!", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        new RandomTrainingSer(((App) getApplication()).getDaoSession()).deleteAll();
                        Toast.makeText(SaveActivity.this, "清空成功!", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        new DribblingGameSer(((App) getApplication()).getDaoSession()).deleteAll();
                        Toast.makeText(SaveActivity.this, "清空成功!", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        new BaseTrainingDataSer(((App) getApplication()).getDaoSession()).deleteAll();
                        Toast.makeText(SaveActivity.this, "清空成功!", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        new WireNetConfrontSer(((App) getApplication()).getDaoSession()).deleteAll();
                        Toast.makeText(SaveActivity.this, "清空成功!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }

    }

    private void saveData(String[] studentNum) {
        boolean FLAG_SUCCESS = true;
        switch (Integer.valueOf(trainingCategory)) {
            case 1:
                ShuttleRunSer shuttleRunSer = new ShuttleRunSer(((App) getApplication()).getDaoSession());
                for (int i = 0; i < studentNum.length; i++) {
                    ShuttleRun shuttleRun = new ShuttleRun(null, studentNum[i], finishTimes.length, totalTrainingTimes, finishTimes[i], DateUtil.DateToString(new Date()), false);
                    long res = shuttleRunSer.addShuttleRun(shuttleRun);
                    Log.d(Constant.LOG_TAG, "save shuttle:" + res);
                    if (res <= 0) {
                        Toast.makeText(SaveActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
                        FLAG_SUCCESS = false;
                        break;
                    }
                }
                if (FLAG_SUCCESS) {
                    Toast.makeText(SaveActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();

                    // this.finish();
                }
                break;
            case 2:
                JumpHighSer jumpHighSer = new JumpHighSer(((App) getApplication()).getDaoSession());
                for (int i = 0; i < studentNum.length; i++) {
                    JumpHigh jumpHigh = new JumpHigh(null, studentNum[i], Integer.valueOf(scoreList.get(i).get("score").toString()), scoreList.size(), groupDeviceNum, totalTrainingTimes, DateUtil.DateToString(new Date()), false);
                    long res = jumpHighSer.addJumpHigh(jumpHigh);
                    Log.i(Constant.LOG_TAG, "save jumpHigh:" + res);
                    if (res < 0) {
                        FLAG_SUCCESS = false;
                        break;
                    }
                }
                break;
            case 3:
                SitUpsSer sitUpsSer = new SitUpsSer(((App) getApplication()).getDaoSession());
                for (int i = 0; i < studentNum.length; i++) {
                    SitUps sitUps = new SitUps(null, trainingName, studentNum[i], scores[i], scores.length, totalTime, DateUtil.DateToString(new Date()), false);
                    long res = sitUpsSer.addSitUps(sitUps);
                    Log.i(Constant.LOG_TAG, "save sitUps:" + res);
                    if (res < 0) {
                        FLAG_SUCCESS = false;
                        break;
                    }
                }
                if (FLAG_SUCCESS) {
                    Toast.makeText(SaveActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
//                    this.finish();
                }
                break;
            case 4:
                RandomTrainingSer randomTrainingSer = new RandomTrainingSer(((App) getApplication()).getDaoSession());
                for (int i = 0; i < studentNum.length; i++) {
                    RandomTraining randomTraining = new RandomTraining(null, trainingName, studentNum[i], totalTrainingTimes, groupDeviceNum, totalTime, DateUtil.DateToString(new Date()), false);
                    long res = randomTrainingSer.addRandomTraining(randomTraining);
                    Log.i(Constant.LOG_TAG, "save randomTraining:" + res);
                    if (res < 0) {
                        FLAG_SUCCESS = false;
                        break;
                    }
                }
                if (FLAG_SUCCESS) {
                    Toast.makeText(SaveActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
//                    this.finish();
                }
                break;
            case 5:
                DribblingGameSer dribblingGameSer = new DribblingGameSer(((App) getApplication()).getDaoSession());
                for (int i = 0; i < studentNum.length; i++) {
                    DribblingGame dribblingGame = new DribblingGame(null, trainingName, studentNum[i], scores[i], scores.length, totalNum, totalTime, DateUtil.DateToString(new Date()), false);
                    long res = dribblingGameSer.addDribblingGame(dribblingGame);
                    Log.i(Constant.LOG_TAG, "save dribblingGame:" + res);
                    if (res < 0) {
                        FLAG_SUCCESS = false;
                        break;
                    }
                }
                if (FLAG_SUCCESS) {
                    Toast.makeText(SaveActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
//                    this.finish();
                }
                break;
            case 6:
                BaseTrainingDataSer baseTrainingDataSer = new BaseTrainingDataSer(((App) getApplication()).getDaoSession());
                for (int i = 0; i < studentNum.length; i++) {
                    BaseTrainingData baseTrainingData = new BaseTrainingData(null, trainingName, studentNum[i], totalTrainingTimes, groupDeviceNum, scores[i], totalTime, groupNum, DateUtil.DateToString(new Date()), false);
                    long res = baseTrainingDataSer.addBaseTrainingData(baseTrainingData);
                    Log.i(Constant.LOG_TAG, "save baseTrainingData:" + res);
                    if (res < 0) {
                        FLAG_SUCCESS = false;
                        break;
                    }
                }
                if (FLAG_SUCCESS) {
                    Toast.makeText(SaveActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
//                    this.finish();
                }
                break;
            case 7:
                WireNetConfrontSer wireNetConfrontSer = new WireNetConfrontSer(((App) getApplication()).getDaoSession());
                for (int i = 0; i < studentNum.length; i++) {
                    WireNetConfront wireNetConfront = new WireNetConfront(null, trainingName, studentNum[i], groupNum, scores[i], totalNum, totalTime, false);
                    long res = wireNetConfrontSer.addWireNetConfront(wireNetConfront);
                    if (res < 0) {
                        FLAG_SUCCESS = false;
                        break;
                    }
                }
                if (FLAG_SUCCESS) {
                    Toast.makeText(SaveActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                }

        }
    }

}

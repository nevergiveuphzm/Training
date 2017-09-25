package com.oucb303.training.model;

import com.oucb303.training.R;

/**
 * Created by HP on 2017/7/4.
 */
public class MotorTechnique implements Category {
    public int title = R.string.module_1;
    //顶部的分类
    int [][] imgIds = {{R.drawable.technique_run,R.drawable.technique_jump,R.drawable.technique_throw,R.drawable.technique_basketball,R.drawable.technique_football,R.drawable.technique_volleyball},
                        {R.drawable.technique_run_choose,R.drawable.technique_jump_choose,R.drawable.technique_throw_choose,R.drawable.technique_basketball_choose,R.drawable.technique_football_choose,R.drawable.technique_volleyball_choose}};

    //中间的名字
    int[] categoryName = {R.string.technique_running,R.string.technique_jumping,R.string.technique_throwing,R.string.technique_basketball,R.string.technique_football,R.string.technique_volleyball};
    //中间具体项目


    int[][] itemText = {{R.string.technique_run_item0,R.string.technique_run_item1,R.string.technique_run_item2,R.string.technique_run_item3,R.string.technique_run_item4,R.string.technique_run_item5,R.string.technique_run_item6},
                            {R.string.technique_jump_item0,R.string.technique_jump_item1,R.string.technique_jump_item2,R.string.technique_jump_item3,R.string.technique_jump_item4},
                            {R.string.technique_throw_item1},
                            {R.string.technique_basketball_item0},
                            {},
                            {}
    };

    String[][] targetString = {{"EightSecondRunActivity.class","ShuttleRunActivity1.class","OrientRunActivity.class","HighKneeActivity.class","QuickSR_BasicActivity.class","QuickSR_Upgrade1Activity.class","QuickSR_Upgrade2Activity.class"},
                                {"JumpHighActivity.class","SingleFootJumpActivity.class","EnduranceActivity.class","CrossJumpActivity.class","CrossJumpSTActivity.class"},
                                {"ThrowingActivity.class"},
                                {"DribblingGameActivity.class"},
                                {""},
                                {""}
    };
    //中部的背景图片
    int[] centerImgIds = {R.mipmap.technique_run_bg,R.mipmap.technique_jump_bg,R.mipmap.technique_throw_bg,R.mipmap.technique_basketball_bg,R.mipmap.technique_football_bg,R.mipmap.technique_volleyball_bg};
    //底部滚动栏图片

    int[][] itemImgIds = {{R.drawable.technique_run_item0,R.drawable.technique_run_item1,R.drawable.technique_run_item2,R.drawable.technique_run_item3,R.drawable.technique_run_item0,R.drawable.technique_run_item0,R.drawable.technique_run_item0},
                            {R.drawable.technique_jump_item0,R.drawable.technique_jump_item1,R.drawable.technique_jump_item2,R.drawable.technique_jump_item0,R.drawable.technique_jump_item1},
                            {R.drawable.technique_throw_item1},
                            {R.drawable.technique_basketball_item0},
                            {},
                            {}
    };
    @Override
    public int[][] getTopImgIds() {
        return imgIds;
    }

    @Override
    public int[][] getCenterNameStringIds() {
        return itemText;
    }

    @Override
    public int[] getCategoryNamesId() {
        return categoryName;
    }

    @Override
    public int getTitle() {
        return title;
    }

    @Override
    public int[][] getBottomImgIds() {
        return itemImgIds;
    }

    @Override
    public int[] getCenterImgIds() {
        return centerImgIds;
    }

    @Override
    public String[][] getTargetString() {
        return targetString;
    }
}

package com.b303.saflight.model;

import com.b303.saflight.R;

/**
 * Created by huzhiming on 2017/7/1.
 * 运动能力
 */

public class CategoryExerciseAbility implements Category
{
    public int title = R.string.module_0;
    //顶部的分类
    int[][] imgIds = {
            {R.drawable.navigation_run, R.drawable.navigation_jump, R.drawable.navigation_throwing, R.drawable.navigation_juggling, R.drawable.navigation_climbing, R.drawable.navigation_rhythming},
            {R.drawable.navigation_run_choose, R.drawable.navigation_jump_choose, R.drawable.navigation_throwing_choose, R.drawable.navigation_juggling_choose, R.drawable.navigation_climbing_choose, R.drawable.navigation_rhythming_choose}
    };

    //中间的名字
    int[] categoryName = {R.string.navigation_running, R.string.navigation_jumping, R.string.navigation_throwing, R.string.navigation_juggling, R.string.navigation_climbing, R.string.navigation_rhythm};
    //中间的具体项目
    int[][] itemText = {
            {R.string.navigation_running_item0, R.string.navigation_running_item1, R.string.navigation_running_item2, R.string.navigation_running_item3, R.string.navigation_running_item4, R.string.navigation_running_item5},
            {R.string.navigation_jumping_item0, R.string.navigation_jumping_item1},
            {R.string.navigation_throwing_item0},
            {R.string.navigation_juggling_item0},
            {R.string.navigation_climbing_item0, R.string.navigation_climbing_item1, R.string.navigation_climbing_item2},
            {R.string.navigation_rhythm_item0}
    };

    //中部的背景图片
    int[] centerImgIds = {R.drawable.navigation_run_bg, R.drawable.navigation_jumping_bg, R.drawable.navigation_throwing_bg, R.drawable.navigation_juggling_bg, R.drawable.navigation_climbing_bg, R.drawable.navigation_rhythm_bg};

    //底部滚动栏图片
    int[][] itemImgIds = {
            {R.drawable.navigation_running_item0, R.drawable.navigation_running_item1, R.drawable.navigation_running_item2, R.drawable.navigation_running_item3, R.drawable.navigation_running_item4, R.drawable.navigation_running_item5},
            {R.drawable.navigation_jumping_item0, R.drawable.navigation_jumping_item1},
            {R.drawable.navigation_throwing_item0},
            {R.drawable.navigation_juggling_item0},
            {R.drawable.navigation_climbing_item0, R.drawable.navigation_climbing_item1, R.drawable.navigation_climbing_item2},
            {R.drawable.navigation_rhythm_item0}
    };

    @Override
    public int[][] getTopImgIds()
    {
        return imgIds;
    }

    @Override
    public int[][] getCenterNameStringIds()
    {
        return itemText;
    }

    @Override
    public int[] getCategoryNamesId()
    {
        return categoryName;
    }

    @Override
    public int getTitle()
    {
        return title;
    }

    @Override
    public int[][] getBottomImgIds()
    {
        return itemImgIds;
    }

    @Override
    public int[] getCenterImgIds()
    {
        return centerImgIds;
    }
}

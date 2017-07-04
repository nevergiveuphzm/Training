package com.oucb303.training.model;

/**
 * Created by huzhiming on 2017/7/1.
 */

public interface Category
{
    //返回顶部分类按钮的图片DI
    int[][] getTopImgIds();

    int[][] getCenterNameStringIds();

    int[] getCategoryNamesId();

    int getTitle();

    int[][] getBottomImgIds();

    int[] getCenterImgIds();

    String[][] getTargetString();
}

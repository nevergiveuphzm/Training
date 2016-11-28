package com.oucb303.training.model;

import android.widget.ImageView;

/**
 * Created by huzhiming on 16/9/24.
 * Description：
 */

public class CheckBox
{
    //被选中的id
    private int checkId = 1;
    private ImageView[] views;

    public CheckBox(int checkId, ImageView[] views)
    {
        this.checkId = checkId;
        this.views = views;
    }

    public int getCheckId()
    {
        return checkId;
    }

    public void setCheckId(int checkId)
    {
        this.checkId = checkId;
    }

    public ImageView[] getViews()
    {
        return views;
    }

    public void setViews(ImageView[] views)
    {
        this.views = views;
    }
}

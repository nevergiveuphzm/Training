package com.oucb303.training.model;

import android.widget.ImageView;

/**
 * Created by huzhiming on 16/9/24.
 * Description：
 */

public class CheckBox
{
    //checkbox 的编号
    private int id;
    //是否被选中
    private boolean checked;
    private ImageView view;

    public CheckBox(int id, boolean checked, ImageView view)
    {
        this.id = id;
        this.checked = checked;
        this.view = view;
    }

    public boolean getChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public ImageView getView()
    {
        return view;
    }

    public void setView(ImageView view)
    {
        this.view = view;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}

package com.oucb303.training.listener;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.oucb303.training.R;
import com.oucb303.training.model.CheckBox;
import com.oucb303.training.utils.Constant;

/**
 * Created by huzhiming on 16/9/24.
 * Description：
 */

public class CheckBoxClickListener implements View.OnClickListener
{
    private CheckBox checkBox;

    public CheckBoxClickListener(CheckBox checkBox)
    {
        this.checkBox = checkBox;
        for (ImageView view : checkBox.getViews())
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        for (int i = 0; i < checkBox.getViews().length; i++)
        {
            if (checkBox.getViews()[i].getId() == view.getId())
            {
                checkBox.setCheckId(i + 1);
                doOtherThings(i + 1);
            }
        }
        Log.d(Constant.LOG_TAG, "checked id: " + checkBox.getCheckId());
        changeState();
    }

    public void doOtherThings(int checkedId)
    {

    }

    public void changeState()
    {
        for (int i = 0; i < checkBox.getViews().length; i++)
        {
            if ((i + 1) == checkBox.getCheckId())
                checkBox.getViews()[i].setImageResource(R.drawable.btn_checkbox_checked_new);
            else
                checkBox.getViews()[i].setImageResource(R.drawable.btn_checkbox_unchecked);
        }
    }
}

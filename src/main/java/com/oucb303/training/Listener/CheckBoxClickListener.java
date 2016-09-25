package com.oucb303.training.listener;

import android.view.View;

import com.oucb303.training.R;
import com.oucb303.training.model.CheckBox;

import java.util.List;

/**
 * Created by huzhiming on 16/9/24.
 * Description：
 */

public class CheckBoxClickListener implements View.OnClickListener
{
    private List<CheckBox> boxs;

    public CheckBoxClickListener(List<CheckBox> boxs)
    {
        this.boxs = boxs;
        for (CheckBox box : boxs)
        {
            box.getView().setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        for (CheckBox box : boxs)
        {
            if (box.getView().getId() == view.getId())
            {
                box.setChecked(true);
                box.getView().setImageResource(R.drawable.btn_checkbox);
            }
            else
            {
                box.setChecked(false);
                box.getView().setImageResource(R.drawable.btn_uncheckbox);
            }
        }
    }
}

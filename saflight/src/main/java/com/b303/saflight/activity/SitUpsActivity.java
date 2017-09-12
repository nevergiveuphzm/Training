package com.b303.saflight.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.b303.saflight.DialogUtils;
import com.b303.saflight.OperateUtils;
import com.b303.saflight.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baichangcai on 2017/8/9.
 */
public class SitUpsActivity extends Activity{
    Context mContext;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.layout_cancel)
    LinearLayout llCancel;
    @Bind(R.id.img_set)
    ImageView imgSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situps);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        initView();
    }
    private void initView() {
        tvTitle.setText("仰卧起坐练习");
    }
    @OnClick({R.id.layout_cancel, R.id.img_set,R.id.img_help,R.id.img_save})
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.layout_cancel:
                finish();
                break;
            case R.id.img_set:
                Dialog dialog = DialogUtils.createLightSetDialog(SitUpsActivity.this);
                OperateUtils.setScreenWidth(this, dialog, 0.95, 0.7);
                dialog.show();
                break;
            case R.id.img_help:
                Dialog dialog_help = DialogUtils.createHelpDialog(SitUpsActivity.this);
                OperateUtils.setScreenWidth(this, dialog_help, 0.95, 0.7);
                dialog_help.show();
                break;
            case R.id.img_save:
                Dialog dialog_save = DialogUtils.createSaveDialog(SitUpsActivity.this);
                OperateUtils.setScreenWidth(this, dialog_save, 0.95, 0.6);
                dialog_save.show();
                break;
        }
    }
}

package com.oucb303.training.activity;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.dialugue.CustomDialog;
import com.oucb303.training.utils.DialogUtils;
import com.oucb303.training.utils.OperateUtils;


public class Main2Activity extends Activity {

    int[] Setting_shuju = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main3);
        Button btn = (Button)findViewById(R.id.btn_pop_dialog_1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog dialog = new  CustomDialog(Main2Activity.this,"From btn 1",new CustomDialog.ICustomDialogEventListener() {
                    @Override
                    public void customDialogEvent(int id) {
                        TextView imageView = (TextView)findViewById(R.id.main_image);

                        int aaa = id;
                        String a  =String.valueOf(aaa);
                        for(int i=0;i<5;i++){
                            Setting_shuju[i] = aaa %10;
                            aaa = aaa/10;
                            imageView.setText(Setting_shuju[i]+"   ");
                        }


                    }
                },R.style.dialog_rank);

                dialog.show();
                OperateUtils operateUtils = new OperateUtils();
                operateUtils.setScreenWidth(Main2Activity.this,dialog,0.97,0.8);
            }
        });

        Button btn2 = (Button)findViewById(R.id.btn_pop_dialog_2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog dialog = new  CustomDialog(Main2Activity.this,"From btn 2",new CustomDialog.ICustomDialogEventListener() {
                    @Override
                        public void customDialogEvent(int id) {
                        TextView imageView = (TextView)findViewById(R.id.main_image);

                        int aaa = id;
                        String a  =String.valueOf(aaa);
                        for(int i=0;i<5;i++){
                            Setting_shuju[i] = aaa % 10;
                            aaa = aaa/10;
                            imageView.append(Setting_shuju[i]+"   ");
                        }
                    }
                },R.style.dialog_rank);

                dialog.show();
                OperateUtils operateUtils = new OperateUtils();
                operateUtils.setScreenWidth(Main2Activity.this,dialog,0.97,0.8);
            }
        });
    }
}

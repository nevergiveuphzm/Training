package com.b303.saflight;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by BaiChangCai on 2017/1/16.
 * Description:封装各种样式的Dialog
 */
public class DialogUtils {

    public static Dialog createLightSetDialog(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_lightset, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_light_set);

        final Dialog dialog = new Dialog(context,R.style.dialog_rank);
        dialog.setContentView(layout);
        return dialog;
    }
    public static Dialog createHelpDialog(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_help, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_help);
        final Dialog dialog = new Dialog(context,R.style.dialog_rank);
        dialog.setContentView(layout);
        Button btnClose = (Button)layout.findViewById(R.id.btn_close_help);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
    public static Dialog createSaveDialog(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_save, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_save);
        final Dialog dialog = new Dialog(context,R.style.dialog_rank);
        dialog.setContentView(layout);
        Button btnClose = (Button)layout.findViewById(R.id.btn_close_save);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }


    public static void close(Dialog dialog)
    {
        if (dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
}

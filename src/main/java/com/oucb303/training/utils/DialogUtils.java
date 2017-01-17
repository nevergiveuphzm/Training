package com.oucb303.training.utils;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oucb303.training.R;

/**
 * Created by BaiChangCai on 2017/1/16.
 * Description:封装各种样式的Dialog
 */
public class DialogUtils {
    /**
     * 加载样式的Dialog
     */
    public static Dialog createLoadingDialog(Context context, String msg, boolean isCancle)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView)v.findViewById(R.id.img_load);
        TextView tipTextView = (TextView)v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog dialog = new Dialog(context, R.style.dialog);// 创建自定义样式dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(isCancle);// 返回键是否可用
        dialog.setContentView(layout, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));// 设置布局
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

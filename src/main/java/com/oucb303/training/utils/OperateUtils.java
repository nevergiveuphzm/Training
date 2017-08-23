package com.oucb303.training.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;

import java.io.File;

/**
 * Created by baichangcai on 2016/9/24.
 */
public class OperateUtils {
    //设置占屏比
    public static void setScreenWidth(Activity activity,Dialog dialog,double height,double width) {

        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow()
                .getAttributes(); // 获取对话框当前的参数值

        p.height = (int) (d.getHeight() * height); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.5

        dialog.getWindow().setAttributes(p); // 设置生效
    }
    //吐司
    public static void toast(Activity activity,String message) {
        View layout = LayoutInflater.from(activity).inflate(
                R.layout.layout_toast, null);
        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(message);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    //设置占屏比
    public static void setScreenWidth1(Activity activity,Activity newActivity,double height,double width) {

        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = newActivity.getWindow()
                .getAttributes(); // 获取对话框当前的参数值

        p.height = (int) (d.getHeight() * height); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.5

        newActivity.getWindow().setAttributes(p); // 设置生效
    }

}

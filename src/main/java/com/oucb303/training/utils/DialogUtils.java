package com.oucb303.training.utils;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.adpter.RankingListViewAdapter;
import com.oucb303.training.listener.CheckBoxClickListener;
import com.oucb303.training.model.CheckBox;

import java.util.List;

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

    /**
     * 加载最终成绩排名的dialog
     * @param context
     * @param isCancle
     * @return
     */
    public static Dialog createRankDialog(Context context, List<Integer> list,String total,int type, boolean isCancle)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_ranking, null);// 得到加载view
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.dialog_ranking);// 加载布局
        // main.xml中的ListView
        ListView lvRanking = (ListView)v.findViewById(R.id.lv_ranking);
        Button btnClose = (Button)v.findViewById(R.id.btn_close);// 提示文字

        RankingListViewAdapter rankingListViewAdapter = new RankingListViewAdapter(context,list,total,type);
        lvRanking.setAdapter(rankingListViewAdapter);

        final Dialog dialog = new Dialog(context,R.style.dialog_rank);// 创建自定义样式dialog
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(isCancle);// 返回键是否可用
        dialog.setContentView(layout);// 设置布局


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
    public static Dialog createLightSetDialog(Context context){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_lightset, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_light_set);
        final Dialog dialog = new Dialog(context,R.style.dialog_rank);
        dialog.setContentView(layout);
        return dialog;
    }
    public static Dialog createHelpDialog(Context context,List<Integer> list){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_dialog_help, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_help);
        TextView tvMethod = (TextView) v.findViewById(R.id.tv_method);
        tvMethod.setText(list.get(0));
        TextView tvStandard = (TextView) v.findViewById(R.id.tv_standard);
        tvStandard.setText(list.get(1));
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
//    public static Dialog createSaveDialog(Context context){
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.layout_dialog_save, null);// 得到加载view
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_save);
//        final Dialog dialog = new Dialog(context,R.style.dialog_rank);
//        dialog.setContentView(layout);
//        Button btnClose = (Button)layout.findViewById(R.id.btn_close_save);
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        return dialog;
//    }

    public static void close(Dialog dialog)
    {
        if (dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
}

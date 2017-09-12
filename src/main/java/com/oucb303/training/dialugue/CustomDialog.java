package com.oucb303.training.dialugue;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.oucb303.training.R;

/**
 * Created by lishuai on 2017/9/8.
 */
public class CustomDialog extends Dialog implements View.OnClickListener{

	//增加一个回调函数,用以从外部接收返回值
    public interface ICustomDialogEventListener {
        public void customDialogEvent(int id);
    }

    ImageView imgActionModeTouch ;
    ImageView imgActionModeLight ;
    ImageView imgActionModeTogether ;
    ImageView imgLightColorBlue ;
    ImageView imgLightColorRed ;
    ImageView imgLightColorBlueRed;
    ImageView imgBlinkModeNone ;
    ImageView imgBlinkModeSlow;
    ImageView imgBlinkModeFast;
    CheckBox cbVoice;
    CheckBox cbEndVoice;
    private ICustomDialogEventListener mCustomDialogEventListener;
    private Context mContext;
    private String mStr;
    int drawableID =-1;
    char[] c_char = new char[5];
    private com.oucb303.training.model.CheckBox actionModeCheckBox, lightModeCheckBox, lightColorCheckBox, blinkModeCheckBox;
    public CustomDialog(Context context) {
        super(context);
        mContext = context;
    }

    public CustomDialog(Context context, String str,ICustomDialogEventListener listener,int theme) {
        super(context, theme);
        mContext = context;
        mStr = str;
        mCustomDialogEventListener = listener;
    }
    private void bindImageClickEvent(View layout){

         imgActionModeTouch = (ImageView) layout.findViewById(R.id.img_action_mode_touch);
         imgActionModeLight = (ImageView) layout.findViewById(R.id.img_action_mode_light);
         imgActionModeTogether = (ImageView) layout.findViewById(R.id.img_action_mode_together);
         imgLightColorBlue = (ImageView) layout.findViewById(R.id.img_light_color_blue);
         imgLightColorRed = (ImageView) layout.findViewById(R.id.img_light_color_red);
         imgLightColorBlueRed = (ImageView) layout.findViewById(R.id.img_light_color_blue_red);
         imgBlinkModeNone = (ImageView) layout.findViewById(R.id.img_blink_mode_none);
         imgBlinkModeSlow = (ImageView) layout.findViewById(R.id.img_blink_mode_slow);
         imgBlinkModeFast = (ImageView) layout.findViewById(R.id.img_blink_mode_fast);
         cbVoice = (android.widget.CheckBox) layout.findViewById(R.id.cb_voice);
        cbEndVoice = (android.widget.CheckBox) layout.findViewById(R.id.cb_endvoice);
        Button btnOk = (Button) layout.findViewById(R.id.btn_ok);
        Button btnCloseSet = (Button) layout.findViewById(R.id.btn_close_set);





        imgActionModeTouch.setOnClickListener(this);
        imgActionModeLight.setOnClickListener(this);
        imgActionModeTogether.setOnClickListener(this);
        imgLightColorBlue.setOnClickListener(this);
        imgLightColorRed.setOnClickListener(this);
        imgLightColorBlueRed.setOnClickListener(this);
        imgBlinkModeNone.setOnClickListener(this);
        imgBlinkModeSlow.setOnClickListener(this);
        imgBlinkModeFast.setOnClickListener(this);
        cbVoice.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCloseSet.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View v = inflater.inflate(R.layout.layout_dialog_lightset, null);// 得到加载view

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_light_set);





        bindImageClickEvent(layout);

        this.setContentView(layout);
        for(int i = 0;i<3;i++)
            c_char[i]='1';
        c_char[3]='0';
        c_char[4]='0';
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(cbVoice.isChecked()){
            c_char[3]='1';
        }
        if(cbEndVoice.isChecked()){
            c_char[4]='1';
        }

        switch (id){
            case R.id.img_action_mode_light:
                c_char[0]='1';
                imgActionModeTouch.setImageResource(R.drawable.btn_checkbox_unchecked); ;
                imgActionModeLight.setImageResource(R.drawable.btn_checkbox_checked); ;
                imgActionModeTogether.setImageResource(R.drawable.btn_checkbox_unchecked);
                break;
            case R.id.img_action_mode_touch:
                c_char[0]='2';
                 imgActionModeTouch.setImageResource(R.drawable.btn_checkbox_checked); ;
                 imgActionModeLight.setImageResource(R.drawable.btn_checkbox_unchecked); ;
                 imgActionModeTogether.setImageResource(R.drawable.btn_checkbox_unchecked);
                break;
            case R.id.img_action_mode_together:
                c_char[0]='3';
                imgActionModeTouch.setImageResource(R.drawable.btn_checkbox_unchecked); ;
                imgActionModeLight.setImageResource(R.drawable.btn_checkbox_unchecked); ;
                imgActionModeTogether.setImageResource(R.drawable.btn_checkbox_checked);
                break;
            case R.id.img_light_color_blue:
                c_char[1]='1';
                 imgLightColorBlue.setImageResource(R.drawable.btn_checkbox_checked) ;
                 imgLightColorRed.setImageResource(R.drawable.btn_checkbox_unchecked) ;
                 imgLightColorBlueRed.setImageResource(R.drawable.btn_checkbox_unchecked);
                break;
            case R.id.img_light_color_red:
                c_char[1]='2';
                imgLightColorBlue.setImageResource(R.drawable.btn_checkbox_unchecked) ;
                imgLightColorRed.setImageResource(R.drawable.btn_checkbox_checked) ;
                imgLightColorBlueRed.setImageResource(R.drawable.btn_checkbox_unchecked);
                break;
            case R.id.img_light_color_blue_red:
                c_char[1]='3';
                imgLightColorBlue.setImageResource(R.drawable.btn_checkbox_unchecked) ;
                imgLightColorRed.setImageResource(R.drawable.btn_checkbox_unchecked) ;
                imgLightColorBlueRed.setImageResource(R.drawable.btn_checkbox_checked);
                break;
            case R.id.img_blink_mode_none:
                c_char[2]='1';
                imgBlinkModeNone.setImageResource(R.drawable.btn_checkbox_checked) ;
                imgBlinkModeSlow.setImageResource(R.drawable.btn_checkbox_unchecked);
                imgBlinkModeFast.setImageResource(R.drawable.btn_checkbox_unchecked);
                break;
            case R.id.img_blink_mode_slow:
                c_char[2]='2';
                imgBlinkModeNone.setImageResource(R.drawable.btn_checkbox_unchecked) ;
                imgBlinkModeSlow.setImageResource(R.drawable.btn_checkbox_checked);
                imgBlinkModeFast.setImageResource(R.drawable.btn_checkbox_unchecked);
                break;
            case R.id.img_blink_mode_fast:
                c_char[2]='3';
                imgBlinkModeNone.setImageResource(R.drawable.btn_checkbox_unchecked) ;
                imgBlinkModeSlow.setImageResource(R.drawable.btn_checkbox_unchecked);
                imgBlinkModeFast.setImageResource(R.drawable.btn_checkbox_checked);
                break;
            case R.id.btn_ok:
                String ss = new String(c_char);
                drawableID  = Integer.parseInt(ss);
                mCustomDialogEventListener.customDialogEvent(drawableID);
                dismiss();
                break;
            case R.id.btn_close_set:
                String ss1 = new String(c_char);
                drawableID  = Integer.parseInt(ss1);
                mCustomDialogEventListener.customDialogEvent(drawableID);
                dismiss();
                break;
        }


    }
}

package com.oucb303.training.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.model.DeviceInfo;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.utils.ConfigParamUtils;
import com.oucb303.training.utils.DataAnalyzeUtils;
import com.oucb303.training.utils.DialogUtils;
import com.oucb303.training.utils.NetworkUtils;
import com.oucb303.training.utils.OperateUtils;
import com.oucb303.training.utils.VersionUtils;
import com.oucb303.training.widget.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingActivity extends AppCompatActivity
{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.tv_pan_id)
    TextView tvPanId;
    @Bind(R.id.tv_default_device_num)
    TextView tvDefaultDeviceNum;

    private PackageInfo packageInfo;
    private Device device;
    private String panId;
    private Dialog mDialog;
    private Context mContext;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    panId = DataAnalyzeUtils.analyzePAN_ID(msg.obj.toString());
                    tvPanId.setText("PAN_ID:" + panId);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mContext = this.getApplicationContext();
        device = new Device(this);
        device.createDeviceList(this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connect(this);
            device.initConfig();
            device.getControllerPAN_ID();
            //开启接收panid
            new ReceiveThread(handler, device.ftDev, ReceiveThread.PAN_ID_THREAD, 1).start();
        } else
        {
            tvPanId.setText("PAN_ID:当前未插入协调器");
        }
        packageInfo = VersionUtils.getAppVersion(this);
        initView();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        device.disconnect();
    }

    private void initView()
    {
        tvTitle.setText("设置");
        tvVersion.setText("当前版本:" + packageInfo.versionName);
        tvDefaultDeviceNum.setText("" + ConfigParamUtils.getDefaultDeviceNum(this));
    }


    @OnClick({R.id.layout_cancel, R.id.ll_about, R.id.btn_turn_on_all_lights, R.id.btn_turn_off_all_lights, R.id.ll_upload, R.id.tv_edit_device_num})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
            case R.id.ll_about:
                Intent intent = new Intent(this, AboutSafLightActivity.class);
                intent.putExtra("panId", panId);
                startActivity(intent);
                break;
            case R.id.btn_turn_on_all_lights:
                for (DeviceInfo info : Device.DEVICE_LIST)
                {
                    device.sendOrder(info.getDeviceNum(),
                            Order.LightColor.BLUE,
                            Order.VoiceMode.NONE,
                            Order.BlinkModel.NONE,
                            Order.LightModel.OUTER,
                            Order.ActionModel.NONE,
                            Order.EndVoice.NONE);
                }
                break;
            case R.id.btn_turn_off_all_lights:
                device.turnOffAllTheLight();
                break;
            case R.id.tv_edit_device_num:
                changeDeviceNum();
                break;
            case R.id.ll_upload:
                if (!NetworkUtils.isNetworkAvailable(this))
                {
                    Toast.makeText(this, "网络不可用！", Toast.LENGTH_SHORT).show();
                } else
                {
                    mDialog = DialogUtils.createLoadingDialog(SettingActivity.this, "正在上传数据，请稍候...", true);
                    mDialog.show();
                }

                break;
        }
    }

    private void changeDeviceNum()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null);
        builder.setView(v);
        Button btnSave = (Button) v.findViewById(R.id.btn_save);
        Button btnCancel = (Button) v.findViewById(R.id.btn_cancel);
        final EditText etDeviceNum = (EditText) v.findViewById(R.id.et_edit_value);
        final AlertDialog dialog = builder.create();
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String temp = etDeviceNum.getText().toString();
                if (!temp.matches("[1-9][0-9]*"))
                {
                    Toast.makeText(SettingActivity.this, "输入不合法!(格式:8、16)", Toast.LENGTH_SHORT).show();
                    return;
                }
                int num = new Integer(temp);
                if (num > 32)
                {
                    Toast.makeText(SettingActivity.this, "数字不能大于32!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConfigParamUtils.changeDefaultDeviceNum(SettingActivity.this, num);
                Toast.makeText(SettingActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                tvDefaultDeviceNum.setText("" + ConfigParamUtils.getDefaultDeviceNum(SettingActivity.this));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}

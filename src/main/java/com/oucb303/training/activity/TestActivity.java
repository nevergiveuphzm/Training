package com.oucb303.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.oucb303.training.R;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity
{
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.sp_color)
    Spinner spColor;
    @Bind(R.id.sp_voice)
    Spinner spVoice;
    @Bind(R.id.sp_light)
    Spinner spLight;
    @Bind(R.id.sp_action_model)
    Spinner spActionModel;
    @Bind(R.id.sp_blink)
    Spinner spBlink;
    @Bind(R.id.et_light_id)
    EditText etLightId;

    private String[] colors = {"000无", "001蓝", "010红","011品红"};
    private String[] voices = {"00无", "01短响", "10连续响1s", "11连续响2s"};
    private String[] blinks = {"00无", "01快闪", "10慢闪", "11先闪后灭"};
    private String[] lights = {"000无", "001外圈灯亮", "010里圈灯亮", "011全部亮", "100关灯"};
    private String[] actionModes = {"000无", "001红外", "010震动", "011全部", "100关闭"};


    private Order.LightColor color = Order.LightColor.NONE;
    private Order.VoiceMode voice = Order.VoiceMode.NONE;
    private Order.BlinkModel blinkModel = Order.BlinkModel.NONE;
    private Order.LightModel lightModel = Order.LightModel.None;
    private Order.ActionModel actionModel = Order.ActionModel.NONE;


    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        tvTitle.setText("测试系统");

        device = new Device(TestActivity.this);

        device.createDeviceList(TestActivity.this);
        // 判断是否插入协调器，
        if (device.devCount > 0)
        {
            device.connectFunction(TestActivity.this);
            device.initConfig();
        }


        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(TestActivity.this,
                android.R.layout.simple_spinner_item, colors);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(TestActivity.this,
                android.R.layout.simple_spinner_item, voices);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(TestActivity.this,
                android.R.layout.simple_spinner_item, blinks);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(TestActivity.this,
                android.R.layout.simple_spinner_item, lights);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(TestActivity.this,
                android.R.layout.simple_spinner_item, actionModes);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spColor.setAdapter(adapter1);
        spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                color = Order.LightColor.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        spVoice.setAdapter(adapter2);
        spVoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                voice = Order.VoiceMode.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        spBlink.setAdapter(adapter3);
        spBlink.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                blinkModel = Order.BlinkModel.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        spLight.setAdapter(adapter4);
        spLight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                lightModel = Order.LightModel.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        spActionModel.setAdapter(adapter5);
        spActionModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                actionModel = Order.ActionModel.values()[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


    }

    @Override
    protected void onPause()
    {
        super.onPause();
        device.disconnectFunction();
    }

    @OnClick({R.id.layout_cancel, R.id.btn_send_order, R.id.btn_clear})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
            case R.id.btn_clear:
                clear();
                break;
            case R.id.btn_send_order:
                sendOrder();
                break;
        }
    }

    public void clear()
    {
        etLightId.setText("");
        spColor.setSelection(0);
        spVoice.setSelection(0);
        spLight.setSelection(0);
        spActionModel.setSelection(0);
        spBlink.setSelection(0);
    }

    public void sendOrder()
    {

        String ids = etLightId.getText().toString();


        if (!device.checkDevice(TestActivity.this))
            return;

        device.sendOrder(ids, color, voice, blinkModel, lightModel, actionModel);
    }
}

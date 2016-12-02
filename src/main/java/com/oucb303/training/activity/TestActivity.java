package com.oucb303.training.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oucb303.training.R;
import com.oucb303.training.device.Device;
import com.oucb303.training.device.Order;
import com.oucb303.training.listener.SpinnerItemSelectedListener;
import com.oucb303.training.model.Constant;
import com.oucb303.training.model.TimeInfo;
import com.oucb303.training.threads.ReceiveThread;
import com.oucb303.training.utils.DataAnalyzeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Bind(R.id.lv_times)
    ListView lvTimes;
    @Bind(R.id.cb_end_voice)
    CheckBox cbEndVoice;
    @Bind(R.id.lv_address)
    ListView lvAddress;
    @Bind(R.id.sp_address)
    Spinner spAddress;
    @Bind(R.id.et_pan_id)
    EditText etPanId;
    @Bind(R.id.et_light_num)
    EditText etLightNum;
    @Bind(R.id.btn_change)
    Button btnChange;
    @Bind(R.id.et_pan_id1)
    EditText etPanId1;
    @Bind(R.id.btn_change1)
    Button btnChange1;

    private String[] colors = {"000无", "001蓝", "010红", "011品红"};
    private String[] voices = {"00无", "01短响", "10连续响1s", "11连续响2s"};
    private String[] blinks = {"00无", "01快闪", "10慢闪", "11先闪后灭"};
    private String[] lights = {"000无", "001外圈灯亮", "010里圈灯亮", "011全部亮", "100关灯"};
    private String[] actionModes = {"000无", "001红外", "010震动", "011全部", "100关闭"};


    private Order.LightColor color = Order.LightColor.NONE;
    private Order.VoiceMode voice = Order.VoiceMode.NONE;
    private Order.BlinkModel blinkModel = Order.BlinkModel.NONE;
    private Order.LightModel lightModel = Order.LightModel.None;
    private Order.ActionModel actionModel = Order.ActionModel.NONE;

    private List<TimeInfo> timeInfos = new ArrayList<>();
    private List<String> addressList = new ArrayList<>();
    private List<String> deviceNums = new ArrayList<>();
    private ReceiveThread thread;
    ArrayAdapter<String> adapter;


    private Device device;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String data = msg.obj.toString();
            switch (msg.what)
            {
                case 1:
                    //接收到时间数据
                    if (data.length() > 0)
                    {
                        timeInfos.addAll(DataAnalyzeUtils.analyzeTimeData(data));
                        timesAdapter.notifyDataSetChanged();
                    }
                case 2:
                    //接收到地址信息
                    if (data.length() > 0)
                    {
                        List<Map<String, String>> list = DataAnalyzeUtils.analyzeAddressData(data);
                        for (Map<String, String> map : list)
                        {
                            if (!addressList.contains(map.get("address")))
                            {
                                addressList.add(map.get("address"));
                                deviceNums.add(map.get("deviceNum"));
                            }
                        }
                        Log.d("AAAA", addressList.size() + "xx");
                        addressAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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

            //开启接收时间的线程
            thread = new ReceiveThread(handler, device.ftDev, ReceiveThread.TIME_RECEIVE_THREAD, 1);
            thread.start();
        }

        lvTimes.setAdapter(timesAdapter);
        lvAddress.setAdapter(addressAdapter);


        spColor.setOnItemSelectedListener(new SpinnerItemSelectedListener(TestActivity.this, spColor, colors)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                color = Order.LightColor.values()[i];
            }
        });


        spVoice.setOnItemSelectedListener(new SpinnerItemSelectedListener(TestActivity.this, spVoice, voices)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                voice = Order.VoiceMode.values()[i];
            }
        });

        spBlink.setOnItemSelectedListener(new SpinnerItemSelectedListener(TestActivity.this, spBlink, blinks)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                blinkModel = Order.BlinkModel.values()[i];
            }
        });

        spLight.setOnItemSelectedListener(new SpinnerItemSelectedListener(TestActivity.this, spLight, lights)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                lightModel = Order.LightModel.values()[i];
            }
        });

        spActionModel.setOnItemSelectedListener(new SpinnerItemSelectedListener(TestActivity.this, spActionModel, actionModes)
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                actionModel = Order.ActionModel.values()[i];
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, addressList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAddress.setAdapter(adapter);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        device.disconnectFunction();
        ReceiveThread.stopThread();
    }

    @OnClick({R.id.layout_cancel, R.id.btn_send_order, R.id.btn_clear, R.id.btn_get_address,
            R.id.btn_change, R.id.btn_change1})
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
                //线程设置成是接收时间线程
                thread.setMsgFlag(1);
                sendOrder();
                break;
            case R.id.btn_get_address:
                thread.setMsgFlag(2);
                addressList.clear();
                deviceNums.clear();
                device.sendMessage("#04a");
                break;
            case R.id.btn_change:
                changeAddress();
                break;

            case R.id.btn_change1:
                String id = etPanId1.getText().toString();
                if (id.equals(""))
                {
                    Toast.makeText(this, "PAN_ID不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                device.sendMessage("#08b" + id);
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

    private void changeAddress()
    {
        if (spAddress.getSelectedItemPosition() > addressList.size())
        {
            Toast.makeText(this, "未选择短地址!", Toast.LENGTH_SHORT).show();
            return;
        }

        String panId = etPanId.getText().toString();
        String lightNum = etLightNum.getText().toString();
        if (panId.equals(""))
        {
            Toast.makeText(this, "未输入PAN_ID!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (lightNum.equals(""))
        {
            Toast.makeText(this, "未输入设备编号!", Toast.LENGTH_SHORT).show();
            return;
        }

        String address = addressList.get(spAddress.getSelectedItemPosition());
        int len = address.length();
        while (len < 5)
        {
            address = "0" + address;
            len++;
        }
        String order = "#14*" +
                etPanId.getText().toString().trim() +
                etLightNum.getText().toString().trim() + address;
        Log.d(Constant.LOG_TAG, "order:" + order);
        device.sendMessage(order);
    }

    private void sendOrder()
    {
        String ids = etLightId.getText().toString();

        if (!device.checkDevice(TestActivity.this))
            return;
        device.sendOrder(ids,
                color,
                voice,
                blinkModel,
                lightModel,
                actionModel,
                Order.EndVoice.values()[cbEndVoice.isChecked() ? 1 : 0]);
    }

    private BaseAdapter timesAdapter = new BaseAdapter()
    {
        @Override
        public int getCount()
        {
            return timeInfos.size();
        }

        @Override
        public Object getItem(int i)
        {
            return null;
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            if (view == null)
                view = LayoutInflater.from(TestActivity.this).inflate(R.layout.item_statistics_time, null);
            TextView id = (TextView) view.findViewById(R.id.tv_num);
            TextView time = (TextView) view.findViewById(R.id.tv_time);
            TextView lightNum = (TextView) view.findViewById(R.id.tv_note);
            id.setText((i + 1) + "");
            lightNum.setText(timeInfos.get(i).getDeviceNum() + "");
            time.setText(timeInfos.get(i).getTime() + "");

            return view;
        }
    };

    private BaseAdapter addressAdapter = new BaseAdapter()
    {
        @Override
        public int getCount()
        {
            return addressList.size();
        }

        @Override
        public Object getItem(int i)
        {
            return null;
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            if (view == null)
                view = LayoutInflater.from(TestActivity.this).inflate(R.layout.item_statistics_time, null);
            TextView id = (TextView) view.findViewById(R.id.tv_num);
            TextView address = (TextView) view.findViewById(R.id.tv_time);
            TextView lightNum = (TextView) view.findViewById(R.id.tv_note);

            id.setText((i + 1) + "");
            address.setText(addressList.get(i));
            lightNum.setText(deviceNums.get(i));

            return view;
        }
    };


}

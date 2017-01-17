package com.oucb303.training.widget;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.us.ble.central.BLEDevice;
import com.us.ble.central.BLEManager;
import com.us.ble.listener.ScanListener;

import java.util.ArrayList;

/**
 * Created by BaiChangCai on 2017/1/11.
 * Description:手环操作类
 */
public class BraceletManager {

    private BluetoothManager myBluetoothManager;
    private BluetoothAdapter myBluetoothAdapter;
    private Context mContext;
    private Activity mActivity;
    private BLEManager mBleManager;
    private boolean scaning = false; // 是否正在扫描
    private BLEDevice dBleDevice; // 当前正在操作的设备
    private String dAddress; // 操作当前的设备
    private ArrayList<BLEDevice> mBLEList;
    private ArrayList<String> mStringBLEList;
    private String BRACELET_ADDRESS="DF:F7:C3:C9:6D:A3";
    private int MAX_TIME=60;//最大扫描手环设备时长
    private boolean isExist = false;
    private final int FIND_BRACELET = 2;

    public BraceletManager(Context context,Activity activity){
        this.mActivity = activity;
        this.mContext = context;
        myBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        myBluetoothAdapter = myBluetoothManager.getAdapter();
        init();
    }
    /**
     * 初始化
     */
    private void init() {
        mBleManager = new BLEManager(mActivity);
        mBLEList = new ArrayList<>();
        mStringBLEList = new ArrayList<>();
        mBleManager.setScanListener(new ScanListener() { // 扫描回调监听器
            @Override
            public void onScanResult(final int result,
                                     final BLEDevice bleDevice, final int rssi,
                                     byte[] scanRecord) {

                new Thread(new Runnable()
                {
                    public void run() {
                        if (result == 0) { // 正在扫描
                            scaning = true;
                            String address = bleDevice.getAddress();
                            if(!mStringBLEList.contains(address)){
                                mStringBLEList.add(address);
                                mBLEList.add(bleDevice);
                            }
                            if (mStringBLEList.contains(BRACELET_ADDRESS)) {
                                isExist = true;
                            }
                        }else {
                            // 扫描结束
                            scaning = false;
                            if (mBLEList.size() <= 0){
                                Log.i("BraceLet","找不到手环设备");
                                isExist = false;
                            } else {
                                for(int i=0;i<mBLEList.size();i++){
                                    Log.i("BraceLet",
                                            "找到了手环设备:"+mBLEList.get(i).getName() +  "Address : "
                                                    + mBLEList.get(i).getAddress());
                                }
                                if (mStringBLEList.contains(BRACELET_ADDRESS)) {
                                    isExist = true;
                                    connectBracelet();
                                }
                            }
                        }
                    }
                }).start();
            }
        });
    }

    public BluetoothAdapter getAdapter(){
        return myBluetoothAdapter;
    }
    /**
     * 判断蓝牙是否开启
     */
    public boolean isBluetoothOpen() {
        if (myBluetoothAdapter.isEnabled())
            return true;
        return false;
    }
    /**
     * 扫描设备
     * */
    public void scanBracelet() {
        if (mBLEList != null) {
            mStringBLEList.clear();
            mBLEList.clear();
        }
        if (scaning)
            stopScan();
        Log.i("BraceLet","开始扫描手环设备");
        mBleManager.startScan(MAX_TIME);
        scaning = true;
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        scaning = false;
        mBleManager.stopScan();
        Log.i("BraceLet","停止扫描手环设备");
    }
    /**
     * 是否存在
     */
    public boolean isExist(){
        return isExist;
    }
    /**
     * 是否正在扫描
     */
    public boolean isScaning() {
        return scaning;
    }

    public void setScaning(boolean scaning) {
        this.scaning = scaning;
    }
    /**
     * 连接设备
     * */
    protected void connectBracelet() {
        // 不要同时连几个蓝牙设备，要等连接成功后再连接下一个
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (scaning)// 先判断是否正在扫描
                        stopScan();
                    getCurrentDevice().connect();
                }
            }).start();
    }

    public BLEDevice getCurrentDevice(){
        if(isExist&&mBLEList.size()>0){
            for(BLEDevice device:mBLEList){
                if(device.getAddress().equals(BRACELET_ADDRESS)){
                    dBleDevice = device;
                }
            }
        }
        return dBleDevice;
    }

}

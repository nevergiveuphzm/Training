package com.oucb303.training.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.oucb303.training.R;
import com.oucb303.training.http.DownLoader;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.FileUtils;
import com.oucb303.training.utils.VersionUtils;

import org.apache.http.Header;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于SafLight
 */
public class AboutSafLightActivity extends AppCompatActivity
{

    @Bind(R.id.tv_app_name)
    TextView tvAppName;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    private final static int UPDATE_PROGRESS = 1;
    private PackageInfo packageInfo;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_saf_light);
        ButterKnife.bind(this);

        packageInfo = VersionUtils.getAppVersion(this);
        IntentFilter filter = new IntentFilter("DownLoad Complete!");
        registerReceiver(downLoadReceiver, filter);
        initView();
    }

    private void initView()
    {
        tvAppName.setText("SafLight v" + packageInfo.versionName);
        tvTitle.setText("关于SafLight");


        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setTitle("下载进度");
        dialog.setMax(100);
    }

    @OnClick({R.id.layout_cancel, R.id.ll_check_update})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.layout_cancel:
                this.finish();
                break;
            case R.id.ll_check_update:
                downLoadAPK();
                break;
        }
    }

    private void checkUpdate()
    {

    }

    private void downLoadAPK()
    {
        Log.d(Constant.LOG_TAG, Environment.getExternalStorageDirectory().getPath());
        dialog.show();
        DownLoader downLoader = new DownLoader();
        String[] allowedContentTypes = {"application/vnd.android.package-archive"};

        downLoader.downLoadFile(
                "http://192.168.1.108:8080/springmvc/user/download",
                new BinaryHttpResponseHandler(allowedContentTypes)
                {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes)
                    {
                        FileUtils.saveFile(bytes, "Training.apk");
                        dialog.dismiss();
                        sendBroadcast(new Intent("DownLoad Complete!"));
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable)
                    {
                        Log.d(Constant.LOG_TAG, throwable.getMessage());
                    }

                    @Override
                    public void onProgress(long bytesWritten, long totalSize)
                    {
                        super.onProgress(bytesWritten, totalSize);
                        dialog.setProgress((int) (bytesWritten * 100 / totalSize));
                        Log.d(Constant.LOG_TAG, bytesWritten + "");
                    }
                });
    }

    private BroadcastReceiver downLoadReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d(Constant.LOG_TAG, "download complete!");
            File file = new File(Constant.DOWNLOAD_PATH + "Training.apk");
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.addCategory(Intent.CATEGORY_DEFAULT);
            //newVersionAPK为新版本的文件
            intent1.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            //设置安装完成后用户可点击完成或者打开新安装的应用
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
    };
}

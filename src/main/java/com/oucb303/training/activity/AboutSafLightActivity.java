package com.oucb303.training.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.oucb303.training.R;
import com.oucb303.training.http.DownLoader;
import com.oucb303.training.http.HttpClientUtils;
import com.oucb303.training.utils.Constant;
import com.oucb303.training.utils.FileUtils;
import com.oucb303.training.utils.VersionUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String panId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_saf_light);
        ButterKnife.bind(this);

        packageInfo = VersionUtils.getAppVersion(this);
        IntentFilter filter = new IntentFilter("DownLoad Complete!");
        registerReceiver(downLoadReceiver, filter);
        panId = getIntent().getStringExtra("panId");
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
                if (panId == null || panId.equals(""))
                {
                    Toast.makeText(this, "当前为插入协调器!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isNetworkAvailable())
                    return;
                checkUpdate();
                break;
        }
    }

    private boolean isNetworkAvailable()
    {
        if (!HttpClientUtils.isNetworkAvailable(this))
        {
            Log.d(Constant.LOG_TAG, "当前网络不可用!");
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
            return false;
        } else
            Log.d(Constant.LOG_TAG, "网络可用!");
        return true;
    }

    private void checkUpdate()
    {
        RequestParams params = new RequestParams();
        params.put("panId", "FF01");
        params.put("versionCode", packageInfo.versionCode);
        HttpClientUtils.post(Constant.SERVER_IP + "/user/json", params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try
                {
                    int state = response.getInt("status");
                    //有新版本
                    if (state == 200)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AboutSafLightActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("检测到新版本,是否更新?");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                downLoadAPK();
                            }
                        });
                        AlertDialog alertdialog = builder.create();
                        alertdialog.show();

                    } else if (state == 400)
                    {
                        Toast.makeText(AboutSafLightActivity.this, "当前已经是最新版本!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                Log.d(Constant.LOG_TAG, "  " + statusCode + "  " + throwable.getMessage());
                Toast.makeText(AboutSafLightActivity.this, "服务器连接失败!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void downLoadAPK()
    {
        Log.d(Constant.LOG_TAG, Environment.getExternalStorageDirectory().getPath());
        dialog.show();
        DownLoader downLoader = new DownLoader();
        String[] allowedContentTypes = {"application/vnd.android.package-archive"};

        downLoader.downLoadFile(
                Constant.SERVER_IP + "/user/download",
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
                        Log.d(Constant.LOG_TAG, "fail:" + i + " " + throwable.getMessage());
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

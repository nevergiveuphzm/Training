package com.oucb303.training.http;

import android.util.Log;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.oucb303.training.model.Constant;

import org.apache.http.Header;

/**
 * Created by huzhiming on 2016/12/19.
 */

public class DownLoader
{
    private String path;

    public DownLoader()
    {

    }

    public DownLoader(String path)
    {

    }

    public void downLoadFile(String url)
    {
        HttpClientUtils.get(url, new BinaryHttpResponseHandler()
        {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes)
            {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable)
            {

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize)
            {
                super.onProgress(bytesWritten, totalSize);
                Log.d(Constant.LOG_TAG, bytesWritten + "");
            }
        });
    }
}

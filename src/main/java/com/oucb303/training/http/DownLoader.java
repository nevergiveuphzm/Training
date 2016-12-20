package com.oucb303.training.http;

import com.loopj.android.http.BinaryHttpResponseHandler;

/**
 * Created by huzhiming on 2016/12/19.
 */

public class DownLoader
{

    public DownLoader()
    {

    }

    public void downLoadFile(String url, BinaryHttpResponseHandler handler)
    {
        HttpClientUtils.get(url, handler);
    }
}

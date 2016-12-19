package com.oucb303.training.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by huzhiming on 2016/12/19.
 */

public class HttpClientUtils
{

    private static AsyncHttpClient client;

    public static AsyncHttpClient getInstance()
    {
        if (client == null)
        {
            client = new AsyncHttpClient();
            //设置超时10s
            client.setTimeout(10000);
        }
        return client;
    }

    //不带参数的get请求
    public static void get(String url, AsyncHttpResponseHandler handler)
    {
        getInstance().get(url, handler);
    }

    //带参数的get请求
    public static void get(String url, AsyncHttpResponseHandler handler, RequestParams params)
    {
        getInstance().get(url, params, handler);
    }

    //下载
    public static void get(String url, BinaryHttpResponseHandler handler)
    {
        getInstance().post(url, handler);
    }

    //不带参数的post请求
    public static void post(String url, AsyncHttpResponseHandler handler)
    {
        getInstance().post(url, handler);
    }

    //带参数的post请求
    public static void post(String url, AsyncHttpResponseHandler handler, RequestParams params)
    {
        getInstance().post(url, params, handler);
    }




}

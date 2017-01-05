package com.oucb303.training.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by huzhiming on 2016/12/19.
 */

public class HttpClientUtils
{

    private static AsyncHttpClient client;

    public synchronized static AsyncHttpClient getInstance()
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
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler)
    {
        getInstance().get(url, params, handler);
    }

    //下载
    public static void get(String url, BinaryHttpResponseHandler handler)
    {
        getInstance().get(url, handler);
    }

    //不带参数的post请求
    public static void post(String url, AsyncHttpResponseHandler handler)
    {
        getInstance().post(url, handler);
    }

    //带参数的post请求
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler handler)
    {
        getInstance().post(url, params, handler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler handler)
    {
        getInstance().post(url, params, handler);
    }

    //检测网络是否可用
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
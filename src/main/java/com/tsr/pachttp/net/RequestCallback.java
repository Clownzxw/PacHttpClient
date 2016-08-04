package com.tsr.pachttp.net;

/**
 * 请求回调接口
 */
public interface RequestCallback
{
    void onSuccess(String content);

    void onFail(String errorMessage);
}

package com.tsr.pachttp.net;

import android.app.Activity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacHttpClient {
    // 配置信息
    static PacHttpClientConfig config;
    // 存放每个Activity对应的RequestManager
    static Map<Activity, RequestManager> managerMap;

    /**
     * 初始化
     * @param config 全局配置信息
     */
    public static void init(PacHttpClientConfig config) {
        PacHttpClient.config = config;
        managerMap = new HashMap<>();
        // 初始化线程池
        RequestThreadPool.init();
    }

    /**
     * 执行HTTP请求(不含请求参数)
     * @param activity 发起HTTP请求的Activity
     * @param apiKey   根据该值从XML文件中获取对应的URLEntity
     * @param callBack HTTP请求执行完毕后的回调接口
     */
    public static void invokeRequest(
            Activity activity, String apiKey, RequestCallback callBack) {
        invokeRequest(activity, apiKey, null, callBack);
    }

    /**
     * 执行HTTP请求(不含请求参数)
     * @param activity 发起HTTP请求的Activity
     * @param apiKey   根据该值从XML文件中获取对应的URLEntity
     * @param params   HTTP请求参数
     * @param callBack HTTP请求执行完毕后的回调接口
     */
    public static HttpRequest invokeRequest(
            Activity activity, String apiKey,
            List<RequestParameter> params, RequestCallback callBack) {
        // 根据apiKey从XML文件中读取封装的URL实体信息
        URLEntity url = URLConfigManager.findURLByKey(apiKey);
        // 获取该activity对应的RequestManager对象，并创建HttpRequest对象
        RequestManager manager = checkRequestManager(activity, true);
        HttpRequest request = manager.createRequest(
                url, params, callBack);
        // 执行请求
        RequestThreadPool.execute(request);

        return request;
    }

    /**
     * 取消指定Activity中发起的所有HTTP请求
     * @param activity
     */
    public static void cancelAllRequest(Activity activity) {
        checkRequestManager(activity, false).cancelAllRequest();
    }

    /**
     * 取消线程池中整个阻塞队列所有HTTP请求
     */
    public static void cancelAllRequest() {
        RequestThreadPool.removeAllTask();
    }

    /**
     * 取消指定Activity中未执行的请求
     * @param activity
     */
    public static void cancelBlockingRequest(Activity activity) {
        checkRequestManager(activity, false).cancelBlockingRequest();
    }

    /**
     * 取消指定请求
     * @param activity
     * @param request
     */
    public static void cancelDesignatedRequest(Activity activity, HttpRequest request) {
        checkRequestManager(activity, false).cancelDesignatedRequest(request);
    }

    /**
     * 访问activity对应的RequestManager对象
     * @param activity
     * @param createNew 当RequestManager对象为null时是否创建新的RequestManager对象
     * @return
     */
    private static RequestManager checkRequestManager(Activity activity, boolean createNew) {
        RequestManager manager;
        if ((manager = managerMap.get(activity)) == null) {
            if (createNew) {
                manager = new RequestManager();
                managerMap.put(activity, manager);
            } else {
                throw new NullPointerException(activity.getClass().getSimpleName() + "'s RequestManager is null!");
            }
        }
        return manager;
    }

    /**
     * 关闭线程池，并等待任务执行完成，不接受新任务
     */
    public static void shutdown() {
        RequestThreadPool.shutdown();
    }

    /**
     * 关闭，立即关闭，并挂起所有正在执行的线程，不接受新任务
     */
    public static void shutdownRightnow() {
        RequestThreadPool.shutdownRightnow();
    }
}

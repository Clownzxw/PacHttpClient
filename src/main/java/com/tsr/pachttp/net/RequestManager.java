package com.tsr.pachttp.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

class RequestManager {

    ArrayList<HttpRequest> requests;

    public RequestManager() {
        requests = new ArrayList<>();
    }

    /**
     * 无参数调用
     */
    public HttpRequest createRequest(URLEntity url, RequestCallback requestCallback) {
        return createRequest(url, null, requestCallback);
    }

    /**
     * 有参数调用
     */
    public HttpRequest createRequest(URLEntity url, List<RequestParameter> params, RequestCallback requestCallback) {
        HttpRequest request = new HttpRequest(this, url, params, requestCallback);
        addRequest(request);
        return request;
    }

    /**
     * 添加Request到列表
     */
    public void addRequest(final HttpRequest request) {
        requests.add(request);
    }

    /**
     * 取消所有的网络请求(包括正在执行的)
     */
    public void cancelAllRequest() {
        BlockingQueue queue = RequestThreadPool.getQuene();
        for (int i = requests.size() - 1; i >= 0; i--) {
            HttpRequest request = requests.get(i);
            if (queue.contains(request)) {
                queue.remove(request);
            } else {
                request.disconnect();
            }
        }
        requests.clear();
    }

    /**
     * 取消未执行的网络请求
     */
    public void cancelBlockingRequest() {
        // 取交集(即取出那些在线程池的阻塞队列中等待执行的请求)
        List<HttpRequest> intersection = (List<HttpRequest>) requests.clone();
        intersection.retainAll(RequestThreadPool.getQuene());
        // 分别删除
        RequestThreadPool.getQuene().removeAll(intersection);
        requests.removeAll(intersection);
    }

    /**
     * 取消指定的网络请求
     */
    public void cancelDesignatedRequest(HttpRequest request) {
        if (!RequestThreadPool.removeTaskFromQueue(request)) {
            request.disconnect();
        }
    }


}

# PacHttpClient
一个轻量级的Android网络请求框架。<br> 
底层采用原生 ThreadPoolExecutor + Runnble + Handler实现。<br> 
Http请求封装；定制线程池；节约资源；可控制请求中断、取消。<br> <br> 
使用简介：<br> 
1、配置框架信息
```Java
PacHttpClientConfig config = 
       new PacHttpClientConfig(getApplicationContext())
       .corePoolZie( ? )
       .maxPoolSize( ? )
       .keepAliveTime( ? )
       .timeUnit( ? )
       .blockingQueue( ? );
```
或直接使用默认配置：
```Java
PacHttpClientConfig config = 
       new PacHttpClientConfig(getApplicationContext())；
```
2、初始化<br> 
```Java
PacHttpClient.init(config);
```
3、发起请求<br> 
```Java
/*HttpRequest request =*/ PacHttpClient.invokeRequest(this/*activity*/, "apiKey", params, new RequestCallback() { 
   @Override
   public void onSuccess(String content) {
                
   }
            
   @Override
   public void onFail(String errorMessage) {

   }
});
```
4、取消(中断)指定请求
```Java
PacHttpClient.cancelDesignatedRequest(this,request);
```

5、取消指定Activity当中未执行的请求
```Java
PacHttpClient.cancelBlockingRequest(this);
```

6、取消(中断)指定Activity当中所有的请求
```Java
PacHttpClient.cancelAllRequest(this);
```

7、取消整个线程池中所有未执行的请求
```Java
PacHttpClient.cancelAllRequest();
```

8、关闭框架
```Java
PacHttpClient.shutdown();
PacHttpClient.shutdownRightnow();
```


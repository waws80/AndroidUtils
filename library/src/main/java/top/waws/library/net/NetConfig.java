package top.waws.library.net;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @desc:
 * @className: NetConfig
 * @author: thanatos
 */
public interface NetConfig {

    /**
     * 初始化Retrofit
     * @return
     */
    Retrofit initRetrofit();

    /**
     * 初始化okhttp
     * @return
     */
    OkHttpClient  initClient();

    /**
     * 是否debug
     * @return
     */
    boolean isDebug();


    /**
     * 是否使用https
     * @return
     */
    boolean useHttps();

    /**
     * 拦截器列表
     * @return
     */
    List<Interceptor> getInterceptor();

    /**
     * 网络拦截器
     * @return
     */
    List<Interceptor> getNetWorkInterceptor();

}

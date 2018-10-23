package top.waws.library.net;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import top.waws.library.AppUtils;
import top.waws.library.gson.GsonUtils;

/**
 * @desc: 默认的网络配置类
 * @className: DefaultNetConfig
 * @author: thanatos
 */
public final class DefaultNetConfig implements NetConfig {

    //baseurl
    private String mBaseUrl;
    //https enable
    private boolean isHttps;

    public DefaultNetConfig(@NonNull String baseUrl){
        this(baseUrl,false);
    }

    public DefaultNetConfig(@NonNull String baseUrl, boolean useHttps){
        this.mBaseUrl = baseUrl;
        this.isHttps = useHttps;
    }

    @Override
    public Retrofit initRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(initClient())
                .validateEagerly(true)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonUtils.getInstance().getGson()))
                .build();
    }

    @Override
    public OkHttpClient initClient() {
        OkHttpClient.Builder build = new OkHttpClient.Builder()
                .connectTimeout(60,TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                ;

        if (isDebug()){
            //添加日志
            build.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        //添加拦截器
        if (!getInterceptor().isEmpty()){
            for (Interceptor interceptor : getInterceptor()) {
                build.addInterceptor(interceptor);
            }
        }
        //添加网络拦截器
        if (!getNetWorkInterceptor().isEmpty()){
            for (Interceptor interceptor : getNetWorkInterceptor()) {
                build.addNetworkInterceptor(interceptor);
            }
        }
        if (useHttps() && getDefault() != null){
            //添加https支持
            build.sslSocketFactory(getDefault(),x509TrustManager);
            build.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        return build.build();
    }

    @Override
    public boolean isDebug() {
        return AppUtils.isDebug();
    }

    @Override
    public boolean useHttps() {
        return this.isHttps;
    }


    @Override
    public List<Interceptor> getInterceptor() {
        return new ArrayList<>();
    }

    @Override
    public List<Interceptor> getNetWorkInterceptor() {
        return Collections.singletonList(new DefaultNetWorkInterceptor());
    }

    private SSLSocketFactory getDefault(){
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(new KeyManager[]{},new TrustManager[]{x509TrustManager},new SecureRandom());
            return ctx.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private X509TrustManager  x509TrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };


}

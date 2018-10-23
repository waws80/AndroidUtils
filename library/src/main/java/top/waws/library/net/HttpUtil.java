package top.waws.library.net;

import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import top.waws.library.AppUtils;
import top.waws.library.gson.GsonUtils;

/**
 * @desc: 网络请求工具类
 * @className: NetUtil
 * @author: thanatos
 */
public class HttpUtil {

    private static volatile NetConfig sConfig;

    private HttpUtil(){}

    private static final class Inner{
        private static final HttpUtil HTTP_UTIL = new HttpUtil();
    }

    /**
     * 初始化网络框架
     * @param config 网络配置
     */
    public static void init(@NonNull NetConfig config) {
        sConfig = config;
    }

    /**
     * 获取当前唯一实类对象
     * @return
     */
    public static HttpUtil getDefault(){
        return Inner.HTTP_UTIL;
    }

    /**
     * 获取retrofit服务
     * @param service
     * @param <T>
     * @return
     */
    public <T> T getService(@NonNull Class<T> service){
        return getWrapperService(service);
    }

    /**
     * get请求
     * @param url
     * @param observer
     * @param clz
     * @param <T>
     */
    public <T> void get(@NonNull String url, @NonNull Observer<T> observer, @NonNull Class<T> clz){
        get(url, new HashMap<>(), observer, clz);
    }

    public <T> void get(@NonNull String url, @NonNull Map<String, String> header,
                        @NonNull Observer<T> observer, @NonNull Class<T> clz){
        get(url,header,new HashMap<>(),observer,clz);
    }

    public <T> void get(@NonNull String url, @NonNull Map<String, String> header,
                        @NonNull Map<String, String> query, @NonNull Observer<T> observer,
                        @NonNull Class<T> clz){
        if (!url.startsWith("http")){
            url = sConfig.initRetrofit().baseUrl().toString() + url;
        }
        getDefaultService().get(url,header,query)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        observer.onSubscribe(d);
                    }

                    @Override
                    public void onNext(String value) {
                        if (clz == String.class){
                            observer.onNext((T) value);
                        }else {
                            observer.onNext(GsonUtils.getInstance().convertObject(value,clz));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    /**
     * 下载大文件
     * @param url
     * @param callback
     * @param saveFile
     */
    public void downloadFile(@NonNull String url, @NonNull DownloadCallback callback, @NonNull File saveFile){
        downloadFile(url, new HashMap<>(), callback, saveFile);
    }

    public void downloadFile(@NonNull String url, @NonNull Map<String, String> header,
                                 @NonNull DownloadCallback callback, @NonNull File saveFile){
        downloadFile(url, header, new HashMap<>(), callback, saveFile);
    }

    public void downloadFile(@NonNull String url, @NonNull Map<String, String> header,
                                 @NonNull Map<String, String> query, @NonNull DownloadCallback callback,
                                 @NonNull File saveFile){
        if (!url.startsWith("http")){
            url = sConfig.initRetrofit().baseUrl().toString() + url;
        }
        getDefaultService().downloadFile(url, header, query)
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        AppUtils.getInstance().getWeakHandler()
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSubscribe(d);
                                    }
                                });
                    }

                    @Override
                    public void onNext(ResponseBody value) {
                        try {
                            saveFile(value, saveFile, callback);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppUtils.getInstance().getWeakHandler()
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(e);
                                    }
                                });
                    }

                    @Override
                    public void onComplete() {
                        AppUtils.getInstance().getWeakHandler()
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onComplete();
                                    }
                                });
                    }
                });
    }

    /**
     * 保存文件
     * @param value 相应体
     * @param saveFile 保存的文件
     * @param callback 回调
     * @throws Exception
     */
    private void saveFile(ResponseBody value, File saveFile, DownloadCallback callback) throws Exception {
        FileOutputStream fos = new FileOutputStream(saveFile);
        ProgressResponseBody body = new ProgressResponseBody(value) {
            @Override
            void download(long read, long total, boolean finish) {
                double offset = 0.0D;
                int progress = finish? 100 : (int) (((read + offset)/total) * 100);
                AppUtils.getInstance().logd(progress);
                AppUtils.getInstance().getWeakHandler()
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                if (finish){
                                    callback.progress(100);
                                }else {
                                    callback.progress(progress);
                                }

                            }
                        });
            }
        };
        fos.write(body.bytes());
        fos.flush();
        fos.close();
        AppUtils.getInstance().getWeakHandler()
                .post(new Runnable() {
                    @Override
                    public void run() {
                        callback.progress(100);
                        callback.onNext(saveFile);
                    }
                });
    }


    /**
     * 提交表单
     * @param url
     * @param form
     * @param observer
     * @param clz
     * @param <T>
     */
    public <T> void form(@NonNull String url,@NonNull Map<String, String> form,
                         @NonNull Observer<T> observer, @NonNull Class<T> clz){
        form(url,new HashMap<>(), form, observer, clz);
    }

    public <T> void form(@NonNull String url, @NonNull Map<String, String> header,
                         @NonNull Map<String, String> form, @NonNull Observer<T> observer,
                         @NonNull Class<T> clz){
        getDefaultService().form(url, header, form)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        observer.onSubscribe(d);
                    }

                    @Override
                    public void onNext(String value) {
                        if (clz == String.class){
                            observer.onNext((T) value);
                        }else {
                            observer.onNext(GsonUtils.getInstance().convertObject(value,clz));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    /**
     * 提交数据
     * @param url
     * @param header
     * @param json
     * @param observer
     * @param clz
     * @param <T>
     */
    public <T> void post(@NonNull String url, @NonNull Map<String, String> header,
                         @NonNull String json, @NonNull Observer<T> observer,
                         @NonNull Class<T> clz){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        getDefaultService().upload(url,header,body)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        observer.onSubscribe(d);
                    }

                    @Override
                    public void onNext(String value) {
                        if (clz == String.class){
                            observer.onNext((T) value);
                        }else {
                            observer.onNext(GsonUtils.getInstance().convertObject(value,clz));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });

    }


    /**
     * 上传数据
     * @param url
     * @param form
     * @param files
     * @param callback
     * @param clz
     * @param <T>
     */
    public <T> void upload(@NonNull String url, @NonNull Map<String, String> form,
                           @NonNull Map<String, File> files, @NonNull UploadCallback<T> callback,
                           @NonNull Class<T> clz){
        upload(url, new HashMap<>(),form,files,callback,clz);
    }

    public <T> void upload(@NonNull String url, @NonNull Map<String, String> header,
                           @NonNull Map<String, String> form, @NonNull Map<String, File> files,
                           @NonNull UploadCallback<T> callback,
                           @NonNull Class<T> clz){

        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (!form.isEmpty()){
            for (Map.Entry<String, String> entry : form.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (!files.isEmpty()){
            String mediaType = "application/octet-stream";
            for (Map.Entry<String, File> fileEntry : files.entrySet()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse(mediaType),fileEntry.getValue());
                builder.addFormDataPart(fileEntry.getKey(),fileEntry.getValue().getName(), fileBody);
            }
        }
        //上传
        getDefaultService().upload(url, header, new ProgressRequestBody(builder.build()) {
            @Override
            public void loading(long current, long total, boolean done) {
                double offset = 0.0D;
                int progress = done? 100 : (int) (((current + offset)/total) * 100);
                AppUtils.getInstance().getWeakHandler()
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                callback.progress(progress);
                            }
                        });
            }
        }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        callback.onSubscribe(d);
                    }

                    @Override
                    public void onNext(String value) {
                        callback.progress(100);
                        if (clz == String.class){
                            callback.onNext((T) value);
                        }else {
                            callback.onNext(GsonUtils.getInstance().convertObject(value,clz));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        callback.onComplete();
                    }
                });


    }

    /**
     * 获取默认的apiService
     * @return {@link DefaultService}
     */
    public DefaultService getDefaultService(){
        return getService(DefaultService.class);
    }


    /**
     * service包裹类 自动将 方法运行在 子线程上，回调运行在主线程
     * @param service
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getWrapperService(@NonNull Class<T> service){
        T serviceInstance = sConfig.initRetrofit().create(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service},
                new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getReturnType() == Observable.class){
                    return ((Observable)method.invoke(serviceInstance,args))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return method.invoke(serviceInstance,args);
            }
        });
    }
}

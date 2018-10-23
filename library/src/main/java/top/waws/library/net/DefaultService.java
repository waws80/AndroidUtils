package top.waws.library.net;

import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @desc:
 * @className: DefaultService
 * @author: thanatos
 */
interface DefaultService {

    /**
     * Get请求
     * @param url 请求的url
     * @return {@link Observable<String>}
     */
    @GET
    Observable<String> get(@NonNull @Url String url);

    /**
     * Get请求
     * @param url 请求的url
     * @param header 请求头
     * @return {@link Observable<String>}
     */
    @GET
    Observable<String> get(@NonNull @Url String url,
                           @NonNull @HeaderMap Map<String, String> header);

    /**
     * Get请求
     * @param url 请求的url
     * @param header 请求头
     * @param query 请求参数
     * @return {@link Observable<String>}
     */
    @GET
    Observable<String> get(@NonNull @Url String url,
                           @NonNull @HeaderMap Map<String, String> header,
                           @NonNull @QueryMap Map<String, String> query);

    /**
     * 下载文件请求
     * @param url 请求的url
     * @return {@link Observable<ResponseBody>}
     */
    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@NonNull @Url String url);

    /**
     * 下载文件请求
     * @param url 请求的url
     * @param header 请求头
     * @return {@link Observable<ResponseBody>}
     */
    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@NonNull @Url String url,
                                          @NonNull @HeaderMap Map<String, String> header);

    /**
     * 下载文件请求
     * @param url 请求的url
     * @param header 请求头
     * @param query 请求参数
     * @return {@link Observable<ResponseBody>}
     */
    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@NonNull @Url String url,
                                          @NonNull @HeaderMap Map<String, String> header,
                                          @NonNull @QueryMap Map<String, String> query);
    /**
     *表单上传
     * @param url 请求的url
     * @param form 表单
     * @return {@link Observable<String>}
     */
    @POST
    @FormUrlEncoded
    Observable<String> form(@NonNull @Url String url,
                            @NonNull @FieldMap Map<String, String> form);

    /**
     * 表单上传
     * @param url 请求的url
     * @param header 请求头
     * @param form 表单
     * @return {@link Observable<String>}
     */
    @POST
    @FormUrlEncoded
    Observable<String> form(@NonNull @Url String url,
                            @NonNull @HeaderMap Map<String, String> header,
                            @NonNull @FieldMap Map<String, String> form);

    /**
     * 上传数据
     * @param url 请求的url
     * @param body 请求体
     * @return {@link Observable<String>}
     */
    @POST
    Observable<String> upload(@NonNull @Url String url,
                              @NonNull @Body RequestBody body);

    /**
     * 上传数据
     * @param url 请求的url
     * @param header 请求头
     * @param body 请求体
     * @return {@link Observable<String>}
     */
    @POST
    Observable<String> upload(@NonNull @Url String url,
                              @NonNull @HeaderMap Map<String, String> header,
                              @NonNull @Body RequestBody body);

}

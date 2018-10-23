package top.waws.library.net;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import top.waws.library.AppUtils;

/**
 * @desc: 网络拦截器
 * @className: DefaultNetWorkInterceptor
 * @author: thanatos
 */
public class DefaultNetWorkInterceptor implements Interceptor {

    //无网络网络状态头
    public static final String NET_STATUS_HEADER = "netStatus";

    //无网络头的值
    public static final String NO_NETWORK_VALUE = "no_network_status_true";

    //无网络响应码
    public static final int NO_NETWORK_RESPONSE_CODE = -999;

    //无网络message
    private String noNetWorkMessage;

    public DefaultNetWorkInterceptor(){
        this("当前网络不可用");
    }

    public DefaultNetWorkInterceptor(@NonNull String noNetWorkMessage){
        this.noNetWorkMessage = noNetWorkMessage;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (AppUtils.getInstance().isConnectedNet()){
            return chain.proceed(chain.request());
        }else {
            MediaType mediaType = null;
            RequestBody body = chain.request().body();
            if (body != null){
                mediaType = body.contentType();
            }
            return new Response.Builder()
                    .message(this.noNetWorkMessage)
                    .code(NO_NETWORK_RESPONSE_CODE)
                    .addHeader(NET_STATUS_HEADER,NO_NETWORK_VALUE)
                    .request(chain.request())
                    .headers(chain.request().headers())
                    .sentRequestAtMillis(System.currentTimeMillis())
                    .receivedResponseAtMillis(System.currentTimeMillis())
                    .body(ResponseBody.create(mediaType,""))
                    .build();
        }
    }
}

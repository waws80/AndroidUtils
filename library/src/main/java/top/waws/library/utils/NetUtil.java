package top.waws.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import top.waws.library.AppUtils;


/**
 *  @desc: 网络连接工具类
 *  @className: NetUtil
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午3:51
 */
public final class NetUtil {

    private NetUtil() { }

    private static final class Inner{
        private static final NetUtil NET_UTIL = new NetUtil();
    }

    public static NetUtil getInstance(){
        return Inner.NET_UTIL;
    }
    /**
     * 判断网络是否连接
     * @return
     */
    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) AppUtils.getInstance().getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) AppUtils.getInstance().getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

}

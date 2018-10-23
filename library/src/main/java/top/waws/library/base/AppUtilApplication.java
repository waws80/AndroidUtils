package top.waws.library.base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import top.waws.library.AppUtils;

/**
 *  @desc: 工具类Application
 *  @className: AppUtilApplication
 *  @author: thanatos
 *  @createTime: 2018/10/20
 *  @updateTime: 2018/10/20 下午9:52
 */
public class AppUtilApplication extends Application {

    /**
     * 是否是调试模式
     */
    private boolean isDebug = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this, isDebug());
    }

    /**
     * 获取是否是调试模式
     * @return
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 设置是否为调试模式
     * @param debug
     */
    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}

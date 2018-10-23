package top.waws.library.navigationbar;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

import java.lang.ref.WeakReference;

import androidx.annotation.ColorInt;
import top.waws.library.AppUtils;

/**
 *  @desc:  手机导航栏工具类
 *  @className: NavigationBarUtil
 *  @author: thanatos
 *  @createTime: 2018/10/20
 *  @updateTime: 2018/10/20 下午6:57
 */
public class NavigationBarUtil {

    private NavigationBarUtil(){ }

    private static final class Inner{
        private static final NavigationBarUtil NAVIGATION_BAR_UTIL = new NavigationBarUtil();
    }

    public static NavigationBarUtil getInstance(){
        return Inner.NAVIGATION_BAR_UTIL;
    }

    /**
     * 设置导航栏
     * @param activity
     * @param color
     */
    public void setNavigationBarColor(Activity activity, @ColorInt int color){
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activityWeakReference.get().getWindow().setNavigationBarColor(color);
        }
    }

    /**
     * 获取导航栏高度
     * @return
     */
    public int getNavigationBarHeight(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        int rid = AppUtils.getInstance().getContext().getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0 && hasNavigation(activityWeakReference.get())) {
            return AppUtils.getInstance().getContext().getResources().getDimensionPixelSize(AppUtils.getInstance().getContext().getResources()
                    .getIdentifier("navigation_bar_height", "dimen", "android"));
        } else {
            return 0;
        }
    }


    /**
     * 屏幕上是否有导航栏
     */
    public boolean hasNavigation(Activity activity){
        Display d = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        float realHeight = realDisplayMetrics.heightPixels;
        float realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        float displayHeight = displayMetrics.heightPixels;
        float displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

}

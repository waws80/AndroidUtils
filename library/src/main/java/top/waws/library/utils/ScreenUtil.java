package top.waws.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import top.waws.library.AppUtils;



/**
 *  @desc: 屏幕工具类
 *  @className: ScreenUtils
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午4:27
 */
public final class ScreenUtil {

    private ScreenUtil(){}

    private static final class Inner{
        private static final ScreenUtil SCREEN_UTIL = new ScreenUtil();
    }

    public static ScreenUtil getInstance(){
        return Inner.SCREEN_UTIL;
    }

    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = AppUtils.getInstance().getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = AppUtils.getInstance().getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取标题栏高度
     *
     * @param activity
     * @return
     */
    public int getTitleBarHeight(Activity activity) {
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusBarHeight();
    }

    /**
     * 在Activity中获取屏幕的高度和宽度
     *
     * @param activity 在真机中，有时候会发现得到的尺寸不是很准确，需要在AndroidManifest中添加如下配置：
     *                 <supports-screens android:smallScreens="true"
     *                 android:normalScreens="true" android:largeScreens="true"
     *                 android:resizeable="true" android:anyDensity="true" />
     */
    public int[] getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return new int[]{
                point.x, point.y
        };
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    public Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    /**
     * 获取屏幕长宽比
     *
     * @param context
     * @return 比例值
     */
    public float getScreenRate(Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    /**
     * 在非Activity中，通常会在Custom View时
     * 目前不推荐使用（3.2及以下）
     *
     * @param context 在真机中，有时候会发现得到的尺寸不是很准确，需要在AndroidManifest中添加如下配置：
     *                <supports-screens android:smallScreens="true"
     *                android:normalScreens="true" android:largeScreens="true"
     *                android:resizeable="true" android:anyDensity="true" />
     */
    public int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return new int[]{
                dm.widthPixels, dm.heightPixels
        };
    }


    /**
     * dp转px
     * @param dp
     * @return
     */
    public float dp2px(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,AppUtils.getInstance()
                .getContext().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param sp
     * @return
     */
    public float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, AppUtils.getInstance()
                        .getContext().getResources().getDisplayMetrics());
    }


    /**
     * px转dp
     */
    public float px2dp(int px){
        float scale = AppUtils.getInstance()
                .getContext().getResources().getDisplayMetrics().density;
        return (px / scale + 0.5f);
    }

    /**
     * px转sp
     */
    public float px2sp(int px){
        float scale = AppUtils.getInstance()
                .getContext().getResources().getDisplayMetrics().density;
        return (px / scale + 0.5f);
    }


}

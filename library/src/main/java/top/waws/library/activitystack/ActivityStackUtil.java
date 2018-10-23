package top.waws.library.activitystack;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import top.waws.library.AppUtils;

/**
 *  @desc: Activity栈工具类
 *  @className: ActivityStackUtil
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 下午1:42
 */
public class ActivityStackUtil {

    //全局上下文
    private static final Application sApplication = (Application) AppUtils.getInstance().getContext();

    //activity活动栈
    private static final Stack<ActivityBean> sActivityStack = new Stack<>();

    //app状态监听
    private static AppStatusListener sAppStatusListener;

    //程序退出的时间
    private static long exitTime = 0;

    private ActivityStackUtil(){
        sApplication.registerActivityLifecycleCallbacks(new InnerActivityLifecycleCallBack());
    }

    private static final class Inner{
        private static final ActivityStackUtil STACK_UTIL = new ActivityStackUtil();
    }

    public static ActivityStackUtil getInstance(){
        return Inner.STACK_UTIL;
    }

    /**
     * 添加app状态唯一对象
     * @param listener 回调
     */
    public void addAppStatusListener(@NonNull AppStatusListener listener){
        sAppStatusListener = listener;
    }

    /**
     * 获取当前显示的activity
     * @return
     */
    public @Nullable Activity getCurrentActivity(){
        if (sActivityStack.isEmpty()) return null;
        if (sActivityStack.lastElement() == null){
            return null;
        }
        return sActivityStack.lastElement().activity;
    }

    /**
     * 关闭当前activity
     */
    public void finishCurrentActivity(){
        if (getCurrentActivity() != null){
            getCurrentActivity().finish();
        }
    }

    /**
     * 关闭某个activity
     * @param clz activity
     */
    public void finish(@NonNull Class<? extends Activity> clz){
        for (ActivityBean activityBean : sActivityStack) {
            if (activityBean.activity.getClass() == clz){
                sActivityStack.remove(activityBean);
                activityBean.activity.finish();
            }
        }
    }

    /**
     * 关闭所有的页面
     */
    public void finishAll(){
        for (ActivityBean activityBean : sActivityStack) {
            activityBean.activity.finish();
        }
        sActivityStack.clear();
    }

    /**
     * 退出程序
     */
    public void AppExit(){
        finishAll();
        AppUtils.getInstance().getWeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        },200L);
    }


    /**
     * 退出程序
     */
    public void exit(){
        exit(2000L,"再按一次退出应用");
    }

    /**
     * 退出程序
     * @param delay 时间可延迟长度
     * @param s 提示语 "再按一次退出应用"
     */
    public void exit(long delay, CharSequence s){
        if (System.currentTimeMillis() - exitTime > delay){
            AppUtils.getInstance().showToast(s);
            exitTime = System.currentTimeMillis();
        }else {
            AppExit();
        }
    }




    /**
     * Activity活动页的生命周期回调
     */
    private static final class InnerActivityLifecycleCallBack implements Application.ActivityLifecycleCallbacks{

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //添加到堆栈中
            sActivityStack.push(new ActivityBean(activity,ACTIVITY_UNKNOW));
        }

        @Override
        public void onActivityStarted(Activity activity) {
            for (ActivityBean activityBean : sActivityStack) {
                if (activityBean.activity == activity){
                    activityBean.status = ACTIVITY_START;
                    break;
                }
            }
            //程序在前台运行
            if (null != sAppStatusListener){
                sAppStatusListener.status(false);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            boolean isBg = true;
            for (ActivityBean activityBean : sActivityStack) {
                if (activityBean.activity == activity){
                    activityBean.status = ACTIVITY_STOP;
                }else {
                    if (activityBean.status == ACTIVITY_START){
                        isBg = false;
                    }
                }
            }

            /**
             * 程序是否在后台运行
             */
            if (null != sAppStatusListener){
                sAppStatusListener.status(isBg);
            }

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            //从堆栈中移除
            for (ActivityBean activityBean : sActivityStack) {
                if (activityBean.activity == activity){
                    sActivityStack.remove(activityBean);
                    break;
                }
            }
        }
    }


    /**
     * activity信息类
     */
    private static final class ActivityBean{
        Activity activity;
        @ActivityStatus int status;

        ActivityBean(Activity activity,@ActivityStatus int status){
            this.activity = activity;
            this.status = status;
        }
    }


    /**
     * activity已经启动
     */
    public static final int ACTIVITY_START = 0;

    /**
     * activity已停止
     */
    public static final int ACTIVITY_STOP = 1;

    /**
     * activity未知状态
     */
    public static final int ACTIVITY_UNKNOW = -1;

}

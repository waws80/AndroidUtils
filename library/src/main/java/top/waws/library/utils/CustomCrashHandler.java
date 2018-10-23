package top.waws.library.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;


/**
 *  @desc: 全局异常捕获类
 *  @className: CustomCrashHandler
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午4:03
 */
public class CustomCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = CustomCrashHandler.class.getSimpleName();
    private Application context;
    private String message = "应用出现异常，即将重启";
    private Thread.UncaughtExceptionHandler defalutHandler;


    private CustomCrashHandler() { }

    private static final class Inner{
        private static final CustomCrashHandler HANDLER = new CustomCrashHandler();
    }

    public static CustomCrashHandler getInstance(){
        return Inner.HANDLER;
    }

    public void init(Application context) {
        init(context,"应用出现异常，即将重启");
    }

    public void init(Application context, String message) {
        this.context = context;
        this.message = message;
        defalutHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        boolean res = handleException(throwable);
        if(!res && defalutHandler != null){
            defalutHandler.uncaughtException(thread,throwable);
        }else {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.gc();
            restart();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    public void restart() {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null){
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ex.printStackTrace();
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

        }.start();
        return true;
    }
}

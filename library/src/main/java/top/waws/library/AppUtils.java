package top.waws.library;

import android.app.Application;
import android.content.Context;
import android.os.Looper;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import top.waws.library.activitystack.ActivityStackUtil;
import top.waws.library.app.AppInfo;
import top.waws.library.dialog.DialogUtil;
import top.waws.library.gson.GsonUtils;
import top.waws.library.handler.WeakHandler;
import top.waws.library.inputfilter.InputFilterUtil;
import top.waws.library.log.LogUtil;
import top.waws.library.mmkv.MMKVUtil;
import top.waws.library.net.HttpUtil;
import top.waws.library.net.NetConfig;
import top.waws.library.permission.IPermissionInterceptor;
import top.waws.library.permission.PermissionUtil;
import top.waws.library.statusbar.StatusBarUtil;
import top.waws.library.tabhost.TabHostBuilder;
import top.waws.library.toast.ToastUtil;
import top.waws.library.utils.CacheUtil;
import top.waws.library.utils.ClipBoardUtil;
import top.waws.library.utils.CustomCrashHandler;
import top.waws.library.utils.CustomHtml;
import top.waws.library.utils.DateFormatUtil;
import top.waws.library.utils.FileUtil;
import top.waws.library.utils.KeyBoardUtil;
import top.waws.library.utils.NetUtil;
import top.waws.library.utils.RegexUtil;
import top.waws.library.utils.RomUtil;
import top.waws.library.utils.SaveBitmapUtil;
import top.waws.library.utils.ScreenUtil;
import top.waws.library.utils.TimerUtils;
import top.waws.library.utils.Util;

/**
 *  @desc: 程序工具类
 *  @className: AppUtils
 *  @author: thanatos
 *  @createTime: 2018/10/17
 *  @updateTime: 2018/10/17 下午10:29
 */
public class AppUtils {

    /**
     * 是否是debug模式
     */
    private static volatile boolean sDebug = false;

    /**
     * app全局上下文对象
     */
    private static volatile Application sApplication;

    /**
     * 私有构造方法，防止外部进行对象的创建
     */
    private AppUtils(){}

    /**
     * 工具类全局初始化
     * @param application 全局上下文对象
     */
    public static void init(Application application){
        init(application, false);
    }

    /**
     * 工具类全局初始化
     * @param application 全局上下文对象
     * @param debug 是否是调试模式
     */
    public static void init(@NonNull Application application, boolean debug){
        sApplication = application;
        sDebug = debug;
        //注册堆栈工具类
        getInstance().stackUtil();
    }

    /**
     * 初始化net
     * @param config 网络配置类
     */
    public static void initNet(@NonNull NetConfig config){
        HttpUtil.init(config);
    }


    public static void initPermission(@NonNull IPermissionInterceptor interceptor){
        //注册权限工具类
        PermissionUtil.init(sDebug);
        PermissionUtil.setIntecepter(interceptor);
    }


    /**
     * 静态内部类 用来构造当前类唯一对象
     */
    private static final class Inner{
        private static final AppUtils UTILS = new AppUtils();
    }

    /**
     * 获取全局唯一对象
     * @return {@link AppUtils}
     */
    public static AppUtils getInstance(){
        checkInit();
        return Inner.UTILS;
    }

    /**
     * 获取是否是调试模式
     * @return true: 是  false：不是
     */
    public static boolean isDebug(){
        return sDebug;
    }

    /**
     * 获取全局唯一上下文对象
     * @return {@link Application}
     */
    public Context getContext(){
        return sApplication;
    }

    /**
     * 一般项目首页使用的切换页面工具类
     * @param activity {@link AppCompatActivity}
     * @return {@link TabHostBuilder}
     */
    public TabHostBuilder setupFragmentTabHost(@NonNull AppCompatActivity activity, @IdRes int contentId){
        return new TabHostBuilder(activity,contentId);
    }


    /**
     * 显示toast
     * @param s 文本
     */
    public void showToast(@NonNull CharSequence s){
        ToastUtil.getInstance().show(s);
    }

    /**
     * 获取tost工具类
     * @return {@link ToastUtil}
     */
    public ToastUtil toast(){
        return ToastUtil.getInstance();
    }


    /**
     * 退出程序
     */
    public void exitApp(){
        ActivityStackUtil.getInstance().exit();
    }

    /**
     * 获取堆栈工具类
     * @return
     */
    public ActivityStackUtil stackUtil(){
        return ActivityStackUtil.getInstance();
    }

    /**
     * 添加数据
     * @param key 键
     * @param value 值
     * @param <T> 值类型
     */
    public <T> void putData(@NonNull String key, @NonNull T value){
        MMKVUtil.getInstance().put(key, value);
    }

    /**
     * 获取数据
     * @param key 键
     * @param def 默认值
     * @param <T> 值类型
     * @return
     */
    public <T> T getData(@NonNull String key, T def){
        return MMKVUtil.getInstance().get(key, def);
    }

    /**
     * 移除数据
     * @param key 键
     */
    public void remove(@NonNull String key){
        MMKVUtil.getInstance().remove(key);
    }

    /**
     * 清楚默认的mmkv所有数据
     */
    public void clearData(){
        MMKVUtil.getInstance().clear();
    }

    /**
     * 清楚某些模块的数据
     * @param modules 模块名字数组
     */
    public void clearData(@NonNull String... modules){
        if (modules.length != 0){
            for (String module : modules) {
                MMKVUtil.getInstance().clear(module);
            }
        }
    }

    /**
     * 获取mmkv工具类
     * @return
     */
    public MMKVUtil getMMKV(){
        return MMKVUtil.getInstance();
    }


    /**
     * 打印日志
     * @param object
     */
    public void logd(@NonNull Object object){
        LogUtil.getInstance().d(object);
    }

    /**
     * 打印json
     * @param json
     */
    public void logJson(@NonNull String json){
        LogUtil.getInstance().json(json);
    }

    /**
     * 打印xml
     * @param xml
     */
    public void logXml(@NonNull String xml){
        LogUtil.getInstance().xml(xml);
    }


    /**
     * 获取dialog对象
     * @return
     */
    public DialogUtil dialog(){
        return DialogUtil.getInstance();
    }


    /**
     * 获取GsonUtils对象
     * @return
     */
    public GsonUtils gson(){
        return GsonUtils.getInstance();
    }


    /**
     * 获取程序信息
     * @return
     */
    public AppInfo getAppInfo(){
        return AppInfo.getInstance();
    }

    /**
     * 注册异常处理
     */
    public void registerCrashHandler(){
        registerCrashHandler("应用出现异常，即将重启");
    }

    /**
     * 注册异常处理
     * @param reStartMessage 重启提示信息
     */
    public void registerCrashHandler(String reStartMessage){
        CustomCrashHandler.getInstance().init((Application) getContext(),
                reStartMessage);
    }

    /**
     * 获取一个弱引用Handler
     * @return
     */
    public WeakHandler getWeakHandler(){
        return new WeakHandler(Looper.getMainLooper());
    }

    /**
     * 获取编辑框过滤器工具类
     * @return
     */
    public InputFilterUtil getInputFilterUtil(){
        return InputFilterUtil.getInstance();
    }

    /**
     * 获取缓存工具类
     * @return
     */
    public CacheUtil getCacheUtil(){
        return CacheUtil.getInstance();
    }

    /**
     * 剪切板工具类
     * @return
     */
    public ClipBoardUtil getClipBoardUtil(){
        return ClipBoardUtil.getInstance();
    }

    /**
     * 自定义html
     * @return
     */
    public CustomHtml getHtml(){
        return CustomHtml.getInstance();
    }

    /**
     * 时间工具类
     * @return
     */
    public DateFormatUtil getDateUtil(){
        return DateFormatUtil.getInstance();
    }

    /**
     * 获取文件工具类
     * @return
     */
    public FileUtil getFileUtil(){
        return FileUtil.getInstance();
    }

    /**
     * 获取键盘工具类
     * @return
     */
    public KeyBoardUtil getKeyBoardUtil(){
        return KeyBoardUtil.getInstance();
    }

    /**
     * 是否连接了网络
     * @return
     */
    public boolean isConnectedNet(){
        return NetUtil.getInstance().isConnected();
    }

    /**
     * 是否是WiFi环境
     * @return
     */
    public boolean isWifi(){
        return NetUtil.getInstance().isWifi();
    }

    /**
     * 获取正则工具类
     * @return
     */
    public RegexUtil getRegexUtil(){
        return RegexUtil.getInstance();
    }

    /**
     * 获取rom工具类
     * @return
     */
    public RomUtil getRomUtil(){
        return RomUtil.getInstance();
    }

    /**
     * 保存图片工具类
     * @return
     */
    public SaveBitmapUtil getSaveBitmapUtil(){
        return SaveBitmapUtil.getInstance();
    }

    /**
     * 获取屏幕工具类
     * @return
     */
    public ScreenUtil getScreenUtil(){
        return ScreenUtil.getInstance();
    }


    /**
     * dp转px
     * @param dp
     * @return
     */
    public float dp2px(float dp){
        return ScreenUtil.getInstance().dp2px(dp);
    }

    /**
     * sp转px
     * @param sp
     * @return
     */
    public float sp2px(float sp){
        return ScreenUtil.getInstance().sp2px(sp);
    }


    /**
     * 获取默认的工具类
     * @return
     */
    public Util getDefaultUtil(){
        return new Util();
    }

    /**
     * 获取自定义工具类
     * @param util
     * @param <T>
     * @return
     */
    public <T extends Util> T getUtil(Class<T> util){
        try {
            return util.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取状态栏工具类
     * @return
     */
    public StatusBarUtil getStatusBarUtil(){
        return StatusBarUtil.getInstance();
    }


    /**
     * 获取httpUtil
     * @return
     */
    public HttpUtil getHttpUtil(){
        return HttpUtil.getDefault();
    }

    /**
     * 获取权限申请工具类
     * @return
     */
    public PermissionUtil getPermissionUtil(){
        return PermissionUtil.getInstance();
    }

    /**
     * 倒计时工具类
     * @return
     */
    public TimerUtils getTimer(){
        return TimerUtils.getTimer();
    }

    /**
     * 检查当前工具类是否初始化了
     */
    private static void checkInit(){
        if (sApplication == null){
            throw new NullPointerException("请先调用 AppUtils 的 init 方法");
        }
    }
}

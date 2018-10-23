package top.waws.library.permission;

import android.content.Context;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import top.waws.library.AppUtils;

/**
 * 权限申请封装类
 * Created on 2017/11/2.
 * @author liuxiongfei
 */
@SuppressWarnings("All")
public final class PermissionUtil {

    /**
     * 是否开启了调试模式
     */
    private static boolean DEBUG = false;

    public static final int OK = 100;

    public static final int ERROR = 101;

    private static final int REQUEST_CODE = 0x100;

    /**
     * 权限全局申请拦截器
     */
    private static IPermissionInterceptor mInterceptor;

    private List<String> mPermissions = new ArrayList<>();

    private WeakReference<FragmentActivity> mActivityWRF;

    private boolean mustAgree = false; //是否必须同意

    private PermissionUtil(){
    }

    /**
     * 内部静态类专门获取Permission实类对象
     */
    private static final class Builder{
        private static final PermissionUtil PERMISSION = new PermissionUtil();
    }

    /**
     * 不开启调试模式的初始化
     * @return Permission
     */
    public static PermissionUtil getInstance(){
        return Builder.PERMISSION;
    }

    /**
     * 开启调试模式的初始化
     * @param debug 是否开启调试模式
     * @return Permission
     */
    public static void init(boolean debug) {
        DEBUG = debug;
    }

    /**
     * 设置请求的权限
     * @param premission 权限
     * @return Permission
     */
    public PermissionUtil request(@NonNull List<String> permission) {
        this.mPermissions.clear();
        this.mPermissions.addAll(permission);
        return this;
    }

    /**
     * 设置请求的权限
     * @param premission 权限
     * @return Permission
     */
    public PermissionUtil request(@NonNull String... permission) {
        this.mPermissions.clear();
        if (permission.length > 0){
            for (String s : permission) {
                this.mPermissions.add(s);
            }
        }
        return this;
    }

    /**
     * 构建上下文
     * @param target 上下文对象或者view
     * @return
     */
    public PermissionUtil build(@NonNull Object target){
        if (target instanceof FragmentActivity){
            this.mActivityWRF = new WeakReference<>((FragmentActivity) target);
        }else if (target instanceof Fragment){
            this.mActivityWRF = new WeakReference<>(((Fragment) target).getActivity());
        }else if (target instanceof View){
            Context context = ((View) target).getContext();
            if (context instanceof FragmentActivity){
                this.mActivityWRF = new WeakReference<>((FragmentActivity) target);
            }else {
                throw new IllegalArgumentException("目标上下文不是一个 FragmentActivity 或 Fragment");
            }
        }else {
            throw new IllegalArgumentException("目标上下文不是一个 FragmentActivity 或 Fragment");
        }
        return this;
    }


    /**
     * 是否必须同意权限
     * @return
     */
    public PermissionUtil mustAgree(){
        this.mustAgree = true;
        return this;
    }


    /**
     * 添加权限申请拦截器
     * @param intecepter
     * @return
     */
    public static void  setIntecepter(@NonNull IPermissionInterceptor intecepter){
        mInterceptor = intecepter;
    }


    /**
     * 执行请求权限并获取回调
     * @param activity Activity
     */
    public void execute(@NonNull PermissionCallBack callBack){
        if (this.mActivityWRF == null || this.mActivityWRF.get() == null){
            AppUtils.getInstance().logd("申请权限的目标栈为空");
            return;
        }
        if (callBack == null) {
            AppUtils.getInstance().logd("权限回调申请为空");
            return;
        }
        if (mPermissions.isEmpty()){
            AppUtils.getInstance().logd("权限列表为空");
            return;
        }
        //添加默认的拦截器
        if (mInterceptor == null){
            mInterceptor = new DefaultInterceptor();
        }
        new Call(mPermissions, REQUEST_CODE, this.mActivityWRF.get(), callBack, mInterceptor, mustAgree);
    }


}

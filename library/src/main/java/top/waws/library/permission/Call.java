package top.waws.library.permission;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created on 2017/11/2.
 * @author liuxiongfei.
 * Desc 权限请求核心类
 */
 final class Call {

    private List<String> mPermissions;
    private int mRequestCode;
    private WeakReference<FragmentActivity> mActivityWrf;
    private PermissionCallBack callBack;
    private boolean mustAgree;

    Call(@NonNull List<String> permissions, int requestCode, @NonNull FragmentActivity activity,
         @NonNull PermissionCallBack callBack, @NonNull IPermissionInterceptor interceptor, boolean mustAgree){
        this.mPermissions = permissions;
        this.mRequestCode = requestCode;
        this.mActivityWrf = new WeakReference<>(activity);
        this.callBack = callBack;
        this.mustAgree = mustAgree;
        build(interceptor);
    }

    private void build(@NonNull IPermissionInterceptor interceptor){

        if (this.mPermissions == null || this.mPermissions.isEmpty()){
            throw new IllegalArgumentException("premissions isEmpty");
        }
        if (this.callBack == null){
            throw new IllegalArgumentException("callBack is null");
        }
        PermissionFragment fragment = new PermissionFragment();
        fragment.setRequestCode(this.mRequestCode);
        fragment.setPermissions(this.mPermissions);
        fragment.setCallBack(this.callBack);
        fragment.setInterceptor(interceptor);
        fragment.setMustAgree(this.mustAgree);
        FragmentManager manager = this.mActivityWrf.get().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(fragment,"PermissionFragment").commit();
    }

}

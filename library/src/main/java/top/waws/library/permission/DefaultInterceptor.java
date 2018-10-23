package top.waws.library.permission;

import java.util.List;

import top.waws.library.AppUtils;

/**
 *  功能描述: 权限拦截器
 *  @className: DefaultIntecepter
 *  @author: thanatos
 *  @createTime: 2018/6/12
 *  @updateTime: 2018/6/12 14:48
 */
public class DefaultInterceptor implements IPermissionInterceptor{

    @Override
    public boolean start(PermissionRequest permissionRequest) {
        return true;
    }

    @Override
    public void request(PermissionRequest permissionRequest) {
        permissionRequest.request();
    }

    @Override
    public void hasRequested(PermissionRequest permissionRequest) {
        permissionRequest.request();
    }

    @Override
    public void noAskComplete(PermissionRequest permissionRequest,List<String> dangers) {
        permissionRequest.startSetting();
    }

    @Override
    public void complete(int code, List<String> dangers) {
        AppUtils.getInstance().logd(String.valueOf(code));
    }
}

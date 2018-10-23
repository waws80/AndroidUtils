package top.waws.library.permission;

import android.content.Context;

import java.util.List;

/**
 *  功能描述: 权限申请 拦截器请求类
 *  @className: PermissionRequest
 *  @author: thanatos
 *  @createTime: 2018/6/12
 *  @updateTime: 2018/6/12 13:45
 */
public interface PermissionRequest {

    void request();

    List<String> getPermissions();

    Context getContext();

    void startSetting();
}

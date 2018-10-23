package top.waws.library.activitystack;

/**
 *  @desc: App状态监听
 *  @className: AppStatusListener
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 下午2:13
 */
public interface AppStatusListener {

    /**
     * 是否在后台运行
     * @param isBackground true: 在后台运行， false：在前台运行
     */
    void status(boolean isBackground);
}

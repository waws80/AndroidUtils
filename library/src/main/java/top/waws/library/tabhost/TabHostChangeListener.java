package top.waws.library.tabhost;

import android.view.View;

import androidx.annotation.NonNull;

/**
 *  @desc: TabHost点击事件回调
 *  @className: TabHostChangeListener
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 上午9:27
 */
public interface TabHostChangeListener {

    /**
     * 选中的item
     * @param item View
     * @param position 选中的下标
     */
    void selected(@NonNull View item, int position, @NonNull TabItem bean);

    /**
     * 没有选中的item
     * @param item View
     */
    void unSelected(@NonNull View item, @NonNull TabItem bean);

}

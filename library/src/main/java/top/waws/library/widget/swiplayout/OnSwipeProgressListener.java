package top.waws.library.widget.swiplayout;

import androidx.annotation.FloatRange;

/**
 *  @className: OnSwipeProgressListener
 *  @author: thanatos
 *  @des 侧滑布局滑动监听
 */
public interface OnSwipeProgressListener {

    /**
     * 滑动进度
     * @param progress 进度
     */
    void onProgressChange(@FloatRange(from = 0f, to = 100f) float progress);
}

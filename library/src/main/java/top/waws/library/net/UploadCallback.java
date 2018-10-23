package top.waws.library.net;


import androidx.annotation.IntRange;
import io.reactivex.disposables.Disposable;

/**
 * @desc:
 * @className: DownloadCallback
 * @author: thanatos
 */
public interface UploadCallback<T> {

    /**
     * 取消订阅
     * @param d
     */
    void onSubscribe(Disposable d);

    /**
     * 完成
     */
    void onComplete();

    /**
     * 实时进度
     * @param progress 0 -100
     */
    void progress(@IntRange(from = 0, to = 100) int progress);

    /**
     * 上传完成
     * @param value
     */
    void onNext(T value);

    /**
     * 下载出错
     * @param e
     */
    void onError(Throwable e);
}

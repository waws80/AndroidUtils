package top.waws.library.utils;

import android.os.CountDownTimer;


/**
 *  倒计时工具类
 *  @className: CountDownTimerUtils
 *  @author: carlos
 *  @createTime: 17-12-1
 *  @updateTime: 17-12-1 下午7:33
 */
public class TimerUtils {
    /**
     * 倒计时结束的回调接口
     */
    public interface FinishDelegate {
        void onFinish();
    }
    /**
     * 定期回调的接口
     */
    public interface TickDelegate {
        void onTick(long pMillisUntilFinished);
    }
    private final static long ONE_SECOND = 1000L;
    /**
     * 总倒计时时间
     */
    private long mTotalMillis = 0;
    /**
     * 定期回调的时间 必须大于0 否则会出现ANR
     */
    private long mCountDownInterval;
    /**
     * 倒计时结束的回调
     */
    private FinishDelegate mFinishDelegate;
    /**
     * 定期回调
     */
    private TickDelegate mTickDelegate;
    private MyCountDownTimer mCountDownTimer;
    /**
     * 获取 CountDownTimerUtils
     * @return CountDownTimerUtils
     */
    public static TimerUtils getTimer() {
        return new TimerUtils();
    }
    /**
     * 设置定期回调的时间 调用{@link #setTickDelegate(TickDelegate)}
     * @param interval 定期回调的时间 必须大于0
     * @return CountDownTimerUtils
     */
    public TimerUtils setInterval(long interval) {
        this.mCountDownInterval=interval;
        return this;
    }
    /**
     * 设置倒计时结束的回调
     * @param pFinishDelegate 倒计时结束的回调接口
     * @return CountDownTimerUtils
     */
    public TimerUtils setFinishDelegate(FinishDelegate pFinishDelegate) {
        this.mFinishDelegate=pFinishDelegate;
        return this;
    }
    /**
     * 设置总倒计时时间
     * @param totalMillis 总倒计时时间
     * @return CountDownTimerUtils
     */
    public TimerUtils setTotalMillis(long totalMillis) {
        this.mTotalMillis=totalMillis;
        return this;
    }
    /**
     * 设置定期回调
     * @param pTickDelegate 定期回调接口
     * @return CountDownTimerUtils
     */
    public TimerUtils setTickDelegate(TickDelegate pTickDelegate) {
        this.mTickDelegate=pTickDelegate;
        return this;
    }
    public void create() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (mCountDownInterval <= 0) {
            mCountDownInterval = mTotalMillis + ONE_SECOND;
        }
        mCountDownTimer = new MyCountDownTimer(mTotalMillis, mCountDownInterval);
        mCountDownTimer.setTickDelegate(mTickDelegate);
        mCountDownTimer.setFinishDelegate(mFinishDelegate);
    }
    /**
     * 开始倒计时
     */
    public void start() {
        if (mCountDownTimer == null) {
            create();
        }
        mCountDownTimer.start();
    }
    /**
     * 取消倒计时
     */
    public void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    private static class MyCountDownTimer extends CountDownTimer {
        private FinishDelegate mFinishDelegate;
        private TickDelegate mTickDelegate;
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            if (mTickDelegate != null) {
                mTickDelegate.onTick(millisUntilFinished);
            }
        }
        @Override
        public void onFinish() {
            if (mFinishDelegate != null) {
                mFinishDelegate.onFinish();
            }
        }
        void setFinishDelegate(FinishDelegate pFinishDelegate) {
            this.mFinishDelegate=pFinishDelegate;
        }
        void setTickDelegate(TickDelegate pTickDelegate) {
            this.mTickDelegate=pTickDelegate;
        }
    }
}

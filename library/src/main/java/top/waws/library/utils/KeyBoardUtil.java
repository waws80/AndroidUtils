package top.waws.library.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import top.waws.library.AppUtils;


/**
 *  @desc: 键盘工具类
 *  @className: KeyboardUtils
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午4:19
 */
public final class KeyBoardUtil {


    private static InputMethodManager sInputMethodManager;

    private KeyBoardUtil(){
        sInputMethodManager = (InputMethodManager) AppUtils
                .getInstance().getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private static final class Inner{
        private static KeyBoardUtil UTIL = new KeyBoardUtil();
    }

    public static KeyBoardUtil getInstance(){
        return Inner.UTIL;
    }

    /**
     * 隐藏软键盘,上下文
     * @param view
     */
    public void hideSoftKeyboard(View view) {
        if (sInputMethodManager == null) return;
            sInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
    }

    /**
     * 隐藏虚拟键盘
     * @param v
     */
    public void hideKeyboard(View v) {
        if (sInputMethodManager == null) return;
        sInputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    /**
     * 显示虚拟键盘
     * @param v
     */
    public void showKeyboard(View v) {
        if (sInputMethodManager == null) return;
        sInputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);//表示强制显示
    }

    /**
     * 强制显示或者关闭系统键盘
     * @param txtSearchKey
     * @param show
     */
    public void keyBoard(final EditText txtSearchKey, final boolean show) {

        AppUtils.getInstance().getWeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    sInputMethodManager.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED);
                } else {
                    sInputMethodManager.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), 0);
                }
            }
        },100L);
    }


    /**
     * 延迟隐藏虚拟键盘
     * @param v
     * @param delay
     */
    public void hideKeyboardDelay(final View v, long delay) {
        AppUtils.getInstance().getWeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sInputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
        },delay);
    }

    /**
     * 输入法是否显示着
     * @return
     */
    public boolean isKeyBoardShow() {
        return sInputMethodManager.isActive();
    }
}

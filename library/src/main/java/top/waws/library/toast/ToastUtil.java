package top.waws.library.toast;

import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import top.waws.library.AppUtils;

/**
 *  @desc: {@link Toast} 工具类
 *  @className: ToastUtil
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 上午10:43
 */
public class ToastUtil {

    /**
     * 借助{@link AppUtils } 生成 toast
     */
    private static Toast sToast = null;
    private static Toast sCustomToast = new Toast(AppUtils.getInstance().getContext());

    private ToastUtil(){}

    private static final class Inner{
        private static final ToastUtil UTIL = new ToastUtil();
    }

    public static ToastUtil getInstance(){
        return Inner.UTIL;
    }

    /**
     * 显示普通toast
     * @param s 文本
     */
    public void show(@NonNull CharSequence s){
        show(s,Toast.LENGTH_SHORT);
    }

    /**
     * 显示普通toast
     * @param s 文本
     * @param duration 时长
     */
    public void show(@NonNull CharSequence s, int duration){

        if (sToast == null){
            sToast = Toast.makeText(AppUtils.getInstance().getContext(),s,duration);
        }else {
            sToast.setText(s);
        }
        sToast.show();
    }

    /**
     * 显示居中toast
     * @param s 文本
     */
    public void center(@NonNull CharSequence s){
        center(s,Toast.LENGTH_SHORT);
    }

    /**
     * 显示居中toast
     * @param s 文本
     * @param duration 时长
     */
    public void center(@NonNull CharSequence s, int duration){
        if (sToast == null){
            sToast = Toast.makeText(AppUtils.getInstance().getContext(),s,duration);
            sToast.setGravity(Gravity.CENTER,0,0);
        }else {
            sToast.setGravity(Gravity.CENTER,0,0);
            sToast.setText(s);
        }
        sToast.show();
    }

    /**
     * 显示自定义toast
     * @param view 自定义布局
     */
    public void custom(@NonNull View view){
        custom(view,Toast.LENGTH_SHORT);
    }

    /**
     * 显示自定义toast
     * @param view 自定义布局
     * @param duration 时长
     */
    public void custom(@NonNull View view, int duration){
        custom(view,duration,Gravity.BOTTOM);
    }

    /**
     * 显示自定义toast
     * @param view 自定义布局
     * @param duration 时长
     * @param gravity 位置
     */
    public void custom(@NonNull View view, int duration, int gravity){
        if (sCustomToast != null){
            sCustomToast.setDuration(duration);
            sCustomToast.setGravity(gravity,0,0);
            sCustomToast.setView(view);
            sCustomToast.show();
        }
    }

}

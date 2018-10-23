package top.waws.library.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import top.waws.library.R;
import top.waws.library.viewutil.ViewUtil;

/**
 *  @desc: 弹窗基类
 *  @className: BaseDialog
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 下午4:44
 */
public class BaseDialog extends DialogFragment {

    private @LayoutRes int layoutId;

    private @DialogUtil.AnimType int animType = DialogUtil.ANIM_NORMAL;

    private float outSideAlpha = 0.3f;//默认 .3f   1.0f 完全不透明  0f 完全透明

    private Drawable windowDraw = new ColorDrawable(Color.TRANSPARENT);

    private DialogUtil.InitViewCall mViewCall;

    private ViewUtil mViewUtil;

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public void setAnim(@DialogUtil.AnimType int animType) {
        this.animType = animType;
    }

    /**
     * 设置 弹窗背景透明度 默认 .3f   1.0f 完全不透明  0f 完全透明
     * @param dialogOutSideAlpha
     */
    public void setOutSideAlpha(float dialogOutSideAlpha) {
        this.outSideAlpha = dialogOutSideAlpha;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BaseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        View root = View.inflate(dialog.getContext(),layoutId,null);
        dialog.setContentView(root);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        if (animType == DialogUtil.ANIM_BOTTOM){
            window.setWindowAnimations(R.style.AnimBottom);
        }
        window.setBackgroundDrawable(windowDraw);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = outSideAlpha;
        window.setAttributes(lp);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mViewUtil = ViewUtil.initViewUtil(root);
        if (mViewCall != null){
            mViewCall.next(dialog,mViewUtil);
        }
        return dialog;
    }

    void setInitViewCall(DialogUtil.InitViewCall mCall) {
        this.mViewCall = mCall;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }
}

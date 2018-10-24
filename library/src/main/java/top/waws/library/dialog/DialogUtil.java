package top.waws.library.dialog;

import android.app.Dialog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import top.waws.library.viewutil.ViewUtil;

/**
 *  @desc:
 *  @className: DialogUtil
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 下午6:14
 */
public class DialogUtil {

    private @LayoutRes int mLayout;

    private InitViewCall mCall;

    private @AnimType int mAnimType = ANIM_NORMAL;

    //无动画
    public static final int ANIM_NORMAL = 0;
    //从底部弹出
    public static final int ANIM_BOTTOM = 1;
    //弹窗外部透明度
    private float mDialogOutSideAlpha = 0.3f;


    @IntDef({ANIM_NORMAL,ANIM_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER,ElementType.FIELD})
    public @interface AnimType{}

    public static DialogUtil getInstance(){
        return new DialogUtil();
    }

    public DialogUtil setLayout(@LayoutRes int layout){
        this.mLayout = layout;
        return this;
    }

    public DialogUtil setAnim(@AnimType int animType){
        this.mAnimType = animType;
        return this;
    }

    public DialogUtil setDialogOutSideAlpha(float dialogOutSideAlpha) {
        this.mDialogOutSideAlpha = dialogOutSideAlpha;
        return this;
    }

    public DialogUtil initView(InitViewCall viewCall){
        this.mCall = viewCall;
        return this;
    }

    public BaseDialog create(){
        BaseDialog dialog = new BaseDialog();
        dialog.setLayoutId(mLayout);
        dialog.setAnim(mAnimType);
        dialog.setInitViewCall(mCall);
        dialog.setOutSideAlpha(mDialogOutSideAlpha);
        return dialog;
    }


    /**
     * 初始化控件
     */
    public interface InitViewCall{

        /**
         * 初始化控件回调
         * @param dialog 弹窗
         * @param viewUtil 初始化控件所需工具类
         */
        void next(@NonNull Dialog dialog, @NonNull ViewUtil viewUtil);
    }
}

package top.waws.library.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import top.waws.library.R;

/**
 *  @desc: 懒加载Fragment
 *  @className: LazyFragment
 *  @author: thanatos
 *  @createTime: 2018/10/20
 *  @updateTime: 2018/10/20 下午7:49
 */
public abstract class LazyFragment extends Fragment {

    private boolean hasCreate = false;

    private boolean noLoad = true;

    private Bundle mBundle;

    private FrameLayout contentView;

    private ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        checkCanInit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        hasCreate = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null){
            contentView = new FrameLayout(inflater.getContext());
            contentView.setLayoutParams(params);
        }
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBundle = savedInstanceState;
        checkCanInit();
    }

    private void  checkCanInit(){
        if (hasCreate && getUserVisibleHint() && noLoad){
            noLoad = false;
            hasCreate = false;
            onInit(mBundle);
        }
    }

    /**
     * 使用此方法初始化数据
     */
    protected abstract void onInit(Bundle savedInstanceState);


    /**
     * 设置布局
     * @param layout
     */
    protected void setContentView(@LayoutRes int layout){
        contentView.addView(View.inflate(getContext(),layout,null),params);
    }

    protected <T extends View> T findViewById(@IdRes int id){
        return contentView.findViewById(id);
    }
}

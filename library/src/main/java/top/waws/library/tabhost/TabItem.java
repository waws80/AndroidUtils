package top.waws.library.tabhost;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 *  @desc: {@link androidx.fragment.app.FragmentTabHost} item bean
 *  @className: TabItem
 *  @author: thanatos
 *  @createTime: 2018/10/17
 *  @updateTime: 2018/10/17 下午11:49
 */
public class TabItem {

    /**
     * 标题
     */
    private CharSequence title;

    /**
     * 图标资源
     */
    private @DrawableRes int drawableRes;

    /**
     * 目标fragment
     */
    private Class<? extends Fragment> fragmentClass;

    /**
     * 向目标传的参数
     */
    private Bundle bundle;

    private View tab;


    /**
     * {@link TabItem} 构造函数
     * @param title 标题
     * @param drawableRes 图标
     * @param fragmentClass fragment容器
     * @param bundle 参数bundle
     * @param tab itemView
     */
    public TabItem(@NonNull CharSequence title, @NonNull int drawableRes,
                   @NonNull Class<? extends Fragment> fragmentClass, @NonNull Bundle bundle, @NonNull View tab) {
        this.title = title;
        this.drawableRes = drawableRes;
        this.fragmentClass = fragmentClass;
        this.bundle = bundle;
        this.tab = tab;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public View getTab() {
        return tab;
    }

    public void setTab(View tab) {
        this.tab = tab;
    }
}

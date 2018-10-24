package top.waws.library.tabhost;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;
import top.waws.library.R;

/**
 *  @desc: {@link FragmentTabHost} 工具类
 *  @className: TabHostBuilder
 *  @author: thanatos
 *  @createTime: 2018/10/17
 *  @updateTime: 2018/10/17 下午11:07
 */
public class TabHostBuilder {

    //活动页面
    private WeakReference<AppCompatActivity> mActivityWrf;
    //控件
    private WeakReference<FragmentTabHost> mTabHostWrf;
    //tabs
    private SparseArray<TabItem> mTabs = new SparseArray<>();

    @IdRes
    private int mContentId;

    public TabHostBuilder(@NonNull AppCompatActivity activity, @IdRes int contentId) {
        mActivityWrf = new WeakReference<>(activity);
        mTabHostWrf = new WeakReference<>(mActivityWrf.get().findViewById(android.R.id.tabhost));
        mContentId = contentId;
        setup();
    }

    private void setup(){
        if (isOk()){
            mTabHostWrf.get().setup(mActivityWrf.get(),
                    mActivityWrf.get().getSupportFragmentManager(),
                    mContentId);
        }
    }

    /**
     * 添加tab
     * @param items tab list
     * @return
     */
    public TabHostBuilder addTabs(@NonNull List<TabItem> items){
        if (isOk()){
            mTabs.clear();
            for (int i = 0; i < items.size(); i++) {
                TabItem item = items.get(i);
                mTabHostWrf.get().addTab(mTabHostWrf.get().newTabSpec(item.getTitle().toString())
                                .setIndicator(item.getTab()),
                        item.getFragmentClass(),item.getBundle());
                mTabs.put(i,item);
            }
        }
        return this;
    }

    /**
     * 添加change事件
     * @param listener {@link TabHostChangeListener}
     * @return
     */
    public TabHostBuilder addChangeListener(@NonNull final TabHostChangeListener listener){
        if (isOk()){
            mTabHostWrf.get().setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    updateTab(listener);
                }
            });
        }
        return this;
    }

    /**
     * 添加某个下标item点击选中前点击事件
     * @param index
     * @param listener
     * @return
     */
    public TabHostBuilder addBeforeClickListener(int index, View.OnClickListener listener){
        if (isOk()){
            mTabHostWrf.get().getTabWidget()
                    .getChildTabViewAt(index)
                    .setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 给某个item添加重复点击事件
     * @param index 下标
     * @param listener 点击事件
     * @return
     */
    public TabHostBuilder addReSelectedListener(final int index, final View.OnClickListener listener){
        if (isOk()){
            mTabHostWrf.get().getTabWidget()
                    .getChildTabViewAt(index)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mTabHostWrf.get().getCurrentTab() == index){
                                listener.onClick(v);
                            }else {
                                mTabHostWrf.get().setCurrentTab(index);
                            }
                        }
                    });
        }
        return this;
    }

    /**
     * 更新item
     * @param listener {@link TabHostChangeListener}
     */
    private void updateTab(@NonNull TabHostChangeListener listener) {
        TabWidget widget = mTabHostWrf.get().getTabWidget();

        for (int i = 0; i < widget.getTabCount(); i++) {
            View item = widget.getChildTabViewAt(i);
            if (mTabHostWrf.get().getCurrentTab() == i){
                listener.selected(item, i, mTabs.get(i));

            }else {
                listener.unSelected(item, mTabs.get(i));
            }
        }
    }

    /**
     * 设置当前默认选中页
     */
    public TabHostBuilder setCurrent(int index){
        if (isOk()){
            mTabHostWrf.get().setCurrentTab(index);
        }
        return this;
    }



    private boolean isOk(){
        return mTabHostWrf != null && mTabHostWrf.get() != null
                && mActivityWrf != null && mActivityWrf.get() != null;
    }


    /**
     * 销毁
     */
    public void onDestroy(){
        if (mTabHostWrf != null){
            mTabHostWrf.clear();
            mTabHostWrf = null;
        }
        if (mActivityWrf != null){
            mActivityWrf.clear();
            mActivityWrf = null;
        }
        if (mTabs != null){
            mTabs.clear();
            mTabs = null;
        }
    }
}

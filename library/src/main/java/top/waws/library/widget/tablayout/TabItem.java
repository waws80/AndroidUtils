package top.waws.library.widget.tablayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.TintTypedArray;

/**
 *  功能描述: 复制自系统的{@link com.google.android.material.tabs.TabItem}
 *  主要提供修改后的{@link TabLayout}使用
 *  @className: TabItem
 *  @author: thanatos
 *  @createTime: 2018/1/17
 *  @updateTime: 2018/1/17 下午5:49
 */
@SuppressWarnings("RestrictedApi")
public final class TabItem extends View {
    final CharSequence mText;
    final Drawable mIcon;
    final int mCustomLayout;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                com.google.android.material.R.styleable.TabItem);
        mText = a.getText(com.google.android.material.R.styleable.TabItem_android_text);
        mIcon = a.getDrawable(com.google.android.material.R.styleable.TabItem_android_icon);
        mCustomLayout = a.getResourceId(com.google.android.material.R.styleable.TabItem_android_layout, 0);
        a.recycle();
    }
}

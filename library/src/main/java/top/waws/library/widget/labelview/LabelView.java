package top.waws.library.widget.labelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.transition.Slide;
import top.waws.library.R;

/**
 *  @desc:  标签布局
 *  @className: LabelView
 *  @author: thanatos
 *  @createTime: 2018/10/22
 *  @updateTime: 2018/10/22 下午2:22
 */
public class LabelView extends ViewGroup {

    private int mGravity = Gravity.LEFT;

    private List<View> lineViews = new ArrayList<>(); //存放每行的view


    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelView);

        if (typedArray.hasValue(R.styleable.LabelView_gravity)){
            int gravity = typedArray.getInt(R.styleable.LabelView_gravity,0);
            if (gravity == 0){
                mGravity = Gravity.LEFT;
            }else if (gravity == 1){
                mGravity = Gravity.RIGHT;
            }else if (gravity == 2){
                mGravity = Gravity.CENTER;
            }else {
                mGravity = Gravity.LEFT;
            }
        }
        typedArray.recycle();
    }

    public void setGravity(@Slide.GravityFlag int gravity){
        mGravity = gravity;
    }

    public void setLabels(@NonNull List<View> labels){
        for (View label : labels) {
            addView(label);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();     //获取viewGroup宽度
        int lineHeight = 0;
        int lineWidth = 0;

        /**
         * 设置子View的位置
         */
        int top = getPaddingTop();
        lineViews.clear();
        /**
         * 获取每行的高度和view
         */
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            LabelViewParams lp = (LabelViewParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            int childMaxWidth = lp.leftMargin + lp.rightMargin + childWidth;
            int childMaxHeight = childHeight + lp.bottomMargin + lp.topMargin;

            //如果需要换行
            if (lineWidth + childMaxWidth > width ) {
                layoutChild(top, width, lineWidth);
                //重置行高行宽
                lineWidth = 0;
                top += childMaxHeight;
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.bottomMargin + lp.topMargin);
            lineViews.add(child);
        }
        //处理最后一行
        layoutChild(top,width, lineWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content时需要计算高度
        int width = 0;
        int height = 0;

        //记录每一行的宽度和高度
        int lineHeight = 0;
        int lineWidth = 0;

        /**
         * 计算宽高, width, height
         */
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //子view的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            int childHeight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                //对比得到最大的宽度
                width = Math.max(width, lineWidth);
                //重置行宽
                lineWidth = childWidth;
                //记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {    //未换行情况
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            //当到最后一个控件时，要高度要加上，宽度要是行宽
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        //当mode为wrap_content时，用计算的宽高;为match_parent时，用父类的宽高
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingRight() + getPaddingLeft(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingBottom() + getPaddingTop());

    }

    /**
     * 测量子view
     * @param top 当前view 距布局顶部的距离
     * @param width 当前布局的可用宽度
     * @param lineWidth 当前行的宽度
     */
    private void layoutChild(int top, int width, int lineWidth){
        //布局子view
        int left = getPaddingLeft();
        for (int j = 0; j < lineViews.size(); j++ ) {
            if (lineViews.get(j).getVisibility() == GONE){
                continue;
            }
            LabelViewParams params = ((LabelViewParams)lineViews.get(j).getLayoutParams());
            int lc;
            int offset = (width - lineWidth);
            //设置控件未知
            if (mGravity == Gravity.CENTER){
                lc = offset/2 + left + params.leftMargin/2;
            }else if (mGravity == Gravity.RIGHT){
                lc = offset + left + params.leftMargin;
            }else{
                lc = left + params.leftMargin;
            }
            int tc = top + params.topMargin;
            int rc = lc + lineViews.get(j).getMeasuredWidth() + params.rightMargin;
            int rb = tc + lineViews.get(j).getMeasuredHeight() + params.bottomMargin;

            lineViews.get(j).layout(lc,tc,rc,rb);
            left += lineViews.get(j).getMeasuredWidth() + params.leftMargin + params.rightMargin;
        }
        lineViews.clear();
    }


    /**
     * 当前viewGroup使用的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return  new LabelViewParams(getContext(),attrs);
    }

    public static class LabelViewParams extends MarginLayoutParams{

        public LabelViewParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LabelViewParams(int width, int height) {
            super(width, height);
        }

        public LabelViewParams(MarginLayoutParams source) {
            super(source);
        }

        public LabelViewParams(LayoutParams source) {
            super(source);
        }
    }

    /**
     * dp转px
     * @param dp
     * @return px
     */
    private int dp2px(float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,
                getResources().getDisplayMetrics());
    }
}

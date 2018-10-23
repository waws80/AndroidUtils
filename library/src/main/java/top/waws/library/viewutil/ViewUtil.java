package top.waws.library.viewutil;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 *  @desc: View工具类
 *  @className: ViewUtil
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 下午5:36
 */
public class ViewUtil {

    private static final String ROOT = "root";

    private static long lastClickTime = 0;

    private final SparseArray<View> viewArray = new SparseArray<>();

    private final Map<String,WeakReference<Object>> rootViewMap = new HashMap<>();


    private ViewUtil(Object object){
        if (object instanceof Activity){
            rootViewMap.put(ROOT,new WeakReference<>(object));
        }else if (object instanceof Fragment){
            rootViewMap.put(ROOT,new WeakReference<>(((Fragment) object).getView()));
        }else if (object instanceof android.app.Fragment){
            rootViewMap.put(ROOT,new WeakReference<>(((android.app.Fragment) object).getView()));
        }else if (object instanceof View){
            rootViewMap.put(ROOT,new WeakReference<>(object));
        }else if (object instanceof Dialog){
            rootViewMap.put(ROOT,new WeakReference<>(object));
        }else if (object instanceof RecyclerView.ViewHolder){
            rootViewMap.put(ROOT,new WeakReference<>(((RecyclerView.ViewHolder) object).itemView));
        }else {
            throw new IllegalArgumentException("参数错误 只能是 activity、fragment、view");
        }
    }

    /**
     * 初始化跟布局
     * @param object 跟布局view
     */
    public static ViewUtil initViewUtil(Object object){
        return new ViewUtil(object);
    }

    /**
     * 获取view
     * @param id
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public  <T extends View> T getView(@IdRes int id){
        WeakReference<Object> rootWRF = rootViewMap.get(ROOT);
        if (rootWRF == null || rootWRF.get() == null){
            throw new IllegalArgumentException("请先初始化 init方法");
        }
        View view = viewArray.get(id);
        if (view == null){
            Object obj = rootWRF.get();
            if (obj instanceof Activity){
                view = ((Activity) obj).findViewById(id);
            }else if (obj instanceof View){
                view = ((View) obj).findViewById(id);
            }else if (obj instanceof Dialog){
                view = ((Dialog) obj).findViewById(id);
            }
            viewArray.put(id,view);
        }
        return (T) view;
    }

    /**
     * 设置监听事件
     * @param id
     * @param listener
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public  <T extends View> T setOnClickListener(@IdRes int id, View.OnClickListener listener){
        View view = getView(id);
        view.setOnClickListener(listener);
        return (T) view;
    }

    /**
     * 设置不可重复点击的监听事件
     * @param id
     * @param delay
     * @param listener
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T setNoDoubleOnClickListener(@IdRes int id, long delay, View.OnClickListener listener){
        View view = getView(id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > delay) {
                    lastClickTime = currentTime;
                    listener.onClick(v);
                }
            }
        });
        return (T) view;
    }

    /**
     * 设置控件是否可见
     * @param id
     * @param visibility
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T setVisibility(@IdRes int id, int visibility){
        View view = getView(id);
        view.setVisibility(visibility);
        return (T) view;
    }

    /**
     * 设置文本
     * @param id
     * @param s
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends TextView> T setText(@IdRes int id, CharSequence s){
        TextView view = getView(id);
        view.setText(s);
        return (T) view;
    }

    /**
     * 设置是否可点击
     * @param id
     * @param enable
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T setEnable(@IdRes int id, boolean enable){
        View view = getView(id);
        view.setEnabled(enable);
        return (T) view;
    }

    /**
     * 设置背景资源
     * @param id
     * @param res
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends ImageView> T setImageResource(@IdRes int id, @DrawableRes int res){
        ImageView view = getView(id);
        view.setImageResource(res);
        return (T) view;
    }

    /**
     * 设置bitmap
     * @param id
     * @param bitmap
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends ImageView> T setImageBitmap(@IdRes int id, Bitmap bitmap){
        ImageView view = getView(id);
        view.setImageBitmap(bitmap);
        return (T) view;
    }

    /**
     * 设置背景资源
     * @param id
     * @param res
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T setBackgroundResource(@IdRes int id, @DrawableRes int res){
        View view = getView(id);
        view.setBackgroundResource(res);
        return (T) view;
    }

    /**
     * 设置背景资源
     * @param id
     * @param res
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T setBackgroundResource(@IdRes int id, Drawable res){
        View view = getView(id);
        res.setBounds(0,0,res.getIntrinsicWidth(), res.getIntrinsicHeight());
        view.setBackground(res);
        return (T) view;
    }

    /**
     * 设置背景颜色
     * @param id
     * @param color
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T setBackgroundColor(@IdRes int id, @ColorInt int color){
        View view = getView(id);
        view.setBackgroundColor(color);
        return (T) view;
    }

}

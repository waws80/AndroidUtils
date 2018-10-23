package top.waws.library.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import top.waws.library.AppUtils;


/**
 *  @desc: 剪切复制工具类
 *  @className: ClipboardUtils
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午3:48
 */
public final class ClipBoardUtil {

    private static ClipboardManager mClipboardManager;
    private static ClipboardManager mNewCliboardManager;

    private static final class Inner{
        private static final ClipBoardUtil UTILS = new ClipBoardUtil();
    }

    private ClipBoardUtil(){}

    public static ClipBoardUtil getInstance(){
        return Inner.UTILS;
    }

    private static void instance(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mNewCliboardManager == null)
                mNewCliboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        } else {
            if (mClipboardManager == null)
                mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        }
    }

    /**
     * 为剪切板设置内容
     * @param text
     */
    public void copyToClipBoard(CharSequence text) {
        Context context = AppUtils.getInstance().getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            instance(context);
            // Creates a new text clip to put on the clipboard
            ClipData clip = ClipData.newPlainText("simple text", text);//label为simple text
            // Set the clipboard's primary clip.
            mNewCliboardManager.setPrimaryClip(clip);
        } else {
            instance(context);
            mClipboardManager.setText(text);
        }
    }

    /**
     * 获取剪切板的内容
     * @return
     */
    public CharSequence getText() {
        Context context = AppUtils.getInstance().getContext();
        StringBuilder sb = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            instance(context);
            if (!mNewCliboardManager.hasPrimaryClip()) {
                return sb.toString();
            } else {
                ClipData clipData = (mNewCliboardManager).getPrimaryClip();
                int count = clipData.getItemCount();
                for (int i = 0; i < count; ++i) {
                    ClipData.Item item = clipData.getItemAt(i);
                    CharSequence str = item.coerceToText(context);
                    sb.append(str);
                }
            }
        } else {
            instance(context);
            sb.append(mClipboardManager.getText());
        }
        return sb.toString();
    }
}

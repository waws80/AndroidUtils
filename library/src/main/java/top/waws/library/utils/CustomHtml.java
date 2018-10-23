package top.waws.library.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 *  功能描述: Html 转 Spannable
 *  @className: CustomHtml
 *  @author: thanatos
 *  @createTime: 2018/2/23
 *  @updateTime: 2018/2/23 11:04
 */
public final class CustomHtml {

    private CustomHtml(){}

    private static final class Inner{
        private static final CustomHtml HTML = new CustomHtml();
    }

    public static CustomHtml getInstance(){
        return Inner.HTML;
    }

    public Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return Html.fromHtml(html,FROM_HTML_MODE_LEGACY,null,new CustomTagHandler());
        }else {
            return Html.fromHtml(html,null,new CustomTagHandler());
        }
    }

    public Spanned fromHtml(String html, Html.ImageGetter getter){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return Html.fromHtml(html,FROM_HTML_MODE_LEGACY,getter,new CustomTagHandler());
        }else {
            return Html.fromHtml(html,getter,new CustomTagHandler());
        }
    }
}

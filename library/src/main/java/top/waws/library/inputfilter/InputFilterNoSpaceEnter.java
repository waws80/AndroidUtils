package top.waws.library.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 *  @desc: 不能输入空格的过滤器
 *  @className: InputFilterNoSpace
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午5:19
 */
public class InputFilterNoSpaceEnter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.equals(" ") || source.equals("\n")){
            return "";
        }
        return source;
    }
}

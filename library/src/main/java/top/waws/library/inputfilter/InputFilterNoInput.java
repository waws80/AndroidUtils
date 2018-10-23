package top.waws.library.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 *  @desc: 禁止输入过滤器
 *  @className: InputFilterNoInput
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午5:22
 */
public class InputFilterNoInput implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return dest.subSequence(dstart,dend);
    }
}

package top.waws.library.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  功能描述: 输入框特殊字符过滤
 *  @className: InputFilterSpecialChar
 *  @author: thanatos
 *  @createTime: 2018/6/11
 *  @updateTime: 2018/6/11 09:29
 */
public class InputFilterSpecialChar implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String regexStr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(source.toString());
        if (matcher.matches()) {
            return "";
        } else {
            return matcher.replaceAll("");
        }
    }

}

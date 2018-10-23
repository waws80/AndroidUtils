package top.waws.library.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 *  @desc: 金钱输入限制
 *  @className: InputFilterMoneyX
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午5:30
 */
public class InputFilterMoneyX implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.toString().trim().equals(".")
                && dstart == 0 && dend == 0){
            return "0"+source+dest;
        }
        if (dest.toString().contains(".") &&
                (dest.length() - dest.toString().indexOf(".")) > 2){
            if ((dest.length() - dstart) < 3){
                return "";
            }
        }
        return null;
    }
}

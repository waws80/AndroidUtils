package top.waws.library.inputfilter;

import android.text.InputFilter;

/**
 *  @desc: 编辑框格式过滤器
 *  @className: InputFilterUtil
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午5:04
 */
public class InputFilterUtil {

    private InputFilterUtil(){}

    private static final class Inner{
        private static final InputFilterUtil UTIL = new InputFilterUtil();
    }

    public static InputFilterUtil getInstance(){
        return Inner.UTIL;
    }

    /**
     * 不能输入表情
     * @return
     */
    public InputFilterEmoji getEmojiFilter(){
        return new InputFilterEmoji();
    }

    /**
     * 不能输入特殊字符
     * @return
     */
    public InputFilterSpecialChar getSpecialCharFilter(){
        return new InputFilterSpecialChar();
    }

    /**
     * 指定长度
     * @param max
     * @return
     */
    public InputFilter.LengthFilter getLengthFilter(int max){
        return new InputFilter.LengthFilter(max);
    }

    /**
     * 只能输入金钱
     * @return
     */
    public InputFilterMoneyX getMoneyXFilter(){
        return new InputFilterMoneyX();
    }

    /**
     * 不能输入
     * @return
     */
    public InputFilterNoInput getNoInputFilter(){
        return new InputFilterNoInput();
    }

    /**
     * 不能输入空格 和换行
     * @return
     */
    public InputFilterNoSpaceEnter getNoSpaceFilter(){
        return new InputFilterNoSpaceEnter();
    }
}

package top.waws.library.inputfilter;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  功能描述:
 *  @className: InputFilterEmoji
 *  @author: thanatos
 *  @createTime: 2018/6/11
 *  @updateTime: 2018/6/11 09:29
 */
public class InputFilterEmoji implements InputFilter {

    private static final String NUM = "^[0-9]*$";

    private static final HashSet<String> mFilterSet = new HashSet<>();

    static {
        // 1F601 - 1F64F
        addUnicodeRangeToSet(0x1F601, 0X1F64F);
        // 2702 - 27B0
        addUnicodeRangeToSet(0x2702, 0X27B0);
        // 1F680 - 1F6C0
        addUnicodeRangeToSet(0X1F680, 0X1F6C0);
        // 24C2 - 1F251
        addUnicodeRangeToSet(0X24C2, 0X1F251);
        // 1F600 - 1F636
        addUnicodeRangeToSet(0X1F600, 0X1F636);
        // 1F681 - 1F6C5
        addUnicodeRangeToSet(0X1F681, 0X1F6C5);
        // 1F30D - 1F567
        addUnicodeRangeToSet(0X1F30D, 0X1F567);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        //如果是数字直接返回
        if (isMatch(NUM,source)){
            return source;
        }
        Pattern specialChar = Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5]+$");
        Matcher specialMatcher = specialChar.matcher(source);
        if (!specialMatcher.find()){
            return "";
        }
        Pattern emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            return "";
        }
        return source;
    }

    private static void addUnicodeRangeToSet(int start, int end) {
        if (start > end) {
            return;
        }
        for (int i = start; i <= end; i++) {
            mFilterSet.add(new String(new int[] {i}, 0, 1));
        }
    }

    private static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
}

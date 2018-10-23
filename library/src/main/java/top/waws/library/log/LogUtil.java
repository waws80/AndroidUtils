package top.waws.library.log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import androidx.annotation.NonNull;
import top.waws.library.AppUtils;

/**
 *  @desc: 日志信息工具类
 *  @className: LogUtil
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 下午4:28
 */
public class LogUtil {

    private static final boolean sDebug = AppUtils.isDebug();

    private LogUtil(){
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private static final class Inner{
        private static final LogUtil UTIL = new LogUtil();
    }

    public static LogUtil getInstance(){
        return Inner.UTIL;
    }

    public void d(@NonNull Object object){
        if (sDebug){
            Logger.d(object);
        }
    }

    public void json(@NonNull String json){
        if (sDebug){
            Logger.json(json);
        }
    }

    public void xml(@NonNull String xml){
        if (sDebug){
            Logger.xml(xml);
        }
    }


}

package top.waws.library.mmkv;

import com.tencent.mmkv.MMKV;

import androidx.annotation.NonNull;
import top.waws.library.AppUtils;

/**
 *  @desc: 腾讯开源的跨进程数据存储 工具类
 *  @className: MMKVUtil
 *  @author: thanatos
 *  @createTime: 2018/10/18
 *  @updateTime: 2018/10/18 下午3:14
 */
public class MMKVUtil {

    private MMKVUtil(){
        //初始化mmkv
        MMKV.initialize(AppUtils.getInstance().getContext());
    }

    private static final class Inner{
        private static final MMKVUtil MMKV_UTIL = new MMKVUtil();
    }

    public static MMKVUtil getInstance(){
        return Inner.MMKV_UTIL;
    }

    /**
     * 添加数据
     * @param module 模块名字
     * @param key
     * @param value
     */
    public void put(@NonNull String module, @NonNull String key, @NonNull Object value){
        put(MMKV.mmkvWithID(module,MMKV.MULTI_PROCESS_MODE), key, value);
    }

    /**
     * 添加数据
     * @param key
     * @param value
     */
    public void put(@NonNull String key, @NonNull Object value){
        put(MMKV.defaultMMKV(), key, value);
    }

    /**
     * 获取数据
     * @param module 模块名字
     * @param key
     * @param def
     * @param <T>
     * @return
     */
    public <T> T get(@NonNull String module, @NonNull String key, @NonNull T def){
        return get(MMKV.mmkvWithID(module,MMKV.MULTI_PROCESS_MODE), key, def);
    }

    /**
     * 获取数据
     * @param key
     * @param def
     * @param <T>
     * @return
     */
    public <T> T get(@NonNull String key, @NonNull T def){
        return get(MMKV.defaultMMKV(), key, def);
    }

    /**
     * 移除数据
     * @param module 模块名字
     * @param key
     */
    public void remove(@NonNull String module, @NonNull String key){
        remove(MMKV.mmkvWithID(module,MMKV.MULTI_PROCESS_MODE),key);
    }

    /**
     * 移除数据
     * @param module 模块名字
     * @param keys
     */
    public void removeKeys(@NonNull String module, @NonNull String ...keys){
        removeKeys(MMKV.mmkvWithID(module,MMKV.MULTI_PROCESS_MODE),keys);
    }

    /**
     * 移除数据
     * @param key
     */
    public void remove(@NonNull String key){
        remove(MMKV.defaultMMKV(), key);
    }

    /**
     * 移除数据
     * @param keys
     */
    public void remove(@NonNull String ...keys){
        removeKeys(MMKV.defaultMMKV(), keys);
    }

    /**
     * 清楚数据
     * @param module 模块名字
     */
    public void clear(@NonNull String module){
        clear(MMKV.mmkvWithID(module, MMKV.MULTI_PROCESS_MODE));
    }

    /**
     * 清除数据
     */
    public void clear(){
        clear(MMKV.defaultMMKV());
    }

    /**
     * 保存数据
     * @param key 键
     * @param value 值
     */
    private void put(MMKV mmkv, String key, Object value){
        if (value instanceof String){
            mmkv.encode(key, (String) value);
        }else if (value instanceof Integer){
            mmkv.encode(key, (Integer)value);
        }else if (value instanceof Boolean){
            mmkv.encode(key, (Boolean)value);
        }else if (value instanceof Double){
            mmkv.encode(key, (Double) value);
        }else if (value instanceof Float){
            mmkv.encode(key, (Float) value);
        }else {
            mmkv.encode(key,value.toString());
        }
    }

    /**
     * 获取数据
     * @param <T> 值类型
     * @param key 键
     * @param def 默认值
     * @return 值
     */
    @SuppressWarnings("unchecked")
    private  <T> T get(MMKV mmkv, String key, T def){
        Object value;
        if (def instanceof String){
            value = mmkv.decodeString(key, (String) def);
        }else if (def instanceof Integer){
            value = mmkv.decodeInt(key, (Integer) def);
        }else if (def instanceof Boolean){
            value = mmkv.decodeBool(key, (Boolean) def);
        }else if (def instanceof Double){
            value = mmkv.decodeDouble(key, (Double) def);
        }else if (def instanceof Float){
            value = mmkv.decodeFloat(key, (Float) def);
        }else {
            value = mmkv.decodeString(key, "");
        }
        return (T) value;
    }

    private void remove(MMKV mmkv, String key){
        mmkv.removeValueForKey(key);
    }

    private void removeKeys(MMKV mmkv, @NonNull String... keys){
        mmkv.removeValuesForKeys(keys);
    }

    private void clear(MMKV mmkv){
        mmkv.clearAll();
    }
}

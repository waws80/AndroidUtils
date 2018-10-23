package top.waws.library.utils;

import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;


/**
 *  @desc: rom工具类
 *  @className: RomUtil
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午4:18
 */
public final class RomUtil {

    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";

    private RomUtil(){}

    private static final class Inner{
        private static final RomUtil ROM_UTIL = new RomUtil();
    }

    public static RomUtil getInstance(){
        return Inner.ROM_UTIL;
    }

    /**
     * 判断是否为MIUI
     *
     * @return
     */
    public boolean isMIUI() {
        String property = getSystemProperty(KEY_MIUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }

    /**
     * 获取MUI版本
     *
     * @return
     */
    public String getMIUIVersion() {
        return isMIUI() ? getSystemProperty(KEY_MIUI_VERSION_NAME, "") : "";
    }

    /**
     * 获取MIUI版本-数字用于大小判断
     *
     * @return
     */
    public int getMIUIVersionCode() {
        int code = -1;
        String property = getMIUIVersion();
        try {
            property = property.trim().toUpperCase().replace("V", "");
            code = Integer.parseInt(property);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 判断是否为EMUI
     *
     * @return
     */
    public boolean isEMUI() {
        String property = getSystemProperty(KEY_EMUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }


    /**
     * 获取EMUI的版本
     *
     * @return
     */
    public String getEMUIVersion() {
        return isEMUI() ? getSystemProperty(KEY_EMUI_VERSION_NAME, "") : "";
    }

    /**
     * 判断是否为Flyme
     *
     * @return
     */
    public boolean isFlyme() {
        return Build.DISPLAY.toLowerCase().contains("flyme");
    }

    /**
     * 获取Flyme的版本
     *
     * @return
     */
    public String getFlymeVersion() {
        return isFlyme() ? Build.DISPLAY : "";
    }

    /**
     * 获取Flyme版本号
     *
     * @return
     */
    public int getFlymeVersionCode() {
        int code = 0;
        String version = getFlymeVersion();
        if (!TextUtils.isEmpty(version)) {
            if (version.toLowerCase().contains("os")) {
                code = Integer.valueOf(version.substring(9, 10));
            } else {
                code = Integer.valueOf(version.substring(6, 7));
            }
        }
        return code;
    }


    /**
     * 通过反射获取系统属性
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

}

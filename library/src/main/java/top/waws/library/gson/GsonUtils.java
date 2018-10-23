package top.waws.library.gson;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 *  @desc: Gson 工具类
 *  @className: GsonUtils
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午2:04
 */
public final class GsonUtils {


    //gson
    private static final Gson sGson = new GsonBuilder().serializeNulls().create();

    private GsonUtils(){ }

    private static final class Inner{
        private static final GsonUtils UTILS = new GsonUtils();
    }

    /**
     * 全局唯一对象
     * @return
     */
    public static GsonUtils getInstance(){
        return Inner.UTILS;
    }

    /**
     * 返回一个gson
     * @return
     */
    public Gson getGson(){
        return sGson;
    }

    /**
     * 节点得到相应的内容
     * @param jsonString json字符串
     * @param note       节点
     * @return 节点对应的内容
     */
    public String getNoteJsonString(@NonNull String jsonString, @NonNull String note) {
        JsonElement element = new JsonParser().parse(jsonString);
        if (element.isJsonNull()) {
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        return element.getAsJsonObject().get(note).toString();
    }

    /**
     * 节点得到节点内容，然后传化为相对应的bean数组
     *
     * @param jsonString 原json字符串
     * @param note       节点标签
     * @param beanClazz  要转化成的bean class
     * @return 返回bean的数组
     */
    public <T> List<T> convertList(String jsonString, String note, Class<T> beanClazz) {
        String noteJsonString = getNoteJsonString(jsonString, note);
        return convertList(noteJsonString, beanClazz);
    }

    /**
     * 节点得到节点内容，转化为一个数组
     *
     * @param jsonString json字符串
     * @param beanClazz  集合里存入的数据对象
     * @return 含有目标对象的集合
     */
    public <T> List<T> convertList(@NonNull String jsonString, @NonNull Class<T> beanClazz) {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement.isJsonNull()) {
            throw new RuntimeException("得到的jsonElement对象为空");
        }
        if (!jsonElement.isJsonArray()) {
            throw new RuntimeException("json字符不是一个数组对象集合");
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<T> beans = new ArrayList<>();
        for (JsonElement jsonElement2 : jsonArray) {
            T bean = new Gson().fromJson(jsonElement2, beanClazz);
            beans.add(bean);
        }
        return beans;
    }

    /**
     * 把相对应节点的内容封装为对象
     *
     * @param jsonString json字符串
     * @param clazzBean  要封装成的目标对象
     * @return 目标对象
     */
    public <T> T convertObject(@NonNull String jsonString, @NonNull Class<T> clazzBean) {
        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement.isJsonNull()) {
            throw new RuntimeException("json字符串为空");
        }
        if (!jsonElement.isJsonObject()) {
            throw new RuntimeException("json不是一个对象");
        }
        return new Gson().fromJson(jsonElement, clazzBean);
    }

    /**
     * 按照节点得到节点内容，转化为一个数组
     *
     * @param jsonString json字符串
     * @param note       json标签
     * @param clazzBean  集合里存入的数据对象
     * @return 含有目标对象的集合
     */
    public <T> T convertObject(String jsonString, String note, Class<T> clazzBean) {
        String noteJsonString = getNoteJsonString(jsonString, note);
        return convertObject(noteJsonString, clazzBean);
    }

    /**
     * 把bean对象转化为json字符串
     *
     * @param obj bean对象
     * @return 返回的是json字符串
     */
    public String toJson(Object obj) {
        if (obj != null) {
            return new Gson().toJson(obj);
        } else {
            throw new RuntimeException("对象不能为空");
        }
    }
}

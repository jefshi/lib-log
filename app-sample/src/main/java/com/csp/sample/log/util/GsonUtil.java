package com.csp.sample.log.util;

import androidx.annotation.NonNull;

import com.csp.lib.log.core.LogCat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gson 工具类
 * Created by csp on 2019/09/29
 * Modified by csp on 2021/02/17
 *
 * @author csp
 * @version 1.1.4
 */
public class GsonUtil {

    private volatile static Gson gson;

    private GsonUtil() {
    }

    /**
     * 定制 Gson 转换格式：
     * 1. 序列化值为 null 的数据
     * 2. 序列化 html 格式字符串时，避免将将 < 等字符序列化为 \ u003 之类的形式
     * 3. transient 关键字修饰的变量不序列
     * 4. 支持 map 的 key 序列化。ps：json 的结果类似把 key 变成一个 map
     *
     * @return Gson 对象
     */
    public static Gson getGson() {
        if (gson == null) {
            synchronized (Gson.class) {
                if (gson == null) {
                    gson = new GsonBuilder()
                            .serializeNulls()
                            .disableHtmlEscaping()
                            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                            .enableComplexMapKeySerialization()
                            .create();
                }
            }
        }
        return gson;
    }

    /**
     * bean -> String
     *
     * @param src 数据源
     * @return json 字符串
     */
    public static String toJson(Object src) {
        try {
            return getGson().toJson(src);
        } catch (Throwable t) {
            LogCat.printStackTrace(t);
            return null;
        }
    }

    /**
     * String -> 单个 bean（即不能是 bean 的集合）
     *
     * @param json     合规的 json 字符串
     * @param classOfT bean Class 对象
     * @param <T>      bean 类型
     * @return bean 对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return getGson().fromJson(json, classOfT);
        } catch (Throwable t) {
            LogCat.printStackTrace(t);
            return null;
        }
    }

    /**
     * String -> bean（单个 bean 和集合 bean 都允许），关于集合参考 see 部分说明
     *
     * @param json    合规的 json 字符串
     * @param typeOfT bean Type 类型，通过 new TypeToken<T>(){}.getType() 获取
     * @param <T>     bean 类型
     * @return bean 对象
     * @see TypeToken
     * @see #fromJsonForList(String)：关于集合参考下该 API 注释
     * @see #fromJsonForMap(String)：关于集合参考下该 API 注释
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return getGson().fromJson(json, typeOfT);
        } catch (Throwable t) {
            LogCat.printStackTrace(t);
            return null;
        }
    }

    /**
     * 这里只是做个记录，并不能使用通用泛型 T，只能使用具体的类
     * <p>
     * 使用通用泛型 T 结果记录：
     * 1.使用通用泛型 T 的结果等同于 List<Object>，返回结果为 {@link com.google.gson.internal.LinkedTreeMap}
     * 2.不过 bean 是 String 的话，返回结果是正确的：List<String>
     *
     * @return 数据集合
     */
    @Deprecated
    private static <T> List<T> fromJsonForList(String json) {
        return getGson().fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * 这里只是做个记录，并不能使用通用泛型 T，只能使用具体的类
     * <p>
     * 使用通用泛型 T 结果记录：
     * 1.使用通用泛型 T 的结果等同于 Map<Object, Object>，返回结果为 {@link com.google.gson.internal.LinkedTreeMap}
     * 2.不过 key，value 都是 String 的话，返回结果是正确的：Map<String, String>
     *
     * @return 数据集合
     */
    @Deprecated
    private static <K, V> Map<K, V> fromJsonForMap(String json) {
        return getGson().fromJson(json, new TypeToken<Map<K, V>>() {
        }.getType());
    }

    /**
     * json -> List<String>
     *
     * @return 数据集合
     * @see #fromJsonForList(String)
     */
    @NonNull
    public static List<String> fromJsonForListString(String json) {
        List<String> list = fromJson(json, new TypeToken<List<String>>() {
        }.getType());
        return list == null ? new ArrayList<>(0) : list;
    }

    /**
     * json -> Map<String, String>
     *
     * @return 数据集合
     * @see #fromJsonForMap(String)
     */
    public static Map<String, String> fromJsonForMapStringString(String json) {
        Map<String, String> map = fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
        return map == null ? new HashMap<>(0) : map;
    }
}

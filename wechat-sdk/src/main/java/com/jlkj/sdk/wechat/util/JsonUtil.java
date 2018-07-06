package com.jlkj.sdk.wechat.util;


import com.alibaba.fastjson.JSON;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 17:34 2018/3/20
 * @Description Json工具类
 */
public class JsonUtil {

    /**
     * 使用Gson生成json字符串
     *
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        //
        return JSON.toJSONString(src);
    }

    /**
     * 将jsonString转化为指定的class对象
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T parseJson(String jsonStr, Class<T> tClass) {
        //
        return JSON.parseObject(jsonStr,tClass);
    }




}


package com.weida.dns.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * json-对象 转换工具Jackson版
 *
 * @author weida
 */
public class JsonUtils {
    private static ObjectMapper om = new ObjectMapper();

    static {
        om.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        om.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    /**
     * json转对象
     *
     * @param json          json
     * @param typeReference 参考类型封装
     * @param <T>           对象类型
     * @return 对象
     */
    public static <T> T jsonToObj(String json, TypeReference<T> typeReference) {
        T obj = null;
        if (!StringUtils.isEmpty(json)) {
            try {
                obj = om.readValue(json, typeReference);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return obj;
    }

    /**
     * 对象转json
     *
     * @param t   对象
     * @param <T> 对象类型
     * @return json
     */
    public static <T> String objToJson(T t) {
        String json = null;
        try {
            json = om.writeValueAsString(t);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            return null;
        }
        return json;
    }


}

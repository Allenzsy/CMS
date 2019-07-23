package com.zsy.cms.utils;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class RequestUtil {

    public static <T> T copyParams(Class<T> entityClass, HttpServletRequest request) {

        Map<String, String[]> paramsMap = request.getParameterMap();
        try {
            T entity = entityClass.newInstance();
            for(Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
                String property = entry.getKey();
                String[] value = entry.getValue();
                if(value != null) {
                    if(value.length == 1) {
                        BeanUtils.copyProperty(entity, property, value[0]);
                    } else {
                        // 这里如果不定义 channels 的类型转换器，那么channels将会赋值失败
                        BeanUtils.copyProperty(entity, property, value);
                    }
                }
            }
            return entity;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

}

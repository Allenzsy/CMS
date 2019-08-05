package com.zsy.cms.backend.dao;

import org.apache.commons.beanutils.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter {

    /**
     * 要将字符串类型额value转换为java.util.Date类型的值
     * @param targetClass   就是要转换为的那个类
     * @param value         就是要被转换的那个值
     * @param <T>
     * @return              返回转换完的类对象
     */

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public <T> T convert(Class<T> targetClass, Object value) {

        if(targetClass != Date.class) {
            return  null;
        }
        if(value instanceof String) {
            try {
                return  (T) format.parse((String) value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}

package com.zsy.cms.utils;

import com.zsy.cms.backend.model.Channel;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

public class ChannelsConverter implements Converter {
    @Override
    public <T> T convert(Class<T> entityClass, Object value) {
        // 这里由于前面的处理，当选中一个channel的时候value是一个String
        // 当选中多个channel的时候value是一个String[]
        String[] channelIds = null;
        if (value != null) {
            if (value instanceof String) {
                channelIds = new String[]{(String)value};
            }
            if (value instanceof String[]) {
                channelIds = (String[])value;
            }
        }
        HashSet<Channel> channels = new HashSet<Channel>();
        if(channelIds != null) {
            try {
//                channels = entityClass.newInstance();
                for(String cid : channelIds) {
                    Channel c = new Channel();
                    c.setId(Integer.parseInt(cid));
                    MethodUtils.invokeMethod(channels,"add",c);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return (T) channels;
    }
}

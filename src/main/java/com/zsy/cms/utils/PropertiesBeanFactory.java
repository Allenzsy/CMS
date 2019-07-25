package com.zsy.cms.utils;

import java.io.IOException;
import java.util.*;
import java.util.Properties;

public class PropertiesBeanFactory implements BeanFactory {

    Map<String,Object> daoBeans = new HashMap<>();

    public PropertiesBeanFactory() {
        this("factory.properties");
    }

    public PropertiesBeanFactory(String properties) {
        try {
            Properties prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(properties));
            for(String daoName : prop.stringPropertyNames()) {
                String className = prop.getProperty(daoName);
                Class clazz = Class.forName(className);
                daoBeans.put(daoName, clazz.newInstance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String name) {
        return daoBeans.get(name);
    }


    public static void main(String[] args) {

        return;
    }
}

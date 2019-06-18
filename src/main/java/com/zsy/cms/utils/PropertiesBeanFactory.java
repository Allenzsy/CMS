package com.zsy.cms.utils;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.dao.imple.ArticleDaoImpleForSQL;
import com.zsy.cms.backend.model.Channel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.Properties;

public class PropertiesBeanFactory implements BeanFactory {

    Map<String,Object> daoBeans = new HashMap<>();

    public PropertiesBeanFactory() {
        try {
            Properties prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("factory.properties"));
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

    @Override
    public ArticleDao getArticleDao() {
        Properties prop = new Properties();
        try {
            System.out.println(prop.getProperty("ArticleDao"));
            Class clazz = Class.forName(prop.getProperty("ArticleDao"));
            return (ArticleDao) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
        e.printStackTrace();
        } catch (IllegalAccessException e) {
                e.printStackTrace();
        }

        return null;

    }

    @Override
    public ChannelDao getChannelDao() {
        Properties prop = new Properties();
        try {
            System.out.println(prop.getProperty("ChannelDao"));
            Class clazz = Class.forName(prop.getProperty("ChannelDao"));
            return (ChannelDao) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

        ChannelDao d = new PropertiesBeanFactory().getChannelDao();
        return;
    }
}

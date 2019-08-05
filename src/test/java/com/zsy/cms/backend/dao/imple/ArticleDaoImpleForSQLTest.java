package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.BeanFactory;
import com.zsy.cms.utils.PropertiesBeanFactory;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

public class ArticleDaoImpleForSQLTest {

    @Test
    public void addArticle() {

        BeanFactory beanFactory = new PropertiesBeanFactory("factory.properties");
        ArticleDao articleDao = (ArticleDao) beanFactory.getBean("ArticleDao");

        Random rand = new Random();

        Article a = new Article();
        a.setTitle("测试文章"+rand.nextInt(999999));
        a.setContent("测试文章内容"+rand.nextInt(9999999));
        a.setHeadline(true);
        a.setType("原创");

        Set<Channel> channels = new HashSet<>();
        Channel c1 = new Channel();
        c1.setId(1);
        Channel c2 = new Channel();
        c2.setId(4);
        channels.add(c1);
        channels.add(c2);
        a.setChannels(channels);

        articleDao.addArticle(a);


    }
    @Test
    public void a() {
        double a = 5/2;
        System.out.println(a);
    }
}
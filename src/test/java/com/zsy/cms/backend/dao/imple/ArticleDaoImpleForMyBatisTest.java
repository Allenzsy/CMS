package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.BeanFactory;
import com.zsy.cms.utils.PageVO;
import com.zsy.cms.utils.PropertiesBeanFactory;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.junit.Assert.*;

public class ArticleDaoImpleForMyBatisTest {

    BeanFactory beanFactory = new PropertiesBeanFactory("factory.properties");
    ArticleDao articleDao = (ArticleDao) beanFactory.getBean("ArticleDao");

    @Test
    public void addArticle() {

        Random rand = new Random();

        Article a = new Article();
        a.setTitle("测试文章"+rand.nextInt(999999));
        a.setContent("测试文章内容"+rand.nextInt(9999999));
        a.setHeadline(true);
        a.setType("原创");

        Set<Channel> channels = new HashSet<>();
        Channel c1 = new Channel();
        c1.setId(10);
        Channel c2 = new Channel();
        c2.setId(11);
        channels.add(c1);
        channels.add(c2);
        a.setChannels(channels);

        articleDao.addArticle(a);
        System.out.println(a.getId());
    }

    @Test
    public void delArticle() {
        articleDao.delArticle(new String[]{"6"});
    }

    @Test
    public void findArticleByTitle() {
        PageVO<Article> pv;
        pv = articleDao.findArticleByTitle("8");
        System.out.println("Total:" + pv.getTotal());
        List<Article> datas = pv.getDatas();
        for (Iterator iterator = datas.iterator(); iterator.hasNext(); ) {
            Article a = (Article) iterator.next();
            System.out.println("title:" + a.getTitle());
        }
    }

    @Test
    public void searchArticleByChannel() {
        PageVO<Article> pv;
        Channel c = new Channel();
        c.setId(4);
        pv = articleDao.findArticleByChannel(c);
        System.out.println("Total:" + pv.getTotal());
        List<Article> datas = pv.getDatas();
        for (Iterator iterator = datas.iterator(); iterator.hasNext(); ) {
            Article a = (Article) iterator.next();
            System.out.println("id: "+a.getId()+",title:" + a.getTitle());
        }
    }

    @Test
    public void findArticleById() {
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        Article article = articleDao.findArticleById("7",request,response);
        System.out.println("id:"+article.getId()+",title:"+article.getTitle());
    }

    @Test
    public void updateArticle() {

        Article a = new Article();
        a.setId(7);
        a.setTitle("啊啊啊啊啊啊");
        a.setContent("集合 TCP");
        a.setUpdateTime(new Date(System.currentTimeMillis()));

        articleDao.updateArticle(a);

    }
}
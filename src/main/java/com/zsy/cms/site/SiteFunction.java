package com.zsy.cms.site;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.backend.view.InitBeanFactoryServlet;
import com.zsy.cms.utils.BeanFactory;

import javax.servlet.jsp.PageContext;

public class SiteFunction {

    /**
     * 根据频道Id拿到channel对象
     * @param pageContext
     * @param channelId
     * @return
     */
    public static Channel findChannelById(PageContext pageContext, String channelId) {
        BeanFactory beanFactory = (BeanFactory) pageContext.getServletContext().getAttribute(InitBeanFactoryServlet.DAO_FACTORY);
        ChannelDao channelDao = (ChannelDao) beanFactory.getBean("ChannelDao");
        return channelDao.findChannelById(channelId,null,null);
    }

    /**
     * 根据文章Id拿到article对象
     * @param pageContext
     * @param articleId
     * @return
     */
    public static Article findArticleById(PageContext pageContext, String articleId) {
        BeanFactory beanFactory = (BeanFactory) pageContext.getServletContext().getAttribute(InitBeanFactoryServlet.DAO_FACTORY);
        ArticleDao articleDao = (ArticleDao) beanFactory.getBean("ArticleDao");
        return articleDao.findArticleById(articleId, null, null);
    }
}

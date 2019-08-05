package com.zsy.cms.backend.dao;

import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.PageVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ArticleDao {

    /**
     * 这里应该声明article的数据库操作方法
     * 也就是说这里的方法是为后面写的Servlet服务的，那么输入输出应该符合Servlet的要求
     * findArticle需要输入offset pagesize，需要输出total articles的list,输出有两个且类型不同时，应该再创建一个类，
     * 这种类实际上是value object
     * 后台操作相关相关的：
     */
    public void addArticle(Article a);
    public void delArticle(String[] ids);
    public Article findArticleById(String id, HttpServletRequest request, HttpServletResponse response);
    public PageVO<Article> findArticleByTitle(String title);
    public PageVO<Article> findArticleByChannel(Channel channel);
    public void updateArticle(Article a);

    /**
     * 首页操作相关：
     */
    public List<Article> findArticles(Channel c, int max);
    public List<Article> findHeadLine(int max);
    public List<Article> findRecommend(int max);





}

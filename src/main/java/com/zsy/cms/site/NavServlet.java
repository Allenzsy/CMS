package com.zsy.cms.site;

import com.zsy.cms.SystemContext;
import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.backend.view.BaseServlet;
import com.zsy.cms.utils.PageVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/NavServlet")
public class NavServlet extends BaseServlet {

    private ArticleDao articleDao;
    private ChannelDao channelDao;

    public void channelList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String channelId = request.getParameter("channelId");
        Channel c = new Channel();
        c.setId(Integer.parseInt(channelId));
        request.setAttribute("c1", articleDao.searchArticles(c, 20));
        request.getRequestDispatcher("/channel.jsp").forward(request, response);

    }

    public void navList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("channels", channelDao.findChannels().getDatas());
        request.getRequestDispatcher("/portlet/channel_list.jsp").include(request, response);

    }
    public void indexChannelArticleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String channelId = request.getParameter("channelId");
        Channel c = channelDao.findChannelById(channelId, request, response);
        request.setAttribute("channel", c);
        request.setAttribute("articles", articleDao.searchArticles(c, 20));
        request.getRequestDispatcher("/portlet/index_channel_article_list.jsp").include(request,response);

    }

    public void headline(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("headline", articleDao.searchHeadLine(2));
        request.getRequestDispatcher("/portlet/headline.jsp").include(request,response);

    }

    public void recommend(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("recommend", articleDao.searchRecommend(10));
        request.getRequestDispatcher("/portlet/recommend.jsp").include(request,response);

    }

    public void latest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 最新发表的10篇文章
        String channelId = request.getParameter("channelId");
        Channel c = null;
        if(channelId != null) {
            c = new Channel();
            c.setId(Integer.parseInt(channelId));
        }
        request.setAttribute("latest", articleDao.searchArticles(c, 10));
        request.getRequestDispatcher("/portlet/latest.jsp").include(request,response);

    }
    // 查询出某个频道上的所有文章，并分页显示
    public void channelIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String channelId = request.getParameter("channelId");
        Channel c = null;
        if(channelId != null) {
            c = channelDao.findChannelById(channelId, request, response);
            request.setAttribute("channel", c);
        }
        PageVO<Article> pv = articleDao.findArticleByChannel(c);
        request.setAttribute("articles", pv.getDatas());
        request.setAttribute("total", pv.getTotal());
        request.getRequestDispatcher("/portlet/channel_index.jsp").include(request, response);
    }
    // 查询文章，并在article_detail.jsp上显示全部内容
    public void articleDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("articleId");
        request.setAttribute("article", articleDao.findArticleById(id, request, response));
        request.getRequestDispatcher("/portlet/article_detail.jsp").include(request,response);

    }

    public void keywords(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("articleId");
        Article article = articleDao.findArticleById(id, request, response);
        String keyword = article.getKeyword();
        request.setAttribute("articleByKeyword", articleDao.searchArticleByKeyword(keyword).getDatas());
        request.getRequestDispatcher("/portlet/keyword_article.jsp").include(request, response);

    }




        public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }
}

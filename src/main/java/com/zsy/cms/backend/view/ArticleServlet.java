package com.zsy.cms.backend.view;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.BeanFactory;
import com.zsy.cms.utils.PageVO;
import com.zsy.cms.utils.PropertiesBeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/backend/ArticleServlet")
public class ArticleServlet extends BaseServlet {

    ArticleDao articleDao;
    ChannelDao channelDao;

    // 用来打开添加文章的界面
    public void addInput(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取提交文章的表单信息

        // 查出所有的频道列表，然后放到request中
        PageVO<Channel> pv = channelDao.findChannelByName(0, Integer.MAX_VALUE, null);
        request.setAttribute("channels", pv.getDatas());
        // forward 到添加文章界面
        request.getRequestDispatcher("/backend/article/add_article.jsp").forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取提交文章的表单信息
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String source = request.getParameter("source");
        String author = request.getParameter("author");
        String keyword = request.getParameter("keyword");     //关键字
        String intro = request.getParameter("intro");       //简介
        String type = request.getParameter("type");        //分类
        String recommend = request.getParameter("recommend");  //是否推荐阅读
        String headline = request.getParameter("headline");   //是否作为首页头条
        String[] channelIds = request.getParameterValues("channelIds");

        Article a = new Article();
        a.setTitle(title);
        a.setContent(content);
        a.setSource(source);
        a.setAuthor(author);
        a.setKeyword(keyword);
        a.setIntro(intro);
        a.setType(type);
        if(recommend != null) {
            a.setRecommend(Boolean.parseBoolean(recommend));
        }
        if(headline != null) {
            a.setHeadline(Boolean.parseBoolean(headline));
        }
        if(channelIds != null) {
            Set<Channel> channels = new HashSet<>();
            for(String cid : channelIds) {
                Channel c = new Channel();
                c.setId(Integer.parseInt(cid));
                channels.add(c);
            }
            a.setChannels(channels);
        }
        a.setCreateTime(new Date());

        articleDao.addArticle(a);

        // 显示文章添加成功页面
        request.getRequestDispatcher("/backend/article/add_article_success.jsp").forward(request, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 拿到唯一标识 这里修改为可以批量删除，拿到所以参数名为id的参数值""
        String[] ids = request.getParameterValues("id");
        // 期望dao能够通过输入String[] ids 来删除文章

        if(ids == null) {
            request.setAttribute("error", "删除错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }

        articleDao.delArticle(ids);

        // 如果正确forward到ArticleServlet（这里不能直接forward到article_list.jsp，因为这样页面中不会有数据）
        // 这里直接sendRedirect到ArticleServlet，因为如果直接forward到依然会携带method参数，然后造成死循环最终堆栈溢出
        response.sendRedirect(request.getContextPath()+"/backend/ArticleServlet");
        return;
    }

    public void openUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 拿到文章id值
        String id = request.getParameter("id");
        if(id == null) {
            request.setAttribute("error", "编辑错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }

        Article a = articleDao.findArticleById(id, request, response);
        PageVO<Channel> pv = channelDao.findChannelByName(0, Integer.MAX_VALUE, null);

        // forward 到更新的jsp页面
        request.setAttribute("updateArticle", a);
        request.setAttribute("channels", pv.getDatas());
        request.getRequestDispatcher("/backend/article/update_article.jsp").forward(request, response);

    }

    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 根据提交内容，获取信息
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String source = request.getParameter("source");
        String author = request.getParameter("author");
        String keyword = request.getParameter("keyword");     //关键字
        String intro = request.getParameter("intro");       //简介
        String type = request.getParameter("type");        //分类
        String recommend = request.getParameter("recommend");  //是否推荐阅读
        String headline = request.getParameter("headline");   //是否作为首页头条
        String[] channelIds = request.getParameterValues("channelIds");

        Article a = new Article();
        a.setId(Integer.parseInt(id));
        a.setTitle(title);
        a.setContent(content);
        a.setSource(source);
        a.setAuthor(author);
        a.setKeyword(keyword);
        a.setIntro(intro);
        a.setType(type);
        if(recommend != null) {
            a.setRecommend(Boolean.parseBoolean(recommend));
        }
        if(headline != null) {
            a.setHeadline(Boolean.parseBoolean(headline));
        }
        if(channelIds != null) {
            Set<Channel> channels = new HashSet<>();
            for(String cid : channelIds) {
                Channel c = new Channel();
                c.setId(Integer.parseInt(cid));
                channels.add(c);
            }
            a.setChannels(channels);
        }
        a.setUpdateTime(new Date());

        if(id == null) {
            request.setAttribute("error", "删除错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        // 更新文章的内容
        articleDao.updateArticle(a);

        // 更新成功forward到更新成功页面
        request.getRequestDispatcher("/backend/article/update_article_success.jsp").forward(request, response);
    }


    // 这里重写BaseServlet中的方法，将缺省方法改为查询
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int offset = 0;
        int pageSize = 5;
        // 希望从requset中获取offset， 从session中获取pageSize，session中如果没有那么则设置缺省值
        try {
            offset = Integer.parseInt(request.getParameter("pager.offset"));
        } catch (Exception ignore) { }
        // 如果从request中拿到了pageSize的值，那么需要更新Session中的PageSize的值
        if(request.getParameter("pageSize") != null) {
            request.getSession().setAttribute("pageSize", Integer.parseInt(request.getParameter("pageSize")));
        }
        Integer ps = (Integer) request.getSession().getAttribute("pageSize");
        if(ps == null) {
            request.getSession().setAttribute("pageSize", pageSize);
        } else {
            pageSize = ps;
        }

        String title = request.getParameter("title");
        PageVO<Article> pv = articleDao.searchArticle(title, offset, pageSize);

        // 将查询到的文章传递给jsp
        request.setAttribute("articles", pv.getDatas());
        // 将总记录数传递给jsp,其他交给jsp中的page-taglib去计算
        request.setAttribute("total", pv.getTotal());

        // forward到article_list.jsp
        request.getRequestDispatcher("/backend/article/article_list.jsp").forward(request, response);
    }

    public void setArticleDao(ArticleDao articleDao) { this.articleDao = articleDao; }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }
}

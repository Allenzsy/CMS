package com.zsy.cms.backend.view;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.dao.imple.ArticleDaoImpleForSQL;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.utils.DBUtil;
import com.zsy.cms.utils.PageVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchArticleServlet extends HttpServlet {
    // 点击查询文章，是只有doGet方法
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int offset = 0;
        int pageSize = 5;
        // 希望从requset中获取offset， 从session中获取pageSize，session中如果没有那么则设置缺省值
        try {
            offset = Integer.parseInt(req.getParameter("pager.offset"));
        } catch (Exception ignore) { }
        // 如果从request中拿到了pageSize的值，那么需要更新Session中的PageSize的值
        if(req.getParameter("pageSize") != null) {
            req.getSession().setAttribute("pageSize", Integer.parseInt(req.getParameter("pageSize")));
        }
        Integer ps = (Integer) req.getSession().getAttribute("pageSize");
        if(ps == null) {
            req.getSession().setAttribute("pageSize", pageSize);
        } else {
            pageSize = ps;
        }

        String title = req.getParameter("title");
        ArticleDao articleDao = new ArticleDaoImpleForSQL();
        PageVO<Article> pv = articleDao.searchArticle(offset, pageSize, title);

        // 将查询到的文章传递给jsp
        req.setAttribute("articles", pv.getDatas());
        // 将总记录数传递给jsp,其他交给jsp中的page-taglib去计算
        req.setAttribute("total", pv.getTotal());

        // forward到article_list.jsp
        req.getRequestDispatcher("/backend/article/article_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}

/*
        for (int i = 0; i < 10; i++) {
        Article a = new Article();
        a.setTitle("这是测试文章标题"+i);
        a.setContent("这是测试文章内容"+i+"..................");
        list.add(a);
        }
*/
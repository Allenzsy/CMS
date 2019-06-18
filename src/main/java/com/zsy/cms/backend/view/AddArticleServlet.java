package com.zsy.cms.backend.view;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.dao.imple.ArticleDaoImpleForSQL;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.utils.DBUtil;
import com.zsy.cms.utils.PropertiesBeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Random;

@WebServlet("/backend/AddArticleServlet")
public class AddArticleServlet extends HttpServlet {

    // 添加文章应该只使用post方法提交表单
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取提交文章的表单信息
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String source = request.getParameter("source");

        Article a = new Article();
        a.setTitle(title);
        a.setContent(content);
        a.setSource(source);

        ArticleDao articleDao = new PropertiesBeanFactory().getArticleDao();
        articleDao.addArticle(a);

        // 显示文章添加成功页面
        request.getRequestDispatcher("/backend/article/add_article_success.jsp").forward(request, response);
    }
}

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@WebServlet("/backend/OpenUpdateArticleServlet")
public class OpenUpdateArticleServlet extends HttpServlet {
    // 由于是点击编辑到 OpenUpdateArticleServlet 不需要提交表单，所以只实现doGet方法。
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 拿到文章id值
        String id = request.getParameter("id");
        if(id == null) {
            request.setAttribute("error", "编辑错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }

        ArticleDao articleDao = new PropertiesBeanFactory().getArticleDao();
        Article a = articleDao.findArticleById(id, request, response);

        // forward 到更新的jsp页面
        request.setAttribute("updateArticle", a);
        request.getRequestDispatcher("/backend/article/update_article.jsp").forward(request, response);
    }
}

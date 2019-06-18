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
import java.text.SimpleDateFormat;

@WebServlet("/backend/UpdateArticleServlet")
public class UpdateArticleServlet extends HttpServlet {

    // 由于在更新文章的jsp页面编辑完成后，需要点击更新按钮将更新内容以表单形式提交到 UpdateArticleServlet上，所以只实现doPost方法
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 根据提交内容，获取信息
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String source = request.getParameter("source");

        if(id == null) {
            request.setAttribute("error", "删除错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }

        Article a = new Article();
        a.setId(Integer.parseInt(id));
        a.setTitle(title);
        a.setContent(content);
        a.setSource(source);

        ArticleDao articleDao = new PropertiesBeanFactory().getArticleDao();
        articleDao.updateArticle(a);

        // 更新成功forward到更新成功页面
        request.getRequestDispatcher("/backend/article/update_article_success.jsp").forward(request, response);
    }

}

package com.zsy.cms.backend.view;

import com.zsy.cms.backend.model.Article;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class ArticleSearchServlet extends HttpServlet {
    // 点击查询文章，是只有doGet方法
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 查寻数据库

        // 将结果存在一个容器中。由于显示文章可能有多个属性，如文章标题，文章内容（部分）， 日期，访问量...
        // 所以，这里应该自己设计一个类，用来完成每条数据存储。
        ArrayList<Article> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Article a = new Article();
            a.setTitle("这是测试文章标题"+i);
            a.setContent("这是测试文章内容"+i+"..................");
            list.add(a);
        }
        req.setAttribute("articles", list);

        // forward到article_list.jsp
        req.getRequestDispatcher("/backend/article/article_list.jsp").forward(req, resp);
    }
}

package com.zsy.cms.backend.view;

import com.zsy.cms.backend.model.Article;
import com.zsy.cms.utils.DBUtil;

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

        // 将结果存在一个容器中。由于显示文章可能有多个属性，如文章标题，文章内容（部分）， 日期，访问量...
        // 所以，这里应该自己设计一个类，用来完成每条数据存储。
        ArrayList<Article> list = new ArrayList<>();

        // 查寻数据库
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select * from t_article");
            rs = pstmt.executeQuery();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            while(rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setContent(rs.getString("content"));
                a.setSource(rs.getString("source"));
                a.setCreateTime(rs.getTimestamp("createtime"));
                a.setUpdateTime(rs.getTimestamp("updatetime"));
                list.add(a);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }
        req.setAttribute("articles", list);

        // forward到article_list.jsp
        req.getRequestDispatcher("/backend/article/article_list.jsp").forward(req, resp);
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
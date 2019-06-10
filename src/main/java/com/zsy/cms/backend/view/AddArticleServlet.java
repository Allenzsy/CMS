package com.zsy.cms.backend.view;

import com.zsy.cms.utils.DBUtil;

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

        request.setCharacterEncoding("UTF-8");
        // 获取提交文章的表单信息
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String source = request.getParameter("source");

        // 插入数据库
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("insert into t_article(title,content,source,createtime) values(?,?,?,?)");
            pstmt.setString(1, title);
            pstmt.setString(2,content);
            pstmt.setString(3,source);
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            pstmt.executeUpdate();
            conn.commit();
        }catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }

        // 显示文章添加成功页面
        request.getRequestDispatcher("/backend/article/add_article_success.jsp").forward(request, response);
    }
}

package com.zsy.cms.backend.view;

import com.zsy.cms.utils.DBUtil;

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

@WebServlet("/backend/DelArticleServlet")
public class DelArticleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 拿到唯一标识
        String id = request.getParameter("id");
        if(id == null) {
            request.setAttribute("error", "删除错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
        }
        // 连接数据库删除文章
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("delete from t_article where id = ?");
            pstmt.setInt(1, Integer.parseInt(id));
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }

        // 如果正确forward到ArticleSearchServlet（这里不能直接forward到article_list.jsp，因为这样页面中不会有数据）
        request.getRequestDispatcher("/backend/ArticleSearchServlet").forward(request, response);
    }
}

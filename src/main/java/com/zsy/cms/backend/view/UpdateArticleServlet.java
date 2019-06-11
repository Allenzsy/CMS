package com.zsy.cms.backend.view;

import com.zsy.cms.backend.model.Article;
import com.zsy.cms.utils.DBUtil;

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

        request.setCharacterEncoding("UTF-8");
        // 根据提交内容，获取信息
        String id = request.getParameter("id");
        if(id == null) {
            request.setAttribute("error", "删除错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        // 根据id，通过update等数据库操作更新数据库。
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("update t_article set title=?, content=?, source=?, updatetime=? where id = ?");
            pstmt.setString(1, request.getParameter("title"));
            pstmt.setString(2, request.getParameter("content"));
            pstmt.setString(3, request.getParameter("source"));
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(5, Integer.parseInt(id));

            pstmt.executeUpdate();
            conn.commit();

        }catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }

        // 更新成功forward到更新成功页面
        request.getRequestDispatcher("/backend/article/update_article_success.jsp").forward(request, response);
    }

}

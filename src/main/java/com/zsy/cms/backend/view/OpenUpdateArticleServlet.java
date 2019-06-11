package com.zsy.cms.backend.view;

import com.zsy.cms.backend.model.Article;
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
import java.text.SimpleDateFormat;

@WebServlet("/backend/OpenUpdateArticleServlet")
public class OpenUpdateArticleServlet extends HttpServlet {
    // 由于是点击编辑到 OpenUpdateArticleServlet 不需要提交表单，所以只实现doGet方法。
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 拿到文章id值
        String id = request.getParameter("id");
        if(id == null) {
            request.setAttribute("error", "删除错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
        // 进行数据库查询，并通过setAttribute放到request中
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Article a = null;
        try {
            pstmt = conn.prepareStatement("select * from t_article where id=?");
            pstmt.setInt(1,Integer.parseInt(id));
            rs = pstmt.executeQuery();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            if(!rs.next()) {
                request.setAttribute("error", "不存在为"+id+"的文章，无法编辑");
                request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
                return;
            }
            a = new Article();
            a.setId(rs.getInt("id"));
            a.setTitle(rs.getString("title"));
            a.setContent(rs.getString("content"));
            a.setSource(rs.getString("source"));
            a.setCreateTime(rs.getTimestamp("createtime"));
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }
        // forward 到更新的jsp页面
        request.setAttribute("updateArticle", a);
        request.getRequestDispatcher("/backend/article/update_article.jsp").forward(request, response);
    }
}

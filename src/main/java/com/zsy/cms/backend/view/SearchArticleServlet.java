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

        int offset = 0;
        int pageSize = 5;
        // 希望从requset中获取offset， 从session中获取pageSize，session中如果没有那么则设置缺省值
        try {
            offset = Integer.parseInt(req.getParameter("offset"));
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

        // 查寻数据库
        String sql = "select * from t_article limit ?,?";
        String sqlForTotal = "select count(*) from t_article";
        if(req.getParameter("title") != null) {
            sql = "select * from t_article where title like '%"+req.getParameter("title")+"%' limit ?,?";;
            sqlForTotal = "select count(*) from t_article where title like '%"+req.getParameter("title")+"%'";
        }
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PreparedStatement pstmtForTotal = null;
        ResultSet rsForTotal = null;
        int total = 0;
        try {
            pstmtForTotal = conn.prepareStatement(sqlForTotal);
            rsForTotal = pstmtForTotal.executeQuery();
            if(rsForTotal.next()) {
                total = rsForTotal.getInt(1);
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
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
            DBUtil.close(rsForTotal);
            DBUtil.close(pstmtForTotal);
            DBUtil.close(conn);
        }
        // 将查询到的文章传递给jsp
        req.setAttribute("articles", list);
        // 将总记录数传递给jsp
        req.setAttribute("total", total);
        // 当前是第几页
        int currentPage = offset / pageSize + 1;
        req.setAttribute("currentPage", currentPage);
        // 一共有多少页
        int maxPage = total / pageSize;
        if((total % pageSize) > 0) {
            maxPage++;
        }
        req.setAttribute("maxPage", maxPage);

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
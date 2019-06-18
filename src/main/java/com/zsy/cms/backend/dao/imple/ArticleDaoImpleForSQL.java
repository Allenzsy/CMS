package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.ArticleDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.utils.DBUtil;
import com.zsy.cms.utils.PageVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ArticleDaoImpleForSQL implements ArticleDao {
    @Override
    public void addArticle(Article a) {
        // 插入数据库
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("insert into t_article(title,content,source,createtime) values(?,?,?,?)");
            pstmt.setString(1, a.getTitle());
            pstmt.setString(2, a.getContent());
            pstmt.setString(3, a.getSource());
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
    }

    @Override
    public void delArticle(String[] ids) {
        // 连接数据库删除文章
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            for(String id : ids) {
                pstmt = conn.prepareStatement("delete from t_article where id = ?");
                pstmt.setInt(1, Integer.parseInt(id));
                pstmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }
    }

    @Override
    public PageVO<Article> searchArticle(int offset, int pageSize, String title) {

        // 将结果存在一个容器中。由于显示文章可能有多个属性，如文章标题，文章内容（部分）， 日期，访问量...
        // 所以，这里应该自己设计一个类，用来完成每条数据存储。
        ArrayList<Article> list = new ArrayList<>();

        // 查寻数据库
        String sql = "select * from t_article limit ?,?";
        String sqlForTotal = "select count(*) from t_article";
        if(title != null) {
            sql = "select * from t_article where title like '%"+title+"%' limit ?,?";;
            sqlForTotal = "select count(*) from t_article where title like '%"+title+"%'";
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

        PageVO<Article> pv = new PageVO();
        pv.setTotal(total);
        pv.setDatas(list);

        return pv;
    }

    @Override
    public Article findArticleById(String id, HttpServletRequest request, HttpServletResponse response) {

        // 进行数据库查询，并通过setAttribute放到request中
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Article a = null;
        try {
            pstmt = conn.prepareStatement("select * from t_article where id=?");
            pstmt.setInt(1,Integer.parseInt(id));
            rs = pstmt.executeQuery();
            if(!rs.next()) {
                request.setAttribute("error", "不存在为"+id+"的文章，无法编辑");
                try {
                    request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
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

        return a;
    }

    @Override
    public void updateArticle(Article a) {
        // 根据id，通过update等数据库操作更新数据库。
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("update t_article set title=?, content=?, source=?, updatetime=? where id = ?");
            pstmt.setString(1, a.getTitle());
            pstmt.setString(2, a.getContent());
            pstmt.setString(3, a.getSource());
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(5, a.getId());

            pstmt.executeUpdate();
            conn.commit();

        }catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }
    }
}

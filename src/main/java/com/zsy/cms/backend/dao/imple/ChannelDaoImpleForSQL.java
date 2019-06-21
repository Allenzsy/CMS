package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.DBUtil;
import com.zsy.cms.utils.PageVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChannelDaoImpleForSQL implements ChannelDao {
    @Override
    public void addChannel(Channel c) {
        // 在数据库中插入数据
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("insert into t_channel(name, description) values(?,?)");
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getDescription());

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
    public void delChannel(String[] ids) {
        // 连接数据库删除文章
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            for(String id : ids) {
                pstmt = conn.prepareStatement("delete from t_channel where id = ?");
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
    public PageVO<Channel> searchChannel(int offset, int pageSize, String name) {

        // 将结果存在一个容器中。由于显示文章可能有多个属性，如文章标题，文章内容（部分）， 日期，访问量...
        // 所以，这里应该自己设计一个类，用来完成每条数据存储。
        ArrayList<Channel> list = new ArrayList<>();

        // 查寻数据库
        String sql = "select * from t_channel limit ?,?";
        String sqlForTotal = "select count(*) from t_channel";
        if(name != null) {
            sql = "select * from t_channel where name like '%"+name+"%' limit ?,?";;
            sqlForTotal = "select count(*) from t_channel where name like '%"+name+"%'";
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
                Channel c = new Channel();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                list.add(c);
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

        PageVO<Channel> pv = new PageVO();
        pv.setTotal(total);
        pv.setDatas(list);

        return pv;
    }

    @Override
    public Channel findChannelById(String id, HttpServletRequest request, HttpServletResponse response) {

        // 通过 id 在数据库中查询相应数据， 并包装成channel对象
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Channel c = null;
        try {
            pstmt = conn.prepareStatement("select * from t_channel where id=?");
            pstmt.setInt(1,Integer.parseInt(id));
            rs = pstmt.executeQuery();
            if(!rs.next()) {
                request.setAttribute("error", "不存在id为"+id+"的频道，无法编辑");
                try {
                    request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            c = new Channel();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            c.setDescription(rs.getString("description"));
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }

        return c;
    }

    @Override
    public void updateChannel(Channel channel) {
        // 根据id更新相应数据库中的数据
        Connection conn= DBUtil.getConn();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("update t_channel set name=?, description=? where id=?");
            pstmt.setString(1,channel.getName());
            pstmt.setString(2,channel.getDescription());
            pstmt.setInt(3, channel.getId());

            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }
    }
}

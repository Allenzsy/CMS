package com.zsy.cms.backend.view;

import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
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

@WebServlet("/backend/OpenUpdateChannelServlet")
public class OpenUpdateChannelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 通过在channel_list.jsp中点击编辑按钮，通过doGet方法发送频道id到OpenUpdateChannelServlet
        String id = request.getParameter("id");
        if(id == null) {
            request.setAttribute("error", "编辑错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }
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
                request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
                return;
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

        // forward 到 Update_channel.jsp
        request.setAttribute("channel", c);
        request.getRequestDispatcher("/backend/channel/update_channel.jsp").forward(request, response);
    }
}

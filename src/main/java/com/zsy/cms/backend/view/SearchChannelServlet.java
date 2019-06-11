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
import java.util.ArrayList;

@WebServlet("/backend/SearchChannelServlet")
public class SearchChannelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 获取数据库中的channel，并包装成channel对象
        ArrayList<Channel> list = new ArrayList<>();

        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select * from t_channel");
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
            DBUtil.close(conn);
        }
        // 放入容器中，并在request中利用setAtrribute放入request中
        request.setAttribute("channels", list);

        // forward 到 channel_list.jsp
        request.getRequestDispatcher("/backend/channel/channel_list.jsp").forward(request, response);
    }

}

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
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/backend/AddChannelServlet")
public class AddChannelServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        // 在 channel_list.jsp 中，点击添加操作，然后跳转到add_channel.jsp，在其中将内容填好，
        // 点击提交，通过 doPost 方法，转到AddChannelServlet
        // 拿到响应信息
        String name = request.getParameter("channelName");
        String description = request.getParameter("description");
        // 在数据库中插入数据
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("insert into t_channel(name, description) values(?,?)");
            pstmt.setString(1, name);
            pstmt.setString(2,description);

            pstmt.executeUpdate();
            conn.commit();
        }catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }

        // 然后 forward 到添加频道成功页面
        request.getRequestDispatcher("/backend/channel/add_channel_success.jsp").forward(request, response);
    }

}

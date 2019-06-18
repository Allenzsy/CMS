package com.zsy.cms.backend.view;

import com.zsy.cms.utils.DBUtil;
import sun.security.pkcs11.Secmod;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/backend/UpdateChannelServlet")
public class UpdateChannelServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 根据update_channel.jsp中点击提交按钮，通过doPost方式提交到UpdateChannelServlet
        String id = request.getParameter("id");
        String name = request.getParameter("channelName");
        String description = request.getParameter("description");
        // 根据id更新相应数据库中的数据
        Connection conn= DBUtil.getConn();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("update t_channel set name=?, description=? where id=?");
            pstmt.setString(1,name);
            pstmt.setString(2,description);
            pstmt.setInt(3, Integer.parseInt(id));

            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }


        // 更新成功 forward 到update_channel_success.jsp
        request.getRequestDispatcher("/backend/channel/update_channel_success.jsp").forward(request, response);
    }

}

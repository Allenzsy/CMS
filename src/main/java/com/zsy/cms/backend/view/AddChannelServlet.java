package com.zsy.cms.backend.view;

import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.DBUtil;
import com.zsy.cms.utils.PropertiesBeanFactory;

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

        // 在 channel_list.jsp 中，点击添加操作，然后跳转到add_channel.jsp，在其中将内容填好，
        // 点击提交，通过 doPost 方法，转到AddChannelServlet
        // 拿到响应信息
        String name = request.getParameter("channelName");
        String description = request.getParameter("description");

        Channel c = new Channel();
        c.setName(name);
        c.setName(description);

        ChannelDao channelDao = new PropertiesBeanFactory().getChannelDao();
        channelDao.addChannel(c);

        // 然后 forward 到添加频道成功页面
        request.getRequestDispatcher("/backend/channel/add_channel_success.jsp").forward(request, response);
    }

}

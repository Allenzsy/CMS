package com.zsy.cms.backend.view;

import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.DBUtil;
import com.zsy.cms.utils.PropertiesBeanFactory;
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

        Channel c = new Channel();
        c.setName(name);
        c.setDescription(description);
        c.setId(Integer.parseInt(id));

        ChannelDao channelDao = new PropertiesBeanFactory().getChannelDao();
        channelDao.updateChannel(c);


        // 更新成功 forward 到update_channel_success.jsp
        request.getRequestDispatcher("/backend/channel/update_channel_success.jsp").forward(request, response);
    }

}

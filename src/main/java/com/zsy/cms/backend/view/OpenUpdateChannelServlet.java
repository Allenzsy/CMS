package com.zsy.cms.backend.view;

import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Article;
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

        ChannelDao channelDao = new PropertiesBeanFactory().getChannelDao();
        Channel c = channelDao.findChannelById(id, request, response);

        // forward 到 Update_channel.jsp
        request.setAttribute("channel", c);
        request.getRequestDispatcher("/backend/channel/update_channel.jsp").forward(request, response);
    }
}

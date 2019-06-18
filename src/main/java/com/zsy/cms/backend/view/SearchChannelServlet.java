package com.zsy.cms.backend.view;

import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.DBUtil;
import com.zsy.cms.utils.PageVO;
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
import java.util.ArrayList;

@WebServlet("/backend/SearchChannelServlet")
public class SearchChannelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int offset = 0;
        int pageSize = 5;
        // 希望从requset中获取offset， 从session中获取pageSize，session中如果没有那么则设置缺省值
        try {
            offset = Integer.parseInt(request.getParameter("pager.offset"));
        } catch (Exception ignore) { }
        // 如果从request中拿到了pageSize的值，那么需要更新Session中的PageSize的值
        if(request.getParameter("pageSize") != null) {
            request.getSession().setAttribute("pageSize", Integer.parseInt(request.getParameter("pageSize")));
        }
        Integer ps = (Integer) request.getSession().getAttribute("pageSize");
        if(ps == null) {
            request.getSession().setAttribute("pageSize", pageSize);
        } else {
            pageSize = ps;
        }
        String name = request.getParameter("name");


        ChannelDao channelDao = new PropertiesBeanFactory().getChannelDao();
        PageVO<Channel> pv = channelDao.searchChannel(offset, pageSize, name);


        // 放入容器中，并在request中利用setAtrribute放入request中
        request.setAttribute("channels", pv.getDatas());
        request.setAttribute("total", pv.getTotal());

        // forward 到 channel_list.jsp
        request.getRequestDispatcher("/backend/channel/channel_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}

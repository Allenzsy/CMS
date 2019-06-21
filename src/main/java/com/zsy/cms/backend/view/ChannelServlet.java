package com.zsy.cms.backend.view;

import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.PageVO;
import com.zsy.cms.utils.PropertiesBeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/backend/ChannelServlet")
public class ChannelServlet extends BaseServlet {

    ChannelDao channelDao;

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 在 channel_list.jsp 中，点击添加操作，然后跳转到add_channel.jsp，在其中将内容填好，
        // 点击提交，通过 doPost 方法，转到AddChannelServlet
        // 拿到响应信息
        String name = request.getParameter("channelName");
        String description = request.getParameter("description");

        Channel c = new Channel();
        c.setName(name);
        c.setDescription(description);

        channelDao.addChannel(c);

        // 然后 forward 到添加频道成功页面
        request.getRequestDispatcher("/backend/channel/add_channel_success.jsp").forward(request, response);
    }
    
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 拿到唯一标识 这里修改为可以批量删除，拿到所以参数名为id的参数值""
        String[] ids = request.getParameterValues("id");
        if(ids == null) {
            request.setAttribute("error", "删除错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }

        channelDao.delChannel(ids);

        response.sendRedirect(request.getContextPath()+"/backend/ChannelServlet");
        return;
    }
    public void openUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 通过在channel_list.jsp中点击编辑按钮，通过doGet方法发送频道id到OpenUpdateChannelServlet
        String id = request.getParameter("id");
        if(id == null) {
            request.setAttribute("error", "编辑错误，id不能为空");
            request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            return;
        }

        Channel c = channelDao.findChannelById(id, request, response);

        // forward 到 Update_channel.jsp
        request.setAttribute("channel", c);
        request.getRequestDispatcher("/backend/channel/update_channel.jsp").forward(request, response);

    }
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 根据update_channel.jsp中点击提交按钮，通过doPost方式提交到UpdateChannelServlet
        String id = request.getParameter("id");
        String name = request.getParameter("channelName");
        String description = request.getParameter("description");

        Channel c = new Channel();
        c.setName(name);
        c.setDescription(description);
        c.setId(Integer.parseInt(id));

        channelDao.updateChannel(c);

        // 更新成功 forward 到update_channel_success.jsp
        request.getRequestDispatcher("/backend/channel/update_channel_success.jsp").forward(request, response);
    }

    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        PageVO<Channel> pv = channelDao.searchChannel(offset, pageSize, name);


        // 放入容器中，并在request中利用setAtrribute放入request中
        request.setAttribute("channels", pv.getDatas());
        request.setAttribute("total", pv.getTotal());

        // forward 到 channel_list.jsp
        request.getRequestDispatcher("/backend/channel/channel_list.jsp").forward(request, response);
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }
}

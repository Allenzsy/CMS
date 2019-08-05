package com.zsy.cms.backend.dao;

import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.PageVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ChannelDao {
    // 这里应该声明article的数据库操作方法
    // 也就是说这里的方法是为后面写的Servlet服务的，那么输入输出应该符合Servlet的要求
    public void addChannel(Channel c);

    public void delChannel(String[] ids);

    // findArticle需要输入offset pagesize，需要输出total articles的list,输出有两个且类型不同时，应该再创建一个类，
    // 这种类实际上是value object
    public PageVO<Channel> findChannelByName(String name);

    public Channel findChannelById(String id, HttpServletRequest request, HttpServletResponse response);

    public void updateChannel(Channel c);

}

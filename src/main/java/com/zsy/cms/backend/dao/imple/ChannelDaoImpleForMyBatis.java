package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.SystemContext;
import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.MyBatisUtil;
import com.zsy.cms.utils.PageVO;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelDaoImpleForMyBatis extends BaseDao implements ChannelDao {
    @Override
    public void addChannel(Channel c) {
        this.add(c);
    }

    @Override
    public void delChannel(String[] ids) {
        this.del(Channel.class, ids);
    }

    @Override
    public PageVO<Channel> findChannelByName(String name) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        SystemContext.setOffset(0);
        SystemContext.setPageSize(Integer.MAX_VALUE);
        PageVO<Channel> pv = this.findPaginated(Channel.class.getName()+".findChannelByName", params);

        return pv;
    }
    @Override
    public PageVO<Channel> findChannels() {
        Map<String, Object> params = new HashMap<>();
        SystemContext.setOffset(0);
        SystemContext.setPageSize(Integer.MAX_VALUE);
        PageVO<Channel> pv = this.findPaginated(Channel.class.getName()+".findChannelByName", params);
        return pv;
    }

    @Override
    public Channel findChannelById(String id, HttpServletRequest request, HttpServletResponse response) {

        Channel channel = findById(Channel.class, id);

        if(channel == null) {
            request.setAttribute("error", "不存在id为"+id+"的频道，无法编辑");
            try {
                request.getRequestDispatcher("/backend/common/error.jsp").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        return channel;
    }

    @Override
    public void updateChannel(Channel c) {
        this.update(c);
    }
}

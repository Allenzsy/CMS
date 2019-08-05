package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.ChannelDao;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.BeanFactory;
import com.zsy.cms.utils.PageVO;
import com.zsy.cms.utils.PropertiesBeanFactory;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.junit.Assert.*;

public class ChannelDaoImpleForMyBatisTest {

    BeanFactory beanFactory = new PropertiesBeanFactory("factory.properties");
    ChannelDao channelDao = (ChannelDao) beanFactory.getBean("ChannelDao");

    @Test
    public void addChannel() {
        Channel c = new Channel();
        c.setName("javaScrip");
        c.setDescription("这个不用学吧，懂点就可以");
        channelDao.addChannel(c);
        System.out.println("id: "+ c.getId());

    }

    @Test
    public void delChannel() {
        channelDao.delChannel(new String[]{"10"});
    }

    @Test
    public void findChannelByName() {
        PageVO<Channel> pv = channelDao.findChannelByName(null);
        List<Channel> channels = pv.getDatas();
        int size = channels.size();
        for(int i = 0; i < size; i++) {
            System.out.println(channels.get(i).getName());
        }

    }

    @Test
    public void findChannelById() {
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        Channel c = channelDao.findChannelById("2",request,response);
        System.out.println("id: "+c.getId()+",name: "+c.getName());
    }

    @Test
    public void updateChannel() {
        Channel c = new Channel();
        c.setId(2);
        c.setName("JavaEE");
        c.setDescription("好好学javaEE");
        channelDao.updateChannel(c);

    }
}
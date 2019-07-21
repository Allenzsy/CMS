package com.zsy.cms.backend.dao.imple;

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

public class ChannelDaoImpleForMyBatis implements ChannelDao {
    @Override
    public void addChannel(Channel c) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            sqlSession.insert(Channel.class.getName()+".addChannel",c);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void delChannel(String[] ids) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            for(String id : ids) {
                sqlSession.insert(Channel.class.getName()+".delChannel", Integer.parseInt(id));
            }
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public PageVO<Channel> findChannelByName(int offset, int pageSize, String name) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        List<Channel> channels = null;
        int total = 0;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            params.put("offset", offset);
            params.put("pageSize", pageSize);
            channels = sqlSession.selectList(Channel.class.getName()+".findChannelByName", params);
            total = sqlSession.selectOne(Channel.class.getName()+".findChannelByNameForTotal",params);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        PageVO<Channel> pv = new PageVO<>();
        pv.setDatas(channels);
        pv.setTotal(total);
        return pv;
    }

    @Override
    public Channel findChannelById(String id, HttpServletRequest request, HttpServletResponse response) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        Channel channel = null;
        try {
            channel = sqlSession.selectOne(Channel.class.getName()+".findChannelById", Integer.parseInt(id));
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
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

        return channel;
    }

    @Override
    public void updateChannel(Channel c) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            sqlSession.update(Channel.class.getName()+".updateChannel",c);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }
}

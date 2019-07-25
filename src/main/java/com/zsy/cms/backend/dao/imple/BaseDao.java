package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.SystemContext;
import com.zsy.cms.backend.model.Article;
import com.zsy.cms.backend.model.Channel;
import com.zsy.cms.utils.MyBatisUtil;
import com.zsy.cms.utils.PageVO;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDao {

    public void add(Object entity) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            sqlSession.insert(entity.getClass().getName()+".add",entity);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    public void update(Object entity) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            sqlSession.insert(entity.getClass().getName()+".update",entity);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    public void del(Class entityClass, int id) {
        del(entityClass, new int[]{id});
    }

    public void del(Class entityClass, int[] ids) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            for(int id : ids) {
                sqlSession.insert(entityClass.getName()+".del", id);
            }
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    public void del(Class entityClass, String[] ids) {
        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
            for(String id : ids) {
                sqlSession.insert(entityClass.getName()+".del", Integer.parseInt(id));
            }
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    public <T> T findById(Class<T> entityClass, String id) {
        SqlSession sqlSession = MyBatisUtil.getSession();
        T entity = null;
        try {
            entity = sqlSession.selectOne(entityClass.getName()+".findById", Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return entity;
    }

    // 分页较为复杂，那么封装的时候，我么可以规定，由子类去传入 需要用到的查询语句id
    public <E> PageVO<E> findPaginated(String sqlId, Map params) {
        SqlSession sqlSession = MyBatisUtil.getSession();
        params.put("offset", SystemContext.getOffset());
        params.put("pageSize", SystemContext.getPageSize());

        List<E> eList = null;
        int total = 0;
        try {
            eList = sqlSession.selectList(sqlId, params);
            total = sqlSession.selectOne(sqlId+"ForTotal",params);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        PageVO<E> pv = new PageVO<>();
        pv.setDatas(eList);
        pv.setTotal(total);
        return pv;
    }

}

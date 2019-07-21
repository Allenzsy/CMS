package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.AdminDao;
import com.zsy.cms.backend.model.Admin;
import com.zsy.cms.utils.MyBatisUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class AdminDaoImpleForMyBatis implements AdminDao {

    @Override
    public Admin findAdminByUsername(String username) {
        System.out.println("test MyBatis dao imple");

        SqlSession sqlSession = MyBatisUtil.getSession();
        Admin admin = null;
        try {
            admin = (Admin) sqlSession.selectOne(Admin.class.getName()+".findAdminByUsername",username);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return admin;
    }

    @Override
    public void addAdmin(Admin admin) {
        System.out.println("test MyBatis dao imple");

        SqlSession sqlSession = MyBatisUtil.getSession();

        try {
//            Admin a = sqlSession.getMapper(Admin.class);
//            a.add(admin);
            sqlSession.insert(Admin.class.getName()+".add",admin);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

    }
}

package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.AdminDao;
import com.zsy.cms.backend.model.Admin;
import com.zsy.cms.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;


public class AdminDaoImpleForMyBatis extends BaseDao implements AdminDao {

    @Override
    public Admin findAdminByUsername(String username) {

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

        this.add(admin);

    }
}

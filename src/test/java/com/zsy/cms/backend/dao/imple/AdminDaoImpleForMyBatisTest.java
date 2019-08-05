package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.AdminDao;
import com.zsy.cms.backend.model.Admin;
import com.zsy.cms.utils.PropertiesBeanFactory;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class AdminDaoImpleForMyBatisTest {

    static PropertiesBeanFactory daoFactory = new PropertiesBeanFactory("factory.properties");

    @Test
    public void findAdminByUsername() {
        AdminDao adminDao = (AdminDao)daoFactory.getBean("AdminDao");
        Admin admin = adminDao.findAdminByUsername("admin");
        System.out.println(admin.getId()+"  "+admin.getUsername()+"  "+admin.getPassword());
    }

    @Test
    public void addAdmin() {
        AdminDao adminDao = (AdminDao)daoFactory.getBean("AdminDao");

        Admin admin = new Admin();
        admin.setUsername("测试用户名"+ new Random().nextInt(9999));
        admin.setPassword("123");
        adminDao.addAdmin(admin);
    }
}
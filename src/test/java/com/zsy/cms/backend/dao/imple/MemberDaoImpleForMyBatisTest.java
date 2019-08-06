package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.MemberDao;
import com.zsy.cms.backend.model.Member;
import com.zsy.cms.utils.BeanFactory;
import com.zsy.cms.utils.PropertiesBeanFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemberDaoImpleForMyBatisTest {

    BeanFactory beanFactory = new PropertiesBeanFactory();
    MemberDao memberDao = (MemberDao) beanFactory.getBean("MemberDao");

    @Test
    public void findMemberByNickName() {
        Member member = memberDao.findMemberByNickName("zsy");
        System.out.println("nickName:"+member.getNickName()+"password:"+member.getPassword());
    }

    @Test
    public void add() {
    }
}
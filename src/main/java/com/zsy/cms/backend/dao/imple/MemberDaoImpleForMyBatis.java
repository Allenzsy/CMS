package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.MemberDao;
import com.zsy.cms.backend.model.Member;
import com.zsy.cms.utils.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class MemberDaoImpleForMyBatis extends BaseDao implements MemberDao {

    @Override
    public Member findMemberByNickName(String nickName) {
        SqlSession sqlSession = MyBatisUtil.getSession();
        Member member = null;
        try {
            member = (Member) sqlSession.selectOne(Member.class.getName()+".findMemberByNickName",nickName);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return member;
    }

    @Override
    public Member findMemberById(int id) {
        return this.findById(Member.class, String.valueOf(id));
    }

    @Override
    public void updatePassword(Member member) {
        SqlSession sqlSession = MyBatisUtil.getSession();
        try {
            sqlSession.update(Member.class.getName()+".updatePassword",member);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

    public void addMember(Member member) {
        //首先查询数据库，昵称是否已经存在！
        Member m = findMemberByNickName(member.getNickName());
        if(m != null){
            throw new RuntimeException("您注册的用户昵称已经存在，请换一个再试");
        }
        this.add(member);
    }

    public void updateMember(Member member) {
        this.update(member);
    }
}

package com.zsy.cms.backend.dao;

import com.zsy.cms.backend.model.Member;

public interface MemberDao {
    public Member findMemberByNickName(String nickName);
    public Member findMemberById(int id);
    public void addMember(Member member);
    public void updateMember(Member member);
    public void updatePassword(Member member);
}

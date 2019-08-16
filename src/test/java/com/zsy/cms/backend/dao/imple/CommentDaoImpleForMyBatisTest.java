package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.CommentDao;
import com.zsy.cms.backend.model.Comment;
import com.zsy.cms.utils.BeanFactory;
import com.zsy.cms.utils.PropertiesBeanFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentDaoImpleForMyBatisTest {

    BeanFactory beanFactory = new PropertiesBeanFactory();
    CommentDao commentDao = (CommentDao) beanFactory.getBean("CommentDao");

    @Test
    public void addComment() {

        Comment c = new Comment();
        c.setArticleId(3);
        c.setMemberId(0);
        c.setName("匿名网友");
        c.setContent("奥德赛刚搜的结果");

        commentDao.addComment(c);

    }

    @Test
    public void delComments() {
    }

    @Test
    public void findAllComments() {
    }

    @Test
    public void findCommentById() {
    }

    @Test
    public void findCommentsByArticleId() {
    }

    @Test
    public void updateComment() {
    }
}
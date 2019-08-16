package com.zsy.cms.backend.dao;

import java.util.List;

import com.zsy.cms.backend.model.Comment;
import com.zsy.cms.utils.PageVO;

public interface CommentDao {
	public void addComment(Comment c);
	public void updateComment(Comment c);
	public void delComments(String[] ids);
	public Comment findCommentById(int id);
	public List findCommentsByArticleId(int articleId);
	public PageVO findAllComments();
}

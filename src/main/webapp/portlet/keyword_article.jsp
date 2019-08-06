<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="/backend/common/taglib.jsp"%>
<%--相关文章--%>
<div class="right" >
	<div class="right_topic_1">
		相关文章
	</div>
	<div class="right_topic_2">
		<a href="#"><img src="images/more_red.gif" style="float:right;margin-top:10px;border:0px"></a>
	</div>
	<div class="right_topic_3">
		<c:forEach items="${articleByKeyword}" var="a">
			· <a href="article.jsp?articleId=${a.id}">${a.title}</a> <br/>
		</c:forEach>
	</div>
</div>

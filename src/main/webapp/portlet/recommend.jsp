<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/backend/common/taglib.jsp"%>
<%--推荐阅读--%>
<div class="right" style="height:200px">
	<div class="right_topic_1">
		推荐阅读
	</div>
	<div class="right_topic_2">
		<a href="#"><img src="images/more_red.gif" style="float:right;margin-top:10px;border:0px"></a>
	</div>
	<div class="right_topic_3">
		<c:forEach items="${recommend}" var="a">
			· <a href="article.jsp?articleId=${a.id}">${a.title}</a> <br/>
		</c:forEach>
	</div>
</div>

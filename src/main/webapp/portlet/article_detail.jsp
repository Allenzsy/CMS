<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="/backend/common/taglib.jsp"%>
<!-- 文章标题及简介 -->
<div class="h2title">${article.title}</div>
<div CLASS="atime">
作者：${article.author}
| 来源：${article.source}
| <fmt:formatDate value="${article.createTime}" pattern="yyyy-MM-dd"/>
</div>
<p>
	${article.content}
</p>

<div>
	<jsp:include page="/CommentServlet?method=comments"></jsp:include>
</div>

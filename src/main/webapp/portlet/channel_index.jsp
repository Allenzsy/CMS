<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/backend/common/taglib.jsp"%>
<!-- 首页左边 -->
<div id="left" style="text-align:left">
	首页 -&gt; ${channel.name}
	<hr>
	<c:forEach items="${articles}" var="a">
		<div class="h2title">${a.title}</div>
		<div CLASS="atime">
			作者：${a.author}
			| 来源：${a.source}
			| <fmt:formatDate value="${a.createTime}" pattern="yyyy-MM-dd"/>
		</div>
		${a.intro}
		<br/>
		<a href="article.jsp?articleId=${a.id}&channelId=${param.channelId}">【阅读全文】</a>
	</c:forEach>

	<jsp:include page="/backend/common/pager.jsp">
		<jsp:param name="url" value="channel.jsp"/>
		<jsp:param name="params" value="channelId"/>
	</jsp:include>

</div>
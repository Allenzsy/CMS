<%@ taglib prefix="fms" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/backend/common/taglib.jsp"%>
<div class="index_topic">
	<img src="images/jiantou.gif" style="float:left">
	<span style="margin-top:8px;float:left;FONT-WEIGHT: bold; FONT-SIZE: 12px; COLOR: #852b2b; FONT-FAMILY: 宋体;">${channel.name}</span>
	<a href="#"><img src="images/more_gray.gif" style="float:right;border:0px"></a>
	<c:forEach items="${articles}" var="a">
		<div class="index_topic_list">
			<img src="images/h_article.gif" >
			<a href="#">${a.title}</a>
			<span>[<fms:formatDate value="${a.createTime}" pattern="yyyy-MM-dd"/>] </span>
		</div>
	</c:forEach>
</div>
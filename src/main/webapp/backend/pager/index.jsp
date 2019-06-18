<%--
  Created by IntelliJ IDEA.
  User: allenzsy
  Date: 2019/6/16
  Time: 22:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ include file="/backend/common/taglib.jsp"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!--导出参数的概念，pg:pager中的当前页码也叫pageNumber， 所以可以将导出参数重命名-->
<%--maxPageItems= 是每页显示多少行文章" maxIndexPages= 就是下面的数字索引每次显示多少个页的索引--%>
<pg:pager items="1001" maxPageItems="20" maxIndexPages="5" export="currentPageNumber=pageNumber"> <!-- items代表总记录数，他会自动帮你分页 -->
    <pg:first> <!--算出首页，会把算出来的地址放在page context的里面，并且起一个名称叫pageUrl-->
        <a href="${pageUrl}">首页</a>
    </pg:first>
    <pg:prev>
        <a href="${pageUrl}">上页</a>
    </pg:prev>
    <pg:pages>
        <c:if test="${currentPageNumber == pageNumber}">
            <span style="color: red">${pageNumber}</span>
        </c:if>
        <c:if test="${currentPageNumber != pageNumber}">
            <a href="${pageUrl}">${pageNumber}</a> <!--页数会放在pageNumber中-->
        </c:if>

    </pg:pages>
    <pg:next>
        <a href="${pageUrl}">下页</a>
    </pg:next>
    <pg:last>
        <a href="${pageUrl}">尾页</a>
    </pg:last>

</pg:pager>
</body>
</html>

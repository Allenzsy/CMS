<%--
  Created by IntelliJ IDEA.
  User: allenzsy
  Date: 2019/6/17
  Time: 23:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/backend/common/taglib.jsp"%>

<script type="text/javascript">
    function selectPagesize(field) {
    window.location = document.getElementById("firstPage").href + "$pageSize="+field.value;
    // 这里进行更加通用化处理，希望代码用在其他地方能够尽可能修改的少一些，利用page-taglib生成的url
    }
</script>

<!--这里的url如果想要复用，那么就不能写死-->
<pg:pager url="${param.url}" items="${total}" maxPageItems="${pageSize}" maxIndexPages="11" export="currentPageNumber=pageNumber">
    <!-- items代表总记录数，他会自动帮你分页 -->
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="33%">
                <div align="left">
                        <span class="STYLE22">&nbsp;&nbsp;&nbsp;&nbsp;共有<strong>${total}</strong> 条记录，
                            当前第<strong>${currentPageNumber}</strong> 页，
                            共 <strong><pg:last>${pageNumber}</pg:last></strong> 页
                        </span>
                </div>
            </td>
            <td width="67%" align=right vAlign="center" noWrap>

                <!--导出参数的概念，pg:pager中的当前页码也叫pageNumber， 所以可以将导出参数重命名-->
                    <%--maxPageItems= 是每页显示多少行文章" maxIndexPages= 就是下面的数字索引每次显示多少个页的索引
                        url="" 就是每次生成的url的根
                        --%>
                <!--会产生附加参数，name是附加参数名称，会自动从pagecontext中找，有就附加，没有就不附加-->
                <c:forEach items="${param.params}" var="p">
                    <pg:param name="${p}"/>
                </c:forEach>
                <pg:first> <!--算出首页，会把算出来的地址放在page context的里面，并且起一个名称叫pageUrl-->
                    <a id="firstPage" href="${pageUrl}">首页</a>
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

                <select name="pageSize" onchange="selectPagesize(this)">
                    <c:forEach begin="5" end="50" step="5" var="i">
                        <option value="${i}" <c:if test="${pageSize == i}">selected</c:if>> ${i}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
    </table>
</pg:pager>

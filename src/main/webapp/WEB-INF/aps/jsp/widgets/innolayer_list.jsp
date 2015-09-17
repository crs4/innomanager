<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="vmr" uri="/innomanager-core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="worklayerDetailActionPage">detail</c:set>
<%--
optional CSS
<wp:headInfo type="CSS" info="widgets/innolayer_list.css" />
--%>
<vmr:innolayers listName="innolayers" />
 
<section class="innolayers">
<h1><wp:i18n key="innomanager_LAYER_TITLE_VIEW" /></h1>
<div id="main">
<p><wp:i18n key="innomanager_LAYER_LIST_SUMMARY" /></p>
        
<c:choose>
    <c:when test="${!(empty innolayers)}">

    <!-- pager -->
        <wp:pager listName="innolayers" objectName="groupLayer" pagerIdFromFrame="true" max="10" advanced="true" offset="5" >
            <c:set var="group" value="${groupLayer}" scope="request" />
            <c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
            <strong><wp:i18n key="innomanager_LAYER_NUMBER" /> : <c:out value="${groupLayer.size}" /></strong>
        <!-- output data //start -->
            <table id="innolayer-list" class="table table-bordered table-condensed" summary="<wp:i18n key="innomanager_LAYER_LIST_SUMMARY" />">
            <thead><tr>
                <th><wp:i18n key="innomanager_LAYER_NAME" /></th>
                <th><wp:i18n key="innomanager_LAYER_BBOX" /></th>
                <th><wp:i18n key="innomanager_LAYER_TYPE" /></th>
            </tr>
            </thead>
            <tbody> 
            <c:forEach var="layer" items="${innolayers}" begin="${groupLayer.begin}" end="${groupLayer.end}">
            <tr>
                <td style="text-align: center;">
                    <p class="btn-group">
                    <a class="btn btn-primary" href="<wp:url page="${worklayerDetailActionPage}" >
                        <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
                        </wp:url>"><span class="icon icon-info-sign icon-white"></span> <c:out value="${layer.name}" />
                    </a>
                    </p>
                </td>
                <td><c:out value="${layer.bboxText}" /></td>
                <td><c:out value="${layer.type}" /></td>
        </tr>
            </c:forEach>
            </tbody></table>
        <!-- output data //end -->
        <!-- pager -->
            <c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
        </wp:pager>
         <script>
                //page
                var urlpagedetails = "<wp:url page="${worklayerDetailActionPage}" />" ;
        </script>
    </c:when>
    <c:otherwise><wp:i18n key="innomanager_LAYER_LIST_EMPTY" /></c:otherwise>
</c:choose>
</section>

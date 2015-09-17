<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="vmr" uri="/innomanager-core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="worklayerEditActionPage">workdetails</c:set>
<c:set var="worklayerElaborateActionPage">workelaborate</c:set>
<%--
optional CSS
<wp:headInfo type="CSS" info="widgets/worklayer_list.css" />
<%@ taglib uri="/innomanager-core" prefix="vmr" %>--%>

<vmr:worklayers listName="worklayers" />


<section class="worklayers_new">
<p>
	<a class="btn btn-info" href="<wp:url page="${worklayerEditActionPage}" />">
           <span class="icon icon-plus icon-white"></span> <wp:i18n key="innomanager_NEW_WORKLAYER" />
	</a>
</p>


<h1><wp:i18n key="innomanager_TITLE_WORKLAYER" /></h1>
<div id="main">
    <h2><wp:i18n key="innomanager_TITLE_LIST_WORKLAYER" /></h2>
<section class="worklayers_list">



<c:choose>
    <c:when test="${!(empty worklayers)}">
        <c:if test="${worklayers != null}">
    <!-- pager -->
        <wp:pager listName="worklayers" objectName="groupLayer" pagerIdFromFrame="true" max="10" advanced="true" offset="5" >
            <c:set var="group" value="${groupLayer}" scope="request" />
            <c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
            <h2><wp:i18n key="innomanager_WORKLAYER_NUMBER" /> : <c:out value="${groupLayer.size}" /></h2>
        <!-- output data //start -->
        <!-- search block //start -->    
<div class="panel-heading" >
             <a id="aria-menu-info" class="display-block btn collapsed" role="menuitem" 
                 style="font-weight: bold;" aria-haspopup="true" href="#tile" 
                 data-toggle="collapse">
             <span class="icon icon-search icon-white"></span> 
             <wp:i18n key="innomanager_SEARCH_WORKLAYERS" />
             <span class="icon icon-chevron-down icon-white" style="float: right;"></span></a>
        </div>
        <div class="collapse" id="tile" >
            <p><form id="searchForm" method="get" action="<wp:url/>" class="form-search">
<input type="text" id="name" name="name" value="<c:out value="${name}" />" placeholder="<wp:i18n key="innomanager_WORKLAYER_NAME" />" class="input-medium search-query" />
<input type="submit" class="btn btn-primary" value="<wp:i18n key="SEARCH" />" />
</form></p>
        </div> 
<!-- search block //end -->
            <table class="table-striped table table-bordered table-condensed" summary="<wp:i18n key="innomanager_WORKLAYERS_LIST_SUMMARY" />">
            <tr>
                <th><wp:i18n key="innomanager_WORKLAYER_NAME" /></th>
                <th class="span3"><wp:i18n key="innomanager_WORKLAYER_OPERATOR" /></th>
                <th class="span3"><wp:i18n key="innomanager_WORKLAYER_STATUS" /></th>
                <th class="span3"><wp:i18n key="innomanager_WORKLAYER_LAST_UPDATE" /></th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach var="layer" items="${worklayers}" begin="${groupLayer.begin}" end="${groupLayer.end}">
            <tr>
                <td>
                <p class="btn-group">
                        <a class="btn btn-primary" href="<wp:url page="${worklayerEditActionPage}"><wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
                        </wp:url>">
                        <span class="icon icon-edit icon-white"></span> <c:out value="${layer.name}" /> </a>
                </td>
                </p>
                <td><c:out value="${layer.operator}" /></td>
                <td><wp:i18n key="${layer.statusText}" /></td>
                <td><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${layer.lastUpdate}" /></td>
                <td>
                 <p class="btn-group">
                    <a class="btn btn-primary" href="<wp:url page="${worklayerElaborateActionPage}" >
                        <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
                        </wp:url>"><span class="icon icon-wrench icon-white"></span> <wp:i18n key="innomanager_WORKLAYER_ACTION" />
                    </a>
                    </p>
                </td><td>    
                    <p class="btn-group">
                    <a class="btn btn-danger" href="<wp:url page="${worklayerElaborateActionPage}" >
                        <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
                        <wp:parameter name="strutAction" >deleteConfirm</wp:parameter>
                        </wp:url>"><span class="icon icon-trash icon-white"></span> <wp:i18n key="innomanager_WORKLAYER_DELETE" />
                    </a>
                    </p>
                </td>
            </tr>
            </c:forEach>
            </table>
        <!-- output data //end -->
        <!-- pager -->
            <c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/pagerBlock.jsp" />
        </wp:pager>
        </c:if>
    </c:when>
        <c:otherwise><h4><wp:i18n key="innomanager_WORKLAYER_LIST_EMPTY" /></h4></c:otherwise>
</c:choose>
</section>
</div>



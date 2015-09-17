<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/aps-core" prefix="wp" %>
<c:set var="worklayerListPage">workarea</c:set>
<p>
	<a class="btn btn-info" href="<wp:url page="${worklayerListPage}"/>">
           <span class="icon icon-arrow-left icon-white"></span><wp:i18n key="innomanager_LABEL_BACK_TO_LIST" />
	</a>
</p>
<h1><wp:i18n key="innomanager_TITLE_WORKLAYER" /></h1>
<div class="alert alert-success">
    <wp:i18n key="innomanager_LABEL_WORK_LAYER_SAVED" />: <c:out value="${name}"/> 
</div>
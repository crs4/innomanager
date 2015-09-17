<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="worklayerListPage">workarea</c:set>
<p>
	<a class="btn btn-info" href="<wp:url page="${worklayerListPage}"/>">
           <span class="icon icon-arrow-left icon-white"></span><wp:i18n key="innomanager_LABEL_WORK_LAYER_LIST" />
	</a>
</p>

<h1><wp:i18n key="innomanager_TITLE_WORKLAYER" /></h1>
<div class="alert alert-block">
	<p><strong><wp:i18n key="innomanager_USER_NOT_ALLOWED" /></strong></p>
</div>



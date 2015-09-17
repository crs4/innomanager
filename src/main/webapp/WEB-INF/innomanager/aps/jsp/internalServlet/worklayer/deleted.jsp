<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<c:set var="worklayerListPage">workarea</c:set>
<div class="span12">
<p>
	<a class="btn btn-info" href="<wp:url page="${worklayerListPage}"/>">
           <span class="icon icon-arrow-left icon-white"></span><wp:i18n key="innomanager_LABEL_BACK_TO_LIST" />
	</a>
</p>

<h1><wp:i18n key="innomanager_TITLE_WORKLAYER" /></h1>
<p class="text-center">
    
    <s:if test="layer == null">    
        <div class="alert alert-block">
            <wp:i18n key="innomanager_WORK_LAYER_DELETED" /> 
        </div>
    </s:if>
</p>    

</div>
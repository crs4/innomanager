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
<div class="alert alert-warning">
<p class="text-center">     
    <s:if test="layer != null"> 
        
        <wp:i18n key="innomanager_WORKLAYER_DELETE_CONFIRM" /> <c:out value="${layer.name}"/> 
        <a class="btn btn-danger" href="<wp:url page="${worklayerElaborateActionPage}" >
            <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
            <wp:parameter name="strutAction" >delete</wp:parameter>
            </wp:url>"><span class="icon icon-trash icon-white"></span> <wp:i18n key="innomanager_WORKLAYER_DELETE" />
        </a>
        
    </s:if>
    <s:else>
        <wp:i18n key="innomanager_WORK_LAYER_NOT_PRESENT" />: <c:out value="${name}"/> 
    </s:else>
</p>
</div>                
</div>

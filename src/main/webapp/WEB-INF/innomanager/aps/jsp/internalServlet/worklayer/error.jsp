<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="worklayerListPage">workarea</c:set>
<div class="span12">
<p>
	<a class="btn btn-info" href="<wp:url page="${worklayerListPage}"/>">
           <span class="icon icon-arrow-left icon-white"></span><wp:i18n key="innomanager_LABEL_BACK_TO_LIST" />
	</a>
</p>
<h1><wp:i18n key="innomanager_TITLE_WORKLAYER" /></h1>
<div class="alert alert-block">
	<div class="alert alert-block">
            <p><strong><wp:i18n key="innomanager_WORKLAYER_ERRORS" /></strong></p>
            <s:if test="hasActionErrors()">
            <ul class="unstyled">
                <s:iterator value="actionErrors">
                    <s:iterator value="value">
                        <li><s:property escape="false" /></li>
                    </s:iterator>
                </s:iterator>
            </ul>
            </s:if>
            <s:if test="hasActionMessages()">
            <ul class="unstyled">
                <s:iterator value="actionMessages">
                    <s:iterator value="value">
                        <li><s:property escape="false" /></li>
                    </s:iterator>
                </s:iterator>
            </ul>
            </s:if>
        </div>
</div>
</div>
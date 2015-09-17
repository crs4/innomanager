<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<c:set var="worklayerListPage">workarea</c:set>

<div class="span12">
    <section><p>
        <a class="btn btn-info" href="<wp:url page="${worklayerListPage}" />">
            <span class="icon icon-arrow-left icon-white"></span> <wp:i18n key="innomanager_LABEL_BACK_TO_LIST" />
        </a>
    </p></section>      

            
<c:set var="valueSubmit"><wp:i18n key="innomanager_WORKLAYER_SAVE" /></c:set>
    <h1><wp:i18n key="innomanager_TITLE_WORKLAYER" /></h1>
    <div id="main">
<s:if test="layer == null">
        <h2><wp:i18n key="innomanager_NEW_WORKLAYER" /></h2>
</s:if>
<s:else>
        <h2><wp:i18n key="innomanager_EDIT_WORKLAYER" /></h2>	
</s:else>
        <section class="worklayer">
        <form class="form-horizontal" action="<wp:action path="/ExtStr2/do/Frontend/WorkLayer/save.action" />" method="post">
            <s:if test="hasActionErrors()">
            <div class="alert alert-block">
                <p><strong><wp:i18n key="innomanager_WORKLAYER_ERRORS" /></strong></p>
                <ul class="unstyled">
                    <s:iterator value="actionErrors">
                        <s:iterator value="value">
                            <li><s:property escape="false" /></li>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
            </s:if>
            <div class="panel panel-primary">
                 <div class="panel-title">
            <h3 class="panel-heading"><wp:i18n key="innomanager_WORKLAYER_NAME"/></h3>
            </div>
            <div class="panel-body">
                <div class="panel-body">
                <s:if test="layer != null">
                    <wpsf:hidden id="name"
                            name="name"
                            value="%{name}" />
                    <strong><c:out value="${name}"/></strong>
                </s:if>
                <s:else>
                    <wpsf:textfield name="name" id="name" value="" cssClass="text" />
                </s:else>
            </div>
            </div>
            <div class="panel panel-primary">
                 <div class="panel-title">
            <h3 class="panel-heading"><wp:i18n key="innomanager_WORKLAYER_DESCRIPTION"/></h3>
            </div>
            <div class="panel-body">
                 <wpsf:textarea id="description"
                            name="description"
                            value="%{description}"
                            accesskey="" cols="40" rows="3" cssClass="text" />


                     <wpsf:hidden id="strutAction"
                            name="strutAction"
                            value="%{strutAction}" />
            </div>
            </div>
            <div>
            <p class="form-actions">
                <input type="submit" name="entandoaction:save" value="${valueSubmit}" class="btn btn-primary" tabindex="2"/>
            </p>
            </div>
        </form>    
        </section>
    </div>
</div>
    
    
    
    
    

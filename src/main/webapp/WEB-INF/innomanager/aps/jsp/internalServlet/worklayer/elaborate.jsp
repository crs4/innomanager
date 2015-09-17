<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<c:set var="worklayerListPage">workarea</c:set>
<div class="span12">
<section><p>
    <a class="btn btn-primary" href="<wp:url page="${worklayerListPage}" />">
        <span class="icon icon-arrow-left icon-white"></span> <wp:i18n key="innomanager_LABEL_BACK_TO_LIST" />
    </a>
</p></section>      
<h1><wp:i18n key="innomanager_TITLE_WORKLAYER" /></h1>
<div id="main">
<section>
    <div class="panel-body">
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
        <s:if test="hasActionMessages()">
             <div class="alert alert-info">
                 <p><strong><wp:i18n key="innomanager_WORKLAYER_MESSAGES" /></strong></p>
                 <ul class="unstyled">
                     <s:iterator value="actionMessages">
                         <s:iterator value="value">
                             <li><s:property escape="false" /></li>
                         </s:iterator>
                     </s:iterator>
                 </ul>
             </div>
        </s:if>
    </div>
</section>         
<c:choose>       
<c:when test="${layer != null}">        
    <h2><wp:i18n key="innomanager_WORKLAYER_ACTION" /> - <c:out value="${layer.name}" /></h2>
    <section class="worklayer">
        <div class="panel-heading" >
             <a id="aria-menu-info" class="display-block collapsed btn btn-info" role="menuitem" 
                 style="font-weight: bold;" aria-haspopup="true" href="#informazioni" 
                 data-toggle="collapse">
             <span class="icon icon-info-sign icon-white"></span> 
             <wp:i18n key="innomanager_WORKLAYER_INFO" />
             <span class="icon icon-chevron-down icon-white" style="float: right;"></span></a>
        </div>
        <div class="collapse in" id="informazioni">
            <table class="table-striped table table-condensed">
                <tr>
                    <td class="span3"><strong><wp:i18n key="innomanager_WORKLAYER_NAME" /></strong></td>
                    <td><c:out value="${layer.name}"/></td>
                </tr>
                <tr>
                    <td class="span3"><wp:i18n key="innomanager_WORKLAYER_OPERATOR" /></td>
                    <td><c:out value="${layer.operator}"/></td>
                </tr>
                <tr>
                    <td class="span3"><wp:i18n key="innomanager_WORKLAYER_LASTUPDATE" /></td>
                    <td><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${layer.lastUpdate}" /></td>
                </tr>
                <tr>
                    <td class="span3"><wp:i18n key="innomanager_WORKLAYER_DESCRIPTION" /></td>
                    <td><c:out value="${layer.description}"/></td>
                </tr>
                <tr>
                    <td class="span3"><wp:i18n key="innomanager_WORKLAYER_STATUS" /></td>
                    <td>
                        <wp:i18n key="${layer.statusText}" /> <br/>
                    </td>
                </tr>
                
            </table>
        </div>
    </section>                     
                        
    <section> 
        <div class="panel panel-primary">
            <div>
                <c:choose>       
                <c:when test='${layer.status == 0 }'>       
                <h4 class="btn-primary"><span class="icon icon-upload icon-white"></span> <wp:i18n key="innomanager_TITLE_DO_UPLOAD" /> </h4>
                    <form action="<wp:action path="/ExtStr2/do/Frontend/WorkLayer/upload.action" />" enctype="multipart/form-data" method="post">
                    <p class="text-center">
                        <s:file name="zippedShapefile" /> <br/>
                        <input type="submit" name="submit" value="Upload" class="btn btn-primary"/>
                        <input type="hidden" name="name" value="${layer.name}" />
                    </p>
                    </form>
                </c:when> 
                <c:when test='${layer.status == 2}'>       
                    <h4 class="btn-primary"><span class="icon icon-upload icon-white"></span> <wp:i18n key="innomanager_TITLE_FILE_UPLOADED" /></h4>
                    <p class="text-center">
                        <a class="btn btn-danger" href="<wp:url page="${worklayerElaborateActionPage}" >
                            <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
                            <wp:parameter name="strutAction" >import</wp:parameter>
                            </wp:url>"><span class="icon icon-trash icon-white"></span> <wp:i18n key="innomanager_LABEL_IMPORT" /></h4>
                        </a>
                    </p>
                </c:when> 
                <c:when test="${layer.status == 4}">    
                    <h4 class="btn-primary"><span class="icon icon- icon-white"></span> <wp:i18n key="innomanager_TITLE_IMPORTED" /></h4>
                    <p class="text-center">
                            <a class="btn btn-danger" href="<wp:url page="${worklayerElaborateActionPage}" >
                                <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
                                <wp:parameter name="strutAction" >elaborate</wp:parameter>
                                </wp:url>"><span class="icon icon-trash icon-white"></span> <wp:i18n key="innomanager_LABEL_ELABORATE" />
                            </a>
                    </p>
                </c:when>   
                <c:when test='${layer.status == 6}'>  
                    <h4 class="btn-primary"><span class="icon icon-upload icon-white"></span> <wp:i18n key="innomanager_TITLE_READY" /></h4>
                    <p class="text-center">
                        <a class="btn btn-primary" href="<wp:url page="${worklayerElaborateActionPage}" >
                            <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
                            <wp:parameter name="strutAction" >export</wp:parameter>
                            </wp:url>"><span class="icon icon-wrench icon-white"></span> <wp:i18n key="innomanager_LABEL_EXPORT" />
                        </a>
                    </p>
                </c:when>
                <c:when test="${layer.status == 8 }"> 
                    <h4 class="btn-primary"><span class="icon icon-upload icon-white"></span> <wp:i18n key="innomanager_TITLE_COMPLETED" /></h4>
                    <p class="text-center">
                        <a class="btn btn-primary" href="<wp:url page="${worklayerElaborateActionPage}" >
                                <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter>
                                <wp:parameter name="strutAction" >deleteConfirm</wp:parameter>
                                </wp:url>"><span class="icon icon-trash icon-white"></span> <wp:i18n key="innomanager_LABEL_DELETE" />
                        </a>
                    </p>
                </c:when> 
                <c:when test="${layer.status == 1}">
                    <h4 class="btn-info"><span class="icon icon-upload icon-white"></span> 
                        <wp:i18n key="innomanager_TITLE_UPLOADING" />
                    </h4>
                </c:when> 
                <c:when test="${layer.status == 3}"> 
                    <h4 class="btn-info"><span class="icon icon-wrench icon-white"></span> 
                        <wp:i18n key="innomanager_TITLE_IMPORTING" />
                    </h4>
                </c:when> 
                <c:when test="${layer.status == 5}"> 
                    <h4 class="btn-info"><span class="icon icon-wrench icon-white"></span> 
                        <wp:i18n key="innomanager_TITLE_ELABORATING" />
                    </h4>
                </c:when>  
                <c:when test="${layer.status == 7}"> 
                    <h4 class="btn-info"><span class="icon icon-wrench icon-white"></span> 
                        <wp:i18n key="innomanager_TITLE_EXPORTING" />
                    </h4>
                </c:when>  
                <c:when test="${layer.status == 9}"> 
                    <h4 class="btn-info"><span class="icon icon-trash icon-white"></span> 
                         <wp:i18n key="innomanager_TITLE_DELETING" />
                    </h4>
                </c:when>
                <c:otherwise>
                    <h4 class="btn-danger"><span class="icon icon-upload icon-white"></span> 
                        <wp:i18n key="innomanager_WRONG_STATUS_DETECTED" />
                    </h4>
                </c:otherwise>
                </c:choose>            
                </p>
                
            </div>
        </div>     
    </section>
    <section>   
        <div class="panel panel-default" >
        <a id="aria-menu-info" class="display-block collapsed btn btn-info" role="menuitem" 
           style="font-weight: bold;" aria-haspopup="true" href="#log" 
           data-toggle="collapse">
            <span class="icon icon-eye icon-white"></span> 
            <wp:i18n key="innomanager_ACTION_LOG" />
            <span class="icon icon-chevron-down icon-white" style="float: right;"></span>
        </a>
        </div>
            <c:choose>
                <c:when test="${layer.status / 2 == 0 }">   
        <div class="collapse" id="log">
                </c:when>
                <c:otherwise>
        <div class="collapse in" id="log">            
                </c:otherwise>
            </c:choose>
            <textarea rows="5" disabled="disabled" style="width: 100%;overflow-y: scroll;"><c:out value="${layer.log}"/></textarea>
            <p class="text-center">    
                <a class="btn btn-primary" href="<wp:url>
                    <wp:parameter name="name" ><c:out value="${layer.name}" /></wp:parameter> 
                    </wp:url>">
                    <span class="icon icon-plus icon-white"></span> <wp:i18n key="innomanager_WORKLAYER_REFRESH" />
                </a>
            </p>
        </div>
    </section>
                     
    <section><p>
    <a class="btn btn-primary" href="<wp:url page="${worklayerListPage}" />">
        <span class="icon icon-arrow-left icon-white"></span> <wp:i18n key="innomanager_LABEL_BACK_TO_LIST" />
    </a>
    </p></section> 
</c:when>
<c:otherwise><wp:i18n key="innomanager_WORKLAYER_NOT_PRESENT" /></c:otherwise>
</c:choose>       
</div>
</div>
    
    
    
    
    



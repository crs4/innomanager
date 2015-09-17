<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="vmr" uri="/innomanager-core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="layerListPage">couchbase</c:set>
<vmr:innolayer objName="layer" />  

<div class="span12">
    <section><p><a class="btn btn-primary" href="<wp:url page="${layerListPage}" />">
        <span class="icon icon-arrow-left icon-white"></span> <wp:i18n key="innomanager_LABEL_BACK_TO_LIST" />
    </a></p></section>
    
    
    <section>    
    <h1><wp:i18n key="innomanager_INNOLAYER_TITLE_VIEW" /> - </h1>
    <div id="main">
        <h2><wp:i18n key="innomanager_INNOLAYER_TITLE" /> - 
        <c:if test="${layer != null}"><c:out value="${layer.name}"/></c:if></h2>

        <c:choose>       
        <c:when test="${layer != null}">        
        <div class="panel-heading" >
             <a id="aria-menu-info" class="display-block collapsed btn btn-info" role="menuitem" 
                 style="font-weight: bold;" aria-haspopup="true" href="#informazioni" 
                 data-toggle="collapse">
             <span class="icon icon-info-sign icon-white"></span> 
             <wp:i18n key="innomanager_INNOLAYER_DETAIL" />
             <span class="icon icon-chevron-down icon-white" style="float: right;"></span></a>
        </div>
        <div class="collapse" id="informazioni">
            <table class="table-striped table table-condensed">
            <tr>
                <td class="span3"><strong><wp:i18n key="innomanager_LAYER_NAME" /></strong></td>
                <td><c:out value="${layer.name}"/></td>
            </tr>
            <tr>
                <td class="span3"><wp:i18n key="innomanager_LAYER_DESCRIPTION" /></td>
                <td><c:out value="${layer.description}"/></td>
            </tr>
            <tr>
                <td class="span3"><wp:i18n key="innomanager_LAYER_BBOX" /></td>
                <td><c:out value="${layer.bboxText}"/></td>
            </tr>
            <tr>
                <td class="span3"><wp:i18n key="innomanager_LAYER_VERTICES" /></td>
                <td><c:out value="${layer.vertices}"/></td>
            </tr>
            <tr>
                <td class="span3"><wp:i18n key="innomanager_LAYER_COUNT" /></td>
                <td><c:out value="${layer.count}"/></td>
            </tr>
            <tr>
                <td class="span3"><wp:i18n key="innomanager_LAYER_TYPE" /></td>
                <td><c:out value="${layer.type}"/></td>
            </tr>
             <tr>
                <td class="span3"><wp:i18n key="innomanager_LAYER_ATTRIBUTES" /></td>
                <td>
                    <c:choose>
                    <c:when test="${!(empty layer.attributes)}">
                    <table class="table-striped table-bordered table table-condensed">
                        <tr><th><wp:i18n key="innomanager_ATTR_NAME"/></th>
                            <th><wp:i18n key="innomanager_ATTR_TYPE"/></th></tr>
                        <c:forEach var="attr" items="${layer.attributes}">
                        <tr><td><c:out  value="${attr.name}"/></td>
                            <td><c:out  value="${attr.type}"/></td></tr>
                        </c:forEach>
                    </table>
                    </c:when>
                    <c:otherwise><wp:i18n key="INNO_ATTRIBUTES_NOT_PRESENT" /></c:otherwise>
                    </c:choose>

                </td>
            </tr>
            <tr>
                <td class="span3"><wp:i18n key="innomanager_LAYER_LEVELS" /></td>
                <td>
                    <c:choose>
                    <c:when test="${!(empty layer.levels)}">
                       <table class="table-striped table-bordered table table-condensed">
                        <tr><th><wp:i18n key="innomanager_LEVEL_NAME"/></th>
                            <th><wp:i18n key="innomanager_LEVEL_BBOX"/></th></tr>
                        <c:forEach var="level" items="${layer.levels}">
                        <tr><td><c:out value="${level.zoom}"/></td>
                            <td><c:out value="${level.tiles}"/></td></tr>
                        </c:forEach>
                    </table>
                    </c:when>
                    <c:otherwise><wp:i18n key="INNO_TILES_NOT_PRESENT" /></c:otherwise>
                    </c:choose>
                </td>
            </tr>
            </table>    
        </div>
    </div>
    </section>
    
    <section>
    <div class="myContent" id="map" style="padding: 0; margin: 0; width: 100%; height: 512px;"></div>
        <script>
            var layername = "<c:out value="${layer.name}"/>";
            var bbox = <c:out value="${layer.bboxText}"/>;
        </script>
        <script src="<wp:resourceURL />static/js/viewmap.js"></script>
        <script src="http://cdn.leafletjs.com/leaflet-0.7.3/leaflet.js"></script>
    </section>
    <section><p><a class="btn btn-primary" href="<wp:url page="${layerListPage}" />">
        <span class="icon icon-arrow-left icon-white"></span> <wp:i18n key="innomanager_LAYER_BACK_TO_LIST" />
    </a></p></section>
    </c:when>
    <c:otherwise><wp:i18n key="innomanager_LAYER_NOT_PRESENT" /></c:otherwise>
    </c:choose>
    </div>
    <script src="<wp:resourceURL />static/js/querytile.js"></script>
</div>
    	
    
    
    
    



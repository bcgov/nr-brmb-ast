<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

    <table class="codeManagementMain" cellpadding="20" cellspacing="20">
      <tr>
        <td valign="top">
          <div class="yui-navset">
            <ul class="yui-nav">
              <li <c:if test="${tabName == 'generic'}">class="selected"</c:if>><a href="<html:rewrite action="farm265"/>"><em><fmt:message key="Generic.Codes"/></em></a></li>
              <li <c:if test="${tabName == 'municipalities'}">class="selected"</c:if>><a href="<html:rewrite action="farm255"/>"><em><fmt:message key="Municipalities"/></em></a></li>
              <li <c:if test="${tabName == 'inventory'}">class="selected"</c:if>><a href="<html:rewrite action="farm420"/>"><em><fmt:message key="Inventory"/></em></a></li>
              <li <c:if test="${tabName == 'lineItems'}">class="selected"</c:if>><a href="<html:rewrite action="farm280"/>"><em><fmt:message key="Line.Items"/></em></a></li>
              <li <c:if test="${tabName == 'fmvs'}">class="selected"</c:if>><a href="<html:rewrite action="farm286"/>"><em><fmt:message key="FMVs"/></em></a></li>
              <li <c:if test="${tabName == 'bpus'}">class="selected"</c:if>><a href="<html:rewrite action="farm290"/>"><em><fmt:message key="BPUs"/></em></a></li>
              <li <c:if test="${tabName == 'structureGroup'}">class="selected"</c:if>><a href="<html:rewrite action="farm292"/>"><em><fmt:message key="Structure.Group"/></em></a></li>
              <li <c:if test="${tabName == 'sectors'}">class="selected"</c:if>><a href="<html:rewrite action="farm772"/>"><em><fmt:message key="Farm.Types"/></em></a></li>
              <li <c:if test="${tabName == 'cropUnitConversions'}">class="selected"</c:if>><a href="<html:rewrite action="farm288"/>"><em><fmt:message key="Crop.Unit.Conversions"/></em></a></li>
              <li <c:if test="${tabName == 'reasonability'}">class="selected"</c:if>><a href="<html:rewrite action="farm730"/>"><em><fmt:message key="Reasonability"/></em></a></li>
              <li <c:if test="${tabName == 'parameters'}">class="selected"</c:if>><a href="<html:rewrite action="farm750"/>"><em><fmt:message key="Parameters"/></em></a></li>
              <li <c:if test="${tabName == 'documentTemplates'}">class="selected"</c:if>><a href="<html:rewrite action="farm760"/>"><em><fmt:message key="Document.Templates"/></em></a></li>
              <li <c:if test="${tabName == 'users'}">class="selected"</c:if>><a href="<html:rewrite action="farm776"/>"><em><fmt:message key="Users"/></em></a></li>
              <li <c:if test="${tabName == 'mrps'}">class="selected"</c:if>><a href="<html:rewrite action="farm778"/>"><em><fmt:message key="MRP"/></em></a></li>
            </ul>
            <div class="yui-content">
  
              <!-- START subTabBody -->
              <tiles:insert attribute="subTabs">
                <tiles:put name="subTabBody" beanName="subTabBody"/>
              </tiles:insert>
              <!-- END subTabBody -->
  
             </div>
          </div>
        </td>
      </tr>
    </table>

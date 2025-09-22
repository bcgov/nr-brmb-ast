<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<!-- reasonabilitySubTabs.jsp -->
<div class="yui-content">

  <div class="yui-navset">
    <ul class="yui-nav">
      <li <c:if test="${screenNumber == '730' or screenNumber == '735'}">class="selected"</c:if>><a href="<html:rewrite action="farm730"/>"><em><fmt:message key="Fruit.Veg.Codes"/></em></a></li>
      <li <c:if test="${screenNumber == '740' or screenNumber == '745'}">class="selected"</c:if>><a href="<html:rewrite action="farm740"/>"><em><fmt:message key="Expected.Production"/></em></a></li>
      
    </ul>
    <div class="yui-content">
      <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
      <!-- START subTabBody -->
      <tiles:insert attribute="subTabBody"/>
      <!-- END subTabBody -->
    </div>
  </div>
</div>

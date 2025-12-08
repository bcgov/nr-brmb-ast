<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<!-- parametersSubTabs.jsp -->
<div class="yui-content">

  <div class="yui-navset">
    <ul class="yui-nav">
      <li <c:if test="${screenNumber == '750' or screenNumber == '755'}">class="selected"</c:if>><a href="<html:rewrite action="farm750"/>"><em><fmt:message key="General"/></em></a></li>
      <li <c:if test="${screenNumber == '756' or screenNumber == '757'}">class="selected"</c:if>><a href="<html:rewrite action="farm756"/>"><em><fmt:message key="Parameters.by.Year"/></em></a></li>
      
    </ul>
    <div class="yui-content">
      <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
      <!-- START subTabBody -->
      <tiles:insert attribute="subTabBody"/>
      <!-- END subTabBody -->
    </div>
  </div>
</div>

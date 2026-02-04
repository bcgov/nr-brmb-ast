<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<!-- sectorsSubTabs.jsp -->
<div class="yui-content">

  <div class="yui-navset">
    <ul class="yui-nav">
      <li <c:if test="${screenNumber == '772' or screenNumber == '773'}">class="selected"</c:if>><a href="<html:rewrite action="farm772"/>"><em><fmt:message key="Farm.Type"/></em></a></li>
      <li <c:if test="${screenNumber == '774' or screenNumber == '775'}">class="selected"</c:if>><a href="<html:rewrite action="farm774"/>"><em><fmt:message key="Farm.Type.Detailed"/></em></a></li>
    </ul>
    <div class="yui-content">
      <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
      <!-- START subTabBody -->
      <tiles:insert attribute="subTabBody"/>
      <!-- END subTabBody -->
    </div>
  </div>
</div>

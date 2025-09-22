<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<!-- inventorySubTabs.jsp -->
<div class="yui-content">

  <div class="yui-navset">
    <ul class="yui-nav">
      <li <c:if test="${screenNumber == '420' or screenNumber == '425'}">class="selected"</c:if>><a href="<html:rewrite action="farm420"/>"><em><fmt:message key="Inventory.Item.Codes"/></em></a></li>
      <li <c:if test="${screenNumber == '430' or screenNumber == '435'}">class="selected"</c:if>><a href="<html:rewrite action="farm430"/>"><em><fmt:message key="Crops"/></em></a></li>
      <li <c:if test="${screenNumber == '440' or screenNumber == '445'}">class="selected"</c:if>><a href="<html:rewrite action="farm440"/>"><em><fmt:message key="Livestock"/></em></a></li>
      <li <c:if test="${screenNumber == '450' or screenNumber == '455'}">class="selected"</c:if>><a href="<html:rewrite action="farm450"/>"><em><fmt:message key="Inputs"/></em></a></li>
      <li <c:if test="${screenNumber == '460' or screenNumber == '465'}">class="selected"</c:if>><a href="<html:rewrite action="farm460"/>"><em><fmt:message key="Receivables"/></em></a></li>
      <li <c:if test="${screenNumber == '470' or screenNumber == '475'}">class="selected"</c:if>><a href="<html:rewrite action="farm470"/>"><em><fmt:message key="Payables"/></em></a></li>
    </ul>
    <div class="yui-content">
      <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
      <!-- START subTabBody -->
      <tiles:insert attribute="subTabBody"/>
      <!-- END subTabBody -->
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<!-- tipsSubTabs.jsp -->
<div class="yui-content">

  <div class="yui-navset">
    <ul class="yui-nav">
        <li <c:if test="${tabName == 'status'}">class="selected"</c:if>><a href="<html:rewrite action="farm258"/>"><em><fmt:message key="Benefit.Triage.Status"/></em></a></li>
        <li <c:if test="${tabName == 'jobs'  }">class="selected"</c:if>><a href="<html:rewrite action="farm259"/>"><em><fmt:message key="Benefit.Triage.Jobs"/></em></a></li>
    </ul>
    <div class="yui-content">
      <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
      <!-- START subTabBody -->
      <tiles:insert attribute="subTabBody"/>
      <!-- END subTabBody -->
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<!-- tipsSubTabs.jsp -->
<div class="yui-content">

  <div class="yui-navset">
    <ul class="yui-nav">
        <li <c:if test="${tabName == 'tipBenchmarks'}">class="selected"</c:if>><a href="<html:rewrite action="farm546"/>"><em><fmt:message key="Benchmarks"/></em></a></li>
        <li <c:if test="${tabName == 'farmType3'}">class="selected"</c:if>><a href="<html:rewrite action="farm480"/>"><em><fmt:message key="Farm.Type.3"/></em></a></li>
        <li <c:if test="${tabName == 'farmType2'}">class="selected"</c:if>><a href="<html:rewrite action="farm490"/>"><em><fmt:message key="Farm.Type.2"/></em></a></li>
        <li <c:if test="${tabName == 'farmType1'}">class="selected"</c:if>><a href="<html:rewrite action="farm535"/>"><em><fmt:message key="Farm.Type.1"/></em></a></li>
        <li <c:if test="${tabName == 'defaultIncomeRange'}">class="selected"</c:if>><a href="<html:rewrite action="farm555"/>"><em><fmt:message key="Default.Income.Ranges"/></em></a></li>
        <li <c:if test="${tabName == 'lineItems'}">class="selected"</c:if>><a href="<html:rewrite action="farm545"/>"><em><fmt:message key="Line.Items"/></em></a></li>
        <li <c:if test="${tabName == 'tipBenchmarkReports'}">class="selected"</c:if>><a href="<html:rewrite action="farm560"/>"><em><fmt:message key="Benchmark.Reports"/></em></a></li>
    </ul>
    <div class="yui-content">
      <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
      <!-- START subTabBody -->
      <tiles:insert attribute="subTabBody"/>
      <!-- END subTabBody -->
    </div>
  </div>
</div>

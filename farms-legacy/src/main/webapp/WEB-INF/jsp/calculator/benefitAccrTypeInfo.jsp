<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<c:choose>
  <c:when test="${form.accrAdjType eq 'PRODUCTION'}">
  	<h3><fmt:message key="Production.Margin.with.Accrual.Adjustments"/></h3>
  	<p>
	  <fmt:message key="benefit.info.production.margin.accrual.adjustments"/>
	</p>
  </c:when>
  <c:when test="${form.accrAdjType eq 'EXPENSES'}">
  	<h3><fmt:message key="Expenses.with.Accrual.Adjustments"/></h3>
  	<p>
	  <fmt:message key="benefit.info.expenses.accrual.adjustments"/>
	</p>
  </c:when>
</c:choose>
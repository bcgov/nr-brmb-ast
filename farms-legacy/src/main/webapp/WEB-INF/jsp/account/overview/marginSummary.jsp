<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li class="selected"><a href="<html:rewrite action="farm500"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Margin.Summary"/></em></a></li> 
    <li><a href="<html:rewrite action="farm510"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Income.and.Expenses"/></em></a></li> 
    <li><a href="<html:rewrite action="farm520"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Inventory"/></em></a></li> 
    <li><a href="<html:rewrite action="farm525"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Accruals"/></em></a></li> 
    <li><a href="<html:rewrite action="farm530"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Productive.Capacity"/></em></a></li> 
  </ul> 
  <div class="yui-content"> 
    <table class="numeric" style="width:100%;">
       <tr>
          <th></th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <th><c:out value="${rs.year}"/></th>
          </c:forEach>
          <th><c:out value="${scenario.year}"/></th>
        </tr>
        
        <tr>
          <th style="text-align:left;">Total Allowable Income</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.totalAllowableIncome}"/></td>
          </c:forEach>
          <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.marginTotal.totalAllowableIncome}"/></td>
        </tr>
        
        <tr>
          <th style="text-align:left;">Total Allowable Expenses</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.totalAllowableExpenses}"/></td>
          </c:forEach>
          <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.marginTotal.totalAllowableExpenses}"/></td>
        </tr>
        
        <tr>
          <th style="text-align:left;">Production Margin</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.unadjustedProductionMargin}"/></td>
          </c:forEach>
          <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.marginTotal.unadjustedProductionMargin}"/></td>
        </tr>
        
        <tr>
          <th style="text-align:left;">Purchased Inputs</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.accrualAdjsPurchasedInputs}"/></td>
          </c:forEach>
          <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.marginTotal.accrualAdjsPurchasedInputs}"/></td>
        </tr>
        
        <tr>
          <th style="text-align:left;">Deferred Income and Receivables</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.accrualAdjsReceivables}"/></td>
          </c:forEach>
          <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.marginTotal.accrualAdjsReceivables}"/></td>
        </tr>
        
        <tr>
          <th style="text-align:left;">Accounts Payable</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.accrualAdjsPayables}"/></td>
          </c:forEach>
          <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.marginTotal.accrualAdjsPayables}"/></td>
        </tr>

        <tr>
          <th style="text-align:left;">Production Margin with Accrual Adjustments</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.productionMargAccrAdjs}"/></td>
          </c:forEach>
          <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.marginTotal.productionMargAccrAdjs}"/></td>
        </tr>
        
        <tr>
          <th style="text-align:left;">Production Margin with Structural Change</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><fmt:formatNumber type="currency" value="${rs.farmingYear.marginTotal.productionMargAftStrChangs}"/></td>
          </c:forEach>
          <td>N/A</td>
        </tr>
        
        <tr>
          <th style="text-align:left;">Years used for Reference Margin</th>
          <c:forEach var="rs" items="${scenario.referenceScenarios}">
            <td><u:formatBoolean test="${rs.usedInCalc}"/></td>
          </c:forEach>
          <td>N/A</td>
        </tr>
    </table>
  </div> 
</div> 
<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<c:choose>
  <c:when test="${form.deductionLineItemType eq 'YARDAGE'}">
    <c:set var="typeMessageKey" value="Yardage"/>
  </c:when>
  <c:when test="${form.deductionLineItemType eq 'PROGRAM_PAYMENT'}">
    <c:set var="typeMessageKey" value="Program.Payments"/>
  </c:when>
  <c:when test="${form.deductionLineItemType eq 'CONTRACT_WORK'}">
    <c:set var="typeMessageKey" value="Contract.Work"/>
  </c:when>
</c:choose>

<h3><fmt:message key="${typeMessageKey}"/> <fmt:message key="Line.Items"/></h3>
<p>
  <fmt:message key="deduction.line.items.instruction.text.1"/>
  <fmt:message key="${typeMessageKey}"/>
  <fmt:message key="deduction.line.items.instruction.text.2"/>
  <c:if test="${form.deductionLineItemType eq 'PROGRAM_PAYMENT'}">
    <fmt:message key="deduction.line.items.instruction.text.3.program.payments"/>
    <c:if test="${form.year ge 2013}">
      <fmt:message key="deduction.line.items.instruction.text.4.program.payments"/>
    </c:if>
  </c:if>
</p>

<div class="yui-navset">
  <div class="yui-content">
    <table class="searchresults" style="width:100%">
      <tr>
        <th scope="col"><fmt:message key="Code"/></th>
        <th scope="col"><fmt:message key="Description"/></th>
        <logic-el:iterate name="form" property="referenceYears" id="year">
          <th width="80"><c:out value="${year}"/></th>
        </logic-el:iterate>
      </tr>
      
      <c:forEach var="item" items="${form.deductionLineItems}">
        <tr>
          <td align="left"><c:out value="${item.lineItem}" /></td>
          <td><c:out value="${item.description}" /></td>
          <td><c:if test="${item.isDeductionProgramYearMinus5}"><img src="images/tick.gif" alt="<fmt:message key="Yes"/>" /></c:if></td>
          <td><c:if test="${item.isDeductionProgramYearMinus4}"><img src="images/tick.gif" alt="<fmt:message key="Yes"/>" /></c:if></td>
          <td><c:if test="${item.isDeductionProgramYearMinus3}"><img src="images/tick.gif" alt="<fmt:message key="Yes"/>" /></c:if></td>
          <td><c:if test="${item.isDeductionProgramYearMinus2}"><img src="images/tick.gif" alt="<fmt:message key="Yes"/>" /></c:if></td>
          <td><c:if test="${item.isDeductionProgramYearMinus1}"><img src="images/tick.gif" alt="<fmt:message key="Yes"/>" /></c:if></td>
        </tr>
      </c:forEach> 
    </table>
  </div>
</div>

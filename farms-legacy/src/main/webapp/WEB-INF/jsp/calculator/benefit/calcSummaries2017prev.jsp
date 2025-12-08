<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<c:if test="${not scenario.lateParticipant}">

  <table style="width:100%" cellspacing="20" cellpadding="20">
    <tr>
      <td valign="top">
        <tiles:insert page="/WEB-INF/jsp/calculator/benefit/marginCalcSummary2013-2017.jsp" />
      </td>

      <c:choose>
        
        <%-- 2017 to current year --%>
        <c:when test="${form.growingForward2017}">
          <td valign="top">
            <tiles:insert page="/WEB-INF/jsp/calculator/benefit/progYearCalcSummary2017.jsp" />
          </td>
        </c:when>
        
        <%-- 2013 to 2016 --%>
        <c:when test="${form.growingForward2013}">
          <td valign="top">
            <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummary2013-2016.jsp" />
          </td>
        </c:when>
        
        <%-- 2012 and previous years --%>
        <c:when test="${not form.growingForward2013}">
          <td valign="top">
            <tiles:insert page="/WEB-INF/jsp/calculator/benefit/calcSummary2012prev.jsp" />
          </td>
        </c:when>
        
      </c:choose>
    </tr>
    
  </table>
</c:if>

<c:if test="${form.hasEnhancedBenefits}">
  <!-- BC Enhanced Benefit -->
  <tiles:insert page="/WEB-INF/jsp/calculator/benefit/bcEnhCalcSummaries2017.jsp" />
</c:if>

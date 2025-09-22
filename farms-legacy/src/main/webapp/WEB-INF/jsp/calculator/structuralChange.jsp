<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<c:choose>
  <c:when test="${form.growingForward2024}">
    <tiles:insert page="/WEB-INF/jsp/calculator/structuralChange2024.jsp" />
  </c:when>
  <c:otherwise>
    <tiles:insert page="/WEB-INF/jsp/calculator/structuralChange2009-2023.jsp" />
  </c:otherwise>
</c:choose>
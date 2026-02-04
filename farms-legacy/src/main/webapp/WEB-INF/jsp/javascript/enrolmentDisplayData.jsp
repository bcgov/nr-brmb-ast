<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getForm var="form" scope="request"/>

Farm.myAccounts = {
     "recordsReturned": <c:out value="${form.enrolmentCount}"/>,
     "totalRecords": <c:out value="${form.enrolmentCount}"/>,
     "startIndex":0,
     "sort":name,
     "dir":"asc",
     "pageSize": 10,
     "records":[


<c:forEach var="enrolment" items="${form.enrolments}" varStatus="loop">
{pin:"<c:out value="${enrolment.pin}"/>",
 pinDisplay:"<a href='javascript:redirectToScenario(<c:out value="${enrolment.pin}"/>, <c:out value="${form.year-2}"/>);'><c:out value="${enrolment.pin}"/></a>",
 status:"<c:out value="${enrolment.enrolmentStatus}"/>",
 state:"<c:out value="${enrolment.scenarioState}"/>",
 producer:"<c:out value="${enrolment.producerName}"/>",
 generatedDate:
  <c:choose>
    <c:when test="${!empty enrolment.generatedDate}">
      new Date(<c:out value="${enrolment.generatedDate.time}"/>)
    </c:when>
    <c:otherwise>null</c:otherwise>
  </c:choose>,
failedDate:
  <c:choose>
    <c:when test="${!empty enrolment.whenUpdated}">
      new Date(<c:out value="${enrolment.whenUpdated.time}"/>)
    </c:when>
    <c:otherwise>null</c:otherwise>
  </c:choose>,
 fee: <c:choose>
        <c:when test="${!empty enrolment.enrolmentFee}"><c:out value="${enrolment.enrolmentFee}"/></c:when>
        <c:otherwise>null</c:otherwise>
      </c:choose>,
 fromCra: <c:choose>
            <c:when test="${enrolment.isGeneratedFromCra}">"Y"</c:when>
            <c:otherwise>"N"</c:otherwise>
          </c:choose>,
 selectedInd: false,
 note: "<c:out value='${enrolment.failedReason}'/>"}
 <c:if test="${!loop.last}">,</c:if>
</c:forEach>

     ]
};

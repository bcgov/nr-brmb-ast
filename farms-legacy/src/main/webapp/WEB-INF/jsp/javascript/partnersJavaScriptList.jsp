<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getForm var="form" scope="request"/>

YAHOO.farm.Data = {
partners: [
<c:forEach var="p" items="${form.partners}">{pn:"<c:out value="${p.participantPin}"/> - <c:out value="${p.displayName}"/> - <c:out value="${p.partnerPercent100}"/>%",p:"<c:out value="${p.participantPin}"/>",t:"<c:out value="${p.partnerPercent100}"/>",f:"<c:out value="${p.firstName}"/>",l:"<c:out value="${p.lastName}"/>",c:"<c:out value="${p.corpName}"/>",n:"<c:out value="${p.displayName}"/> - <c:out value="${p.participantPin}"/> - <c:out value="${p.partnerPercent100}"/>%"},</c:forEach>
]};

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/vue3.global.prod.js"></script>

<h1 style="color: green;">
  <fmt:message key="Participant.Created"/>
</h1>

<div style="text-align:left;width:70%">
  
  <u:yuiButton buttonId="viewParticipantButton" buttonLabel="View.Participant" action="farm800" urlParams="pin=${form.participant.participantPin}&year=${form.participant.programYear}" />
  <u:yuiButton buttonId="createParticipantButton" buttonLabel="Create.Another.Participant" action="farm770" />

  <u:yuiButton buttonId="doneButton" buttonLabel="Done" action="welcome" />
</div>

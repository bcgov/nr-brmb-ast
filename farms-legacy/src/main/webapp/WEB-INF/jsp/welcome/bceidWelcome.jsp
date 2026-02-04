<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript">
  function onPageLoad(){
    document.getElementById('processingMessage').innerText = '<fmt:message key="BCeID.Message"/>...';
    showProcessing();
    document.location.href = "activateSubscription.do";
  }
</script>

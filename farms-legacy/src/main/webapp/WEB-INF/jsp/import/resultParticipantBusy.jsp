<%@ include file="/WEB-INF/jsp/common/include.jsp" %>


<script type="text/javascript"> 

  function onPageLoad(){
    showProcessing();
    document.location.href = "farm240.do?pin=" + <c:out value="${form.pin}" /> + "&importVersionId="+ <c:out value="${form.importVersionId}" />;
  }

</script> 


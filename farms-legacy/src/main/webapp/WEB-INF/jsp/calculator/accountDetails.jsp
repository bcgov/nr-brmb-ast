<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<div class="yui-content">

  <div class="yui-navset">
    <ul class="yui-nav">
      <li <c:if test="${screenNumber == '800'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm800"/>')"><em><fmt:message key="Status"/></em></a></li>
      <li <c:if test="${screenNumber == '810'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm810"/>')"><em><fmt:message key="Participant"/></em></a></li>
<%--       <li <c:if test="${screenNumber == '340'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm340"/>')"><em><fmt:message key="Subscriptions"/></em></a></li> --%>
      <c:if test="${scenario.farmingYear != null}">
        <li <c:if test="${screenNumber == '820'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm820"/>')"><em><fmt:message key="Farm.Detail"/></em></a></li>
        <li <c:if test="${screenNumber == '825'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm825"/>')"><em><fmt:message key="Operation"/></em></a></li>
        <li <c:if test="${screenNumber == '950'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm950"/>')"><em><fmt:message key="Operation.Alignment"/></em></a></li>
        <li <c:if test="${screenNumber == '827'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm827"/>')"><em><fmt:message key="Partners"/></em></a></li>
        <li <c:if test="${screenNumber == '920'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm920"/>')"><em><fmt:message key="Combined"/></em></a></li>
        <c:if test="${scenario.preVerification}">
          <li <c:if test="${screenNumber == '960'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm960"/>')"><em><fmt:message key="Pre-Verification"/></em></a></li>
        </c:if>
      </c:if>
    </ul>
    <div class="yui-content">
      <%@ include file="/WEB-INF/jsp/common/messages.jsp" %>
      <!-- START subTabBody -->
      <tiles:insert attribute="subTabBody"/>
      <!-- END subTabBody -->
    </div>
  </div>
</div>


<script type="text/javascript">
  //<![CDATA[
  
  function viewTab(url) {
  	showProcessing();
  	try {
      document.location.href = url + "?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
  	} catch(exception) {
  	  // User got the "You have unsaved changes" warning and clicked Cancel.
  	  undoShowProcessing();
  	}
  }

  //]]>
</script>

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<c:set var="showBenefitTabs" value="${(scenario.userScenario or scenario.fifoScenario) and not scenario.unknownCategory}"/>
<c:set var="showEnrolmentTab" value="${scenario.enrolmentNoticeWorkflow}"/>
<c:set var="showNegativeMarginTab" value="${scenario.negativeMarginCalculationEnabled}"/>

<div class="yui-content">

  <c:choose>
    <c:when test="${screenNumber != '970'}">
      <c:set var="navsetWidth" value="width:1000px;"/>
    </c:when>
    <c:otherwise>
      <c:set var="navsetWidth" value=""/>
    </c:otherwise>
  </c:choose>

  <div class="yui-navset" style="<c:out value="${navsetWidth}"/>">
    <ul class="yui-nav">
      <li <c:if test="${screenNumber == '830'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm830"/>')"><em><fmt:message key="Scenarios"/></em></a></li>
      <li <c:if test="${screenNumber == '870'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm870"/>')"><em><fmt:message key="Income.and.Expenses"/></em></a></li>
      <li <c:if test="${screenNumber == '880'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm880"/>')"><em><fmt:message key="Inventory"/></em></a></li>
      <li <c:if test="${screenNumber == '940'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm940"/>')"><em><fmt:message key="Accruals"/></em></a></li>
      <li <c:if test="${screenNumber == '890'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm890"/>')"><em><fmt:message key="Productive.Units"/></em></a></li>
      <c:if test="${showBenefitTabs}">
        <li <c:if test="${screenNumber == '930'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm930"/>')"><em><fmt:message key="Structural.Change"/></em></a></li>
      </c:if>
      <w:ifUserCanPerformAction action="viewReasonabilityTests">
        <c:if test="${showBenefitTabs and form.growingForward2013}">
          <li <c:if test="${screenNumber == '840'}">class="selected"</c:if>><a href="#" onclick="viewTab('<html:rewrite action="farm840"/>')"><em><fmt:message key="Reasonability.Tests"/></em></a></li>
        </c:if>
      </w:ifUserCanPerformAction>
      <c:if test="${showBenefitTabs}">
        <li <c:if test="${screenNumber == '850'}">class="selected"</c:if>><a href="#" onclick="benefitTab()"><em><fmt:message key="Benefits.and.Margins"/></em></a></li>
      </c:if>
      <c:if test="${showEnrolmentTab}">
        <li <c:if test="${screenNumber == '860'}">class="selected"</c:if>><a href="#" onclick="enrolmentTab()"><em><fmt:message key="Enrolment"/></em></a></li>
      </c:if>
      <c:if test="${showNegativeMarginTab}">
        <li <c:if test="${screenNumber == '970'}">class="selected"</c:if>><a href="#" onclick="negativeMarginTab()"><em><fmt:message key="Negative.Margin"/></em></a></li>
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

<c:if test="${showBenefitTabs}">
  <c:set var="readOnlyWithoutBenefitCalculated" value="${not form.canModifyScenario and not scenario.benefitSuccessfullyCalculated  and not scenario.fifoScenario}"/>
  <c:set var="readOnlyWithoutEnrolmentCalculated" value="${not form.canModifyScenario and not scenario.enwEnrolmentCalculated}"/>

  <script>
    
    function benefitTab() {
      <c:choose>
        <c:when test="${readOnlyWithoutBenefitCalculated}">
        	alert("<fmt:message key="read.only.without.benefit.calculated"/>");
        	return;
        </c:when>
        <c:otherwise>
        	showProcessing();
        	try {
          	document.location.href = "<html:rewrite action="farm850"/>?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
        	} catch(exception) {
        	  // User got the "You have unsaved changes" warning and clicked Cancel.
        	  undoShowProcessing();
        	}
        </c:otherwise>
      </c:choose>
    }
  
    function enrolmentTab() {
      <c:choose>
        <c:when test="${readOnlyWithoutEnrolmentCalculated}">
        	alert("<fmt:message key="read.only.without.enrolment.calculated"/>");
        	return;
        </c:when>
        <c:otherwise>
        	showProcessing();
        	try {
          	document.location.href = "<html:rewrite action="farm860"/>?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
        	} catch(exception) {
        	  // User got the "You have unsaved changes" warning and clicked Cancel.
        	  undoShowProcessing();
        	}
        </c:otherwise>
      </c:choose>
    }

    function negativeMarginTab() {
      showProcessing();
      try {
        document.location.href = "<html:rewrite action="farm970"/>?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
      } catch(exception) {
        // User got the "You have unsaved changes" warning and clicked Cancel.
        undoShowProcessing();
      }
    }

  </script>
</c:if>
  
<script>
  
  function viewTab(url) {
  	showProcessing();
  	try {
      document.location.href = url + "?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
  	} catch(exception) {
  	  // User got the "You have unsaved changes" warning and clicked Cancel.
  	  undoShowProcessing();
  	}
  }

</script>

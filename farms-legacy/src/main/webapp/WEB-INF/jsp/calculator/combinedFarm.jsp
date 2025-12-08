<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script>
  function addPin() {
    var actionField = document.getElementById('action');
    actionField.value = "ADD";
    submitForm(document.getElementById('combinedForm'));
  }

  function updateScenarios() {
    var actionField = document.getElementById('action');
    actionField.value = "UPDATE";
    submitForm(document.getElementById('combinedForm'));
  }
</script>

<html:form action="saveCombined" styleId="combinedForm" method="post" onsubmit="showProcessing()">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="scenarioRevisionCount"/>
  <html:hidden property="action" styleId="action"/>
  <html:hidden property="scenarioIdToRemove" styleId="scenarioIdToRemove"/>
  
  <c:if test="${empty scenario.combinedFarmClients}">
    <p><fmt:message key="combined.farm.pin.not.combined"/></p>
  </c:if>

  <c:if test="${ ! form.readOnly }">
    <table>
      <tr>
        <th style="width:50px"><fmt:message key="PIN"/>:</th>
        <td style="width:100px"><html:text property="pinToAdd" /></td>
        <td style="width:80px;text-align:center"><u:yuiButton buttonLabel="Add" buttonId="addButton" function="addPin"/></td>
      </tr>
    </table>
    
    <br />
  </c:if>
  
  <c:if test="${! empty scenario.combinedFarmClients}">

    <table class="searchresults" style="width:50%">
      <tr>
        <th style="width:70px"><fmt:message key="PIN"/></th>
        <th style="width:200px"><fmt:message key="Name"/></th>
        <th style="width:100px"><fmt:message key="Scenario"/></th>
        <c:if test="${ ! form.readOnly }">
          <th style="width:50px"></th>
        </c:if>
      </tr>
      
      <c:forEach var="client" items="${scenario.combinedFarmClients}">
        <c:set var="curPin"><c:out value="${client.participantPin}"/></c:set>
        <tr>
          <td style="text-align:left">
            <c:choose>
              <c:when test="${curPin eq scenario.client.participantPin}">
                <c:out value="${curPin}"/>
              </c:when>
              <c:otherwise>
                <a href="<html:rewrite action="farm800"/>?pin=<c:out value="${curPin}"/>&year=<c:out value="${form.year}"/>&scenarioNumber=<c:out value="${client.scenarioNumber}"/>"><c:out value="${curPin}"/></a>
              </c:otherwise>
            </c:choose>
          </td>
          <td style="text-align:left"><c:out value="${client.fullName}"/></td>
          <td>
            <c:choose>
              <c:when test="${ form.readOnly }">
                <c:out value="${form.combinedScenarioNumbers[curPin]}"/>
              </c:when>
              <c:otherwise>
                <html-el:hidden property="combinedScenarioId(${curPin})" />
                <html-el:select property="combinedScenarioNumber(${curPin})" style="width:40px">
                  <html-el:optionsCollection name="request.list.scenario.numbers.${curPin}"/>
                </html-el:select>
              </c:otherwise>
            </c:choose>
          </td>
          <c:if test="${ ! form.readOnly }">
            <td>
              <script type="text/javascript">
                //<![CDATA[
                function removePin<c:out value="${curPin}"/>() {
                  var actionField = document.getElementById('action');
                  var scenarioIdToRemoveField = document.getElementById('scenarioIdToRemove');
                  actionField.value = "REMOVE";
                  scenarioIdToRemoveField.value = "<c:out value="${form.combinedScenarioIds[curPin]}"/>";
                  submitForm(document.getElementById('combinedForm'));
                }
                //]]>
              </script>
              <u:yuiButton buttonLabel="Remove" buttonId="removeButton${curPin}" function="removePin${curPin}"/>
            </td>
          </c:if>
        </tr>
      </c:forEach>
    </table>
    
    <c:if test="${ ! form.readOnly }">
      <br />
      <div style="width:410px;text-align:right">
        <u:yuiButton buttonLabel="Save" buttonId="saveButton" function="updateScenarios"/>
      </div>
    </c:if>

  </c:if>

</html:form>

<script>
	// Disable submitting the form with the Enter key
	$('#combinedForm').keypress(function(event){
	    var keycode = (event.keyCode ? event.keyCode : event.which);
	    if(keycode == '13'){
	        event.preventDefault();
	        return false;
	    }
	});
</script>

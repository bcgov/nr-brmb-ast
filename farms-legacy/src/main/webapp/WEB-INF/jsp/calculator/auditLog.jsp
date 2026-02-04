<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>
          
  <h1><fmt:message key="Scenario.Audit.Log"/></h1>

  <div class="yui-content">            
    <form method="post" action="#" id="form1">
      <table class="searchresults" style="width:100%">
        <tr>
          <th scope="col">Date</th>
          <th scope="col">Audit Message</th>
          <th scope="col">User</th>
        </tr>
        
        <c:forEach var="log" items="${scenario.scenarioLogs}">
          <tr>
            <td valign="top" nowrap="nowrap"><fmt:formatDate value="${log.logDate}" pattern="yyyy-MM-dd hh:mm:ss a"/></td>
            <td align="left"><c:out value="${log.logMessage}" /></td>
            <td><c:out value="${log.userIdDisplay}" /></td>
          </tr>
        </c:forEach> 
      </table>
    </form>
  </div>

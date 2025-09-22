<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<h1><fmt:message key="Import.Detail"/></h1> 

<u:importParticipantLog var="participant" pin="${form.pin}"/>


<table class="details">
    <tr>
        <th>PIN:</th>
        <td><c:out value="${participant.pin}"/></td>
        <th>Account:</th>
        <td>
          <c:out value="${participant.corpName}"/>&nbsp;
          <c:out value="${participant.firstName}"/>&nbsp;
          <c:out value="${participant.lastName}"/>
        </td>
    </tr>
  <c:if test="${participant.AGRISTATIBLITYCLIENT.ERROR != null}">
    <tr>
        <th>Error:</th>
        <td colspan="3"><c:out value="${participant.AGRISTATIBLITYCLIENT.ERROR}"/></td>
    </tr>
  </c:if>
</table>


<c:forEach var="person" items="${participant.AGRISTATIBLITYCLIENT.PERSON}">
    <p></p>
    <h2>Person</h2>
    
        <c:choose>
            <c:when test="${person.ERROR}">
              <table class="details">
                <tr>
                    <th>Error:</th>
                    <td colspan="3">
                        <c:out value="${person.ERROR}" />
                    </td>
                </tr>
              </table>
            </c:when>
            <c:otherwise>
                <table class="searchresults">
                    <tr>
                        <th>Attribute</th>
                        <th>Old Value</th>
                        <th>New Value</th>
                    </tr>
                    <c:forEach var="attr" items="${person.ATTR}">
                        <tr>
                            <td>
                                <c:out value="${attr.name}" />
                            </td>
                            <td>
                                <c:out value="${attr.old}" />
                            </td>
                            <td>
                                <c:out value="${attr.new}" />
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    
</c:forEach>


<c:forEach var="py" items="${participant.PROGRAMYEARS.PROGRAMYEAR}">
    <c:if test="${py.altered eq 'true'}">
        <p></p>
        
        <c:choose>
            <c:when test="${py.numberOfErrors > 0}">
                <h2>
                    <c:out value="${py.year}" />
                </h2>
                <table class="details">
                    <c:forEach var="err" items="${py.ERROR}">
                      <tr>
                        <th>Error:</th>
                        <td colspan="3">
                            <c:out value="${err}" />
                        </td>
                      </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:when test="${py.numberOfWarnings > 0}">
                <h2>
                    <c:out value="${py.year}" />
                </h2>
                <table class="details">
                    <c:forEach var="warning" items="${py.WARNING}">
                      <tr>
                        <th>Warning:</th>
                        <td colspan="3">
                            <c:out value="${warning}" />
                        </td>
                      </tr>
                    </c:forEach>
                </table>
            </c:when>
        </c:choose>
    </c:if>
</c:forEach>


<p>
<a id="closeButton" href="javascript:closeClick()">Close</a>

<script type="text/javascript"> 
  new YAHOO.widget.Button("closeButton");
  
  function closeClick() {
    window.close();
  }
  
</script> 


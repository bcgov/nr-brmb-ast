<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/jquery.min.js"></script>

<w:ifUserCanPerformAction action="editPin">
  <c:set var="showParticpantPinEdit" value="true"/>  
</w:ifUserCanPerformAction>
<html:form action="saveParticipant" styleId="participantForm" method="post">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="scenarioRevisionCount"/>
  <html:hidden property="clientRevisionCount"/>

  <fieldset>
    <legend><fmt:message key="Participant.Info"/></legend>
    <html:hidden property="owner.revisionCount"/>

    <table class="formInput" style="width:100%">
    <c:choose>
      <c:when test="${ showParticpantPinEdit }">
      <tr>
        <th><fmt:message key="PIN"/>:</th>
        <td><html:text property="newPin" maxlength="9"/></td>
        <th></th>
        <td></td>
      </tr>
      </c:when>
      <c:otherwise>
      <html:hidden property="newPin"/>
      </c:otherwise>
    </c:choose>
      <tr>
        <th><fmt:message key="Corporation.Name"/>:</th>
        <td><html:text property="owner.corpName" maxlength="100"/></td>
        <th><fmt:message key="SIN"/>:</th>
        <td><html:text property="sin" maxlength="9"/></td>
      </tr>
      <tr>
        <th><fmt:message key="First.Name"/>:</th>
        <td><html:text property="owner.firstName" maxlength="100"/></td>
        <th><fmt:message key="Business.Number"/>:</th>
        <td><html:text property="businessNumber" maxlength="15"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Last.Name"/>:</th>
        <td><html:text property="owner.lastName" maxlength="100"/></td>
        <th><fmt:message key="Trust.Number"/>:</th>
        <td><html:text property="trustNumber" maxlength="9"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Address.1"/>:</th>
        <td><html:text property="owner.addressLine1" maxlength="80"/></td>
        <th><fmt:message key="Phone.FAX"/>:</th>
        <td><html:text property="owner.faxNumber" maxlength="20"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Address.2"/>:</th>
        <td><html:text property="owner.addressLine2" maxlength="80"/></td>
        <th><fmt:message key="Phone.Day"/>:</th>
        <td><html:text property="owner.daytimePhone" maxlength="20"/></td>
      </tr>
      <tr>
        <th><fmt:message key="City"/>:</th>
        <td><html:text property="owner.city" maxlength="80"/></td>
        <th><fmt:message key="Phone.Evening"/>:</th>
        <td><html:text property="owner.eveningPhone" maxlength="20"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Province"/>:</th>
        <td><html:text property="owner.provinceState" maxlength="2"/></td>
        <th><fmt:message key="Phone.Cell"/>:</th>
        <td><html:text property="owner.cellNumber" maxlength="20"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Postal.Code"/>:</th>
        <td><html:text property="owner.postalCode" maxlength="6"/></td>
        <th><fmt:message key="Email.Address"/>:</th>
        <td><html:text property="owner.emailAddress" maxlength="150"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Country"/>:</th>
        <td><html:text property="owner.country" maxlength="3"/></td>
        <th><fmt:message key="Participant.Type"/>:</th>
          <td>
            <html:select property="participantClassCode">
              <html:option value=""/>
              <html:optionsCollection name="server.list.participant.classes"/>
            </html:select>
          </td>
      </tr>
    </table>
  </fieldset>

  <fieldset>
    <html:hidden property="contact.revisionCount"/>
    <legend><fmt:message key="Contact.Info"/></legend>
    <table class="formInput" style="width:100%">
      <tr>
        <th><fmt:message key="Business.Name"/>:</th>
        <td><html:text property="contact.corpName" maxlength="100"/></td>
        <th><fmt:message key="First.Name"/>:</th>
        <td><html:text property="contact.firstName" maxlength="100"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Address.1"/>:</th>
        <td><html:text property="contact.addressLine1" maxlength="80"/></td>
        <th><fmt:message key="Last.Name"/>:</th>
        <td><html:text property="contact.lastName" maxlength="100"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Address.2"/>:</th>
        <td><html:text property="contact.addressLine2" maxlength="80"/></td>
        <th><fmt:message key="Phone"/>:</th>
        <td><html:text property="contact.daytimePhone" maxlength="20"/></td>
      </tr>
      <tr>
        <th><fmt:message key="City"/>:</th>
        <td><html:text property="contact.city" maxlength="80"/></td>
        <th><fmt:message key="Phone.FAX"/>:</th>
        <td><html:text property="contact.faxNumber" maxlength="20"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Province"/>:</th>
        <td><html:text property="contact.provinceState" maxlength="2"/></td>
        <th><fmt:message key="Phone.Cell"/>:</th>
        <td><html:text property="contact.cellNumber" maxlength="20"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Postal.Code"/>:</th>
        <td><html:text property="contact.postalCode" maxlength="6"/></td>
        <th><fmt:message key="Email.Address"/>:</th>
        <td><html:text property="contact.emailAddress" maxlength="150"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Country"/>:</th>
        <td><html:text property="contact.country" maxlength="3"/></td>
        <th>&nbsp;</th>
        <td>&nbsp;</td>
      </tr>
    </table>
  </fieldset>

  <c:if test="${ ! form.readOnly }">
    <script>
      function saveParticipant() {
        submitForm(document.getElementById('participantForm'));
      }
      registerFormForDirtyCheck(document.getElementById("participantForm"));
    </script>

    <br />
    <div style="width:915px;text-align:right">
      <u:yuiButton buttonLabel="Save" buttonId="saveButton" function="saveParticipant"/>
    </div>
  </c:if>

</html:form>

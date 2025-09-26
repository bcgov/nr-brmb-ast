<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

	<logic:messagesPresent name="infoMessages">
	  <div class="messages">
	    <ul>
	      <html:messages id="message" name="infoMessages">
	        <li><c:out value="${message}"/></li>
	      </html:messages>
	    </ul>
	  </div>
	</logic:messagesPresent>

  <html:form action="saveFarmDetail" styleId="farmDetailForm" method="post">
    <html:hidden property="pin"/>
    <html:hidden property="year"/>
    <html:hidden property="scenarioNumber"/>
    <html:hidden property="scenarioRevisionCount"/>
    <html:hidden property="programYearVersionRevisionCount"/>

    <fieldset>
    <legend><fmt:message key="Farm.Detail"/></legend>
    <table class="formInput" style="width:100%">
      <tr>
        <th><fmt:message key="Number.of.Years.Farming"/>:</th>
        <td><html:text property="farmYears" size="30" maxlength="2"/></td>
        <th><fmt:message key="Federal.Status"/>:</th>
        <td>
          <html:select property="agristabFedStsCode">
            <html:optionsCollection name="server.list.federal.statuses"/>
          </html:select>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Province.of.Residence"/>:</th>
        <td><html:text property="provinceOfResidence" size="30" maxlength="2"/></td>
        <th><fmt:message key="Province.of.Main.Farmstead"/>:</th>
        <td>
          <div style="float:left;width:97%">
            <html:text property="provinceOfMainFarmstead" size="30" maxlength="2"/>
          </div>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Other.Text"/>:</th>
        <td><html:text property="otherText" size="30" maxlength="100"/></td>
        <th><fmt:message key="Municipality"/>:</th>
        <td>
          <c:choose>
            <c:when test="${form.municipalityLocked}">
              <c:out value="${form.municipalityCodeDescription}"></c:out>
              <html:hidden property="municipalityCode" />
              <html:hidden property="municipalityCodeDescription" />
            </c:when>
            <c:otherwise>
              <html:select property="municipalityCode">
                <html:option value=""/>
                <html:optionsCollection name="server.list.municipalities"/>
              </html:select>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="CRA.Statement.A.Received.Date"/>:</th>
        <td><fmt:formatDate value="${scenario.craStatementAReceivedDate}" pattern="yyyy-MM-dd"/></td>
        <th><fmt:message key="Local.Supplemental.Date"/>:</th>
        <td>
          <div style="float:left;width:85%">
            <html:text property="localSupplementalReceivedDate" styleId="localSupplementalReceivedDate" size="30" maxlength="10"/>
          </div>
          <u:datePicker fieldId="localSupplementalReceivedDate"/>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="CRA.Supplemental.Date"/>:</th>
        <td><fmt:formatDate value="${scenario.craSupplementalReceivedDate}" pattern="yyyy-MM-dd"/></td>
        <th><fmt:message key="Local.Statement.A.Received.Date"/>:</th>
        <td>
          <div style="float:left;width:85%">
            <html:text property="localStatementAReceivedDate" styleId="localStatementAReceivedDate" size="30" maxlength="10"/>
          </div>
          <u:datePicker fieldId="localStatementAReceivedDate"/>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="CRA.Post.Mark.Date"/>:</th>
        <td><fmt:formatDate value="${scenario.farmingYear.postMarkDate}" pattern="yyyy-MM-dd"/></td>
        <th></th>
        <td></td>
      </tr>
      <tr>
        <th><fmt:message key="Common.Share.Total"/>:</th>
        <td><html:text property="commonShareTotal" size="30" maxlength="8"/></td>
        <th><fmt:message key="Participant.Profile"/>:</th>
        <td>
          <html:select property="participantProfileCode">
            <html:optionsCollection name="server.list.participant.profiles"/>
          </html:select>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Sole.Proprietor"/>:</th>
        <td><html:checkbox property="soleProprietor"/></td>
        <th><fmt:message key="Completed.Prod.Cycle"/>:</th>
        <td><html:checkbox property="completedProdCycle"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Corporate.Shareholder"/>:</th>
        <td><html:checkbox property="corporateShareholder"/></td>
        <th><fmt:message key="Disaster"/>:</th>
        <td><html:checkbox property="disaster"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Partnership.Member"/>:</th>
        <td><html:checkbox property="partnershipMember"/></td>
        <th><fmt:message key="Last.Year.Farming"/>:</th>
        <td><html:checkbox property="lastYearFarming"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Co.op.Member"/>:</th>
        <td><html:checkbox property="coopMember"/></td>
        <th><fmt:message key="Accrual.Cash.Conversion"/>:</th>
        <td><html:checkbox property="accrualCashConversion"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Combined.This.Year"/>:</th>
        <td><html:checkbox property="combinedFarm"/></td>
        <th><fmt:message key="Send.copy.to.contact.person"/>:</th>
        <td><html:checkbox property="canSendCobToRep"/></td>
      </tr>
      <tr>
        <th><fmt:message key="CWB.Worksheet"/>:</th>
        <td><html:checkbox property="cwbWorksheet"/></td>
        <th><fmt:message key="Receipts"/>:</th>
        <td><html:checkbox property="receipts"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Accrual.Worksheet"/>:</th>
        <td><html:checkbox property="accrualWorksheet"/></td>
        <th><fmt:message key="Perishable.Commodities"/>:</th>
        <td><html:checkbox property="perishableCommodities"/></td>
      </tr>
      <tr>
        <th></th>
        <td></td>
        <th><fmt:message key="Simplified.Margins.Opt.In"/>:</th>
        <td>
          <w:ifUserCanPerformAction action="editCashMarginsFlag">
           <c:set var="showCashMarginsCheckbox" value="true"/>
          </w:ifUserCanPerformAction>
          <c:choose>
            <c:when test="${showCashMarginsCheckbox}">
              <html:checkbox property="cashMargins" styleId="cashMargins"/>
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${form.cashMargins}">
                  <fmt:message key="Yes"/>
                </c:when>
                <c:otherwise>
                  <fmt:message key="No"/>
                </c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th></th>
        <td></td>
            <th><fmt:message key="Simplified.Margins.Opt.In.Date" />:</th>
            <td>
              <c:choose>
                <c:when test="${showCashMarginsCheckbox}">
                  <div style="float:left;width:85%">
                    <html:text property="cashMarginsOptInDate" styleId="cashMarginsOptInDate" size="30" maxlength="10"/>
                  </div>
                  <u:datePicker fieldId="cashMarginsOptInDate"/>
                </c:when>
                <c:otherwise>
                  <fmt:formatDate value="${scenario.farmingYear.cashMarginsOptInDate}" pattern="yyyy-MM-dd"/>
                </c:otherwise>
              </c:choose>
            </td>
      </tr>
    </table>
    </fieldset>

    <c:if test="${ ! form.readOnly }">
      <script>
        function submitFunc() {
          submitForm(document.getElementById('farmDetailForm'));
        }
        registerFormForDirtyCheck(document.getElementById("farmDetailForm"));
      </script>
      <div style="text-align:right">
        <u:yuiButton buttonLabel="Save" buttonId="saveButton" function="submitFunc"/>
      </div>
    </c:if>

  </html:form>

  

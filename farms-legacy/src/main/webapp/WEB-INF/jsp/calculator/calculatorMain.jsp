<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>

  <table class="tombstone">
    <tr  id="tombrow1" bgcolor="#F2F2F2">
      <th><img id="expanderImage" src="yui/2.8.2r1/build/assets/skins/sam/menuitem_submenuindicator.png" alt="" /><fmt:message key="Name"/>:</th>
      <td><a href="#" onclick="javascript:toggleDisplayElementsAndImage(['tombrow2','tombrow3','tombrow4','tombrow5','tombrow6','tombrow7'], 'expanderImage');"><c:out value="${scenario.client.owner.fullName}"/></a></td>
      <th><fmt:message key="PIN"/>:</th>
      <td><a href="#" onclick="javascript:toggleDisplayElementsAndImage(['tombrow2','tombrow3','tombrow4','tombrow5','tombrow6','tombrow7'], 'expanderImage');"><c:out value="${scenario.client.participantPin}"/></a></td>
      <th><fmt:message key="Benefit.Status"/>:</th>
      <td><a href="#" onclick="javascript:toggleDisplayElementsAndImage(['tombrow2','tombrow3','tombrow4','tombrow5','tombrow6','tombrow7'], 'expanderImage');"><c:out value="${scenario.scenarioStateCodeDescription}"/></a> <c:if test="${scenario.inCombinedFarm}"><span id="combinedSpan">(C)</span></c:if></td>
      <th><fmt:message key="Program.Year"/>:</th>
      <td>
        <u:programYearSelect action="farm800.do" year="${form.year}" urlParams="pin=${form.pin}&refresh=true"/>
        <c:if test="${scenario.scenarioId != null}">
          <span style="float: right;">
	          <a class="notes-edit-icon" href="#" onclick="loadWindow('<html:rewrite action="farm818" name="form" property="scenarioParams"/>', '630', '700', 'scenarioLogWindow');">
	            <img src="images/icon_scenario_log.png" name="scenarioLogButton" width="20" height="20" border="0" id="scenarioLogButton" alt="Scenario Log" />
	          </a>
	          
	          <a class="notes-edit-icon" href="#" onclick="loadWindow('<html:rewrite action="farm817" name="form" property="scenarioParams"/>&noteType=INTERIM', '1000', '750', 'interimNotesWindow');">
	            <img src="images/icon_interim_notes.png" name="interimNotesButton" width="20" height="20" border="0" id="interimNotesButton" alt="Interim Verification Notes" />
	          </a>
	          
	          <a class="notes-edit-icon" href="#" onclick="loadWindow('<html:rewrite action="farm817" name="form" property="scenarioParams"/>&noteType=FINAL', '1000', '750', 'finalNotesWindow');">
	            <img src="images/icon_final_notes.png" name="finalNotesButton" width="20" height="20" border="0" id="finalNotesButton" alt="Final Verification Notes" />
	          </a>
	          
	          <a class="notes-edit-icon" href="#" onclick="loadWindow('<html:rewrite action="farm817" name="form" property="scenarioParams"/>&noteType=ADJUSTMENT', '1000', '750', 'adjustmentNotesWindow');">
	            <img src="images/icon_adjustment_notes.png" name="adjustmentNotesButton" width="20" height="20" border="0" id="adjustmentNotesButton" alt="Adjustment Verification Notes" />
	          </a>
	        </span>
        </c:if>
      </td>
    </tr>
    <tr id="tombrow2" style="display:none;">
      <th><fmt:message key="Num.Ops"/>:</th>
      <td><c:out value="${scenario.farmingYear.farmingOperationCount}"/></td>
      <th><fmt:message key="Part.Type"/>:</th>
      <td><c:out value="${scenario.client.participantClassCodeDescription}"/></td>
      <th>Pgm Year Version:</th>
      <td><c:out value="${scenario.farmingYear.programYearVersionNumber}"/></td>
      <th><fmt:message key="Scenario.Number"/>:</th>
      <td><c:out value="${scenario.scenarioNumber}"/></td>
    </tr>
    <tr id="tombrow3" style="display:none;">
      <th><fmt:message key="SIN"/>:</th>
      <td><c:out value="${scenario.client.sin}"/></td>
      <th><fmt:message key="Business.Number"/>:</th>
      <td><c:out value="${scenario.client.businessNumber}"/></td>
      <th><fmt:message key="Trust.Number"/>:</th>
      <td><c:out value="${scenario.client.trustNumber}"/></td>
      <th>&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    <tr id="tombrow4" style="display:none;">
      <th><fmt:message key="Day.Phone"/>:</th>
      <td><c:out value="${scenario.client.owner.daytimePhoneDisplay}"/></td>
      <th><fmt:message key="Address"/>:</th>
      <td><c:out value="${scenario.client.owner.addressLine1}"/></td>
      <th><fmt:message key="Contact.Business"/>:</th>
      <td><c:out value="${scenario.client.contact.corpName}"/></td>
      <th><fmt:message key="Contact.Address"/>:</th>
      <td><c:out value="${scenario.client.contact.addressLine1}"/></td>
    </tr>
    <tr id="tombrow5" style="display:none;">
      <th><fmt:message key="Eve.Phone"/>:</th>
      <td><c:out value="${scenario.client.owner.eveningPhoneDisplay}"/></td>
      <th></th>
      <td><c:out value="${scenario.client.owner.city}"/></td>
      <th><fmt:message key="Contact.Name"/>:</th>
      <td><c:out value="${scenario.client.contact.firstName} ${scenario.client.contact.lastName}"/></td>
      <td>&nbsp;</td>
      <td><c:out value="${scenario.client.contact.city}"/></td>
    </tr>
    <tr id="tombrow6" style="display:none;">
      <th><fmt:message key="Cell.Phone"/>:</th>
      <td><c:out value="${scenario.client.owner.cellNumberDisplay}"/></td>
      <th>&nbsp;</th>
      <td><c:out value="${scenario.client.owner.postalCode}"/></td>
      <th><fmt:message key="Contact.Phone"/>:</th>
      <td><c:out value="${scenario.client.contact.daytimePhoneDisplay}"/></td>
      <th>&nbsp;</th>
      <td><c:out value="${scenario.client.contact.postalCode}"/></td>
    </tr>
    <tr id="tombrow7" style="display:none;">
      <th>&nbsp;</th>
      <td>&nbsp;</td>
      <th>&nbsp;</th>
      <td>&nbsp;</td>
      <th><fmt:message key="Contact.Cell.Phone"/>:</th>
      <td><c:out value="${scenario.client.contact.cellNumberDisplay}"/></td>
      <th><fmt:message key="Contact.Fax"/>:</th>
      <td><c:out value="${scenario.client.contact.faxNumberDisplay}"/></td>
    </tr>
  </table>

  <p></p>

  <u:yuiTooltip targetId="scenarioLogButton" messageKey="tool.tip.scenario.audit.log"/>
  <u:yuiTooltip targetId="interimNotesButton" messageKey="tool.tip.interim.verification.notes"/>
  <u:yuiTooltip targetId="finalNotesButton" messageKey="tool.tip.final.verification.notes"/>
  <u:yuiTooltip targetId="adjustmentNotesButton" messageKey="tool.tip.adjustment.verification.notes"/>

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

  <c:if test="${scenario.inCombinedFarm}">
    <u:yuiTooltip targetId="combinedSpan" messageKey="part.of.combined.farm"/>
  </c:if>

  <div class="yui-navset">
      <ul class="yui-nav">
        <li <c:if test="${tabName == 'accountDetails'}">class="selected"</c:if>><a  href="#" onclick="viewTab('<html:rewrite action="farm800"/>')"><em><fmt:message key="Account.Details"/></em></a></li>
        <c:if test="${scenario.scenarioId != null}">
          <li <c:if test="${tabName == 'benefitCalculation'}">class="selected"</c:if>><a  href="#" onclick="viewTab('<html:rewrite action="farm830"/>')"><em><fmt:message key="Benefit.Calculation"/></em></a></li>
        </c:if>
      </ul>
      <!-- START subTabs -->
      <tiles:insert attribute="subTabs">
        <tiles:put name="subTabBody" beanName="subTabBody"/>
      </tiles:insert>
      <!-- END subTabs -->
  </div>

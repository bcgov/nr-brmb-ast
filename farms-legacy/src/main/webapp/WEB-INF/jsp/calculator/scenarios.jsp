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

<c:choose>
  <c:when test="${! empty form.inProgressCombinedFarmNumber}">
    <c:set var="checkOutWarning"><fmt:message key="check.out.warning.combined.farm"/></c:set>
  </c:when>
  <c:otherwise>
    <c:set var="checkOutWarning"><fmt:message key="check.out.warning"/></c:set>
  </c:otherwise>
</c:choose>

<c:choose>
  <c:when test="${! empty form.inProgressCombinedFarmNumber}">
    <c:set var="saveAsNewWarning"><fmt:message key="save.as.new.combined.farm.in.progress.warning"/></c:set>
  </c:when>
  <c:when test="${! empty form.verifiedCombinedFarmNumber}">
    <c:set var="saveAsNewWarning"><fmt:message key="save.as.new.combined.farm.verified.warning"/></c:set>
  </c:when>
  <c:otherwise>
    <c:set var="saveAsNewWarning"><fmt:message key="save.as.new.warning"/></c:set>
  </c:otherwise>
</c:choose>

<c:choose>
  <c:when test="${form.combinedFarmChanged}">
    <c:set var="saveScenarioWarning"><fmt:message key="save.scenario.combined.farm.changed.warning"/></c:set>
  </c:when>
  <c:when test="${scenario.inCombinedFarm}">
    <c:set var="saveScenarioWarning"><fmt:message key="save.scenario.combined.farm.state.change.warning"/></c:set>
  </c:when>
  <c:otherwise>
    <c:set var="saveScenarioWarning"></c:set>
  </c:otherwise>
</c:choose>

<c:if test="${scenario.benefit.prodInsurDeemedBenefit != null && scenario.benefit.prodInsurDeemedBenefit != 0}">
  <c:set var="prodInsuranceMsg">
    <fmt:message key="save.as.new.prod.insurance.warning.part.1"/><fmt:formatNumber type="currency" value="${scenario.benefit.prodInsurDeemedBenefit}"/><fmt:message key="save.as.new.prod.insurance.warning.part.2"/>
  </c:set>
  <c:set var="hasProdInsurance" value="true"/>
</c:if>

<script type="text/javascript">
  //<![CDATA[
  
  function checkOut() {
    var msg = "<c:out value="${checkOutWarning}"/>";
    if(confirm(msg)) {
      showProcessing();
      document.location.href = "<html:rewrite action="scenariosCheckOut"/>?<c:out value="${form.scenarioParamsString}&scenarioRevisionCount=${form.scenarioRevisionCount}" escapeXml="false"/>";
    }
  }
  
  function saveAsNew() {
    var saveAsNewMsg = "<c:out value="${saveAsNewWarning}"/>";
    var prodInsuranceMsg = "<c:out value="${prodInsuranceMsg}"/>";
    if(confirm(saveAsNewMsg)) {
      if('<c:out value="${hasProdInsurance}"/>' !== 'true' || confirm(prodInsuranceMsg)) {
        showProcessing();
        document.location.href = "<html:rewrite action="saveAsNewScenario"/>?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
      }
    }
  }
  
  function saveScenario() {
    var msg = "<c:out value="${saveScenarioWarning}"/>";
    var stateCodeField = document.getElementById("scenarioStateCode");
    var stateCode = stateCodeField.value;
    var showWarning = msg && (stateCode == 'COMP' || stateCode != '<c:out value="${form.oldScenarioStateCode}"/>');
    if(!showWarning || confirm(msg)) {
      var form = document.getElementById('scenarioForm');
      submitForm(form);
    }
  }
  
  function stateChange() {
    var reasonField = document.getElementById("stateChangeReason");
    reasonField.value = "";
    reasonField.focus();
  }
  
  function viewScenario(pin, programYear, scenarioNumber) {
    showProcessing();
    document.location.href = '<html:rewrite action="farm830"/>?pin=' + pin
        + '&year=' + programYear
        + '&scenarioNumber=' + scenarioNumber
        + '&refresh=true';
  }
  
  function copyProgramYearVersion() {
    var areYouSureMsg = '<fmt:message key="Are.you.sure.you.want.to.copy.program.year.version"/>';
    if(confirm(areYouSureMsg)) {
      showProcessing();
      document.location.href = "<html:rewrite action="copyProgramYearVersion"/>?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
    }
  }

  //]]>
</script>

<html:form action="saveScenario" styleId="scenarioForm" method="post" onsubmit="showProcessing()">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="scenarioRevisionCount"/>
  <html:hidden property="oldScenarioStateCode"/>

  <fieldset>
  <legend><fmt:message key="Current.Scenario"/></legend>
  <table class="formInput" style="width:100%">
    <tr>
      <th><fmt:message key="Scenario.Number"/>:</th>
      <td>
        <u:menuSelect action="farm830.do"
             name="scenarioNumberPicker"
             paramName="scenarioNumber"
             options="${form.scenarioNumberOptions}"
             selectedValue="${form.scenarioNumber}"
             urlParams="pin=${form.pin}&year=${form.year}&refresh=true"
             toolTip="Click here to open a different Scenario Number for this participant."/>
      </td>
      <th><fmt:message key="Pgm.Year.Version"/>:</th>
      <td><c:out value="${scenario.farmingYear.programYearVersionNumber}"/></td>
      <th><fmt:message key="Verified.By"/>:</th>
      <td>
        <c:choose>
          <c:when test="${form.readOnly or not scenario.inProgress}">
            <html:hidden property="verifierUserId"/>
            <c:out value="${scenario.verifierAccountName}"/>
          </c:when>
          <c:otherwise>
            <html-el:select property="verifierUserId" styleId="verifierUserId">
              <html-el:option value=""></html-el:option>
              <c:forEach var="verifier" items="${form.verifierOptions}">
                <html-el:option value="${verifier.value}"><c:out value="${verifier.label}"/></html-el:option>
              </c:forEach>
            </html-el:select>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <th><fmt:message key="State"/>:</th>
      <td>
        <c:choose>
          <c:when test="${form.readOnly}">
            <c:out value="${scenario.scenarioStateCodeDescription}"/>
          </c:when>
          <c:otherwise>
            <html-el:select property="scenarioStateCode" styleId="scenarioStateCode" onchange="stateChange()">
              <c:forEach var="stateItem" items="${form.scenarioStateSelectOptions}">
                <html-el:option value="${stateItem.value}"><c:out value="${stateItem.label}"/></html-el:option>
              </c:forEach>
            </html-el:select>
          </c:otherwise>
        </c:choose>
      </td>
      <th><fmt:message key="Last.Updated"/>:</th>
      <td><fmt:formatDate value="${scenario.whenUpdatedTimestamp}" pattern="yyyy-MM-dd"/></td>
      <th><fmt:message key="Checked.Out.By"/>:</th>
      <td>
        <c:choose>
          <c:when test="${empty scenario.assignedToUserIdDisplay}">
            <fmt:message key="Not.Checked.Out"/>
          </c:when>
          <c:otherwise>
            <c:out value="${scenario.assignedToUserIdDisplay}"/>
          </c:otherwise>
        </c:choose>
      </td>
      
    </tr>
    <tr>
      <th><fmt:message key="Scenario.Category"/>:</th>
      <td>
        <c:choose>
          <c:when test="${form.readOnly or scenario.scenarioCategoryCode != 'UNK'}">
            <c:out value="${scenario.scenarioCategoryCodeDescription}"/>
            <html:hidden property="scenarioCategoryCode"/>
          </c:when>
          <c:otherwise>
            <html-el:select property="scenarioCategoryCode" styleId="scenarioCategoryCode">
              <c:forEach var="categoryItem" items="${form.scenarioCategorySelectOptions}">
                <html-el:option value="${categoryItem.value}"><c:out value="${categoryItem.label}"/></html-el:option>
              </c:forEach>
            </html-el:select>
          </c:otherwise>
        </c:choose>
      </td>
      <th></th>
      <td></td>
      <th><fmt:message key="Default.Indicator"/>:</th>
      <td><html:checkbox property="defaultInd"/></td>
    </tr>
    <tr>
      <th><fmt:message key="State.Change.Reason"/>:</th>
      <td colspan="5">
        <c:choose>
          <c:when test="${form.readOnly}">
            <c:out value="${form.stateChangeReason}"/>
          </c:when>
          <c:otherwise>
            <html:text property="stateChangeReason" styleId="stateChangeReason" size="30" maxlength="2000"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <th><fmt:message key="Scenario.Description"/>:</th>
      <td colspan="5">
        <c:choose>
          <c:when test="${form.readOnly}">
            <c:out value="${form.scenarioDescription}"/>
          </c:when>
          <c:otherwise>
            <html:text property="scenarioDescription" size="30" maxlength="2000"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </table>
  </fieldset>

  <div style="text-align:right">
    <p>
      <w:ifUserCanPerformAction action="copyProgramYearVersion">
        <u:yuiButton buttonLabel="Copy.Program.Year.Version" buttonId="copyProgramYearVersionButton" function="copyProgramYearVersion"/>
      </w:ifUserCanPerformAction>
      <w:ifUserCanPerformAction action="editScenario">
        <c:if test="${ ! form.assignedToCurrentUser }">
          <u:yuiButton buttonLabel="Check.Out" buttonId="checkOutButton" function="checkOut"/>
        </c:if>
        <c:if test="${ form.assignedToCurrentUser and scenario.scenarioCategoryCode != 'TRIAGE' }">
          <u:yuiButton buttonLabel="Save.as.New.Scenario" buttonId="saveAsNewButton" function="saveAsNew" disabled="${scenario.chefsFormTypeCode == 'SUPP' or scenario.chefsFormTypeCode == 'INTERIM'}"/>
        </c:if>
      </w:ifUserCanPerformAction>
      <c:if test="${ ! form.readOnly }">
        <u:yuiButton buttonLabel="Save" buttonId="saveButton" function="saveScenario"/>
      </c:if>
    </p>
  </div>

</html:form>

<div id="thisYearDetails" style="display: none;">

  <fieldset>
    <legend><fmt:message key="Scenario.Details"/></legend>
    
    <div>
      <span style="float:left"><a href="javascript:toggleRefScenarioRow()">
        <span class="hideRefScenario"><fmt:message key="Hide.Reference.Scenarios"/></span>
        <span class="showRefScenario"><fmt:message key="Show.Reference.Scenarios"/></span>
        </a></span>
        <span style="float:right"><a href="javascript:showReferenceYears()"><fmt:message key="Show.Reference.Years"/></a></span>
    </div>

    <table class="searchresults" style="width:100%">
      <tr>
        <th><fmt:message key="Year"/></th>
        <th width="100"><fmt:message key="Pgm.Year.Version"/></th>
        <th width="70"><fmt:message key="Scenario"/></th>
        <th><fmt:message key="Type"/></th>
        <th><fmt:message key="Category"/></th>
        <th width="80"><fmt:message key="Created"/></th>
        <th width="80"><fmt:message key="State"/></th>
        <th><fmt:message key="Default"/></th>
        <th><fmt:message key="Description"/></th>
        <th><fmt:message key="Created.By"/></th>
      </tr>

<c:forEach var="item" items="${scenario.scenarioMetaDataList}">
  <c:if test="${item.programYear == form.year}">
      <c:choose>
        <c:when test="${form.scenarioByIdMap[item.scenarioId] != null}">
          <c:set var="rowStyle" value="font-weight:bold;"/>
        </c:when>
        <c:otherwise>
          <c:set var="rowStyle" value=""/>
        </c:otherwise>
      </c:choose>
      <c:choose>
        <c:when test="${item.scenarioTypeCode == 'REF'}">
          <c:set var="refClass" value="refRow"/>
        </c:when>
        <c:otherwise>
          <c:set var="refClass" value=""/>
        </c:otherwise>
      </c:choose>
      <tr class="<c:out value="${refClass}"/>">
        <td style="<c:out value="${rowStyle}"/>"><c:out value="${item.programYear}"/></td>
        <td style="<c:out value="${rowStyle}"/>"><c:out value="${item.programYearVersion}"/></td>
        <td style="<c:out value="${rowStyle}"/>">
          <c:choose>
            <c:when test="${item.programYear == form.year and item.scenarioNumber == form.scenarioNumber}">
              <fmt:message key="Scenario"/> <c:out value="${item.scenarioNumber}"/>
            </c:when>
            <c:otherwise>
              <a href="#" onclick="viewScenario(<c:out value="${form.pin}"/>, <c:out value="${item.programYear}"/>, <c:out value="${item.scenarioNumber}"/>)">
                <fmt:message key="Scenario"/> <c:out value="${item.scenarioNumber}"/>
              </a>
            </c:otherwise>
          </c:choose>
        </td>
        <td style="<c:out value="${rowStyle}"/>"><c:out value="${item.scenarioTypeCode}"/></td>
        <td style="<c:out value="${rowStyle}"/>"><c:out value="${item.scenarioCategoryDescription}"/></td>
        <td style="<c:out value="${rowStyle}"/>"><fmt:formatDate value="${item.scenarioCreatedDate}" pattern="yyyy-MM-dd"/></td>
        <td style="<c:out value="${rowStyle}"/>">
          <c:out value="${item.scenarioStateDescription}"/>
          <c:if test="${not empty item.combinedFarmNumber}">
            <span id="combinedSpan<c:out value="${item.scenarioId}"/>">(C)</span>
            <script type="text/javascript">
              //<![CDATA[
              new YAHOO.widget.Tooltip("combinedSpan<c:out value="${item.scenarioId}"/>TT",  
                                { context:"combinedSpan<c:out value="${item.scenarioId}"/>",  
                                  text:"<fmt:message key="part.of.combined.farm"/>", 
                      autodismissdelay: 7500,
                      effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} }); 
              //]]>
            </script>
          </c:if>
        </td>
        <td style="<c:out value="${rowStyle}"/>"><c:if test="${item.defaultInd}">Y</c:if></td>
        <td style="<c:out value="${rowStyle}"/>" align="left"><c:out value="${item.scenarioDescription}"/></td>
        <td style="<c:out value="${rowStyle}"/>">
          <c:choose>
            <c:when test="${not empty item.chefsViewSubmissionUrl}">
              <a href="<c:out value="${item.chefsViewSubmissionUrl}"/>" target="_blank"><c:out value="${item.scenarioCreatedByDisplay}"/></a>
            </c:when>
            <c:otherwise>
              <c:out value="${item.scenarioCreatedByDisplay}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
  </c:if>
</c:forEach>

    </table>

  </fieldset>
</div>


<div id="allYearsDetails" style="display: none;">

  <fieldset>
    <legend><fmt:message key="Scenario.Details"/></legend>
    
    <div>
      <span style="float:left"><a href="javascript:toggleRefScenarioRow()">
        <span class="hideRefScenario"><fmt:message key="Hide.Reference.Scenarios"/></span>
        <span class="showRefScenario"><fmt:message key="Show.Reference.Scenarios"/></span>
      </a></span>
      <span style="float:right"><a href="javascript:hideReferenceYears()"><fmt:message key="Hide.Reference.Years"/></a></span>
    </div>

    <table class="searchresults" style="width:100%">
      <tr>
        <th><fmt:message key="Year"/></th>
        <th width="100"><fmt:message key="Pgm.Year.Version"/></th>
        <th width="70"><fmt:message key="Scenario"/></th>
        <th><fmt:message key="Type"/></th>
        <th><fmt:message key="Category"/></th>
        <th width="80"><fmt:message key="Created"/></th>
        <th width="80"><fmt:message key="State"/></th>
        <th><fmt:message key="Default"/></th>
        <th><fmt:message key="Description"/></th>
        <th><fmt:message key="Created.By"/></th>
      </tr>

  <c:forEach var="item" items="${scenario.scenarioMetaDataList}">
      <c:choose>
        <c:when test="${form.scenarioByIdMap[item.scenarioId] != null}">
          <c:set var="rowStyle" value="font-weight:bold;"/>
        </c:when>
        <c:otherwise>
          <c:set var="rowStyle" value=""/>
        </c:otherwise>
      </c:choose>
      <c:choose>
        <c:when test="${item.scenarioTypeCode == 'REF'}">
          <c:set var="refClass" value="refRow"/>
        </c:when>
        <c:otherwise>
          <c:set var="refClass" value=""/>
        </c:otherwise>
      </c:choose>
      <tr class="<c:out value="${refClass}"/>">
        <td style="<c:out value="${rowStyle}"/>"><c:out value="${item.programYear}"/></td>
        <td style="<c:out value="${rowStyle}"/>"><c:out value="${item.programYearVersion}"/></td>
        <td style="<c:out value="${rowStyle}"/>">
          <c:choose>
            <c:when test="${item.programYear == form.year and item.scenarioNumber == form.scenarioNumber}">
              <fmt:message key="Scenario"/> <c:out value="${item.scenarioNumber}"/>
            </c:when>
            <c:otherwise>
              <a href="#" onclick="viewScenario(<c:out value="${form.pin}"/>, <c:out value="${item.programYear}"/>, <c:out value="${item.scenarioNumber}"/>)">
                <fmt:message key="Scenario"/> <c:out value="${item.scenarioNumber}"/>
              </a>
            </c:otherwise>
          </c:choose>
        </td>
        <td style="<c:out value="${rowStyle}"/>"><c:out value="${item.scenarioTypeCode}"/></td>
        <td style="<c:out value="${rowStyle}"/>"><c:out value="${item.scenarioCategoryDescription}"/></td>
        <td style="<c:out value="${rowStyle}"/>"><fmt:formatDate value="${item.scenarioCreatedDate}" pattern="yyyy-MM-dd"/></td>
        <td style="<c:out value="${rowStyle}"/>">
          <c:out value="${item.scenarioStateDescription}"/>
          <c:if test="${not empty item.combinedFarmNumber}">
            <span id="combinedSpanAll<c:out value="${item.scenarioId}"/>">(C)</span>
            <script type="text/javascript">
              //<![CDATA[
              new YAHOO.widget.Tooltip("combinedSpanAll<c:out value="${item.scenarioId}"/>TT",  
                                { context:"combinedSpanAll<c:out value="${item.scenarioId}"/>",  
                                  text:"<fmt:message key="part.of.combined.farm"/>", 
                      autodismissdelay: 7500,
                      effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} }); 
              //]]>
            </script>
          </c:if>
        </td>
        <td style="<c:out value="${rowStyle}"/>"><c:if test="${item.defaultInd}">Y</c:if></td>
        <td style="<c:out value="${rowStyle}"/>" align="left"><c:out value="${item.scenarioDescription}"/></td>
        <td style="<c:out value="${rowStyle}"/>">
          <c:choose>
            <c:when test="${not empty item.chefsViewSubmissionUrl}">
              <a href="<c:out value="${item.chefsViewSubmissionUrl}"/>" target="_blank"><c:out value="${item.scenarioCreatedByDisplay}"/></a>
            </c:when>
            <c:otherwise>
              <c:out value="${item.scenarioCreatedByDisplay}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
  </c:forEach>

    </table>


  </fieldset>
</div>

<script type="text/javascript">
  //<![CDATA[

  function showReferenceYears() {
    $('#thisYearDetails').hide();
    $('#allYearsDetails').show();
  }

  function hideReferenceYears() {
    $('#thisYearDetails').show();
    $('#allYearsDetails').hide();
  }
  
  function toggleRefScenarioRow() {
    $('.refRow').toggle();
    $('.showRefScenario').toggle();
    $('.hideRefScenario').toggle();
  }

  $('.showRefScenario').show();
  $('.hideRefScenario').hide();
  $('.refRow').hide()
  hideReferenceYears();

  //]]>
</script>


<c:if test="${ ! form.readOnly }">
  <u:dirtyFormCheck formId="scenarioForm"/>
</c:if>

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<w:ifUserCanPerformAction action="copyYear" var="canCopy"></w:ifUserCanPerformAction>
<w:ifUserCanPerformAction action="regenerateCob" var="canRegenerateCob"></w:ifUserCanPerformAction>

<c:set var="showGenerateCob" value="${form.assignedToCurrentUser and not scenario.coverageNotice and not scenario.hasBenefitDocument and (scenario.verified or scenario.amended)}"/>
<c:set var="showRegenerateCob" value="${form.assignedToCurrentUser and canRegenerateCob and not scenario.coverageNotice and scenario.hasBenefitDocument and (scenario.verified or scenario.amended)}"/>

<c:set var="showGenerateCoverageNotice" value="${form.assignedToCurrentUser and scenario.coverageNotice and not scenario.hasBenefitDocument and (scenario.verified or scenario.amended)}"/>
<c:set var="showRegenerateCoverageNotice" value="${form.assignedToCurrentUser and scenario.coverageNotice and scenario.hasBenefitDocument and scenario.completed}"/>

<c:choose>
  <c:when test="${! empty form.inProgressCombinedFarmNumber}">
    <c:set var="checkOutWarning"><fmt:message key="check.out.warning.combined.farm"/></c:set>
  </c:when>
  <c:otherwise>
    <c:set var="checkOutWarning"><fmt:message key="check.out.warning"/></c:set>
  </c:otherwise>
</c:choose>

<script type="text/javascript">
  //<![CDATA[
  function onPageLoad(){
//  <c:if test="${form.reportUrl != null}">
      openNewWindow('<c:out value="${form.reportUrl}?${form.scenarioParamsString}" escapeXml="false" />');
//  </c:if>
  }
  
  function checkOut() {
    var msg = "<c:out value="${checkOutWarning}"/>";
    if(confirm(msg)) {
      showProcessing();
      document.location.href = "<html:rewrite action="statusCheckOut"/>?<c:out value="${form.scenarioParamsString}&scenarioRevisionCount=${form.scenarioRevisionCount}" escapeXml="false"/>";
    }
  }
  //]]>
</script>


<table cellspacing="10" style="width:100%;">
  <tr>
    <td width="50%" valign="top">
      <fieldset>
        <legend><fmt:message key="Claim.Data"/></legend>
        <table style="width:100%" class="formInput">
          <tr>
            <th><fmt:message key="Program.Year.Tax.Data"/>: </th>
            <td>
              <c:choose>
                <c:when test="${form.newBaseDataArrived[scenario.year]}">
                  <img id="year<c:out value="${scenario.year}"/>Ind" src="images/flag_yellow.gif" alt="New Base Data Arrived"/>
                  <c:set var="yearHasToolTip" value="true"/>
                  <c:set var="toolTipText" value="New base data has arrived for this year."/>
                </c:when>
                <c:when test="${form.taxYearDataGenerated[scenario.year]}">
                  <img id="year<c:out value="${scenario.year}"/>Ind" src="images/tick_yellow.gif" alt="Locally Generated Farming Year" />
                  <c:set var="yearHasToolTip" value="true"/>
                  <c:set var="toolTipText" value="The data for this year has been locally generated."/>
                </c:when>
                <c:when test="${scenario.farmingYear.programYearVersionId != null}">
                  <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
                  <c:set var="yearHasToolTip" value="false"/>
                  <c:set var="toolTipText" value=""/>
                </c:when>
                <c:otherwise>
                  <img id="year<c:out value="${scenario.year}"/>Ind" src="images/cross.gif" alt="Program Year does not exist."/>
                  <c:set var="yearHasToolTip" value="true"/>
                  <c:set var="toolTipText" value="Data does not exist for this year."/>
                </c:otherwise>
              </c:choose>

              <c:if test="${yearHasToolTip}">
                <script type="text/javascript">
                  //<![CDATA[
                  new YAHOO.widget.Tooltip("year<c:out value="${scenario.year}"/>IndTT",  
                                           { context:"year<c:out value="${scenario.year}"/>Ind",  
                                             text:"<c:out value="${toolTipText}"/>", 
                                             autodismissdelay: 7500,
                                             effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} }); 
                  //]]>
                </script>
              </c:if>

            </td>

            <c:choose>
              <c:when test="${form.newBaseDataArrived[scenario.year]}">
                <td colspan="2" align="center">
                  <span class="button"><a href="javascript:loadWindow('<html:rewrite action="farm805" name="form" property="scenarioParams"/>&amp;diffYear=<c:out value="${scenario.year}"/>', '850', '500')" id="viewDiffReport<c:out value="${scenario.year}"/>Button"><fmt:message key="View.Diff.Report"/></a></span>
                  <script type="text/javascript">
                    //<![CDATA[
                    new YAHOO.widget.Button("viewDiffReport<c:out value="${scenario.year}"/>Button");
                    //]]>
                  </script>
                </td>
              </c:when>
              <c:when test="${!form.readOnly and canCopy and scenario.farmingYear.programYearVersionId == null}">
                <td>
                  <form id="createYearForm0" action="">
                    <c:set var="buttonId" value="createYearButton0" scope="page" />
                    <input type="hidden" name="year" value="<c:out value="${scenario.year}"/>" />
                      <fmt:message key="Num.Ops"/><input type="text" name="numOperations" class="textBoxSmall" value="1" />
                  </form>
                </td>
                <td>
                  <span class="button"><a id="<c:out value="${buttonId}"/>" href="#"><fmt:message key="Create"/></a></span>
                  <script type="text/javascript">
                    //<![CDATA[
                    new YAHOO.widget.Button("<c:out value="${buttonId}"/>");
                    //]]>
                  </script>
                </td>
              </c:when>
            </c:choose>

          </tr>
          <tr>
            <th><fmt:message key="Program.Year.Supplemental"/>:</th>
            <td>
              <c:choose>
                <c:when test="${form.hasProgramYearSupplemental}">
                  <c:set var="supplDate"><fmt:formatDate value="${scenario.craSupplementalReceivedDate}" pattern="yyyy-MM-dd HH:mm:ss"/></c:set>
                  <c:if test="${empty supplDate}">
                    <c:set var="supplDate"><fmt:formatDate value="${scenario.localSupplementalReceivedDate}" pattern="yyyy-MM-dd HH:mm:ss"/></c:set>
                  </c:if>
                  <c:choose>
                    <c:when test="${form.hasCraProgramYearSupplemental and empty scenario.localSupplementalReceivedDate}">
                      <c:set var="supplementalImage" value="images/tick.gif"/>
                      <c:set var="supplementalTooltipMessage"><fmt:message key="Received"/>: <c:out value="${supplDate}"/></c:set>
                    </c:when>
                    <c:otherwise>
                      <c:set var="supplementalImage" value="images/tick_yellow.gif"/>
                      <c:set var="supplementalTooltipMessage"><fmt:message key="supplemental.locally.generated"/>: <c:out value="${supplDate}"/></c:set>
                    </c:otherwise>
                  </c:choose>
                  <img src='<c:out value="${supplementalImage}"/>' alt="<fmt:message key="Yes"/>" id="supplementalTick"/>
                  <u:yuiTooltip targetId="supplementalTick" messageText="${supplementalTooltipMessage}"/>
                </c:when>
                <c:otherwise>
                  <img src="images/cross.gif" alt="<fmt:message key="No"/>" id="supplementalCross"/>
                </c:otherwise>
              </c:choose>
            </td>
            <td colspan="2" align="center">&nbsp;</td>
          </tr>

          <c:forEach var="year" varStatus="yearLoop" items="${form.refYears}">
            <tr>
              <th><fmt:message key="Reference.Year.Data"/> (<c:out value="${year}"/>):</th>
              <td>
                <c:choose>
                  <c:when test="${form.newBaseDataArrived[year]}">
                    <img id="year<c:out value="${year}"/>Ind" src="images/flag_yellow.gif" alt="New Base Data Arrived"/>
                    <c:set var="yearHasToolTip" value="true"/>
                    <c:set var="toolTipText" value="New base data has arrived for this year."/>
                  </c:when>
                  <c:when test="${form.taxYearDataGenerated[year]}">
                    <img id="year<c:out value="${year}"/>Ind" src="images/tick_yellow.gif" alt="Locally Generated Farming Year" />
                    <c:set var="yearHasToolTip" value="true"/>
                    <c:set var="toolTipText" value="The data for this year has been locally generated."/>
                  </c:when>
                  <c:when test="${form.yearScenarioMap[year].farmingYear.programYearVersionId != null}">
                    <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
                    <c:set var="yearHasToolTip" value="false"/>
                    <c:set var="toolTipText" value=""/>
                  </c:when>
                  <c:otherwise>
                    <img id="year<c:out value="${year}"/>Ind" src="images/cross.gif" alt="Program Year does not exist."/>
                    <c:set var="yearHasToolTip" value="true"/>
                    <c:set var="toolTipText" value="Data does not exist for this year."/>
                  </c:otherwise>
                </c:choose>

                <c:if test="${yearHasToolTip}">
                  <script type="text/javascript">
                    //<![CDATA[
                    new YAHOO.widget.Tooltip("year<c:out value="${year}"/>IndTT",  
                                             { context:"year<c:out value="${year}"/>Ind",  
                                               text:"<c:out value="${toolTipText}"/>", 
                                               autodismissdelay: 7500,
                                               effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} }); 
                    //]]>
                  </script>
                </c:if>

              </td>

              <c:choose>
                <c:when test="${form.newBaseDataArrived[year]}">
                  <td colspan="2" align="center">
                    <span class="button"><a href="javascript:loadWindow('<html:rewrite action="farm805" name="form" property="scenarioParams"/>&amp;diffYear=<c:out value="${year}"/>', '850', '500')" id="viewDiffReport<c:out value="${year}"/>Button"><fmt:message key="View.Diff.Report"/></a></span>
                    <script type="text/javascript">
                      //<![CDATA[
                      new YAHOO.widget.Button("viewDiffReport<c:out value="${year}"/>Button");
                      //]]>
                    </script>
                  </td>
                </c:when>
                <c:when test="${!form.readOnly
                                 and canCopy
                                 and form.yearScenarioMap[year].farmingYear.programYearVersionId == null
                                 and form.assignedToCurrentUser}">
                  <td>
                    <c:set var="buttonId" value="createYearButton${yearLoop.count}" scope="page" />
                    <form id="createYearForm<c:out value="${yearLoop.count}"/>" action="">
                      <input type="hidden" name="year" value="<c:out value="${year}"/>" />
                      <fmt:message key="Num.Ops"/><input type="text" name="numOperations" class="textBoxSmall" value="1" />
                    </form>
                  </td>
                  <td>
                    <span class="button"><a id="<c:out value="${buttonId}"/>" href="#"><fmt:message key="Create"/></a></span>
                    <script type="text/javascript">
                      //<![CDATA[
                      new YAHOO.widget.Button("<c:out value="${buttonId}"/>");
                      //]]>
                    </script>
                  </td>
                </c:when>
              </c:choose>

            </tr>
          </c:forEach>
          <c:if test="${scenario.realBenefit}">
          <tr>
            <th><fmt:message key="Calculation.of.Benefit.CoB"/>:</th>
            <td>
              <c:choose>
                <c:when test="${scenario.benefitDocCreatedDate != null}">
                  <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
                </c:when>
                <c:otherwise>
                  <c:choose>
                    <c:when test="${scenario.verified}">
                      <c:set var="cobTTText" value="The Benefit has not yet been calculated."/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="cobTTText" value="The Calculation of Benefit cannot be created until the Claim is Verified."/>
                    </c:otherwise>
                  </c:choose>
                  <img id="COBInd" src="images/cross.gif" alt="No"/>
                  <script type="text/javascript">
                    //<![CDATA[
                    new YAHOO.widget.Tooltip("COBIndTT",  
                                      { context:"COBInd",  
                                        text:"<c:out value="${cobTTText}"/>", 
                                        autodismissdelay: 7500,
                                        effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} });
                    //]]>
                  </script>
                </c:otherwise>
              </c:choose>
            </td>
            <td colspan="2" align="center">
              <c:choose>
                <c:when test="${scenario.hasBenefitDocument}">
                  <a id="viewBenefitDocumentButton" href="javascript:viewBenefitDocument();"><fmt:message key="View.COB"/></a> 
                  <c:if test="${showRegenerateCob}">
                    <br/><br/><a id="regenerateCobButton" href="javascript:regenerateCob();"><fmt:message key="Reprint.COB"/></a> 
                  </c:if>
                </c:when>
                <c:when test="${showGenerateCob}">
                  <a id="generateCobButton" href="javascript:generateCob();"><fmt:message key="Print.COB"/></a>
                </c:when>
                <c:otherwise>
                  &nbsp;
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          </c:if>
          <c:if test="${scenario.coverageNotice}">
          <tr>
            <th><fmt:message key="Coverage.Notice"/>:</th>
            <td>
              <c:choose>
                <c:when test="${scenario.hasBenefitDocument}">
                  <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
                </c:when>
                <c:otherwise>
                  <c:choose>
                    <c:when test="${scenario.completed}">
                      <c:set var="coverageTTText" value="The Coverage Notice has not yet been generated."/>
                    </c:when>
                    <c:otherwise>
                      <c:set var="coverageTTText" value="The Coverage Notice will be created when the scenario is Completed."/>
                    </c:otherwise>
                  </c:choose>
                  <img id="COVInd" src="images/cross.gif" alt="No"/>
                  <script type="text/javascript">
                    //<![CDATA[
                    new YAHOO.widget.Tooltip("COVIndTT",  
                                      { context:"COVInd",  
                                        text:"<c:out value="${coverageTTText}"/>", 
                                        autodismissdelay: 7500,
                                        effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} });
                    //]]>
                  </script>
                </c:otherwise>
              </c:choose>
            </td>
            <td colspan="2" align="center">
              <c:choose>
                <c:when test="${scenario.hasBenefitDocument}">
                  <a id="viewCNButton" href="javascript:viewCoverageNotice();"><fmt:message key="View.Coverage.Notice"/></a> 
                  <c:if test="${showRegenerateCoverageNotice}">
                    <br/><br/><a id="regenerateCNButton" href="javascript:regenerateCoverageNotice();"><fmt:message key="Reprint.Coverage.Notice"/></a> 
                  </c:if>
                </c:when>
                <c:when test="${showGenerateCoverageNotice}">
                  <a id="generateCNButton" href="javascript:generateCoverageNotice();"><fmt:message key="Print.Coverage.Notice"/></a>
                </c:when>
                <c:otherwise>
                  &nbsp;
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          </c:if>
        </table>
      </fieldset>

      <w:ifUserCanPerformAction action="editScenario">
        <c:set var="showLocalDates" value="true"/>  
      </w:ifUserCanPerformAction>
      <c:if test="${ showLocalDates && scenario.farmingYear.programYearVersionId != null}">
        <html:form action="saveDateReceived" styleId="dateReceivedForm"
          method="post">
          <html:hidden property="pin" />
          <html:hidden property="year" />
          <html:hidden property="scenarioNumber" />
          <html:hidden property="scenarioRevisionCount" />
          <fieldset>
            <legend>
              <fmt:message key="Date.Received" />
            </legend>
            <c:set var="localSupplementalReceivedDate">
              <fmt:formatDate
                value="${scenario.localSupplementalReceivedDate}"
                pattern="yyyy-MM-dd" />
            </c:set>
            <c:set var="localStatementAReceivedDate">
              <fmt:formatDate value="${scenario.localStatementAReceivedDate}"
                pattern="yyyy-MM-dd" />
            </c:set>
            <table style="width: 100%" class="formInput">
              <tr>
                <th><fmt:message key="CRA.Supplemental.Date" />:</th>
                <td><fmt:formatDate
                    value="${scenario.craSupplementalReceivedDate}"
                    pattern="yyyy-MM-dd" /></td>
              </tr>
              <tr>
                <th><fmt:message key="Local.Supplemental.Date" />:</th>
                <td>
                  <div style="float: left; width: 80%">
                    <html:text property="localSupplementalReceivedDate" styleId="localSupplementalReceivedDate" size="30" maxlength="10"/>
                  </div><u:datePicker fieldId="localSupplementalReceivedDate" />
                </td>
              </tr>
              <tr>
                <th><fmt:message key="CRA.Statement.A.Received.Date" />:</th>
                <td><fmt:formatDate
                    value="${scenario.craStatementAReceivedDate}"
                    pattern="yyyy-MM-dd" /></td>
              </tr>
              <tr>
                <th><fmt:message key="Local.Statement.A.Received.Date" />:</th>
                <td>
                  <div style="float: left; width: 80%">
                    <html:text property="localStatementAReceivedDate" styleId="localStatementAReceivedDate" size="30" maxlength="10"/>
                  </div><u:datePicker fieldId="localStatementAReceivedDate" />
                </td>
              </tr>
              <tr>
                <th></th>
                <td><script>
                    function submitFunc() {
                      submitForm(document.getElementById('dateReceivedForm'));
                    }
                    registerFormForDirtyCheck(document.getElementById("dateReceivedForm"));
                  </script>
                  <div style="text-align: right">
                    <u:yuiButton buttonLabel="Save" buttonId="saveButton"
                      function="submitFunc" />
                  </div></td>
              </tr>
            </table>
          </fieldset>
        </html:form>
      </c:if>
    </td>

    <td width="50%" valign="top">
      <fieldset>
        <legend><fmt:message key="Scenario.Information"/></legend>
        <table style="width:100%" class="formInput">
          <tr>
            <th><fmt:message key="Scenario.State"/>:</th>
            <td><c:out value="${scenario.scenarioStateCodeDescription}"/></td>
          </tr>
          <tr>
            <th><fmt:message key="Scenario.Category"/>:</th>
            <td><c:out value="${scenario.scenarioCategoryCodeDescription}"/></td>
          </tr>
          <tr>
            <th><fmt:message key="Current.Scenario.Number"/>:</th>
            <td><c:out value="${scenario.scenarioNumber}"/></td>
          </tr>
          <tr>
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
              <br />
              <w:ifUserCanPerformAction action="editScenario">
                <c:if test="${ ! form.assignedToCurrentUser and scenario.farmingYear.programYearVersionId != null }">
                  <u:yuiButton buttonLabel="Check.Out" buttonId="checkOutButton" function="checkOut"/>
                </c:if>
              </w:ifUserCanPerformAction>
            </td>
          </tr>
          <tr>
            <th><fmt:message key="Program.Year.Non.Participant"/>:</th>
            <td>
              <c:choose>
                <c:when test="${form.readOnly}">
                  <c:choose>
                    <c:when test="${form.nonParticipant}">
                      <fmt:message key="Yes"/>
                    </c:when>
                    <c:otherwise>
                      <fmt:message key="No"/>
                    </c:otherwise>
                  </c:choose>
                </c:when>
                <c:otherwise>
                   <script type="text/javascript">
                     //<![CDATA[
                       function onClickNonParticipant() {
                         var isRealBenefit = <c:out value="${scenario.realBenefit}"/>;
                         if(isRealBenefit) {
                           showProcessing();
                           var form = document.getElementById('saveNonParticipantIndForm');
                           form.submit();
                         } else {
                           var nonParticipantCheckbox = document.getElementById("nonParticipant");
                           if(nonParticipantCheckbox.checked) {
                             nonParticipantCheckbox.checked = false;
                           } else {
                             nonParticipantCheckbox.checked = true;
                           }
                           alert('<fmt:message key="text.non-participant.toggle.real.benefit.only"/>');
                         }
                       }
                     //]]>
                   </script>
                  <html:form action="saveNonParticipantInd" styleId="saveNonParticipantIndForm" method="post">
                    <html:hidden property="pin"/>
                    <html:hidden property="year"/>
                    <html:hidden property="scenarioNumber"/>
                    <html:hidden property="scenarioRevisionCount"/>
                    <html:checkbox property="nonParticipant" styleId="nonParticipant" onclick="onClickNonParticipant()"/>
                  </html:form>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <th><fmt:message key="Previous.Year.Verified"/>:</th>
            <td>
              <c:choose>
                <c:when test="${form.previousYearVerified}">
                  <fmt:message key="Yes"/>
                </c:when>
                <c:otherwise>
                  <fmt:message key="No"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <th><fmt:message key="Prev.Yr.Comb.Whole.Farm"/>:</th>
            <td>
              <c:choose>
                <c:when test="${form.previousYearCombinedWholeFarm}">
                  <fmt:message key="Yes"/>
                </c:when>
                <c:otherwise>
                  <fmt:message key="No"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <th><fmt:message key="BPU.Set.Complete"/>:</th>
            <td>
              <c:choose>
                <c:when test="${form.bpuSetComplete}">
                  <fmt:message key="Yes"/>
                </c:when>
                <c:otherwise>
                  <fmt:message key="No"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <th><fmt:message key="FMV.Set.Complete"/>:</th>
            <td>
              <c:choose>
                <c:when test="${form.fmvSetComplete}">
                  <fmt:message key="Yes"/>
                </c:when>
                <c:otherwise>
                  <fmt:message key="No"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <th><fmt:message key="Farm.Type"/>:</th>
            <td>
              <w:ifUserCanPerformAction action="editScenario">
                <c:set var="showFarmTypeDropdown" value="true"/>
              </w:ifUserCanPerformAction>
              <c:choose>
                <c:when test="${ showFarmTypeDropdown && scenario.farmingYear.programYearVersionId != null}">
                  <script type="text/javascript">
                    //<![CDATA[
                      function onSelectFarmType() {
                        var form = document.getElementById('saveFarmTypeForm');
                        showProcessing();
                        form.submit();
                      }
                    //]]>
                  </script>
                  <html:form action="saveFarmType" styleId="saveFarmTypeForm" method="post">
                    <html:hidden property="pin"/>
                    <html:hidden property="year"/>
                    <html:hidden property="scenarioNumber"/>
                    <html:hidden property="scenarioRevisionCount"/>
                    <html:select property="scenario.farmTypeCode" onchange="onSelectFarmType()">
                      <html:option value=""/>
                      <html:optionsCollection name="server.list.farm.types"/>
                    </html:select>
                  </html:form>
                </c:when>
                <c:otherwise>
                  <c:out value="${scenario.farmTypeCodeDescription}"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <tr>
            <th><fmt:message key="Farm.Type.Detailed"/>:</th>
            <td>
              <%-- Values are in the next row --%>
            </td>
          </tr>
          <tr>
            <td colspan="2" style="padding-left: 30px;">
              <c:forEach var="puc" items="${scenario.farmTypeDetailedProductiveCapacities}">
                <c:out value="${puc.code}"/> - <c:out value="${puc.description}"/><br/>
              </c:forEach>
            </td>
          </tr>
          <c:if test="${form.hasEnhancedBenefits}">
            <tr>
              <th><fmt:message key="BC.Enhanced.Benefit"/>:</th>
              <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.benefit.enhancedAdditionalBenefit}"/></td>
            </tr>
          </c:if>
          <tr>
            <th><fmt:message key="Total.Calculated.Benefit"/>:</th>
            <td><fmt:formatNumber type="currency" value="${scenario.farmingYear.benefit.totalBenefit}"/></td>
          </tr>
          <c:if test="${form.displayLateParticipant}">
            <tr>
              <th><fmt:message key="Late.Participant"/>:</th>
              <td>
                <w:ifUserCanPerformAction action="editScenario">
                 <c:set var="showLateParticipantCheckbox" value="true"/>
                </w:ifUserCanPerformAction>
                <c:choose>
                  <c:when test="${ showLateParticipantCheckbox && form.lateParticipantEnabled && scenario.farmingYear.programYearVersionId != null}">
                     <script type="text/javascript">
                       //<![CDATA[
                         function onClickLateParticipant() {
                           var isRealBenefit = <c:out value="${scenario.realBenefit}"/>;
                           showProcessing();
                           var form = document.getElementById('saveLateParticipantIndForm');
                           form.submit();
                         }
                       //]]>
                     </script>
                    <html:form action="saveLateParticipantInd" styleId="saveLateParticipantIndForm" method="post">
                      <html:hidden property="pin"/>
                      <html:hidden property="year"/>
                      <html:hidden property="scenarioNumber"/>
                      <html:hidden property="scenarioRevisionCount"/>
                      <html:checkbox property="lateParticipant" styleId="lateParticipant" onclick="onClickLateParticipant()"/>
                    </html:form>
                  </c:when>
                  <c:otherwise>
                    <c:choose>
                      <c:when test="${form.lateParticipant}">
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
          </c:if>
          <tr>
            <th><fmt:message key="Simplified.Margins.Opt.In"/>:</th>
            <td>
              <c:choose>
                <c:when test="${form.cashMargins}">
                  <fmt:message key="Yes"/>
                </c:when>
                <c:otherwise>
                  <fmt:message key="No"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <c:choose>
            <c:when test="${form.cashMargins}">
              <tr>
                <th><fmt:message key="Simplified.Margins.Opt.In.Date" />:</th>
                <td><fmt:formatDate pattern="yyyy-MM-dd" value="${form.cashMarginsOptInDate}" /></td>
              </tr>
            </c:when>
          </c:choose>
        </table>
      </fieldset>

      <fieldset>
        <legend><fmt:message key="Scenario.State.History"/></legend>
        <table class="formInput" style="width:100%">
          <tr>
            <th><fmt:message key="Date"/></th>
            <th><fmt:message key="State"/></th>
            <th><fmt:message key="User.ID"/></th>
            <th>&nbsp;</th>
          </tr>

          <c:forEach var="audit" varStatus="auditLoop" items="${scenario.scenarioStateAudits}">
            <tr>
              <td><fmt:formatDate value="${audit.stateChangeTimestamp}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
              <td><c:out value="${audit.scenarioStateCodeDesc}"/></td>
              <td><c:out value="${audit.stateChangedByUserIdDisplay}"/></td>
              <td>
                <c:if test="${ !empty audit.stateChangeReason}">
                  <img id="audit<c:out value="${auditLoop.index}"/>Ind" src="images/script.gif" alt="" width="16" height="16" />

                  <script type="text/javascript">
                    //<![CDATA[
                    new YAHOO.widget.Tooltip("audit<c:out value="${auditLoop.index}"/>TT",  
                                      { context:"audit<c:out value="${auditLoop.index}"/>Ind",  
                                        text:"<c:out value="${audit.stateChangeReason}"/>", 
                            autodismissdelay: 7500,
                            effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} }); 
                    //]]>
                  </script>

                </c:if>
              </td>
            </tr>
          </c:forEach>
          
        </table>
      </fieldset>
    </td>
  </tr>
</table>

<script type="text/javascript">
  //<![CDATA[

  if(document.getElementById("createYearButton0")) {
    var cyb0 = new YAHOO.widget.Button("createYearButton0");
    cyb0.subscribe("click", createYearClick);
  }
  if(document.getElementById("createYearButton1")) {
    var cyb1 = new YAHOO.widget.Button("createYearButton1");
    cyb1.subscribe("click", createYearClick);
  }
  if(document.getElementById("createYearButton2")) {
    var cyb2 = new YAHOO.widget.Button("createYearButton2");
    cyb2.subscribe("click", createYearClick);
  }
  if(document.getElementById("createYearButton3")) {
    var cyb3 = new YAHOO.widget.Button("createYearButton3");
    cyb3.subscribe("click", createYearClick);
  }
  if(document.getElementById("createYearButton4")) {
    var cyb4 = new YAHOO.widget.Button("createYearButton4");
    cyb4.subscribe("click", createYearClick);
  }
  if(document.getElementById("createYearButton5")) {
    var cyb5 = new YAHOO.widget.Button("createYearButton5");
    cyb5.subscribe("click", createYearClick);
  }
  
  function createYearClick() {
    var buttonId = this.get("id");
    var buttonNumber = buttonId.substring(16, 17);  // always going to be less than 10
    var formId = "createYearForm" + buttonNumber;
    var createForm = document.getElementById(formId);
    var numOps = createForm.numOperations.value;
    var forYear = createForm.year.value;
    
    if(!isNumeric(numOps)) {
      alert("The number of operations must be a positive number.");
      return false;
    } else if(numOps < 1 || numOps > 100){
      alert("The number of operations must be between 1 and 100.");
      return false;
    }
    
    var msg = "Are you sure you want to create the missing year with " + numOps + " operations for " + forYear + "?";
    
    if(confirm(msg)) {
      showProcessing();
      location.href = "createMissingYear.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>&scenarioRevisionCount=<c:out value="${form.scenarioRevisionCount}"/>&yearToCreate=" + forYear + "&numOperations=" + numOps;
      return true;
    }
    
    return false;
  }
  
  
  
  function isNumeric(someText){
     var validChars = "0123456789";
     var isNumber = true;
     var char;

     for (i = 0; i < someText.length && isNumber == true; i++) { 
      var char = someText.charAt(i); 
      
      if (validChars.indexOf(char) == -1) {
        isNumber = false;
      }
     }
   
     return isNumber;
   }

  
  new YAHOO.widget.Button("viewBenefitDocumentButton");
  function viewBenefitDocument() {
    openNewWindow("viewBenefitDocument.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>");
  }

  <c:if test="${showGenerateCob}">
    new YAHOO.widget.Button("generateCobButton");

    function generateCob() {
      showProcessing();
      document.location.href = "generateBenefitDocument.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
    }
  </c:if>


  <c:if test="${showRegenerateCob}">
    new YAHOO.widget.Button("regenerateCobButton");

    function regenerateCob() {
      if(confirm("<fmt:message key="reprint.cob.warning"/>")) {
        showProcessing();
        document.location.href = "regenerateBenefitDocument.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
      }
    }
  </c:if>
  
  new YAHOO.widget.Button("viewCNButton");
  function viewCoverageNotice() {
    openNewWindow("viewBenefitDocument.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>");
  }
	
	
	<c:if test="${showGenerateCoverageNotice}">
	  new YAHOO.widget.Button("generateCNButton");
	
	  function generateCoverageNotice() {
	    showProcessing();
	    document.location.href = "generateBenefitDocument.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
	  }
	</c:if>
	
	
	<c:if test="${showRegenerateCoverageNotice}">
	  new YAHOO.widget.Button("regenerateCNButton");
	
	  function regenerateCoverageNotice() {
	    if(confirm("<fmt:message key="reprint.coverage.notice.warning"/>")) {
	      showProcessing();
	      document.location.href = "regenerateBenefitDocument.do?<c:out value="${form.scenarioParamsString}" escapeXml="false"/>";
	    }
	  }
	</c:if>
  //]]>
</script>


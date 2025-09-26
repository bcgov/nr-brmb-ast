<%@ include file="/WEB-INF/jsp/common/include.jsp" %>


<script type="text/javascript">
	//<![CDATA[

  function displayFilterResults() {
    if(document.getElementById('inputsRadio').checked == true) type='class3';
    if(document.getElementById('receivablesRadio').checked == true) type='class4';
    if(document.getElementById('payablesRadio').checked == true) type='class5';

    if(document.getElementById('adjustedRadio').checked == true) view='total';
    if(document.getElementById('adjustmentsRadio').checked == true) view='adjust';
    if(document.getElementById('craRadio').checked == true) view='cra';
    var tableId = view + '_' + type;

    <logic-el:iterate name="form" property="itemTypes" id="itemType">
      document.getElementById('total_<c:out value="${itemType}"/>').style.display = 'none';
      document.getElementById('adjust_<c:out value="${itemType}"/>').style.display = 'none';
      document.getElementById('cra_<c:out value="${itemType}"/>').style.display = 'none';
    </logic-el:iterate>

    document.getElementById(tableId).style.display = '';
  }
  
  function onPageLoad() {
    displayFilterResults();
  }

	//]]>
</script>
  
  

<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li><a href="<html:rewrite action="farm500"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Margin.Summary"/></em></a></li> 
    <li><a href="<html:rewrite action="farm510"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Income.and.Expenses"/></em></a></li> 
    <li><a href="<html:rewrite action="farm520"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Inventory"/></em></a></li> 
    <li class="selected"><a href="<html:rewrite action="farm525"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Accruals"/></em></a></li> 
    <li><a href="<html:rewrite action="farm530"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Productive.Capacity"/></em></a></li> 
  </ul>
  
  <div class="yui-content">
    <html:form action="farm525" onsubmit="showProcessing()">
      <html:hidden property="pin"/>
      <html:hidden property="year"/>
      <html:hidden property="scenarioNumber"/>
      
      <table style="width:100%">
        <tr>
          <td>
            <table>
              <tr>
                <td>
                  <fieldset style="z-index:1;position: relative;">
                  <legend><fmt:message key="Farm.View"/></legend>
                    <table>
                      <tr>
                        <td>
                          <u:menuSelect action="farm525.do"
                              name="farmViewYearPicker"
                              paramName="farmViewYear"
                              additionalFieldIds="inputsRadio,receivablesRadio,payablesRadio,adjustedRadio,adjustmentsRadio,craRadio"
                              options="${form.farmViewYearOptions}"
                              selectedValue="${form.farmViewYear}"
                              urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}&farmView=${form.farmView}"
                              toolTip="Click here to change between years of the farm data."/>
                        </td>
                        <td>
                          <u:menuSelect action="farm525.do"
                              name="farmViewPicker"
                              paramName="farmView"
                              additionalFieldIds="inputsRadio,receivablesRadio,payablesRadio,adjustedRadio,adjustmentsRadio,craRadio"
                              options="${form.farmViewOptions}"
                              selectedValue="${form.farmView}"
                              urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}&farmViewYear=${form.farmViewYear}"
                              toolTip="Click here to change between views of the farm data."/>
                        </td>
                      </tr>
                    </table>
                  </fieldset>
                </td>
              </tr>
            </table>
            <table>
              <tr>
                <td>
                  <fieldset>
                    <legend><fmt:message key="Type"/></legend>
                      <table>
                      <tr>
                        <td><html:radio property="itemTypeRadio" styleId="inputsRadio" value="input" onclick="javascript:displayFilterResults();"/><fmt:message key="Input"/></td>
                        <td><html:radio property="itemTypeRadio" styleId="receivablesRadio" value="receivable" onclick="javascript:displayFilterResults();"/><fmt:message key="Receivable"/></td>
                        <td><html:radio property="itemTypeRadio" styleId="payablesRadio" value="payable" onclick="javascript:displayFilterResults();"/><fmt:message key="Payable"/></td>
                      </tr>
                    </table>
                  </fieldset>
                </td>
                <td>
                  <fieldset>
                    <legend><fmt:message key="Financial.View"/></legend>
                      <table>
                      <tr>
                        <td><html-el:radio property="financialViewRadio" styleId="adjustedRadio" value="adjusted" onclick="javascript:displayFilterResults();" disabled="${!scenario.userScenario}" /><fmt:message key="Adjusted"/></td>
                        <td><html-el:radio property="financialViewRadio" styleId="adjustmentsRadio" value="adjustments" onclick="javascript:displayFilterResults();" disabled="${!scenario.userScenario}" /><fmt:message key="Adjustments"/></td>
                        <td><html-el:radio property="financialViewRadio" styleId="craRadio" value="cra" onclick="javascript:displayFilterResults();" disabled="${!scenario.userScenario}" /><fmt:message key="CRA"/></td>
                      </tr>
                    </table>
                  </fieldset>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      
      <logic-el:iterate name="form" property="itemTypes" id="itemType">
        <div id="total_<c:out value="${itemType}"/>" style="display:none;">
          <table class="numeric"  style="width:100%">
            <tr>
              <th><fmt:message key="Code"/></th>
              <th><fmt:message key="Description"/></th>
              <th width="100"><fmt:message key="Start.Value"/></th>
              <th width="100"><fmt:message key="End.Value"/></th>
            </tr>
            <logic-el:iterate name="form" property="lineKeys" id="lineKey">
              <c:if test="${form.items[lineKey].itemType eq itemType}">
                <tr>
                  <th><c:out value="${form.items[lineKey].lineCode}"/></th>
                  <th><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                  <td width="100"><fmt:formatNumber type="number" value="${form.items[lineKey].totalStartOfYearAmount}"/></td>
                  <td width="100"><fmt:formatNumber type="number" value="${form.items[lineKey].totalEndOfYearAmount}"/></td>
                </tr>
              </c:if>
            </logic-el:iterate>
          </table>
        </div>
        
        <div id="adjust_<c:out value="${itemType}"/>" style="display:none;">
          <table class="numeric"  style="width:100%">
            <tr>
              <th><fmt:message key="Code"/></th>
              <th><fmt:message key="Description"/></th>
              <th width="100"><fmt:message key="Start.Value"/></th>
              <th width="100"><fmt:message key="End.Value"/></th>
            </tr>
            <logic-el:iterate name="form" property="lineKeys" id="lineKey">
              <c:if test="${form.items[lineKey].itemType eq itemType}">
                <tr>
                  <th><c:out value="${form.items[lineKey].lineCode}"/></th>
                  <th><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                  <td width="100"><fmt:formatNumber type="number" value="${form.items[lineKey].adjStartOfYearAmount}"/></td>
                  <td width="100"><fmt:formatNumber type="number" value="${form.items[lineKey].adjEndOfYearAmount}"/></td>
                </tr>
              </c:if>
            </logic-el:iterate>
          </table>
        </div>
        
        <div id="cra_<c:out value="${itemType}"/>" style="display:none;">
          <table class="numeric"  style="width:100%">
            <tr>
              <th><fmt:message key="Code"/></th>
              <th><fmt:message key="Description"/></th>
              <th width="100"><fmt:message key="Start.Value"/></th>
              <th width="100"><fmt:message key="End.Value"/></th>
            </tr>
            <logic-el:iterate name="form" property="lineKeys" id="lineKey">
              <c:if test="${form.items[lineKey].itemType eq itemType}">
                <tr>
                  <th><c:out value="${form.items[lineKey].lineCode}"/></th>
                  <th><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                  <td width="100"><fmt:formatNumber type="number" value="${form.items[lineKey].reportedStartOfYearAmount}"/></td>
                  <td width="100"><fmt:formatNumber type="number" value="${form.items[lineKey].reportedEndOfYearAmount}"/></td>
                </tr>
              </c:if>
            </logic-el:iterate>
          </table>
        </div>
        
      </logic-el:iterate>
      
    </html:form>
  </div>
</div> 
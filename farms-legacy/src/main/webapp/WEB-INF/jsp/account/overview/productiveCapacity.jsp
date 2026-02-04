<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript">

  function displayFilterResults() {
    if(document.getElementById('adjustedRadio').checked == true) view='total';
    if(document.getElementById('adjustmentsRadio').checked == true) view='adjust';
    if(document.getElementById('craRadio').checked == true) view='cra';
    var tableId = view;

    document.getElementById('total').style.display = 'none';
    document.getElementById('adjust').style.display = 'none';
    document.getElementById('cra').style.display = 'none';
    document.getElementById(tableId).style.display = '';
  }

  function onPageLoad() {
    displayFilterResults();
  }

</script>

  

<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li><a href="<html:rewrite action="farm500"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Margin.Summary"/></em></a></li> 
    <li><a href="<html:rewrite action="farm510"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Income.and.Expenses"/></em></a></li> 
    <li><a href="<html:rewrite action="farm520"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Inventory"/></em></a></li> 
    <li><a href="<html:rewrite action="farm525"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Accruals"/></em></a></li> 
    <li class="selected"><a href="<html:rewrite action="farm530"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Productive.Capacity"/></em></a></li> 
  </ul>
  
  <div class="yui-content">
    <html:form action="farm530" onsubmit="showProcessing()">
      <html:hidden property="pin"/>
      <html:hidden property="year"/>
      <html:hidden property="scenarioNumber"/>
      
      <table>
        <tr>
          <td>
              <fieldset>
              <legend><fmt:message key="Farm.View"/></legend>
                <table>
                <tr>
                  <td>
                    <u:menuSelect action="farm530.do"
                        name="farmView"
                        paramName="farmView"
                        additionalFieldIds="adjustedRadio,adjustmentsRadio,craRadio"
                        options="${form.farmViewOptions}"
                        selectedValue="${form.farmView}"
                        urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}"
                        toolTip="Click here to change between views of the farm data."/>
                  </td>
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
      
      <div id="total" style="display:none;">
        <table class="numeric" style="width:100%">
          <tr>
            <th><fmt:message key="Code"/></th>
            <th><fmt:message key="Description"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <th width="80"><c:out value="${year}"/></th>
            </logic-el:iterate>
          </tr>
          <logic-el:iterate name="form" property="lineKeys" id="lineKey">
            <tr>
              <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
              <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
              <logic-el:iterate name="form" property="requiredYears" id="year">
                <td width="80">
                  <fmt:formatNumber type="number" value="${form.items[lineKey].adjustedValues[year]}"/>
                </td>
              </logic-el:iterate>
            </tr>
          </logic-el:iterate>
        </table>
      </div>
      
      <div id="adjust" style="display:none;">
        <table class="numeric" style="width:100%">
          <tr>
            <th><fmt:message key="Code"/></th>
            <th><fmt:message key="Description"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <th width="80"><c:out value="${year}"/></th>
            </logic-el:iterate>
          </tr>
          <logic-el:iterate name="form" property="lineKeys" id="lineKey">
            <tr>
              <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
              <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
              <logic-el:iterate name="form" property="requiredYears" id="year">
                <td width="80">
                  <fmt:formatNumber type="number" value="${form.items[lineKey].adjustmentValues[year]}"/>
                </td>
              </logic-el:iterate>
            </tr>
          </logic-el:iterate>
        </table>
      </div>
      
      <div id="cra" style="display:none;">
        <table class="numeric" style="width:100%">
          <tr>
            <th><fmt:message key="Code"/></th>
            <th><fmt:message key="Description"/></th>
            <logic-el:iterate name="form" property="requiredYears" id="year">
              <th width="80"><c:out value="${year}"/></th>
            </logic-el:iterate>
          </tr>
          <logic-el:iterate name="form" property="lineKeys" id="lineKey">
            <tr>
              <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
              <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
              <logic-el:iterate name="form" property="requiredYears" id="year">
                <td width="80">
                  <fmt:formatNumber type="number" value="${form.items[lineKey].craValues[year]}"/>
                </td>
              </logic-el:iterate>
            </tr>
          </logic-el:iterate>
        </table>
      </div>
      
    </html:form>
  </div>
</div> 
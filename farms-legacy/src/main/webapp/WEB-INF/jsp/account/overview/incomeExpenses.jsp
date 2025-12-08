<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>


<script type="text/javascript">

  function displayFilterResults() {
    var displayExpense = false;
    var displayEligible = true;
    var view;

    if(document.getElementById('expenseRadio').checked == true) displayExpense = true;
    if(document.getElementById('ineligibleRadio').checked == true) displayEligible = false;

    if(document.getElementById('adjustedRadio').checked == true) view='total';
    if(document.getElementById('adjustmentsRadio').checked == true) view='adjust';
    if(document.getElementById('craRadio').checked == true) view='cra';

    <logic-el:iterate name="form" property="incomeExpenseFilterValues" id="incomeExpenseFilterValue">
      <logic-el:iterate name="form" property="incomeEligibilityFilterValues" id="incomeEligibilityFilterValue">

        document.getElementById('total_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>').style.display = 'none';
        document.getElementById('adjust_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>').style.display = 'none';
        document.getElementById('cra_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>').style.display = 'none';

      </logic-el:iterate>
    </logic-el:iterate>

    document.getElementById(view + '_' + displayExpense + '_' + displayEligible).style.display = '';
  }


  function onPageLoad() {
    displayFilterResults();
  }

</script>


<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li><a href="<html:rewrite action="farm500"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Margin.Summary"/></em></a></li> 
    <li class="selected"><a href="<html:rewrite action="farm510"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Income.and.Expenses"/></em></a></li> 
    <li><a href="<html:rewrite action="farm520"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Inventory"/></em></a></li> 
    <li><a href="<html:rewrite action="farm525"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Accruals"/></em></a></li> 
    <li><a href="<html:rewrite action="farm530"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>"  onClick="showProcessing()"><em><fmt:message key="Productive.Capacity"/></em></a></li> 
  </ul> 
  <div class="yui-content"> 
    <html:form action="farm510" onsubmit="showProcessing()">
      <html:hidden property="pin"/>
      <html:hidden property="year"/>
      <html:hidden property="scenarioNumber"/>

      <table>
        <tr>
          <td>
            <fieldset style="z-index:1;position: relative;">
              <legend><fmt:message key="Farm.View"/></legend>
              <table>
                <tr>
                  <td>
                    <u:menuSelect action="farm510.do"
                        name="farmViewPicker"
                        paramName="farmView"
                        additionalFieldIds="incomeRadio,expenseRadio,eligibleRadio,ineligibleRadio,adjustedRadio,adjustmentsRadio,craRadio"
                        options="${form.farmViewOptions}"
                        selectedValue="${form.farmView}"
                        urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}"
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
              <legend><fmt:message key="Income"/>/<fmt:message key="Expenses"/></legend>
              <table>
                <tr>
                  <td><html:radio property="incomeExpenseRadio" styleId="incomeRadio" value="income" onclick="javascript:displayFilterResults();"/><fmt:message key="Income"/></td>
                  <td><html:radio property="incomeExpenseRadio" styleId="expenseRadio" value="expense" onclick="javascript:displayFilterResults();"/><fmt:message key="Expenses"/></td>
                </tr>
              </table>
            </fieldset>
          </td>

          <td>
            <fieldset>
              <legend><fmt:message key="Eligible"/>/<fmt:message key="Ineligible"/></legend>
              <table>
                <tr>
                  <td><html:radio property="eligibilityRadio" styleId="eligibleRadio" value="eligible" onclick="javascript:displayFilterResults();"/><fmt:message key="Eligible"/></td>
                  <td><html:radio property="eligibilityRadio" styleId="ineligibleRadio" value="ineligible" onclick="javascript:displayFilterResults();"/><fmt:message key="Ineligible"/></td>
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

      <logic-el:iterate name="form" property="incomeExpenseFilterValues" id="incomeExpenseFilterValue">
        <logic-el:iterate name="form" property="incomeEligibilityFilterValues" id="incomeEligibilityFilterValue">

          <div id="total_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" style="display:none;">
            <table class="numeric" style="width:100%">
              <tr>
                <th><fmt:message key="Code"/></th>
                <th><fmt:message key="Description"/></th>
                <logic-el:iterate name="form" property="requiredYears" id="year">
                  <th width="80"><c:out value="${year}"/></th>
                </logic-el:iterate>
              </tr>

              <logic-el:iterate name="form" property="lineKeys" id="lineKey">
                <c:if test="${form.items[lineKey].new == false
                              and form.items[lineKey].expense == incomeExpenseFilterValue
                              and form.items[lineKey].eligible == incomeEligibilityFilterValue}">
                  <tr>
                    <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
                    <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                    <logic-el:iterate name="form" property="requiredYears" id="year">
                      <td width="80">
                        <fmt:formatNumber type="currency" value="${form.items[lineKey].adjustedValues[year]}"/>
                      </td>
                    </logic-el:iterate>
                  </tr>
                </c:if>
              </logic-el:iterate>
            </table>
          </div>

          <div id="adjust_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" style="display:none;">
            <table class="numeric" style="width:100%">
              <tr>
                <th><fmt:message key="Code"/></th>
                <th><fmt:message key="Description"/></th>
                <logic-el:iterate name="form" property="requiredYears" id="year">
                  <th width="80"><c:out value="${year}"/></th>
                </logic-el:iterate>
              </tr>

              <logic-el:iterate name="form" property="lineKeys" id="lineKey">
                <c:if test="${form.items[lineKey].new == false
                              and form.items[lineKey].expense == incomeExpenseFilterValue
                              and form.items[lineKey].eligible == incomeEligibilityFilterValue}">
                  <tr>
                    <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
                    <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                    <logic-el:iterate name="form" property="requiredYears" id="year">
                      <td width="80">
                        <fmt:formatNumber type="currency" value="${form.items[lineKey].adjustmentValues[year]}"/>
                      </td>
                    </logic-el:iterate>
                  </tr>
                </c:if>
              </logic-el:iterate>
            </table>
          </div>

          <div id="cra_<c:out value="${incomeExpenseFilterValue}_${incomeEligibilityFilterValue}"/>" style="display:none;">
            <table class="numeric" style="width:100%">
              <tr>
                  <th><fmt:message key="Code"/></th>
                  <th><fmt:message key="Description"/></th>
                <logic-el:iterate name="form" property="requiredYears" id="year">
                  <th width="80"><c:out value="${year}"/></th>
                </logic-el:iterate>
              </tr>

              <logic-el:iterate name="form" property="lineKeys" id="lineKey">
                <c:if test="${form.items[lineKey].new == false
                              and form.items[lineKey].expense == incomeExpenseFilterValue
                              and form.items[lineKey].eligible == incomeEligibilityFilterValue}">
                  <tr>
                    <th class="row"><c:out value="${form.items[lineKey].lineCode}"/></th>
                    <th class="row"><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                    <logic-el:iterate name="form" property="requiredYears" id="year">
                      <td width="80">
                        <fmt:formatNumber type="currency" value="${form.items[lineKey].craValues[year]}"/>
                      </td>
                    </logic-el:iterate>
                  </tr>
                </c:if>
              </logic-el:iterate>
            </table>
          </div>    
        </logic-el:iterate>
      </logic-el:iterate>
    </html:form>
  </div> 
</div> 
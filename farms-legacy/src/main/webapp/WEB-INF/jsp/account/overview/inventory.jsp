<%@ include file="/WEB-INF/jsp/common/include.jsp" %>


<script>

  function displayFilterResults() {
    if(document.getElementById('cropsRadio').checked == true) type='<c:out value="${form.typeCrop}"/>';
    if(document.getElementById('livestockRadio').checked == true) type='<c:out value="${form.typeLivestock}"/>';

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

</script>
  
  

<div class="yui-navset"> 
  <ul class="yui-nav"> 
    <li><a href="<html:rewrite action="farm500"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Margin.Summary"/></em></a></li> 
    <li><a href="<html:rewrite action="farm510"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Income.and.Expenses"/></em></a></li> 
    <li class="selected"><a href="<html:rewrite action="farm520"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Inventory"/></em></a></li> 
    <li><a href="<html:rewrite action="farm525"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Accruals"/></em></a></li> 
    <li><a href="<html:rewrite action="farm530"/>?pin=<c:out value="${form.pin}"/>&year=<c:out value="${form.year}"/>" onClick="showProcessing()"><em><fmt:message key="Productive.Capacity"/></em></a></li> 
  </ul>
  
  <div class="yui-content">
    <html:form action="farm520" onsubmit="showProcessing()">
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
                          <u:menuSelect action="farm520.do"
                              name="farmViewYearPicker"
                              paramName="farmViewYear"
                              additionalFieldIds="cropsRadio,livestockRadio,adjustedRadio,adjustmentsRadio,craRadio"
                              options="${form.farmViewYearOptions}"
                              selectedValue="${form.farmViewYear}"
                              urlParams="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}&farmView=${form.farmView}"
                              toolTip="Click here to change between years of the farm data."/>
                        </td>
                        <td>
                          <u:menuSelect action="farm520.do"
                              name="farmViewPicker"
                              paramName="farmView"
                              additionalFieldIds="cropsRadio,livestockRadio,adjustedRadio,adjustmentsRadio,craRadio"
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
                        <td><html:radio property="itemTypeRadio" styleId="cropsRadio" value="crop" onclick="javascript:displayFilterResults();"/><fmt:message key="Crop"/></td>
                        <td><html:radio property="itemTypeRadio" styleId="livestockRadio" value="livestock" onclick="javascript:displayFilterResults();"/><fmt:message key="Livestock"/></td>
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
              <c:if test="${itemType eq form.typeCrop}">
                <th><fmt:message key="Units"/></th>
                <th><fmt:message key="On.Farm.Acres"/></th>
                <th width="80"><fmt:message key="Qty.Produced"/></th>
              </c:if>
              <c:if test="${itemType eq form.typeLivestock}">
                <th><fmt:message key="Market.Commodity"/></th>
              </c:if>
              <th width="80"><fmt:message key="Qty.Start"/></th>
              <th width="80"><fmt:message key="Price.Start"/></th>
              <th width="80"><fmt:message key="Qty.End"/></th>
              <th width="80"><fmt:message key="Price.End"/></th>
            </tr>
            <logic-el:iterate name="form" property="lineKeys" id="lineKey">
              <c:if test="${form.items[lineKey].itemType eq itemType}">
                <tr>
                  <th><c:out value="${form.items[lineKey].lineCode}"/></th>
                  <th><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                  <c:if test="${itemType eq form.typeCrop}">
                    <th><c:out value="${form.items[lineKey].cropUnitCodeDescription}"/></th>
                    <th><c:out value="${form.items[lineKey].onFarmAcres}"/></th>
                    <td><fmt:formatNumber type="number" value="${form.items[lineKey].totalQuantityProduced}"/></td>
                  </c:if>
                  <c:if test="${itemType eq form.typeLivestock}">
                    <th>
                      <c:if test="${form.items[lineKey].marketCommodity}">
                        <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
                      </c:if>
                    </th>
                  </c:if>
                  <td><fmt:formatNumber type="number" value="${form.items[lineKey].totalQuantityStart}"/></td>
                  <td><fmt:formatNumber type="currency" value="${form.items[lineKey].totalPriceStart}"/></td>
                  <td><fmt:formatNumber type="number" value="${form.items[lineKey].totalQuantityEnd}"/></td>
                  <td><fmt:formatNumber type="currency" value="${form.items[lineKey].totalPriceEnd}"/></td>
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
              <c:if test="${itemType eq form.typeCrop}">
                <th><fmt:message key="Units"/></th>
                <th><fmt:message key="On.Farm.Acres"/></th>
                <th width="80"><fmt:message key="Qty.Produced"/></th>
              </c:if>
              <c:if test="${itemType eq form.typeLivestock}">
                <th><fmt:message key="Market.Commodity"/></th>
              </c:if>
              <th width="80"><fmt:message key="Qty.Start"/></th>
              <th width="80"><fmt:message key="Price.Start"/></th>
              <th width="80"><fmt:message key="Qty.End"/></th>
              <th width="80"><fmt:message key="Price.End"/></th>
            </tr>
            
              <logic-el:iterate name="form" property="lineKeys" id="lineKey">
                <c:if test="${form.items[lineKey].itemType eq itemType}">
                  <tr>
                    <th><c:out value="${form.items[lineKey].lineCode}"/></th>
                    <th><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                    <c:if test="${itemType eq form.typeCrop}">
                      <th><c:out value="${form.items[lineKey].cropUnitCodeDescription}"/></th>
                      <th><c:out value="${form.items[lineKey].onFarmAcres}"/></th>
                      <td><fmt:formatNumber type="number" value="${form.items[lineKey].adjQuantityProduced}"/></td>
                    </c:if>
                    <c:if test="${itemType eq form.typeLivestock}">
                      <th>
                        <c:if test="${form.items[lineKey].marketCommodity}">
                          <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
                        </c:if>
                      </th>
                    </c:if>
                    <td><fmt:formatNumber type="number" value="${form.items[lineKey].adjQuantityStart}"/></td>
                    <td><fmt:formatNumber type="currency" value="${form.items[lineKey].adjPriceStart}"/></td>
                    <td><fmt:formatNumber type="number" value="${form.items[lineKey].adjQuantityEnd}"/></td>
                    <td><fmt:formatNumber type="currency" value="${form.items[lineKey].adjPriceEnd}"/></td>
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
              <c:if test="${itemType eq form.typeCrop}">
                <th><fmt:message key="Units"/></th>
                <th><fmt:message key="On.Farm.Acres"/></th>
                <th width="80"><fmt:message key="Qty.Produced"/></th>
              </c:if>
              <c:if test="${itemType eq form.typeLivestock}">
                <th><fmt:message key="Market.Commodity"/></th>
              </c:if>
              <th width="80"><fmt:message key="Qty.Start"/></th>
              <th width="80"><fmt:message key="Price.Start"/></th>
              <th width="80"><fmt:message key="Qty.End"/></th>
              <th width="80"><fmt:message key="Price.End"/></th>
            </tr>
            <logic-el:iterate name="form" property="lineKeys" id="lineKey">
              <c:if test="${form.items[lineKey].itemType eq itemType}">
                <tr>
                  <th><c:out value="${form.items[lineKey].lineCode}"/></th>
                  <th><c:out value="${form.items[lineKey].lineCodeDescription}"/></th>
                  <c:if test="${itemType eq form.typeCrop}">
                    <th><c:out value="${form.items[lineKey].cropUnitCodeDescription}"/></th>
                    <th><c:out value="${form.items[lineKey].onFarmAcres}"/></th>
                    <td><fmt:formatNumber type="number" value="${form.items[lineKey].reportedQuantityProduced}"/></td>
                  </c:if>
                  <c:if test="${itemType eq form.typeLivestock}">
                    <th>
                      <c:if test="${form.items[lineKey].marketCommodity}">
                        <img src="images/tick.gif" alt="<fmt:message key="Yes"/>" />
                      </c:if>
                    </th>
                  </c:if>
                  <td><fmt:formatNumber type="number" value="${form.items[lineKey].reportedQuantityStart}"/></td>
                  <td><fmt:formatNumber type="currency" value="${form.items[lineKey].reportedPriceStart}"/></td>
                  <td><fmt:formatNumber type="number" value="${form.items[lineKey].reportedQuantityEnd}"/></td>
                  <td><fmt:formatNumber type="currency" value="${form.items[lineKey].reportedPriceEnd}"/></td>
                </tr>
              </c:if>
            </logic-el:iterate>
          </table>
        </div>
        
      </logic-el:iterate>
    </html:form>
  </div>
</div> 
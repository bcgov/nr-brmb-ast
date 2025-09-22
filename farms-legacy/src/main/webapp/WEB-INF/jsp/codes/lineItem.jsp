<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getApplicationCache var="sectorDetailCodes" scope="request" key="server.list.sector.details"/>

<w:ifUserCanPerformAction action="editCodes">
  
  <script type="text/javascript">
    //<![CDATA[

    Farm.sectors = [];
    Farm.sectors[''] = '';
    //<c:forEach var="sector" items="${sectorDetailCodes}">
    Farm.sectors['<c:out value="${sector.sectorDetailCode}"/>'] = '<c:out value="${sector.sectorCodeDescription}"/>';
    //</c:forEach>

    $(function() {
      $("#sectorDetailCode").on("change", function(e) {
        var sectorDetailCode = $(e.target).val();
        var sectorCodeDescription = Farm.sectors[sectorDetailCode];
        $("#sectorCodeDescription").val(sectorCodeDescription);
        $("#sectorCodeDescriptionTd").text(sectorCodeDescription);
      });
  });
    //]]>
  </script>

</w:ifUserCanPerformAction>

<w:ifUserCanPerformAction action="editCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<h1>
  <c:choose>
    <c:when test="${!canEdit}">
      <fmt:message key="View"/>
    </c:when>
    <c:when test="${form.new}">
      <fmt:message key="Create.New"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="Edit"/>
    </c:otherwise>
  </c:choose>
  <fmt:message key="Line.Item"/>
</h1>
<p></p>

<html:form action="saveLineItem" styleId="lineItemForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="lineItemId"/>
  <html:hidden property="lineItemYear"/>
  <html:hidden property="sectorDetailLineItemId"/>
  <html:hidden property="sectorCodeDescription"/>
  <html:hidden property="commodityTypeCodeDescription"/>
  <html:hidden property="fruitVegTypeCodeDescription"/>
  <html:hidden property="revisionCount"/>
  
  <c:if test="${!form.new}">
    <html:hidden property="lineItem"/>
  </c:if>

  <fieldset style="width:97%">
    <table class="formInput" style="width:97%">
      <tr>
        <th style="width:150px"><fmt:message key="Program.Year"/>:</th>
        <td>
          <div style="float:left;width:15%">
            <c:out value="${form.lineItemYear}"/>
          </div>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Code"/>:</th>
        <td>
          <div style="float:left;width:15%">
	          <c:choose>
	            <c:when test="${form.new}">
	              <html:text property="lineItem" size="30" maxlength="4"/>
	            </c:when>
	            <c:otherwise>
	              <c:out value="${form.lineItem}"/>
	            </c:otherwise>
	          </c:choose>
          </div>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Description"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <html:text property="description" size="80" maxlength="256"/>
            </c:when>
            <c:otherwise>
              <c:out value="${form.description}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th>
          <c:choose>
            <c:when test="${form.growingForward2013}">
              <fmt:message key="Eligible.in.Program.Year"/>:
            </c:when>
            <c:otherwise>
              <fmt:message key="Eligible"/>:
            </c:otherwise>
          </c:choose>
        </th>
        <td>
          <div style="float:left;width:100%">
            <html:checkbox property="eligible"/>
          </div>
        </td>
      </tr>
      <c:if test="${form.growingForward2013}">
	      <tr>
	        <th><fmt:message key="Eligible.in.Reference.Years"/>:</th>
	        <td>
	          <div style="float:left;width:100%">
	            <html:checkbox property="eligibleRefYears"/>
	          </div>
	        </td>
	      </tr>
      </c:if>
      <tr>
        <th><fmt:message key="Yardage"/>:</th>
        <td>
          <div style="float:left;width:100%">
            <html:checkbox property="yardage"/>
          </div>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Program.Payment"/>:</th>
        <td>
          <div style="float:left;width:100%">
            <html:checkbox property="programPayment"/>
          </div>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Contract.Work"/>:</th>
        <td>
          <div style="float:left;width:100%">
            <html:checkbox property="contractWork"/>
          </div>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Supply.Managed.Commodity"/>:</th>
        <td>
          <div style="float:left;width:100%">
            <html:checkbox property="supplyManagedCommodity"/>
          </div>
        </td>
      </tr>
      <c:if test="${form.growingForward2013}">
        <tr>
          <th><fmt:message key="Exclude.From.Revenue.Calculation"/>:</th>
          <td>
            <div style="float:left;width:100%">
              <html:checkbox property="excludeFromRevenueCalculation"/>
            </div>
          </td>
        </tr>
      </c:if>
      <tr>
        <th><fmt:message key="Industry.Average.Expense"/>:</th>
        <td>
          <div style="float:left;width:100%">
            <html:checkbox property="industryAverageExpense"/>
          </div>
        </td>
      </tr>           
      <tr>
        <th><fmt:message key="Farm.Type"/>:</th>
        <td id="sectorCodeDescriptionTd"><c:out value="${form.sectorCodeDescription}"/></td>
      </tr>
      <tr>
        <th><fmt:message key="Farm.Type.Detailed"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <div style="float:left;width:40%">
                <html:select property="sectorDetailCode" styleId="sectorDetailCode">
                  <option value=""></option>
                  <html:optionsCollection name="server.list.sector.details"/>
                </html:select>
              </div>
            </c:when>
            <c:otherwise>
              <c:out value="${form.sectorDetailCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Fruit.Veg.Type"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <div style="float:left;width:40%">
                <html:select property="fruitVegTypeCode">
                  <option value=""></option>
                  <html:optionsCollection property="fruitVegListViewItems"/>
                </html:select>
              </div>
            </c:when>
            <c:otherwise>
              <c:out value="${form.fruitVegTypeCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
      <tr>
        <th><fmt:message key="Commodity.Type"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
              <div style="float:left;width:40%">
                <html:select property="commodityTypeCode">
                  <option value=""></option>
                  <html:optionsCollection property="commodityTypesListViewItems"/>
                </html:select>
              </div>
            </c:when>
            <c:otherwise>
              <c:out value="${form.commodityTypeCodeDescription}"/>
            </c:otherwise>
          </c:choose>
        </td>
      </tr>    
    </table>
  </fieldset>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="lineItemForm"/>
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="lineItemForm" action="deleteLineItem" confirmMessage="delete.line.item.warning"/>
      </c:if>
      <u:dirtyFormCheck formId="lineItemForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm280"/>
  </div>

</html:form>

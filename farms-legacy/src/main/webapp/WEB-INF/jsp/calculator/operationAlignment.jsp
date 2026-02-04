<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<w:ifUserCanPerformAction action="opAlignEditAllYears" var="editAllYears"></w:ifUserCanPerformAction>

<script type="text/javascript" src="yui/2.8.2r1/build/json/json-min.js"></script>

<logic:messagesPresent name="infoMessages">
  <div class="messages">
    <ul>
      <html:messages id="message" name="infoMessages">
        <li><c:out value="${message}"/></li>
      </html:messages>
    </ul>
  </div>
</logic:messagesPresent>

<table cellpadding="40" style="width:100%">
  <tr>
    <td valign="top" scope="col">
      <html:form action="saveOperationAlignment" styleId="operationAlignmentForm" method="post" onsubmit="showProcessing()">
        <html:hidden property="pin"/>
        <html:hidden property="year"/>
        <html:hidden property="scenarioNumber"/>
        <html:hidden property="scenarioRevisionCount"/>

        <fieldset>
          <legend>Operation Alignment</legend>

          <table class="formInput">
            <tr>
              <logic-el:iterate name="form" property="requiredYears" id="year">
                <th scope="col">&nbsp;</th>
                <th scope="col"><c:out value="${year}"/></th>
              </logic-el:iterate>
            </tr>

            <logic-el:iterate name="form" property="schedules" id="schedule">
              <tr>
              <th>OP Map <c:out value="${schedule}"/></th>
                <logic-el:iterate name="form" property="requiredYears" id="year" indexId="yearIndex">
                  <c:set var="fieldName" value="opYearFormData(${year}).scheduleOpNum(${schedule})"/>
                  <c:set var="fieldId" value="opYearFormData${year}_scheduleOpNum${schedule}"/>
                  <td>
                    <c:choose>
                      <c:when test="${form.yearScheduleOpNumMap[year].errors[schedule]}">
                        <c:set var="inputClass" value="adjustmentExistsError"/>
                      </c:when>
                      <c:otherwise>
                        <c:set var="inputClass" value=""/>
                      </c:otherwise>
                    </c:choose>
                    <c:choose>
                      <c:when test="${form.year == year or editAllYears}">
                        <c:set var="readonly" value="false"/>
                      </c:when>
                      <c:otherwise>
                        <c:set var="readonly" value="true"/>
                        <c:set var="inputClass" value="readonly"/>
                      </c:otherwise>
                    </c:choose>
                    <html-el:text property="${fieldName}" styleId="${fieldId}" onfocus="updateOpInfo('${fieldId}')" style="width:40px;text-align:center" styleClass="${inputClass}" readonly="${readonly}"/>
                    </td>
                  <td>
                    <c:if test="${yearIndex < (form.numberOfRequiredYears - 1)}">
                      <img src="images/red_arrow.gif" alt="->" width="9" height="11" />
                    </c:if>
                  </td>
                </logic-el:iterate>
              </tr>
            </logic-el:iterate>

          </table>
        </fieldset>

      </html:form>
    </td>

    <td width="60%" valign="top" scope="col">
      <fieldset>
        <legend>Operation Detail</legend>
        <table class="formInput">
          <tr>
            <th>Year:</th>
            <td><div id="opYear"></div></td>
          </tr>
          <tr>
            <th>Operation Number:</th>
            <td><div id="opNumber"></div></td>
          </tr>
          <tr>
            <th>Partnership Name:</th>
            <td><div id="opPartName"></div></td>
          </tr>
          <tr>
            <th>Partnership Percent:</th>
            <td><div id="opPartPercent"></div></td>
          </tr>

          <tr>
            <th>Partnership PIN:</th>
            <td><div id="opPartPin"></div></td>
          </tr>

          <tr>
            <th>Income (Top 5):</th>
            <td>
              <ol id="opTop5Income" style="padding-left:0px;margin-top:0px;margin-bottom:0px">
                <li></li>
              </ol>
            </td>
          </tr>
        </table>
      </fieldset>
    </td>
  </tr>
</table>

<c:if test="${ ! form.readOnly }">
  <div style="text-align:right">
    <a id="saveButton" href="#"><fmt:message key="Save"/></a>
  </div>
</c:if>

<script type="text/javascript">
  //<![CDATA[
  new YAHOO.widget.Button("saveButton");
  function submitFunc() {
    var msg = "Changing the alignment may affect previous adjustments and calculations." +
      " Automated inventory valuation adjustments may have been performed and should be reviewed.";
    if(confirm(msg)) {
      submitForm(document.getElementById('operationAlignmentForm'));
    }
  }
  YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", submitFunc);

  <c:if test="${ ! form.readOnly }">
    registerFormForDirtyCheck(document.getElementById("operationAlignmentForm"));
  </c:if>

  var opData ='[' + 
        <c:forEach var="detail" items="${form.operationDetails}" varStatus="detailLoop">
          '{"year":"<c:out value="${detail.year}"/>","op":"<c:out value="${detail.operationNumber}"/>","partnershipName":"<c:out value="${detail.partnershipName}"/>","partnershipPercent":"<c:out value="${detail.partnershipPercent}"/>","partnershipPin":"<c:out value="${detail.partnershipPin}"/>",' + 
          '"topIncome1":"<c:out value="${detail.topIncome[0]}"/>",' + 
          '"topIncome2":"<c:out value="${detail.topIncome[1]}"/>",' +
          '"topIncome3":"<c:out value="${detail.topIncome[2]}"/>",' +
          '"topIncome4":"<c:out value="${detail.topIncome[3]}"/>",' +
          '"topIncome5":"<c:out value="${detail.topIncome[4]}"/>"}' +
          <c:if test="${ ! detailLoop.last }">
            ',' +
          </c:if>
        </c:forEach>
        ']';
 
 
  var ops = YAHOO.lang.JSON.parse(opData);        
  function blankOpInfo() {
    document.getElementById('opYear').innerHTML = '';
    document.getElementById('opNumber').innerHTML = '';
    document.getElementById('opPartName').innerHTML = '';
    document.getElementById('opPartPercent').innerHTML = '';
    document.getElementById('opPartPin').innerHTML = '';
    document.getElementById('opTop5Income').innerHTML = '';
  }       

  function updateOpInfo(textBoxId) {
    // Blank the values
    blankOpInfo();
    var tb = document.getElementById(textBoxId);
    var year = textBoxId.substr(14, 4);
    var op = tb.value;
    for (var i = 0, len = ops.length; i < len; ++i) {
      if( (ops[i].year == year) && (ops[i].op == op) ) {
        // update the op info table
        document.getElementById('opYear').innerHTML = ops[i].year;
        document.getElementById('opNumber').innerHTML = ops[i].op;
        document.getElementById('opPartName').innerHTML = ops[i].partnershipName;
        document.getElementById('opPartPercent').innerHTML = ops[i].partnershipPercent;
        document.getElementById('opPartPin').innerHTML = ops[i].partnershipPin;
        var liString = "<li>" + ops[i].topIncome1 + "</li>";
        liString += "<li>" + ops[i].topIncome2 + "</li>";
        liString += "<li>" + ops[i].topIncome3 + "</li>";
        liString += "<li>" + ops[i].topIncome4 + "</li>";
        liString += "<li>" + ops[i].topIncome5 + "</li>";
        document.getElementById('opTop5Income').innerHTML = liString;
      }
    }
  }

  blankOpInfo();                        

  //]]>
</script>

<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:getScenario var="scenario" pin="${form.pin}" year="${form.year}" scenarioNumber="${form.scenarioNumber}" scope="request"/>

<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/vue.min.js"></script>
<script>

</script>

<html:form action="saveEnwEnrolment" styleId="enwEnrolmentForm" onsubmit="showProcessing()">
  
  <div id="vueApp" v-cloak>
    <html:hidden property="pin"/>
    <html:hidden property="year"/>
    <html:hidden property="scenarioNumber"/>
    <html:hidden property="scenarioRevisionCount"/>
    <html:hidden property="enrolmentJson" styleId="enrolmentJson"/>
    <input type="hidden" name="enrolmentJsonReactive" id="enrolmentJsonReactive" v-model="JSON.stringify(enw)" />
    
    
    <fieldset style="margin-bottom: 30px;">
      <div>
        <h1>
          <fmt:message key="Enrolment.Fee"/>
        </h1>
        
        <div class="rTable numeric blueGrayTable">
          <div class="rTableRow">
            <div class="rTableHead"><fmt:message key="Contribution.Margin"/></div>
            <div class="rTableHead"><fmt:message key="Fee"/></div>
          </div>
          <div class="rTableRow">
            <div class="rTableCell alignRight">&nbsp;{{enw.contributionMargin | toCurrency}}</div>
            <div class="rTableCell alignRight">&nbsp;{{enw.enrolmentFee | toCurrency}}</div>
          </div> 
        </div>
      </div>
    </fieldset>
    
    
    <fieldset>
      <h1>
        <input type="radio" name="enrolmentCalculationTypeCode" v-model="enw.enrolmentCalculationTypeCode" value="BENEFIT" @change="updateFeeToUse" :disabled="readOnly">
        <fmt:message key="Calculate.using.Benefit.Margins"/>
      </h1>
      
      <div v-if="enw.benefitCalculated">
        <p v-if="enw.inCombinedFarm">Combined Farm: {{enw.combinedFarmPercent | toPercent1}} has been applied to each margin.</p>
        <h2>
          <fmt:message key="Production.Margins.after.Structural.Change"/>
        </h2 >
  
        <div class="rTable numeric blueGrayTable">
          <div class="rTableRow">
            <div class="rTableHead"></div>
            <div class="rTableHead alignCenter" v-for="year in enw.benefitMarginYears">{{ year }}</div>
            <div class="rTableHead alignCenter"><fmt:message key="Contribution.Margin"/></div>
            <div class="rTableHead alignCenter"><fmt:message key="Enrolment.Fee"/></div>
          </div>
          <div class="rTableRow">
            <div class="rTableHead"><fmt:message key="Margin"/></div>
            <div class="rTableCell alignRight">{{enw.benefitMarginYearMinus6 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.benefitMarginYearMinus5 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.benefitMarginYearMinus4 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.benefitMarginYearMinus3 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.benefitMarginYearMinus2 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.benefitContributionMargin | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.benefitEnrolmentFee | toCurrency}}</div>
          </div> 
          <div class="rTableRow">
            <div class="rTableHead"><fmt:message key="Used.in.Calculation"/></div>
            <div class="rTableCell alignCenter">{{enw.benefitMarginYearMinus6Used | toYesNo}}</div>
            <div class="rTableCell alignCenter">{{enw.benefitMarginYearMinus5Used | toYesNo}}</div>
            <div class="rTableCell alignCenter">{{enw.benefitMarginYearMinus4Used | toYesNo}}</div>
            <div class="rTableCell alignCenter">{{enw.benefitMarginYearMinus3Used | toYesNo}}</div>
            <div class="rTableCell alignCenter">{{enw.benefitMarginYearMinus2Used | toYesNo}}</div>
            <div class="rTableCell"></div>
            <div class="rTableCell"></div>
          </div> 
        </div>
      </div>
      <div v-else class="errors">
          <ul>
            <li>Benefit calculation failed. Unable to calculate from Production Margins after Structural Change.</li>
          </ul>
      </div>
    </fieldset>
    
    
    <fieldset>
      <h1>
        <input type="radio" name="enrolmentCalculationTypeCode" v-model="enw.enrolmentCalculationTypeCode" value="PROXY" @change="updateFeeToUse" :disabled="readOnly">
        <fmt:message key="Calculate.using.Proxy.Margins"/>
      </h1>
        
      <div v-if="enw.proxyMarginsCalculated">
        <p v-if="enw.inCombinedFarm">Combined Farm: {{enw.combinedFarmPercent | toPercent1}} has been applied to each margin.</p>
        <h2>
          <fmt:message key="Program.Year.Productive.Value"/>
    		</h2>
    
        <div class="rTable numeric blueGrayTable" style="width: 100%;">
          <div class="rTableRow">
            <div class="rTableHead"><fmt:message key="Code"/></div>
            <div class="rTableHead"><fmt:message key="Description"/></div>
            <div class="rTableHead"><fmt:message key="Productive.Capacity"/></div>
            <template v-for="year in enw.proxyMarginYears">
              <div class="rTableHead alignRight">{{ year }} <fmt:message key="BPU"/></div>
              <div class="rTableHead alignRight">{{ year }} <fmt:message key="Margin"/></div>
            </template>
          </div>
          <div class="rTableRow" v-for="puc in enw.enwProductiveUnits">
            <div class="rTableHead">{{puc.code}}</div>
            <div class="rTableHead">{{puc.description}}</div>
            <div class="rTableCell alignRight">{{puc.productiveCapacity | toDecimal3}}</div>
            <template v-for="enwProductiveValue in puc.productiveValues">
              <div class="rTableCell alignRight">{{ enwProductiveValue.bpuMargin | toCurrency}}</div>
              <div class="rTableCell alignRight">{{ enwProductiveValue.productiveValue | toCurrency}}</div>
            </template>
          </div>
          <div class="rTableRow">
            <div class="rTableHead"><fmt:message key="Total"/></div>
            <div class="rTableHead"></div>
            <div class="rTableCell alignRight"></div>
            <div class="rTableCell alignRight"></div>
            <div class="rTableCell alignRight">{{ enw.productiveValueYearMinus4 | toCurrency}}</div>
            <div class="rTableCell alignRight"></div>
            <div class="rTableCell alignRight">{{ enw.productiveValueYearMinus3 | toCurrency}}</div>
            <div class="rTableCell alignRight"></div>
            <div class="rTableCell alignRight">{{ enw.productiveValueYearMinus2 | toCurrency}}</div>
          </div>
        </div>
        
        <p></p>
        
        <div class="rTable numeric blueGrayTable">
          <div class="rTableRow">
            <div class="rTableHead alignCenter" v-for="year in enw.proxyMarginYears">{{ year }}</div>
            <div class="rTableHead alignCenter"><fmt:message key="Contribution.Margin"/></div>
            <div class="rTableHead" v-if="enw.combinedFarmPercent != null"><fmt:message key="Combined.Farm.Percent"/></div>
            <div class="rTableHead alignCenter"><fmt:message key="Enrolment.Fee"/></div>
          </div>
          <div class="rTableRow">
            <div class="rTableCell alignRight">{{enw.proxyMarginYearMinus4 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.proxyMarginYearMinus3 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.proxyMarginYearMinus2 | toCurrency}}</div>
            <div class="rTableCell">{{enw.proxyContributionMargin | toCurrency}}</div>
            <div class="rTableCell alignRight" v-if="enw.combinedFarmPercent != null">{{enw.combinedFarmPercent | toPercent1}}</div>
            <div class="rTableCell">{{enw.proxyEnrolmentFee | toCurrency}}</div>
          </div> 
        </div>
      </div>
      <div v-else class="errors">
          <ul>
            <li v-if="!enw.hasBpus">Failed to calculate proxy margins:</li>
            <li v-if="!enw.hasBpus">Missing BPUs for program year Productive Units.</li>
            <li v-if="!enw.hasProductiveUnits">Program year has no Productive Units.</li>
            <li v-if="enw.inCombinedFarm && !enw.benefitCalculated">Benefit calculation failed so Combined Farm % has not been calculated.</li>
          </ul>
      </div>
    </fieldset>
    
    
    <fieldset>
      <div>
        <h1>
          <input type="radio" name="enrolmentCalculationTypeCode" v-model="enw.enrolmentCalculationTypeCode" value="MANUAL" @change="updateFeeToUse" :disabled="readOnly">
          <fmt:message key="Calculate.using.Manually.Entered.Margins"/>
        </h1>
        <p v-if="enw.inCombinedFarm">Margins entered should have Combined Farm % already applied.</p>
        
        <div class="rTable numeric blueGrayTable">
          <div class="rTableRow">
            <div class="rTableHead alignCenter" v-for="year in enw.proxyMarginYears">{{ year }}</div>
          </div>
          <div class="rTableRow" v-if="readOnly">
            <div class="rTableCell alignRight">{{enw.manualMarginYearMinus4 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.manualMarginYearMinus3 | toCurrency}}</div>
            <div class="rTableCell alignRight">{{enw.manualMarginYearMinus2 | toCurrency}}</div>
          </div> 
          <div class="rTableRow" v-else>
            <div class="rTableCell alignRight"><input type="text" v-model="enw.manualMarginYearMinus4" @keyup="calculateManual"></div>
            <div class="rTableCell alignRight"><input type="text" v-model="enw.manualMarginYearMinus3" @keyup="calculateManual"></div>
            <div class="rTableCell alignRight"><input type="text" v-model="enw.manualMarginYearMinus2" @keyup="calculateManual"></div>
          </div> 
        </div>
        
        <div class="errors" v-if="manualMarginsCalculationErrors.length > 0">
          <ul>
            <li v-for="error in manualMarginsCalculationErrors">{{ error }}</li>
          </ul>
        </div>
        
        <p></p>
        
        <div class="rTable numeric blueGrayTable">
          <div class="rTableRow">
            <div class="rTableHead"><fmt:message key="Contribution.Margin"/></div>
            <div class="rTableHead"><fmt:message key="Enrolment.Fee"/></div>
          </div>
          <div class="rTableRow">
            <div class="rTableCell">&nbsp;{{enw.manualContributionMargin | toCurrency}}</div>
            <div class="rTableCell">&nbsp;{{enw.manualEnrolmentFee | toCurrency}}</div>
          </div> 
        </div>
      </div>
    </fieldset>
    
  </div> <!-- End vueApp -->

    <c:if test="${ ! form.readOnly }">
      <div style="text-align:right">
        <script>
          function saveEnwEnrolment() {
            if(vueData.manualMarginsCalculationErrors.length > 0) {
              alert("You must resolve the errors with the calculation using manually entered margins before saving.")
            } else {
              $('#enrolmentJson').val( $('#enrolmentJsonReactive').val() );
              submitForm(document.getElementById('enwEnrolmentForm'));
            }
          }
        </script>
      
        <u:yuiButton buttonId="saveButton" buttonLabel="Save" function="saveEnwEnrolment" />
<%--         <u:dirtyFormCheck formId="enwEnrolmentForm"/> --%>
      </div>
    </c:if>
</html:form>


<script>

  Vue.filter('toCurrency', function (value) {
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2
    });
    return formatter.format(value);
  });

  Vue.filter('toCurrency0', function (value) {
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0
    });
    return formatter.format(value);
  });

  Vue.filter('toPercent0', function (value) {
      if(value == null) {
          return 'N/A';
       }
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'percent',
      minimumFractionDigits: 0
    });
    return formatter.format(value);
  });

  Vue.filter('toPercent1', function (value) {
    if(value == null) {
       return 'N/A';
    }
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'percent',
      minimumFractionDigits: 1
    });
    return formatter.format(value);
  });

  Vue.filter('toDecimal0', function (value) {
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 0
    });
    return formatter.format(value);
  });

  Vue.filter('toDecimal2', function (value) {
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 2
    });
    return formatter.format(value);
  });

  Vue.filter('toDecimal3', function (value) {
    if(value == null) {
       return 'N/A';
    }
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 3
    });
    return formatter.format(value);
  });

  Vue.filter('toYesNo', function (value) {
    if (value) {
      return '<fmt:message key="Yes"/>';
    }
    return '<fmt:message key="No"/>';
  });

  var vueData = {
      manualMarginsCalculationErrors: [],
      readOnly: '<c:out value="${form.readOnly}"/>' === 'true',
      enw: JSON.parse($('#enrolmentJson').val())
  };
  
  var currencyFormatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0
  });
  
  var vm = new Vue({
    el: '#vueApp',
    data: vueData,
    computed: {
    },
    methods: {
      updateFeeToUse: function () {
        let enw = vueData.enw;
        
        if(enw.enrolmentCalculationTypeCode == "BENEFIT") {
          enw.contributionMargin = enw.benefitContributionMargin;
          enw.enrolmentFee = enw.benefitEnrolmentFee;
          enw.marginYearMinus2 = enw.benefitMarginYearMinus2;
          enw.marginYearMinus3 = enw.benefitMarginYearMinus3;
          enw.marginYearMinus4 = enw.benefitMarginYearMinus4;
          enw.marginYearMinus5 = enw.benefitMarginYearMinus5;
          enw.marginYearMinus6 = enw.benefitMarginYearMinus6;
          enw.marginYearMinus2Used = enw.benefitMarginYearMinus2Used;
          enw.marginYearMinus3Used = enw.benefitMarginYearMinus3Used;
          enw.marginYearMinus4Used = enw.benefitMarginYearMinus4Used;
          enw.marginYearMinus5Used = enw.benefitMarginYearMinus5Used;
          enw.marginYearMinus6Used = enw.benefitMarginYearMinus6Used;
          
        } else if(enw.enrolmentCalculationTypeCode == "PROXY") {
          enw.contributionMargin = enw.proxyContributionMargin;
          enw.enrolmentFee = enw.proxyEnrolmentFee;
          enw.marginYearMinus2 = enw.proxyMarginYearMinus2;
          enw.marginYearMinus3 = enw.proxyMarginYearMinus3;
          enw.marginYearMinus4 = enw.proxyMarginYearMinus4;
          enw.marginYearMinus5 = null;
          enw.marginYearMinus6 = null;
          enw.marginYearMinus2Used = true;
          enw.marginYearMinus3Used = true;
          enw.marginYearMinus4Used = true;
          enw.marginYearMinus5Used = false;
          enw.marginYearMinus6Used = false;
          
        } else if(enw.enrolmentCalculationTypeCode == "MANUAL") {
          enw.contributionMargin = enw.manualContributionMargin;
          enw.enrolmentFee = enw.manualEnrolmentFee;
          enw.marginYearMinus2 = enw.manualMarginYearMinus2;
          enw.marginYearMinus3 = enw.manualMarginYearMinus3;
          enw.marginYearMinus4 = enw.manualMarginYearMinus4;
          enw.marginYearMinus5 = null;
          enw.marginYearMinus6 = null;
          enw.marginYearMinus2Used = true;
          enw.marginYearMinus3Used = true;
          enw.marginYearMinus4Used = true;
          enw.marginYearMinus5Used = false;
          enw.marginYearMinus6Used = false;
        }
        
      },
      calculateManual: function () {
        let enw = vueData.enw;
        enw.enrolmentCalculationTypeCode = "MANUAL";
        
        let errorList = [];
        if(isNaN(enw.manualMarginYearMinus4)) {
          errorList.push("Invalid " + (enw.enrolmentYear - 4) + " margin.");
        }
        if(isNaN(enw.manualMarginYearMinus3)) {
          errorList.push("Invalid " + (enw.enrolmentYear - 3) + " margin.");
        }
        if(isNaN(enw.manualMarginYearMinus2)) {
          errorList.push("Invalid " + (enw.enrolmentYear - 2) + " margin.");
        }
        
        let hasAllMargins =
          enw.manualMarginYearMinus4 != null
          && enw.manualMarginYearMinus3 != null
          && enw.manualMarginYearMinus2 != null
          && enw.manualMarginYearMinus4 != ""
          && enw.manualMarginYearMinus3 != ""
          && enw.manualMarginYearMinus2 != ""
          && !isNaN(enw.manualMarginYearMinus4)
          && !isNaN(enw.manualMarginYearMinus3)
          && !isNaN(enw.manualMarginYearMinus2);
        
        vueData.manualMarginsCalculationErrors = errorList;
        
        if( errorList.length == 0 &&  hasAllMargins) {
          let total = parseFloat(enw.manualMarginYearMinus4) + parseFloat(enw.manualMarginYearMinus3) + parseFloat(enw.manualMarginYearMinus2);
          let contributionMargin = roundCurrency(total / 3);
          
          let fee = roundCurrency(contributionMargin * enw.enrolmentFeeCalculationFactor);
          if(fee < enw.enrolmentFeeMinimum) {
            fee = enw.enrolmentFeeMinimum;
          }
          
          enw.manualContributionMargin = contributionMargin;
          enw.manualEnrolmentFee = fee;
          enw.manualMarginsCalculated = true;
        } else {
          enw.manualContributionMargin = null;
          enw.manualEnrolmentFee = null;
          enw.manualMarginsCalculated = false;
        }
        
        this.updateFeeToUse();
      }
    }
  });

</script>

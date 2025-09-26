<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/vue.min.js"></script>

<%@ include file="/WEB-INF/jsp/common/vue-filters.jsp" %>

<script>
  function runTests() {
    showProcessing();
    document.location.href = "run840.do?<c:out value="pin=${form.pin}&year=${form.year}&scenarioNumber=${form.scenarioNumber}" escapeXml="false"/>";
  }
</script> 

<div>
  <fmt:message key="Reasonability.Tests.Instructions"/>
</div>

<c:if test="${form.results != null && !form.results.isFresh}">
  <div class="warnings">
    <ul>
      <li><fmt:message key="Reasonability.Tests.Stale"/></li>
    </ul>
  </div>
</c:if>

<c:if test="${form.testsDisabled}">
  <div class="errors">
    <ul>
      <li><fmt:message key="Reasonability.Tests.Disabled"/></li>
    </ul>
  </div>
</c:if>

<c:choose>
  <c:when test="${form.results == null}">
    <c:set var="runButtonLabel" value="Run.Tests.Now" />
  </c:when>
  <c:otherwise>
    <c:set var="runButtonLabel" value="Re-run.Tests" />
  </c:otherwise>
</c:choose>

<c:if test="${!form.readOnly}">
  <div>
    <u:yuiButton buttonId="runTestsButton" buttonLabel="${runButtonLabel}" function="runTests" disabled="${form.testsDisabled}" />
    <c:if test="${form.testsDisabled}">
      <u:yuiTooltip targetId="runTestsButton" messageKey="Benefit.calculation.not.completed" />
    </c:if>
  </div>
</c:if>

<div id="vueApp">

  <div v-if="results" v-cloak>
  
    <div style="width:100%;" class="alignRight">
      <a href="javascript:;" @click="expandAllTests()"><img src="images/expand_all.png" alt="Expand All" width="27" height="27"></img></a>
      <a href="javascript:;" @click="collapseAllTests()"><img src="images/collapse_all.png" alt="Expand All" width="27" height="27"></img></a>
    </div>
    
    <div class="rTable numeric blueGrayTable" style="width:100%;">
    
      <div class="rTableRow">
        <div class="rTableHead"><fmt:message key="Test.Name"/></div>
        <div class="rTableHead"><fmt:message key="Result"/></div>
      </div>
      
      <!-- START BENEFIT RISK ASSESSMENT -->
      <div class="rTableRow">
        <div class="rTableCell">
          <expander :show="showBenefitRisk" @toggle="showBenefitRisk=!showBenefitRisk"></expander>
          <span style="font-weight: bold"><fmt:message key="TEST"/> 1: <fmt:message key="Benefit.Risk.Assessment"/></span>
          <div v-show="showBenefitRisk">
            <fmt:message key="Benefit.Risk.Assessment.description"/>
          </div>
        </div>
        <div class="rTableCell alignCenter"><test-result-status :status="results.benefitRisk.result"></test-result-status></div>
      </div>
      <div v-show="showBenefitRisk" class="rTableRow">
        <div class="rTableCell">

          <test-result-error-messages :test="results.benefitRisk"></test-result-error-messages>
          <test-result-warning-messages :test="results.benefitRisk"></test-result-warning-messages>
          <test-result-info-messages :test="results.benefitRisk"></test-result-info-messages>
          
          <div v-if="results.forageProductiveUnitsConsumed">
            <%@ include file="/WEB-INF/jsp/calculator/reasonability/forageConsumers.jsp" %>
          </div>

          <%@ include file="/WEB-INF/jsp/calculator/reasonability/productiveUnits.jsp" %>
          
          <h2 style="margin-top: 30px; margin-bottom: 4px;" class="reasonability"><fmt:message key="Benefit.Benchmark"/></h2>
          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 600px;">
            <div class="rTableRow">
              <div class="rTableHead alignCenter" style="width: 170px;"></div>
              <div class="rTableHead alignCenter" style="width: 200px;"><fmt:message key="Calculation"/></div>
              <div class="rTableHead alignCenter"><fmt:message key="Benefit.Benchmark"/></div>
            </div>
            <div class="rTableRow">
              <div class="rTableHead alignLeft"><fmt:message key="Industry.Average.Margin"/></div>
              <div class="rTableCell alignRight"><fmt:message key="Sum.of.Producer.Margins"/> = </div>
              <div class="rTableCell alignCenter">{{results.benefitRisk.benchmarkMargin | toCurrency}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableHead alignLeft"><fmt:message key="Less"/> {{results.benefitRisk.benefitRiskDeductible | toPercent0}} <fmt:message key="Deductible"/></div>
              <div class="rTableCell alignRight">{{results.benefitRisk.benchmarkMargin | toCurrency}} &times; {{1 - results.benefitRisk.benefitRiskDeductible | toPercent0}} = </div>
              <div class="rTableCell alignCenter">{{results.benefitRisk.benefitBenchmarkLessDeductible | toCurrency}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableHead alignLeft"><fmt:message key="Less.Program.Year.Margin"/></div>
              <div class="rTableCell alignRight">{{results.benefitRisk.benefitBenchmarkLessDeductible | toCurrency}} &minus; {{results.benefitRisk.programYearMargin | toCurrency}} = </div>
              <div class="rTableCell alignCenter">{{results.benefitRisk.benefitBenchmarkLessProgramYearMargin | toCurrency}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableHead alignLeft">&times; <fmt:message key="Payout.Level"/> {{results.benefitRisk.benefitRiskPayoutLevel | toPercent0}}</div>
              <div class="rTableCell alignRight">{{results.benefitRisk.benefitBenchmarkLessProgramYearMargin | toCurrency}} &times; {{results.benefitRisk.benefitRiskPayoutLevel | toPercent0}} = </div>
              <div class="rTableCell alignCenter">{{results.benefitRisk.benefitBenchmarkBeforeCombinedFarmPercent | toCurrency}}</div>
            </div>
            <div class="rTableRow" v-if="results.inCombinedFarm">
              <div class="rTableHead alignLeft">&times; Combined Farm Portion {{results.benefitRisk.combinedFarmPercent | toPercent1}}</div>
              <div class="rTableCell alignRight">{{results.benefitRisk.benefitBenchmarkBeforeCombinedFarmPercent | toCurrency}} &times; {{results.benefitRisk.combinedFarmPercent | toPercent1}} = </div>
              <div class="rTableCell alignCenter">{{results.benefitRisk.benefitBenchmark | toCurrency}}</div>
            </div>
          </div>
          
          <h2 style="margin-top: 30px; margin-bottom: 4px;" class="reasonability"><fmt:message key="Result"/></h2>
          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%; margin-bottom: 50px;">
            <div class="rTableRow">
              <div class="rTableHead alignCenter"><fmt:message key="Program.Year.Margin"/></div>
              <div class="rTableHead alignCenter"><fmt:message key="Calculated.Benefit"/></div>
              <div class="rTableHead alignCenter"><fmt:message key="Benefit.Benchmark"/></div>
              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
              <div class="rTableHead alignCenter"><fmt:message key="Result"/> (<fmt:message key="Limit"/> {{results.benefitRisk.varianceLimit | toPercent0}})</div>
            </div>
            <div class="rTableRow">
              <div class="rTableCell alignCenter">{{results.benefitRisk.programYearMargin | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.benefitRisk.totalBenefit | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.benefitRisk.benefitBenchmark | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.benefitRisk.variance | toPercent1}}</div>
              <div class="rTableCell alignCenter"><test-result-status :status="results.benefitRisk.result"></test-result-status></div>
            </div>
          </div>
          
        </div>
        <div class="rTableCell"></div>
      </div>
      <!-- END BENEFIT RISK ASSESSMENT -->


      <!-- START REFERENCE MARGIN TEST -->
      <div class="rTableRow">
        <div class="rTableCell">
          <expander :show="showReferenceMarginTest" @toggle="showReferenceMarginTest=!showReferenceMarginTest"></expander>
          <span style="font-weight: bold"><fmt:message key="TEST"/> 2: <fmt:message key="Margin.Test.Reference.Margin"/></span>
          <div v-show="showReferenceMarginTest">
            <fmt:message key="Margin.Test.Reference.Margin.description"/>
          </div>
        </div>
        <div class="rTableCell alignCenter"><test-result-status :status="results.marginTest.withinLimitOfReferenceMargin"></test-result-status></div>
      </div>
  
      <div v-show="showReferenceMarginTest" class="rTableRow">
        <div class="rTableCell">

          <test-result-error-messages :test="results.marginTest"></test-result-error-messages>
          <test-result-warning-messages :test="results.marginTest"></test-result-warning-messages>
          <test-result-info-messages :test="results.marginTest"></test-result-info-messages>

          <h2 style="margin-bottom: 4px;" class="reasonability"><fmt:message key="Reference.Margin"/></h2>
          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%; margin-bottom: 30px;">
            <div class="rTableRow">
              <div class="rTableHead"><fmt:message key="Program.Year.Margin"/></div>
              <div class="rTableHead"><fmt:message key="Reference.Margin"/></div>
              <div class="rTableHead"><fmt:message key="Variance"/></div>
              <div class="rTableHead"><fmt:message key="Program.Year.Within"/> {{results.marginTest.adjustedReferenceMarginVarianceLimit | toPercent0}} <fmt:message key="of.Reference.Margin"/></div>
            </div>
            <div class="rTableRow">
              <div class="rTableCell alignCenter">{{results.marginTest.programYearMargin | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.marginTest.adjustedReferenceMargin | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.marginTest.adjustedReferenceMarginVariance | toPercent1}}</div>
              <div class="rTableCell alignCenter"><test-result-status :status="results.marginTest.withinLimitOfReferenceMargin"></test-result-status></div>
            </div>
          </div>
          <p></p>
    
        </div>
        <div class="rTableCell"></div>
      </div>
      <!-- END REFERENCE MARGIN TEST -->


      <!-- START INDUSTRY AVERAGE MARGIN TEST -->
      <div class="rTableRow">
        <div class="rTableCell">
          <expander :show="showIndustryAverageMarginTest" @toggle="showIndustryAverageMarginTest=!showIndustryAverageMarginTest"></expander>
          <span style="font-weight: bold"><fmt:message key="TEST"/> 3: <fmt:message key="Margin.Test.Industry.Average"/></span>
          <div v-show="showIndustryAverageMarginTest">
            <fmt:message key="Margin.Test.Industry.Average.description"/>
          </div>
        </div>
        <div class="rTableCell alignCenter"><test-result-status :status="results.marginTest.withinLimitOfIndustryAverage"></test-result-status></div>
      </div>
  
      <div v-show="showIndustryAverageMarginTest" class="rTableRow">
        <div class="rTableCell">

          <test-result-error-messages :test="results.marginTest"></test-result-error-messages>
          <test-result-warning-messages :test="results.marginTest"></test-result-warning-messages>
          <test-result-info-messages :test="results.marginTest"></test-result-info-messages>

          <div v-if="results.forageProductiveUnitsConsumed">
            <%@ include file="/WEB-INF/jsp/calculator/reasonability/forageConsumers.jsp" %>
          </div>

          <%@ include file="/WEB-INF/jsp/calculator/reasonability/productiveUnits.jsp" %>

          <h2 style="margin-top: 30px; margin-bottom: 4px;" class="reasonability"><fmt:message key="Industry.Average.Margin"/></h2>
          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="margin-bottom: 50px;">
            <div class="rTableRow">
              <div class="rTableHead"><fmt:message key="Program.Year.Margin"/></div>
              <div class="rTableHead"><fmt:message key="Industry.Average"/></div>
              <div class="rTableHead"><fmt:message key="Variance"/></div>
              <div class="rTableHead"><fmt:message key="Within"/> {{results.marginTest.industryVarianceLimit | toPercent0}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableCell alignCenter">{{results.marginTest.programYearMargin | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.marginTest.industryAverage | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.marginTest.industryVariance | toPercent1}}</div>
              <div class="rTableCell alignCenter"><test-result-status :status="results.marginTest.withinLimitOfIndustryAverage"></test-result-status></div>
            </div>
          </div>
    
        </div>
        <div class="rTableCell"></div>
      </div>
      <!-- END INDUSTRY AVERAGE MARGIN TEST -->


      <!-- START STRUCTURAL CHANGE TEST -->
      <div class="rTableRow">
        <div class="rTableCell">
          <expander :show="showStructuralChangeTest" @toggle="showStructuralChangeTest=!showStructuralChangeTest"></expander>
          <span style="font-weight: bold"><fmt:message key="TEST"/> 4: <fmt:message key="Structural.Change.Test"/></span>
          <div v-show="showStructuralChangeTest">
            <fmt:message key="Structural.Change.Test.description"/>
          </div>
        </div>
        <div class="rTableCell alignCenter"><test-result-status :status="results.structuralChangeTest.result"></test-result-status></div>
      </div>
  
      <div v-show="showStructuralChangeTest" class="rTableRow">
        <div class="rTableCell">
    
          <test-result-error-messages :test="results.structuralChangeTest"></test-result-error-messages>
          <test-result-warning-messages :test="results.structuralChangeTest"></test-result-warning-messages>
          <test-result-info-messages :test="results.structuralChangeTest"></test-result-info-messages>

          <div class="rTable numeric blueGrayTable reasonabilityTestTable">
            <div class="rTableRow">
              <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Production.Margin.with.Accrual.Adjustments"/></div>
              <div class="rTableCell alignCenter">{{results.structuralChangeTest.productionMargAccrAdjs | toCurrency}}</div>
            </div>
          </div>
          
          <h2 style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;" class="reasonability"><fmt:message key="Reference.Margin"/></h2>
          <div class="rTable numeric blueGrayTable reasonabilityTestTable">
            <div class="rTableRow">
              <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Ratio"/></div>
              <div class="rTableCell alignCenter">{{results.structuralChangeTest.ratioReferenceMargin | toCurrency}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Additive"/></div>
              <div class="rTableCell alignCenter">{{results.structuralChangeTest.additiveReferenceMargin | toCurrency}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Difference"/></div>
              <div class="rTableCell alignCenter">{{results.structuralChangeTest.ratioAdditiveDiffVariance | toCurrency}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Within"/> {{results.structuralChangeTest.ratioAdditiveDiffVarianceLimit | toCurrency0}}</div>
              <div class="rTableCell alignCenter"><test-result-status :status="results.structuralChangeTest.withinRatioAdditiveDiffLimit"></test-result-status></div>
            </div>
          </div>
          
          <h2 style="margin-top: 10px; margin-bottom: 4px;" class="reasonability"><fmt:message key="Additive.Division"/></h2>
          <div class="rTable numeric blueGrayTable reasonabilityTestTable">
            <div class="rTableRow">
              <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Difference.divided.by.ARM"/></div>
              <div class="rTableCell alignCenter">{{results.structuralChangeTest.additiveDivisionRatio | toDecimal3}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Between"/> -{{results.structuralChangeTest.additiveDivisionRatioLimit | toDecimal0}} <fmt:message key="and"/> {{results.structuralChangeTest.additiveDivisionRatioLimit | toDecimal0}}</div>
              <div class="rTableCell alignCenter"><test-result-status :status="results.structuralChangeTest.withinAdditiveDivisionLimit"></test-result-status></div>
            </div>
          </div>
          
          <c:if test="${! form.growingForward2024}">
            <div style="height: 20px;"></div>
            <div class="rTable numeric blueGrayTable reasonabilityTestTable">
              <div class="rTableRow">
                <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Farm.Size.Ratios.Within"/> {{results.structuralChangeTest.farmSizeRatioLimit | toPercent0}}</div>
                <div class="rTableCell alignCenter"><test-result-status :status="results.structuralChangeTest.withinFarmSizeRatioLimit"></test-result-status></div>
              </div>
            </div>
          </c:if>
          <div style="height: 20px;"></div>
          <div class="rTable numeric blueGrayTable reasonabilityTestTable">
            <div class="rTableRow">
              <div class="rTableHead structureChangeTestColumn1"><fmt:message key="Which.method.to.use"/>:</div>
              <div class="rTableCell alignCenter">{{results.structuralChangeTest.methodToUse}}</div>
            </div>
          </div>
    
        </div>
        <div class="rTableCell"></div>
      </div>
      <!-- END STRUCTURAL CHANGE TEST -->
      
      
      <!-- START REVENUE RISK TEST -->
      <div class="rTableRow">
        <div class="rTableCell">
          <expander :show="showRevenueRiskTest" @toggle="showRevenueRiskTest=!showRevenueRiskTest"></expander>
          <span style="font-weight: bold"><fmt:message key="TEST"/> 5: <fmt:message key="Revenue.Risk.Test"/></span>
          <div v-show="showRevenueRiskTest">
            <fmt:message key="Revenue.Risk.Test.description"/>
          </div>
        </div>
       	<div class="rTableCell alignCenter"><test-result-status :status="results.revenueRiskTest.result"></test-result-status></div> 
      </div>

      <div v-show="showRevenueRiskTest" class="rTableRow">
        <div class="rTableCell">

          <test-result-error-messages :test="results.revenueRiskTest"></test-result-error-messages>
          <test-result-warning-messages :test="results.revenueRiskTest"></test-result-warning-messages>
          <test-result-info-messages :test="results.revenueRiskTest"></test-result-info-messages>
          
          <div v-if="!results.revenueRiskTest.hasGrainForage
                     && !results.revenueRiskTest.hasFruitVeg
                     && !results.revenueRiskTest.hasCattle
                     && !results.revenueRiskTest.hasNursery
                     && !results.revenueRiskTest.hasHogs
                     && !results.revenueRiskTest.hasPoultryBroilers
                     && !results.revenueRiskTest.hasPoultryEggs"
               style="margin-top: 10px;margin-bottom: 10px;">
            <fmt:message key="No.revenue.tests.run.for.this.PIN"/>
          </div>

          <!-- START FORAGE & GRAIN REVENUE RISK TEST -->
          <div v-if="results.revenueRiskTest.hasGrainForage">
	          <h1 class="black"><fmt:message key="Forage.and.Grain"/></h1>
	          <div v-if="results.revenueRiskTest.forageInventoryConsumed">
	            <%@ include file="/WEB-INF/jsp/calculator/reasonability/forageConsumers.jsp" %>
	          </div>
	          
            <div style="margin-top: 30px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Inventory"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.revenueRiskTest.forageGrainInventory.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Units"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Opening"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Produced"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Ending"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Consumed"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Sold"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Price"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.forageGrainInventory" v-bind:key="testRecord.inventoryItemCode">
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCode}}</div>
	              <div class="rTableCell alignLeft">{{testRecord.inventoryItemCodeDescription}}</div>
	              <div class="rTableCell alignLeft">{{testRecord.cropUnitCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityStart | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityProduced | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityEnd | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityConsumed | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantitySold | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedPrice | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedRevenue | toCurrency}}</div>
	            </div> 
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          
	          <div style="margin-top: 30px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Revenue"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.revenueRiskTest.forageGrainIncomes.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
	              <div class="rTableHead"><fmt:message key="Within"/> {{results.revenueRiskTest.forageGrainVarianceLimit | toPercent0}} </div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.forageGrainIncomes" v-bind:key="testRecord.lineItemId">
	              <div class="rTableCell alignRight">{{testRecord.lineItemCode}}</div>
	              <div class="rTableCell alignLeft">{{testRecord.description}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedRevenue| toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedRevenue | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.variance | toPercent1}}</div>
	              <div class="rTableCell alignCenter"><test-result-status :status="testRecord.pass"></test-result-status></div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
          </div>
          <!-- END FORAGE & GRAIN REVENUE RISK TEST -->



          <!-- START FRUIT & VEGETABLE REVENUE RISK TEST -->
	        <div v-if="results.revenueRiskTest.hasFruitVeg">
	          <h1 class="black"><fmt:message key="Fruit.and.Vegetables"/></h1>
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Inventory"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.revenueRiskTest.fruitVegInventory.length != 0">
	            <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Type"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Pounds" /></div>
								<div class="rTableHead alignCenter"><fmt:message key="Expected.Price"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.fruitVegInventory" v-bind:key="testRecord.inventoryItemCode">
	              <div class="rTableCell alignLeft">{{testRecord.fruitVegTypeCodeDescription}}</div>
	              <div class="rTableCell alignLeft">{{testRecord.inventoryItemCode}}</div>
	              <div class="rTableCell alignLeft">{{testRecord.inventoryItemCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityProduced | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.fmvPrice | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedRevenue | toCurrency}}</div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>

          	<div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Revenue.By.Type"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.revenueRiskTest.fruitVegResults.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Type"/></div>
								<div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Pounds" /></div>
								<div class="rTableHead alignCenter"><fmt:message key="Reported.Price" /></div>
								<div class="rTableHead alignCenter"><fmt:message key="Expected.Price"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance.Limit"/></div>
	              <div class="rTableHead"><fmt:message key="Within.Limit"/></div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.fruitVegResults" v-bind:key="testRecord.fruitVegTypeCode">
	              <div class="rTableCell alignLeft">{{testRecord.fruitVegTypeDesc}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityProduced | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedPrice | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedPrice | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedRevenue | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedRevenue | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.variance | toPercent1}}</div>
	              <div class="rTableCell alignRight">{{testRecord.varianceLimit | toPercent0}}</div>
	              <div class="rTableCell alignCenter"><test-result-status :status="testRecord.pass"></test-result-status></div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
          </div>
          <!-- END FRUIT & VEGETABLE REVENUE RISK TEST -->



<%--
          <!-- START CATTLE REVENUE RISK TEST -->
          <div v-if="results.revenueRiskTest.hasCattle">
	          <h1 class="black"><fmt:message key="Cattle"/></h1>
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Productive.Units"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.revenueRiskTest.cattle.productiveUnits.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Productive.Units"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Deaths"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Produced"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Heifers"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Steers"/></div>
	            </div>
	            <div class="rTableRow" v-for="productiveUnit in results.revenueRiskTest.cattle.productiveUnits" v-bind:key="productiveUnit.code">
	              <div class="rTableCell alignRight">{{productiveUnit.code}}</div>
	              <div class="rTableCell alignLeft">{{productiveUnit.description}}</div>
	              <div class="rTableCell alignRight">{{productiveUnit.productiveCapacityAmount | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{productiveUnit.deaths | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{productiveUnit.produced | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{productiveUnit.heifers | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{productiveUnit.steers | toDecimal3}}</div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Inventory"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.revenueRiskTest.cattle.inventory.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Produced"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Opening"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Ending"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Sold"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Price"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.cattle.inventory" v-bind:key="testRecord.inventoryItemCode">
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCode}}</div>
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityProduced}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityStart}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityEnd}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantitySold | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedPrice | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedRevenue | toCurrency}}</div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Revenue"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" v-if="results.revenueRiskTest.cattle.incomes.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.cattle.incomes" v-bind:key="testRecord.lineItemId">
	              <div class="rTableCell alignRight">{{testRecord.lineItemCode}}</div>
	              <div class="rTableCell alignLeft">{{testRecord.description}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedRevenue| toCurrency}}</div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Result"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Within"/> {{results.revenueRiskTest.cattle.varianceLimit | toPercent0}}</div>
	            </div>
	            <div class="rTableRow">
	              <div class="rTableCell alignCenter">{{results.revenueRiskTest.cattle.reportedRevenue | toCurrency}}</div>
	              <div class="rTableCell alignCenter">{{results.revenueRiskTest.cattle.expectedRevenue | toCurrency}}</div>
	              <div class="rTableCell alignCenter">{{results.revenueRiskTest.cattle.variance | toPercent1}}</div>
	              <div class="rTableCell alignCenter"><test-result-status :status="results.revenueRiskTest.cattle.subTestPass"></test-result-status></div>
	            </div>
	          </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
	        </div>
          <!-- END CATTLE REVENUE RISK TEST -->
--%>


          <!-- START NURSERY REVENUE RISK TEST -->
          <div v-if="results.revenueRiskTest.hasNursery">
          	<h1 class="black"><fmt:message key="Nursery"/></h1>
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Inventory"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.revenueRiskTest.nursery.inventory.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Produced"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Opening"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Ending"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Sold"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="FMV.Price"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.nursery.inventory" v-bind:key="testRecord.inventoryItemCode">
	              <div class="rTableCell alignLeft">{{testRecord.inventoryItemCode}}</div>
	              <div class="rTableCell alignLeft">{{testRecord.inventoryItemCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityProduced}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityStart}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantityEnd}}</div>
	              <div class="rTableCell alignRight">{{testRecord.quantitySold | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.fmvPrice | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedRevenue | toCurrency}}</div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
          
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Revenue"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" v-if="results.revenueRiskTest.nursery.incomes.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.nursery.incomes" v-bind:key="testRecord.lineItemId">
	              <div class="rTableCell alignRight">{{testRecord.lineItemCode}}</div>
	              <div class="rTableCell alignLeft">{{testRecord.description}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedRevenue| toCurrency}}</div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Result"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Within"/> {{results.revenueRiskTest.nursery.varianceLimit | toPercent0}}</div>
	            </div>
	            <div class="rTableRow">
	              <div class="rTableCell alignCenter">{{results.revenueRiskTest.nursery.reportedRevenue | toCurrency}}</div>
	              <div class="rTableCell alignCenter">{{results.revenueRiskTest.nursery.expectedRevenue | toCurrency}}</div>
	              <div class="rTableCell alignCenter">{{results.revenueRiskTest.nursery.variance | toPercent1}}</div>
	              <div class="rTableCell alignCenter"><test-result-status :status="results.revenueRiskTest.nursery.subTestPass"></test-result-status></div>
	            </div>
	          </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
          </div>
          <!-- END NURSERY REVENUE RISK TEST -->



          <!-- START HOGS REVENUE RISK TEST -->
          <div v-if="results.revenueRiskTest.hasHogs">
	          <h1 class="black"><fmt:message key="Hogs"/></h1>
            <div v-if="results.revenueRiskTest.hogs.farrowToFinishOperation">
              <h2 class="black"><fmt:message key="Farrow.to.Finish.Operation"/></h2>
            </div>
            <div v-else-if="results.revenueRiskTest.hogs.feederOperation">
              <h2 class="black"><fmt:message key="Feeder.Operation"/></h2>
            </div>
            <div style="margin-top: 20px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Revenue.and.Expenses"/></div>
            <div class="rTable numeric blueGrayTable reasonabilityTestTable">
              <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Reported.Expense"/></div>
              </div>
              <div class="rTableRow">
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.reportedRevenue | toCurrency}}</div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.reportedExpenses | toCurrency}}</div>
              </div>
            </div>
            
            <div v-if="results.revenueRiskTest.hogs.farrowToFinishOperation">
	            <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Births"/></div>
	            <div class="rTable numeric blueGrayTable reasonabilityTest2ColumnTable">
	              <div class="rTableRow">
	                <div class="rTableHead alignLeft"><fmt:message key="Sows.Breeding"/></div>
                  <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.sowsBreeding | toDecimal0}}</div>
                </div>
                <div class="rTableRow">
	                <div class="rTableHead alignLeft">x {{results.revenueRiskTest.hogs.birthsPerCycle | toDecimal0}} <fmt:message key="Births"/></div>
                  <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.totalBirthsPerCycle | toDecimal0}}</div>
                </div>
                <div class="rTableRow">
	                <div class="rTableHead alignLeft">x {{results.revenueRiskTest.hogs.birthCyclesPerYear | toDecimal1}} <fmt:message key="Cycles"/></div>
                  <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.totalBirthsAllCycles | toDecimal0}}</div>
                </div>
                <div class="rTableRow">
	                <div class="rTableHead alignLeft">{{results.revenueRiskTest.hogs.deathRate | toPercent0}} <fmt:message key="Deaths"/></div>
                  <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.deaths | toDecimal0}}</div>
                </div>
	            </div>
            </div>
            
            <div style="margin-top: 20px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Inventory"/></div>
            <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.revenueRiskTest.hogs.inventory.length != 0">
              <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Opening"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Ending"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="FMV.Price"/></div>
              </div>
              <div class="rTableRow" v-for="testRecord in results.revenueRiskTest.hogs.inventory" v-bind:key="testRecord.inventoryItemCode">
                <div class="rTableCell alignRight">{{testRecord.inventoryItemCode}}</div>
                <div class="rTableCell alignLeft">{{testRecord.inventoryItemCodeDescription}}</div>
                <div class="rTableCell alignRight">{{testRecord.quantityStart}}</div>
                <div class="rTableCell alignRight">{{testRecord.quantityEnd}}</div>
                <div class="rTableCell alignRight">{{testRecord.fmvPrice | toCurrency}}</div>
              </div>
            </div>
            <div v-else>
              <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
            </div>

            <div v-if="results.revenueRiskTest.hogs.farrowToFinishOperation || results.revenueRiskTest.hogs.feederOperation">
              <div style="margin-top: 20px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Assumed.Purchases"/></div>
            </div>
            
            <div v-if="results.revenueRiskTest.hogs.farrowToFinishOperation" class="rTable numeric blueGrayTable">
              <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Type"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Qty.Purchased"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Purchase.Price"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Expected.Expense"/></div>
              </div>
              <div class="rTableRow">
                <div class="rTableCell alignCenter"><fmt:message key="Boars"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.boarPurchaseCount | toDecimal0}}</div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.boarPurchasePrice | toCurrency}}</div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.boarPurchaseExpense | toCurrency}}</div>
              </div>
              <div class="rTableRow">
                <div class="rTableCell alignCenter"><fmt:message key="Sows"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.sowPurchaseCount | toDecimal0}}</div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.sowPurchasePrice | toCurrency}}</div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.sowPurchaseExpense | toCurrency}}</div>
              </div>
            </div>
            
            <div v-else-if="results.revenueRiskTest.hogs.feederOperation" class="rTable numeric blueGrayTable">
              <div class="rTableRow">
                <div class="rTableCell alignCenter"></div>
                <div class="rTableHead alignCenter"><fmt:message key="Reported.Expense"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Purchase.Price"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Qty.Purchased"/></div>
              </div>
              <div class="rTableRow">
                <div class="rTableHead alignLeft"><fmt:message key="Assumed.Weanlings.Purchased"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.weanlingPurchaseExpense | toCurrency}}</div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.weanlingPurchasePrice | toCurrency}}</div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.weanlingPurchaseCount | toDecimal0}}</div>
              </div>
            </div>
          
            <div style="margin-top: 20px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Sales"/></div>
            <div class="rTable numeric blueGrayTable reasonabilityTest2ColumnTable">
              <div class="rTableRow">
                <div class="rTableHead alignLeft"><fmt:message key="Opening"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.totalQuantityStart | toDecimal0}}</div>
              </div>
              <div class="rTableRow" v-if="results.revenueRiskTest.hogs.farrowToFinishOperation">
                <div class="rTableHead alignLeft">+ <fmt:message key="Births"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.totalBirthsAllCycles | toDecimal0}}</div>
              </div>
              <div class="rTableRow">
                <div class="rTableHead alignLeft">+ <fmt:message key="Purchases"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.totalPurchaseCount | toDecimal0}}</div>
              </div>
              <div class="rTableRow" v-if="results.revenueRiskTest.hogs.farrowToFinishOperation">
                <div class="rTableHead alignLeft">- <fmt:message key="Deaths"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.deaths | toDecimal0}}</div>
              </div>
              <div class="rTableRow">
                <div class="rTableHead alignLeft">- <fmt:message key="Ending"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.totalQuantityEnd | toDecimal0}}</div>
              </div>
              <div class="rTableRow">
                <div class="rTableHead alignLeft"><fmt:message key="Total.Expected.Sold"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.expectedSold | toDecimal0}}</div>
              </div>
              <div class="rTableRow">
                <div class="rTableHead alignLeft">x <fmt:message key="FMV.Price"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.heaviestHogFmvPrice | toCurrency}}</div>
              </div>
              <div class="rTableRow">
                <div class="rTableHead alignLeft"><fmt:message key="Expected.Revenue"/></div>
                <div class="rTableCell alignRight">{{results.revenueRiskTest.hogs.expectedRevenue | toCurrency}}</div>
              </div>
            </div>
            
            <div style="margin-top: 20px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Result"/></div>
            <div class="rTable numeric blueGrayTable reasonabilityTestTable">
              <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Within"/> {{results.revenueRiskTest.hogs.varianceLimit | toPercent0}}</div>
              </div>
              <div class="rTableRow">
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.hogs.reportedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.hogs.expectedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.hogs.revenueVariance | toPercent1}}</div>
                <div class="rTableCell alignCenter"><test-result-status :status="results.revenueRiskTest.hogs.hogsPass"></test-result-status></div>
              </div>
            </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
          </div>
          <!-- END HOGS REVENUE RISK TEST -->



          <!-- START POULTRY - BROILERS REVENUE RISK TEST -->
          <div v-if="results.revenueRiskTest.hasPoultryBroilers">
	          <h1 class="black"><fmt:message key="Poultry.Broilers"/></h1>
            <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;">
              <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Type"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Kg.Produced"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Average.Weight.kg"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Expected.Sold"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Price"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Variance.Limit"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Pass"/></div>
              </div>
              <div v-if="results.revenueRiskTest.poultryBroilers.hasChickens" class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Chickens"/></div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.chickenKgProduced | toDecimal3}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.chickenAverageWeightKg | toDecimal2}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.chickenExpectedSoldCount | toDecimal0}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.chickenPricePerBird | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.chickenExpectedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.chickenReportedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.chickenVariance | toPercent1}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.chickenVarianceLimit | toPercent0}}</div>
                <div class="rTableCell alignCenter"><test-result-status :status="results.revenueRiskTest.poultryBroilers.chickenPass"></test-result-status></div>
              </div>
              <div  v-if="results.revenueRiskTest.poultryBroilers.hasTurkeys" class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Turkeys"/></div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.turkeyKgProduced | toDecimal3}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.turkeyAverageWeightKg | toDecimal2}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.turkeyExpectedSoldCount | toDecimal0}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.turkeyPricePerBird | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.turkeyExpectedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.turkeyReportedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.turkeyVariance | toPercent1}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryBroilers.turkeyVarianceLimit | toPercent0}}</div>
                <div class="rTableCell alignCenter"><test-result-status :status="results.revenueRiskTest.poultryBroilers.turkeyPass"></test-result-status></div>
              </div>
            </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
          </div>
          <!-- END POULTRY - BROILERS REVENUE RISK TEST -->



          <!-- START POULTRY - EGGS REVENUE RISK TEST -->
          <div v-if="results.revenueRiskTest.hasPoultryEggs">
	          <h1 class="black"><fmt:message key="Poultry.Eggs.for.consumption.hatching"/></h1>
            <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;">
              <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Type"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Layers"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Average.Eggs.Per.Layer"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Total.Eggs"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Dozen"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Price"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Expected.Revenue"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Reported.Revenue"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Variance.Limit"/></div>
                <div class="rTableHead alignCenter"><fmt:message key="Pass"/></div>
              </div>
              <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Consumption"/></div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionLayers | toDecimal3}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionAverageEggsPerLayer | toDecimal3}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionEggsTotal | toDecimal3}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionEggsDozen | toDecimal0}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionEggsDozenPrice | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionExpectedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionReportedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionVariance | toPercent1}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.consumptionVarianceLimit | toPercent0}}</div>
                <div class="rTableCell alignCenter"><test-result-status :status="results.revenueRiskTest.poultryEggs.consumptionPass"></test-result-status></div>
              </div>
              <div class="rTableRow">
                <div class="rTableHead alignCenter"><fmt:message key="Hatching"/></div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingLayers | toDecimal3}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingAverageEggsPerLayer | toDecimal3}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingEggsTotal | toDecimal3}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingEggsDozen | toDecimal0}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingEggsDozenPrice | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingExpectedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingReportedRevenue | toCurrency}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingVariance | toPercent1}}</div>
                <div class="rTableCell alignCenter">{{results.revenueRiskTest.poultryEggs.hatchingVarianceLimit | toPercent0}}</div>
                <div class="rTableCell alignCenter"><test-result-status :status="results.revenueRiskTest.poultryEggs.hatchingPass"></test-result-status></div>
              </div>
            </div>
          </div>
          <!-- END POULTRY - EGGS REVENUE RISK TEST -->

          <div style="margin-bottom: 30px;"></div>
          
        </div>
        <div class="rTableCell"></div>
      </div>
      <!-- END REVENUE RISK TEST -->
      
      
      <!-- START EXPENSE TEST - INDUSTRY AVERAGE COMPARISON -->
      <div class="rTableRow">
        <div class="rTableCell">
          <expander :show="showExpenseTestIAC" @toggle="showExpenseTestIAC=!showExpenseTestIAC"></expander>
          <span style="font-weight: bold"><fmt:message key="TEST"/> 6: <fmt:message key="Expense.Test.Industry.Average.Comparison"/></span>
          <div v-show="showExpenseTestIAC">
            <fmt:message key="Expense.Test.Industry.Average.Comparison.description"/>
          </div>
        </div>
        <div class="rTableCell alignCenter"><test-result-status :status="results.expenseTestIAC.result"></test-result-status></div>
      </div>
  
      <div v-show="showExpenseTestIAC" class="rTableRow">
        <div class="rTableCell">

          <test-result-error-messages :test="results.expenseTestIAC"></test-result-error-messages>
          <test-result-warning-messages :test="results.expenseTestIAC"></test-result-warning-messages>
          <test-result-info-messages :test="results.expenseTestIAC"></test-result-info-messages>

          <div class="rTable numeric blueGrayTable reasonabilityTestTable">
            <div class="rTableRow">
              <div class="rTableHead"><fmt:message key="Total.Eligible.Expenses.with.Accrual.Adjustments"/></div>
              <div class="rTableHead"><fmt:message key="Total.Industry.Average.Expense.IAE"/></div>
              <div class="rTableHead"><fmt:message key="Variance"/></div>
              <div class="rTableHead"><fmt:message key="Within"/> {{results.expenseTestIAC.industryVarianceLimit | toPercent0}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableCell alignCenter">{{results.expenseTestIAC.expenseAccrualAdjs | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestIAC.industryAverage | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestIAC.industryVariance | toPercent1}}</div>
              <div class="rTableCell alignCenter"><test-result-status :status="results.expenseTestIAC.result"></test-result-status></div>
            </div>
          </div>
    
        </div>
        <div class="rTableCell"></div>
      </div>
      <!-- END EXPENSE TEST - INDUSTRY AVERAGE COMPARISON -->
      
      
      <!-- START EXPENSE TEST - GENERAL CROPS -->
      <div class="rTableRow">
        <div class="rTableCell">
          <expander :show="showExpenseTestRefYearCompGC" @toggle="showExpenseTestRefYearCompGC=!showExpenseTestRefYearCompGC"></expander>
          <span style="font-weight: bold"><fmt:message key="TEST"/> 7: <fmt:message key="Expense.Test.Reference.Year.Comparison"/></span>
          <div v-show="showExpenseTestRefYearCompGC">
            <fmt:message key="Expense.Test.Reference.Year.Comparison.description"/>
          </div>
        </div>
        <div class="rTableCell alignCenter"><test-result-status :status="results.expenseTestRefYearCompGC.result"></test-result-status></div>
      </div>
  
      <div v-show="showExpenseTestRefYearCompGC" class="rTableRow">
        <div class="rTableCell">

          <test-result-error-messages :test="results.expenseTestRefYearCompGC"></test-result-error-messages>
          <test-result-warning-messages :test="results.expenseTestRefYearCompGC"></test-result-warning-messages>
          <test-result-info-messages :test="results.expenseTestRefYearCompGC"></test-result-info-messages>

          <div class="rTable numeric blueGrayTable reasonabilityTestTable">
            <div class="rTableRow">
              <div class="rTableHead"><fmt:message key="Total.Eligible.Expenses.with.Accrual.Adjustments"/></div>
              <c:forEach var="year" items="${form.referenceYears}">
                <div class="rTableHead alignCenter"><c:out value="${year}"/></div>
              </c:forEach>
              <div class="rTableHead"><fmt:message key="Expenses.with.Structural.Change.Average"/></div>
              <div class="rTableHead"><fmt:message key="Variance"/></div>
              <div class="rTableHead"><fmt:message key="Within"/> {{results.expenseTestRefYearCompGC.varianceLimit | toPercent0}}</div>
            </div>
            <div class="rTableRow">
              <div class="rTableCell alignCenter">{{results.expenseTestRefYearCompGC.programYearAcrAdjExpense | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestRefYearCompGC.expenseStructuralChangeYearMinus5 | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestRefYearCompGC.expenseStructuralChangeYearMinus4 | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestRefYearCompGC.expenseStructuralChangeYearMinus3 | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestRefYearCompGC.expenseStructuralChangeYearMinus2 | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestRefYearCompGC.expenseStructuralChangeYearMinus1 | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestRefYearCompGC.expenseStructrualChangeAverage | toCurrency}}</div>
              <div class="rTableCell alignCenter">{{results.expenseTestRefYearCompGC.variance | toPercent0}}</div>
              <div class="rTableCell alignCenter"><test-result-status :status="results.expenseTestRefYearCompGC.result"></test-result-status></div>
            </div>
          </div>
    
        </div>
        <div class="rTableCell"></div>
      </div>
      <!-- END EXPENSE TEST - GENERAL CROPS -->      
      
      <!-- START PRODUCTION TEST -->
      <div class="rTableRow">
        <div class="rTableCell">
          <expander :show="showProductionTest" @toggle="showProductionTest=!showProductionTest"></expander>
          <span style="font-weight: bold"><fmt:message key="TEST"/> 8: <fmt:message key="Production.Test"/></span>
          <div v-show="showProductionTest">
            <fmt:message key="Production.Test.Description"/>
          </div>
        </div>
        <div class="rTableCell alignCenter"><test-result-status :status="results.productionTest.result"></test-result-status></div>
      </div>
      <div v-show="showProductionTest" class="rTableRow">
        <div class="rTableCell">
                  
          <test-result-error-messages :test="results.productionTest"></test-result-error-messages>
          <test-result-warning-messages :test="results.productionTest"></test-result-warning-messages>
          <test-result-info-messages :test="results.productionTest"></test-result-info-messages>
          
          <div v-if="!results.productionTest.hasForage
                     && !results.productionTest.hasForageSeed
                     && !results.productionTest.hasFruitVeg
                     && !results.productionTest.hasGrain"
               style="margin-top: 10px;margin-bottom: 10px;">
            <fmt:message key="No.production.tests.run.for.this.PIN"/>
          </div>

          <!-- START FORAGE PRODUCTION TEST -->
          <div v-if="results.productionTest.hasForage">
	          <h1 class="black" style="margin-top: 10px;"><fmt:message key="Forage"/></h1>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Productive.Units"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production.Per.Unit"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Per.Unit"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Tonnes"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
	              <div class="rTableHead"><fmt:message key="Within"/> {{results.productionTest.forageProducedVarianceLimit | toPercent0}}</div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.productionTest.forageTestResults" v-bind:key="testRecord.inventoryItemCode">
	
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCode}}</div>
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.productiveCapacityAmount | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedProductionPerUnit | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProductionPerUnit | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProduction | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedQuantityProduced | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.variance | toPercent1}}</div>
	              <div class="rTableCell alignCenter"><test-result-status :status="testRecord.pass"></test-result-status></div>
	            </div>
	          </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
          </div>
          <!-- END FORAGE PRODUCTION TEST -->
          
          
          <!-- START FORAGE SEED PRODUCTION TEST -->
          <div v-if="results.productionTest.hasForageSeed">
	          <h1 class="black" style="margin-top: 10px;"><fmt:message key="Forage.Seed"/></h1>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Productive.Units"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production.Per.Unit"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Per.Unit"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Pounds"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
	              <div class="rTableHead"><fmt:message key="Within"/> {{results.productionTest.forageProducedVarianceLimit | toPercent0}}</div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.productionTest.forageSeedTestResults" v-bind:key="testRecord.inventoryItemCode">
	
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCode}}</div>
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.productiveCapacityAmount | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedProductionPerUnit | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProductionPerUnit | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProduction | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedQuantityProduced | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.variance | toPercent1}}</div>
	              <div class="rTableCell alignCenter"><test-result-status :status="testRecord.pass"></test-result-status></div>
	            </div>
	          </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
          </div>
          <!-- END FORAGE SEED PRODUCTION TEST -->
          
          
          <!-- START FRUIT & VEGETABLE PRODUCTION TEST -->
          <div v-if="results.productionTest.hasFruitVeg">
	          <h1 class="black"><fmt:message key="Fruit.and.Vegetables"/></h1>
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Inventory.and.Productive.Units"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.productionTest.fruitVegInventoryItems.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Type"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Productive.Units"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production.Per.Unit"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Per.Unit"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Pounds"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production"/></div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.productionTest.fruitVegInventoryItems" v-bind:key="testRecord.inventoryItemCode">
	
	              <div class="rTableCell alignRight">{{testRecord.fruitVegTypeCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCode}}</div>
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.productiveCapacityAmount | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedProductionPerUnit | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProductionPerUnit | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProduction | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedQuantityProduced | toDecimal3}}</div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          <div style="margin-top: 10px; margin-bottom: 4px; font-weight: bold;"><fmt:message key="Production.By.Type"/></div>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.productionTest.fruitVegTestResults.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Type"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Pounds"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
	              <div class="rTableHead"><fmt:message key="Within"/> {{results.productionTest.fruitVegProducedVarianceLimit | toPercent0}}</div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.productionTest.fruitVegTestResults" v-bind:key="testRecord.fruitVegTypeCode">
	
	              <div class="rTableCell alignRight">{{testRecord.fruitVegTypeCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProduction | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedQuantityProduced | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.variance | toPercent1}}</div>
	              <div class="rTableCell alignCenter"><test-result-status :status="testRecord.pass"></test-result-status></div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>
	          <hr style="margin-top: 10px; margin-bottom: 30px;">
          </div>
          <!-- END FRUIT & VEGETABLE PRODUCTION TEST -->
          
          
          <!-- START GRAINS PRODUCTION TEST -->
          <div v-if="results.productionTest.hasGrain">
	          <h1 class="black" style="margin-top: 10px;"><fmt:message key="Grains"/></h1>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Productive.Units"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production.Per.Unit"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Per.Unit"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Reported.Production.Tonnes"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Expected.Production"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Variance"/></div>
	              <div class="rTableHead"><fmt:message key="Within"/> {{results.productionTest.grainProducedVarianceLimit | toPercent0}}</div>
	            </div>
	            <div class="rTableRow" v-for="testRecord in results.productionTest.grainItemTestResults" v-bind:key="testRecord.inventoryItemCode">
	
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCode}}</div>
	              <div class="rTableCell alignRight">{{testRecord.inventoryItemCodeDescription}}</div>
	              <div class="rTableCell alignRight">{{testRecord.productiveCapacityAmount | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedProductionPerUnit | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProductionPerUnit | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.reportedProduction}}</div>
	              <div class="rTableCell alignRight">{{testRecord.expectedQuantityProduced | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{testRecord.variance | toPercent1}}</div>
	              <div class="rTableCell alignCenter"><test-result-status :status="testRecord.pass"></test-result-status></div>
	            </div>
	          </div>
          </div>
          <!-- END GRAINS PRODUCTION TEST -->
          
          
        </div>
        <div class="rTableCell">                
        </div>
      </div>      
      <!-- END PRODUCTION TEST -->                        
      
    </div>
  </div>
</div>

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

  Vue.component('test-result-status', {
    props: ['status'],
    template: '<img :src="imagePath" :alt="status" width="16" height="16" />',
    computed: {
      imagePath: function() {
        var path = 'images/';
        if(this.status) {
          path += 'tick.gif';
        } else {
          path += 'cross_red.gif';
        }
        return path;
      }
    }
  });
      
  Vue.component('test-result-error-messages', {
      props: ['test'],
      template: '\
        <div class="errors" v-if="test.errorMessages.length != 0">\
          <ul>\
            <div class="rTableRow" v-for="message in test.errorMessages">\
            <li>{{message.message}}</li>\
          </ul>\
        </div>',
  });
  
  Vue.component('test-result-warning-messages', {
      props: ['test'],
      template: '\
        <div class="warningsOnGray" v-if="test.warningMessages.length != 0">\
          <ul>\
            <div class="rTableRow" v-for="message in test.warningMessages">\
            <li>{{message.message}}</li>\
          </ul>\
        </div>',
  });
  
  Vue.component('test-result-info-messages', {
      props: ['test'],
      template: '\
          <div class="messages" v-if="test.infoMessages.length != 0">\
          <ul>\
            <div class="rTableRow" v-for="message in test.infoMessages">\
            <li>{{message.message}}</li>\
          </ul>\
        </div>',
  });

  Vue.component('expander', {
    props: ['show'],
    template: '<a href="javascript:;" @click="onClick()"><img :src="imagePath" alt="More Info" :width="width" :height="height" /></a>',
    data: {
      width: '12',
      height: '7'
    },
    computed: {
      imagePath: function() {
        var path = 'yui/2.8.2r1/build/assets/skins/sam/';
        if(this.show) {
          path += 'menubaritem_submenuindicator.png';
          width = '16';
          height = '4';
        } else {
          path += 'menuitem_submenuindicator.png';
          width = '12';
          height = '7';
        }
        return path;
      }
    },
    methods: {
      onClick: function() {
        this.$emit('toggle');
      }
    }
  });
  
  var expandTestsOnLoad = false;
  
  var vm = new Vue({
    el: '#vueApp',
    data: {
      results: <c:out value="${form.resultsJson}" escapeXml="false"/>,
      showBenefitRisk: expandTestsOnLoad,
      showReferenceMarginTest: expandTestsOnLoad,
      showIndustryAverageMarginTest: expandTestsOnLoad,
      showStructuralChangeTest: expandTestsOnLoad,
      showRevenueRiskTest: expandTestsOnLoad,
      showExpenseTestIAC: expandTestsOnLoad,
      showProductionTest: expandTestsOnLoad,
      showExpenseTestRefYearCompGC: expandTestsOnLoad
    },
    methods: {
        expandAllTests: function() {
            this.showBenefitRisk = true,
            this.showReferenceMarginTest = true,
            this.showIndustryAverageMarginTest = true,
            this.showStructuralChangeTest = true,
            this.showRevenueRiskTest = true,
            this.showExpenseTestIAC = true,
            this.showProductionTest = true,
            this.showExpenseTestRefYearCompGC = true
        },
        collapseAllTests: function() {
            this.showBenefitRisk = false,
            this.showReferenceMarginTest = false,
            this.showIndustryAverageMarginTest = false,
            this.showStructuralChangeTest = false,
            this.showRevenueRiskTest = false,
            this.showExpenseTestIAC = false,
            this.showProductionTest = false,
            this.showExpenseTestRefYearCompGC = false
        }
    }
  });
  
</script>


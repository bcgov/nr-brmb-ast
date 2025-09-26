	          <h2 style="margin-top: 25px; margin-bottom: 4px;" class="reasonability"><fmt:message key="Productive.Units"/></h2>
	          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;" v-if="results.benefitRisk.benefitRiskProductiveUnits.length != 0">
	            <div class="rTableRow">
	              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
	              <div class="rTableHead alignCenter" v-if="results.forageProductiveUnitsConsumed"><fmt:message key="Reported.Productive.Units"/></div>
	              <div class="rTableHead alignCenter" v-if="results.forageProductiveUnitsConsumed"><fmt:message key="Consumed"/></div>
	              <div class="rTableHead alignCenter" v-if="results.forageProductiveUnitsConsumed"><fmt:message key="Net.Productive.Units"/></div>
	              <div class="rTableHead alignCenter" v-if="!results.forageProductiveUnitsConsumed"><fmt:message key="Productive.Units"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="BPU.Calculated"/></div>
	              <div class="rTableHead alignCenter"><fmt:message key="Producer.Margin.from.BPUxPU"/></div>
	            </div>
	            <div class="rTableRow" v-for="productiveUnit in results.benefitRisk.benefitRiskProductiveUnits" v-bind:key="productiveUnit.code">
	              <div class="rTableCell alignRight">{{productiveUnit.code}}</div>
	              <div class="rTableCell alignLeft">{{productiveUnit.description}}</div>
	              <div class="rTableCell alignRight" v-if="results.forageProductiveUnitsConsumed">{{productiveUnit.reportedProductiveCapacityAmount | toDecimal3}}</div>
	              <div class="rTableCell alignRight" v-if="results.forageProductiveUnitsConsumed">{{productiveUnit.consumedProductiveCapacityAmount | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{productiveUnit.netProductiveCapacityAmount | toDecimal3}}</div>
	              <div class="rTableCell alignRight">{{productiveUnit.bpuCalculated | toCurrency}}</div>
	              <div class="rTableCell alignRight">{{productiveUnit.estimatedIncome | toCurrency}}</div>
	            </div>
	          </div>
	          <div v-else>
	            <div style="margin-top: 10px; margin-bottom: 4px;"><fmt:message key="None"/></div>
	          </div>

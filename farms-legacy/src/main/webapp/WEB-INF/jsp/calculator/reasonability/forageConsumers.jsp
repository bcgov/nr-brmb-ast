	            <h2 style="margin-top: 20px; margin-bottom: 4px; font-weight: bold;" class="reasonability"><fmt:message key="Cattle.Consuming.Forage"/></h2>
	            <div style="margin-top: 4px; margin-bottom: 4px;"><fmt:message key="Cattle.Consuming.Forage.note"/></div>
		          <div class="rTable numeric blueGrayTable reasonabilityTestTable" style="width: 100%;">
		            <div class="rTableRow">
		              <div class="rTableHead alignCenter"><fmt:message key="Code"/></div>
		              <div class="rTableHead alignCenter"><fmt:message key="Description"/></div>
		              <div class="rTableHead alignCenter"><fmt:message key="Productive.Units"/></div>
		              <div class="rTableHead alignCenter"><fmt:message key="Tonnes.Consumed.Per.Unit"/></div>
		              <div class="rTableHead alignCenter"><fmt:message key="Tonnes.Consumed.potentially"/></div>
		            </div>
		            <div class="rTableRow" v-for="testRecord in results.forageConsumers" v-bind:key="testRecord.structureGroupCode">
		              <div class="rTableCell alignRight">{{testRecord.structureGroupCode}}</div>
		              <div class="rTableCell alignLeft">{{testRecord.structureGroupCodeDescription}}</div>
		              <div class="rTableCell alignRight">{{testRecord.productiveUnitCapacity | toDecimal3}}</div>
		              <div class="rTableCell alignRight">{{testRecord.quantityConsumedPerUnit | toDecimal3}}</div>
		              <div class="rTableCell alignRight">{{testRecord.quantityConsumed | toDecimal3}}</div>
		            </div> 
		            <div class="rTableRow">
		              <div class="rTableCell alignLeft" style="font-weight: bold;"><fmt:message key="Total.Tonnes.Consumed.potentially"/></div>
		              <div class="rTableCell alignLeft"></div>
		              <div class="rTableCell alignLeft"></div>
		              <div class="rTableCell alignLeft"></div>
		              <div class="rTableCell alignRight" style="font-weight: bold;">{{results.forageConsumerCapacity | toDecimal3}}</div>
		            </div> 
		          </div>

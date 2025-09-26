<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/vue.min.js"></script>

<%@ include file="/WEB-INF/jsp/common/vue-filters.jsp" %>

<h1>
  <fmt:message key="Next.Step.Checklist"/>
</h1>

<div id="vueApp">

  <div>
    
	  <div class="rTable numeric blueGrayTable" style="width: 600px;">
	    <div class="rTableRow">
	      <div class="rTableHead alignLeft" style="width: 200px;"><fmt:message key="A.Payment.File"/></div>
	      <div class="rTableCell alignCenter">{{checklist.hasBenefitPayment | toYesNo}}</div>
	    </div>
	    <div class="rTableRow">
	      <div class="rTableHead alignLeft"><fmt:message key="Payment.greater.than"/> {{checklist.paymentAmountRequiringASpecialist | toCurrency0}}?</div>
	      <div class="rTableCell alignCenter">{{checklist.paymentOverSpecialistThreshold | toYesNo}}</div>
	    </div>
	    <div class="rTableRow">
	      <div class="rTableHead alignLeft"><fmt:message key="Reference.Margin.Test.Failing"/></div>
	      <div class="rTableCell alignCenter">{{checklist.referenceMarginTestFailed | toYesNo}}</div>
	    </div>
	    <div class="rTableRow">
	      <div class="rTableHead alignLeft"><fmt:message key="Structure.Change.Test.Failing"/></div>
	      <div class="rTableCell alignCenter">{{checklist.structureChangeTestFailed | toYesNo}}</div>
	    </div>
	    <div class="rTableRow">
	      <div class="rTableHead alignLeft"><fmt:message key="A.Combined.File"/></div>
	      <div class="rTableCell alignCenter">{{checklist.inCombinedFarm | toYesNo}}</div>
	    </div>
	    <div class="rTableRow">
	      <div class="rTableHead alignLeft"><fmt:message key="Allowable.Income.greater.than"/> {{checklist.incomeAmountRequiringASpecialist | toCurrency0}}?</div>
	      <div class="rTableCell alignCenter">{{checklist.allowableIncomeOverSpecialistThreshold | toYesNo}}</div>
	    </div>
	    <div class="rTableRow">
	      <div class="rTableHead alignLeft"><fmt:message key="Type.of.Farm"/>:</div>
	      <div class="rTableCell alignCenter">{{checklist.farmTypeDetailCodeDescription}}</div>
	    </div>
	    <div class="rTableRow">
	      <div class="rTableHead alignLeft"><fmt:message key="Create.Task.in.BARN.and.Store.in"/>:</div>
	      <div class="rTableCell alignCenter">{{checklist.triageQueueDescription}}</div>
	    </div>
	  </div>
	  
  </div>
</div>

<script>

  var vueData = {
    checklist: <c:out value="${form.checklistJson}" escapeXml="false"/>
  }

  var vm = new Vue({
    el: '#vueApp',
    data: vueData
  });

</script>


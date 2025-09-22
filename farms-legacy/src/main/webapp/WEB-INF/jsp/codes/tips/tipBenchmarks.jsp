<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/vue3.global.prod.js"></script>

<%@ include file="/WEB-INF/jsp/common/vue3-filters.jsp" %>

<script>

function generateBenchmarks(year) {
  var message = '<fmt:message key="Are.you.sure.you.want.to.generate.the.TIP.Report.benchmark.data"/> ' + year + '.';
  var answer = confirm(message);
  if(answer) {
    showProcessing();
    document.location.href = '<html:rewrite action="generateTipReportBenchmarkData"/>?year=' + year;
    form.submit();
  }     
}

</script>
  
<c:if test="${not form.benchmarksMatchConfig}">
  <div class="warnings">
    <ul>
      <li><fmt:message key="Benchmark.configuration.changed.1"/><c:out value="${form.latestBenchmarkYear}"/><fmt:message key="Benchmark.configuration.changed.2"/></li>
    </ul>
  </div>
</c:if>

<fieldset style="width:50%">
  
  <div class="rTable" style="width: 100%;">
    <div class="rTableRow">
      <div class="rTableHead"><fmt:message key="Year"/></div>
      <div class="rTableHead"><fmt:message key="Operations"/></div>
      <div class="rTableHead"><fmt:message key="Generated"/></div>
    </div>

    <c:forEach var="benchmark" items="${form.benchmarkInfos}" varStatus="loop">
      <div class="rTableRow">
        <div class="rTableCell alignleft"><c:out value="${benchmark.year}"/></div>
        <div class="rTableCell"><fmt:formatNumber type="number" value="${benchmark.operationCount}"/></div>
        <div class="rTableCell"><fmt:formatDate value="${benchmark.generatedDate}" pattern="yyyy-MM-dd"/></div>
        <div class="rTableCell">
          <c:set var="functionName" value="generateBenchmarks${benchmark.year}"/>
          <c:choose>
            <c:when test="${empty benchmark.generatedDate}">
              <c:set var="buttonLabel" value="Generate"/>
            </c:when>
            <c:otherwise>
              <c:set var="buttonLabel" value="Regenerate"/>
            </c:otherwise>
          </c:choose>
          <script>

            function <c:out value="${functionName}"/>() {
              generateBenchmarks('<c:out value="${benchmark.year}"/>');
            }

          </script>
          <u:yuiButton buttonLabel="${buttonLabel}" buttonId="generate${loop.index}" function="${functionName}"/>
        </div>
      </div>
    </c:forEach>
  </div>

</fieldset>                  
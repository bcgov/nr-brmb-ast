<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/vue.min.js"></script>
<script>

function runBackgroundReport() {
  $('#reportStatusDiv').hide();
  $('#processingTable').show();
  $('#generateReportButton').hide();
  var form = $('#benchmarkReportForm')
  var url = '<html:rewrite action="runBackgroundTipBenchmarkReport" />';
  var serializedForm = form.serialize();
  $.ajax({
    type: 'POST',
    async: true,
    url: url,
    data: serializedForm
  });
}

function runReport() {
  let reportType = $('#reportType').val();
  let reportYear = $('#reportYear').val();
  let farmTypeLevel = $('#farmTypeLevel').val();
  let farmType = $('#farmType').val();
  let incomeRangeLow = $('#incomeRangeLow').val();
  let form = document.getElementById('benchmarkReportForm');
  
  if(reportType == 'BENCHMARK_EXTRACT') {
    if(reportYear) {
      form.action = '<html:rewrite action="runAndOpenBenchmarkReport"/>';
      form.submit();
    } else {
      alert('Report Year is required for this report.')
    }

  } else if(reportType == 'BENCHMARK_SUMMARY') {
    if(reportYear
        && farmTypeLevel
        && farmType
        && incomeRangeLow != '') {
      
      form.action = '<html:rewrite action="runAndOpenBenchmarkReport"/>';
      form.submit();
    }

  } else if(reportType == 'GROUPING_CONFIG') {
    form.action = '<html:rewrite action="runAndOpenBenchmarkReport"/>';
    form.submit();
    
  } else if(reportType == 'BACKGROUND_REPORT') {
  // BACKGROUND_REPORT is placeholder in case we add a report that needs
  // to be run in the background. Initially BENCHMARK_EXTRACT was set up
  // this way, but it turns out that was not needed.

    if(reportYear) {
      
      runBackgroundReport();
      
    } else {
      alert('Report Year is required for this report.')
    }
  }
}

function checkReportStatus(keepChecking) {
  let reportType = $('#reportType').val()
  var url = '<html:rewrite action="tipBenchmarkReportStatus" />?reportType=' + reportType;
  $.ajax({
    type: 'GET',
    async: true,
    url: url,
    success: function(data) {
      $('#requestorAccountName').html(data.requestorAccountName);
      $('#reportRequestDate').html(data.reportRequestDate);
      $('#fileName').html(data.fileName);
      $('#reportFileDate').html(data.reportFileDate);
      
      $('#reportStatusDiv').show();
      
      if(data.reportFileDate) {
        $('#requestInfoDiv').hide();
        $('#processingTable').hide();
        
        $('#generateReportButton').show();
        $('#fileInfoDiv').show();
      } else if(data.reportRequestDate) {
        let timeRequested = Date.parse(data.reportRequestDate);
        let currentTime = new Date().getTime();
        let totalSecondsSinceRequested = Math.round((currentTime - timeRequested) / 1000);
        let seconds = totalSecondsSinceRequested % 60;
        let minutes = Math.trunc(totalSecondsSinceRequested / 60);
        let runningTime = '';
        if(seconds) {
          runningTime = seconds + ' seconds';
        }
        if(minutes) {
          runningTime = minutes + ' minutes, ' + runningTime;
        }
        $('#runningTime').html(runningTime);
        
        $('#requestInfoDiv').show();
        $('#processingTable').show();
        
        $('#fileInfoDiv').hide();
        
        if(minutes > 30) {
          $('#generateReportButton').show();
        } else {
          $('#generateReportButton').hide();
        }
      } else {
        $('#reportStatusDiv').hide();
      }
    }
  });
}

function downloadTipBenchmarkReport() {
  let reportType = $('#reportType').val();
  var url = '<html:rewrite action="downloadTipBenchmarkReport" />?reportType=' + reportType;
  document.location.href = url;
}

// YAHOO.util.Event.onDOMReady(function() {
//   setInterval(function () {
//     checkReportStatus(true);
//   }, 5000);
// });
</script>

<h1>
  <fmt:message key="Report.Criteria"/>
</h1>

<html:form action="runAndOpenBenchmarkReport" styleId="benchmarkReportForm" onsubmit="showProcessing()">
  <html:hidden property="benchmarkGroupsJson" styleId="benchmarkGroupsJson"/>
  
  <div id="vueApp" v-cloak>
    <fieldset>
      <div class="rTable">
        <div class="rTableRow">
          <div class="rTableHead"><fmt:message key="Report.Name"/></div>
          <div class="rTableCell">
            <select v-model="reportType" name="reportType" id="reportType">
              <option value="BENCHMARK_EXTRACT"><fmt:message key="Benchmark.Extract"/></option>
              <option value="BENCHMARK_SUMMARY"><fmt:message key="Benchmark.Summary.Report"/></option>
              <option value="GROUPING_CONFIG"><fmt:message key="Grouping.Configuration"/></option>
            </select>
          </div>
        </div>
        <div class="rTableRow" v-if="showYearParam">
          <div class="rTableHead"><fmt:message key="Report.Year"/></div>
          <div class="rTableCell">
            <select v-model="reportYear" name="reportYear" id="reportYear">
            	<option v-for="option in yearOptions()" v-bind:value="option.value">
            	  {{option.text}}
            	</option>
            </select>
          </div>
        </div>
        <div class="rTableRow" v-if="showFarmTypeLevelParam">
          <div class="rTableHead"><fmt:message key="Farm.Type.Level"/></div>
          <div class="rTableCell">
            <select v-model="farmTypeLevel" name="farmTypeLevel" id="farmTypeLevel">
              <option value="3"><fmt:message key="Farm.Type"/> 3</option>
              <option value="2"><fmt:message key="Farm.Type"/> 2</option>
              <option value="1"><fmt:message key="Farm.Type"/> 1</option>
            </select>
          </div>
        </div>
        <div class="rTableRow" v-if="showFarmTypeParam">
          <div class="rTableHead"><fmt:message key="Farm.Type"/></div>
          <div class="rTableCell">
            <select v-model="farmType" name="farmType" id="farmType">
            	<option v-for="option in farmTypeOptions(reportYear, farmTypeLevel)" v-bind:value="option.value">
            	  {{option.text}}
            	</option>
            </select>
          </div>
        </div>
        <div class="rTableRow" v-if="showIncomeRangeParams">
          <div class="rTableHead"><fmt:message key="Income.Range"/></div>
          <div class="rTableCell">
            <select v-model="incomeRangeLow" name="incomeRangeLow" id="incomeRangeLow">
            	<option v-for="option in incomeRangeOptions(reportYear, farmTypeLevel, farmType, 'LOW')" v-bind:value="option.value">
            	  {{option.text}}
            	</option>
            </select>
            <span v-if="showIncomeRangeSeparateParams">
              <fmt:message key="To"/>
	            <select v-model="incomeRangeHigh" name="incomeRangeHigh" id="incomeRangeHigh">
	            	<option v-for="option in incomeRangeOptions(reportYear, farmTypeLevel, farmType, 'HIGH')" v-bind:value="option.value">
	            	  {{option.text}}
	            	</option>
	            </select>
            </span>
          </div>
        </div>
      </div>
    </fieldset>
  </div>

  <p></p> 
  <u:yuiButton buttonLabel="Generate.Report" buttonId="generateReportButton" function="runReport"/>
</html:form>


<script>
  const INCOME_RANGE_MAX = 9999999999.0;

  var vueData = {
      benchmarkGroups: JSON.parse($('#benchmarkGroupsJson').val()),
      reportType: "BENCHMARK_EXTRACT",
      reportYear: "",
      farmTypeLevel: "",
      farmType: "",
      incomeRangeLow: "",
      incomeRangeHigh: ""
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
      maxYear: function() {
        let keys = Object.keys(obj);
        keys.sort().reverse();
        return keys[0];
      },
      showYearParam: function() {
        return this.reportType == 'BENCHMARK_EXTRACT'
            || this.reportType == 'BENCHMARK_SUMMARY';
      },
      showFarmTypeLevelParam: function() {
        return this.reportType == 'BENCHMARK_EXTRACT'
            || this.reportType == 'BENCHMARK_SUMMARY';
      },
      showFarmTypeParam: function() {
        return this.reportType == 'BENCHMARK_EXTRACT'
            || this.reportType == 'BENCHMARK_SUMMARY';
      },
      showIncomeRangeParams: function() {
        return this.reportType == 'BENCHMARK_EXTRACT'
          || this.reportType == 'BENCHMARK_SUMMARY';
      },
      showIncomeRangeSeparateParams: function() {
        return this.reportType == 'BENCHMARK_EXTRACT';
      },
    },
    methods: {
      yearOptions: function () {
        let values = Object.keys(this.benchmarkGroups);
        
        let options = this.buildOptions(values, true);
        return options;
      },
      farmTypeOptions: function (reportYear, farmTypeLevel) {
        let options = [];
        if(reportYear && farmTypeLevel) {
          let values = Object.keys(this.benchmarkGroups[reportYear][farmTypeLevel]);
          options = this.buildOptions(values, false);
        }
        
        return options;
      },
      incomeRangeOptions: function (reportYear, farmTypeLevel, farmType, incomeRangeLowHigh) {
        let options = [ {text: "", value: ""} ];
        let values = [];
        let benchmarkGroupList = [];
        if(reportYear && farmTypeLevel && farmType && incomeRangeLowHigh) {
          let groupsObject = this.benchmarkGroups[reportYear][farmTypeLevel][farmType];
          
          if(groupsObject) {
            let groupObjectKeys = Object.keys(groupsObject);
            for(let j = 0; j < groupObjectKeys.length; j++) {
              let groupKey = groupObjectKeys[j];
              let group = groupsObject[groupKey];
              benchmarkGroupList.push(group);
            }
          }
          
          benchmarkGroupList.sort(function(a, b){
            return a.incomeRangeLow - b.incomeRangeLow;
          });
          
          for(let i = 0; i < benchmarkGroupList.length; i++) {
            let curGroup = benchmarkGroupList[i];
            let formatted;
            let curValue;
            if(incomeRangeLowHigh == 'HIGH') {
              curValue = curGroup.incomeRangeHigh;
              if(curValue == INCOME_RANGE_MAX) {
                formatted = "Infinity";
              } else {
                formatted = currencyFormatter.format(curGroup.incomeRangeHigh);
              }
            } else if(incomeRangeLowHigh == 'LOW') {
              curValue = curGroup.incomeRangeLow;
              formatted = currencyFormatter.format(curValue);
              if(this.reportType == 'BENCHMARK_SUMMARY') {
                formatted += ' - ';
                if(curGroup.incomeRangeHigh == INCOME_RANGE_MAX) {
                  formatted += "Infinity";
                } else {
                  formatted += currencyFormatter.format(curGroup.incomeRangeHigh);
                }
              }
            }
            
            options.push({text: formatted, value: curValue});
          }
        }
        
        return options;
      },
      buildOptions: function (values, reverseSort) {
        values.sort();
        if(reverseSort) {
          values.reverse();
        }
        
        let options = [];
        options.push({text: "", value: ""});
        
        for(let i = 0; i < values.length; i++) {
          let curValue = values[i];
          options.push({text: curValue, value: curValue});
        }
        return options;
      }
    }
  });

</script>

<div id="reportStatusDiv" style="display: none;">
  <h1 style="margin-top: 30px; margin-bottom: 4px;"><fmt:message key="Report.Status"/></h1>
  <div id="requestInfoDiv">
    <table class="details">
      <tr>
        <th><fmt:message key="background.report.requestor"/>:</th>
        <td id="requestorAccountName"></td>
      </tr>
      <tr>
        <th><fmt:message key="background.report.request.date"/>:</th>
        <td id="reportRequestDate"></td>
      </tr>
      <tr>
        <th><fmt:message key="Running.Time"/>:</th>
        <td id="runningTime"></td>
      </tr>
    </table>
  </div> 
  <div id="fileInfoDiv">
    <table class="details">
      <tr>
        <th><fmt:message key="File.Name"/>:</th>
        <td id="fileName"></td>
      </tr>
      <tr>
        <th><fmt:message key="Date.Generated"/>:</th>
        <td id="reportFileDate"></td>
      </tr>
    </table>
    <u:yuiButton buttonLabel="Download" buttonId="downloadBenchmarkExtractButton" function="downloadTipBenchmarkReport"/>
  </div>
</div>

<table id="processingTable" style="display:none">
  <tr>
    <td style="vertical-align:middle"><img id="processingImage" src="images/processing.gif" alt="" title=""/></td>
    <td id="processingMessage"><fmt:message key="Processing"/>...</td>
  </tr>
</table>    

<script>
  $('#reportType').change(function() {
    if($(this).val() == 'BACKGROUND_REPORT') {
      checkReportStatus(false);
    } else {
      $('#generateReportButton').show();
      $('#reportStatusDiv').hide();
      $('#processingTable').hide();
    }
  });
</script>
<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<u:formatBoolean var="statusStr" test="${form.isRunning}" trueValue="Running" falseValue="Stopped"/>


<h1>Import Agent Timer</h1> 

<table>
    <tr>
      <th>Timer Status:</th>
      <td>
        <c:out value="${statusStr}"/>
      </td>
    </tr>
</table>
<p>


<c:if test="${form.isRunning}">
  <input id="stopButton" type="button" value="Stop Timer" /> 
  
  <script type="text/javascript">
    var stopButton = new YAHOO.widget.Button("stopButton");
     function stopClick(e) { 
       showProcessing();
       document.location.href = "agentStop.do";
     };        
    stopButton.on("click", stopClick);
  </script>
</c:if>

<c:if test="${!form.isRunning}">
  <input id="startButton" type="button" value="Start Timer" /> 
  
  <script type="text/javascript">
    var startButton = new YAHOO.widget.Button("startButton");
    function startClick(e) { 
      showProcessing();
      document.location.href = "agentStart.do";
    };        
    startButton.on("click", startClick);
  </script>
</c:if>

 


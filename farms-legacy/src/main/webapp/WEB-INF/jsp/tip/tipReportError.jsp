<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<h1>
	<fmt:message key="TIP.Report.Error.Heading"/>
</h1>

<pre style="word-wrap: break-word; white-space: pre-wrap; font-size: 11px;">
<c:out value="${form.tipReportErrorText}" />
</pre>

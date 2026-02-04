<%@ include file="/WEB-INF/jsp/common/include.jsp"%>

<div class="yui-content">

	<div class="yui-navset">
		<div class="yui-content">
			<%@ include file="/WEB-INF/jsp/common/messages.jsp"%>
			<!-- START subTabBody -->
			<tiles:insert attribute="subTabBody" />
			<!-- END subTabBody -->
		</div>
	</div>
</div>

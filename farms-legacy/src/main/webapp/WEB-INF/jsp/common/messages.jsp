<%@ include file="/WEB-INF/jsp/common/include.jsp" %>
<!-- START messages.jsp -->

<logic:messagesPresent message="false">
  <div class="errors">
    <ul>
      <html:messages id="error">
        <li><c:out value="${error}"/></li>
      </html:messages>
    </ul>
  </div>
</logic:messagesPresent>

<logic:messagesPresent message="true">
  <div class="warnings">
    <ul>
      <html:messages id="warning" message="true">
        <li><c:out value="${warning}"/></li>
      </html:messages>
    </ul>
  </div>
</logic:messagesPresent>

<!-- END messages.jsp -->

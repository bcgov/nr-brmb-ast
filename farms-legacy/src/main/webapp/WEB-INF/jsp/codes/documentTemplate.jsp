<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<w:ifUserCanPerformAction action="editCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<h1>
  <c:choose>
    <c:when test="${!canEdit}">
      <fmt:message key="View"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="Edit"/>
    </c:otherwise>
  </c:choose>
  <fmt:message key="Document.Template"/>
</h1>

<h2>
  <c:out value="${form.templateName}"/>
</h2>

<div id="editor" class="editor">
</div>


<script src="trumbowyg/dist/trumbowyg.min.js"></script>
<script src="trumbowyg/dist/plugins/table/trumbowyg.table.min.js"></script>

<script>

  $('#editor').trumbowyg({
      btns: [
        ['viewHTML'],
        ['undo', 'redo'],
        ['formatting'],
        ['strong', 'em', 'del', 'underline'],
        ['fontsize'],
        ['superscript', 'subscript'],
        ['link'],
        ['justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull'],
        ['unorderedList', 'orderedList'],
        ['horizontalRule'],
        ['removeformat'],
        ['fullscreen'],
        ['table']
      ]
  
  })
  ;

</script>


<html:form action="/saveDocumentTemplate" styleId="documentTemplateForm" method="post">
  <html:hidden property="templateName"/>
  <html:hidden property="templateContent" styleId="templateContent" />

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editCodes">
      <script>
        function submitFunc() {
          // Copy the HTML content from the editor to the templateContent hidden field
          $('#templateContent').val($('#editor').trumbowyg('html'));
          submitForm(document.getElementById('documentTemplateForm')); 
        }
      </script>

      <u:yuiButton buttonId="saveButton" buttonLabel="Save" function="submitFunc"/>
      <u:dirtyFormCheck formId="documentTemplateForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm760"/>
  </div>

</html:form>

<script>
  //Set the HTML content for the editor from the hidden field
  $('#editor').trumbowyg('html', $('#templateContent').val())
  
  $('#editor').trumbowyg().on('tbwchange', function(){
    markFormAsDirty();
  });
</script>

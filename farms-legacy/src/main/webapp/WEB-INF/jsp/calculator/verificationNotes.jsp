<%@ include file="/WEB-INF/jsp/common/include.jsp"%>

<div id="saveModal" class="modal">

  <div class="modal-content">
    <table id="editor-content-processing" style="margin:0 auto; display:none;">
      <tr>
        <td style="vertical-align:middle"><img id="processingImage" src="images/processing.gif" alt="" title=""/></td>
        <td id="processingMessage"><fmt:message key="Processing"/>...</td>
      </tr>
    </table>    
    <h1 class="saveMessage" style="display:none; text-align: center;" id="successMessage">
      <fmt:message key="Saved"/>
    </h1>
    <h1 class="saveMessage" style="display:none; color: red; text-align: center;" id="errorMessage">
      <fmt:message key="Error.saving"/>
    </h1>
  </div>

</div>

<c:choose>
  <c:when test="${form.noteType == 'INTERIM'}">
    <c:set var="formName" value="interimNotesForm"></c:set>
    <c:set var="pageTitle"><fmt:message key="Interim.Verification.Notes"/></c:set>
  </c:when>
  <c:when test="${form.noteType == 'FINAL'}">
    <c:set var="pageTitle"><fmt:message key="Final.Verification.Notes" /></c:set>
    <c:set var="formName" value="finalNotesForm"></c:set>
  </c:when>
  <c:when test="${form.noteType == 'ADJUSTMENT'}">
    <c:set var="pageTitle"><fmt:message key="Adjustment.Verification.Notes"/></c:set>
    <c:set var="formName" value="adjustmentNotesForm"></c:set>
  </c:when>
</c:choose>

<h1 style="margin-left: 10px;"><c:out value="${pageTitle}" /></h1>

<p style="margin-left: 10px;">
  <fmt:message key="Verification.Notes.instruction.text" />
</p>

<html-el:form action="/saveVerificationNotes" styleId="${formName}" method="post">
  <html:hidden property="pin" />
  <html:hidden property="year" />
  <html:hidden property="scenarioNumber" />
  <html:hidden property="noteType"/>
  <html:hidden property="notes" styleId="notes" />

</html-el:form>

<div id="editor" class="editor">
</div>

  
<c:if test="${ ! form.readOnly }">
   <div style="width: 100%; overflow: hidden; margin-top: 10px;">
     <div id="autoSavingMessage" style="width: 860px; float: left; text-align: right; color: #003399; font-weight: bold; margin-top: 5px; display: none;">
       <fmt:message key="Auto.saving" />...
     </div>
     <div style="margin-left: 880px;"> <a id="saveButton" href="#"><fmt:message key="Save" /></a> </div>
  </div>
</c:if>

<p style="margin-left: 0px; text-align: right;">
  <fmt:message key="Verification.Notes.shortcut.keys" />
</p>
<p style="margin-left: 0px; text-align: right;">
  <fmt:message key="Verification.Notes.autosave.instruction.text" />
</p>

<script>

  function showProcessing() {
    document.getElementById('editor-content-processing').style.display = '';
    startAnimation();
  }
  
  function undoShowProcessing() {
    document.getElementById('editor-content-processing').style.display = 'none';
  }

  var saveModal = document.getElementById("saveModal");
  var saveInProgress = false;
  var modalCounter = 0;
  const AUTOSAVE_INTERVAL = 60000; // 60 seconds
  
  // When the user clicks anywhere outside of the modal, close it
  window.onclick = function(event) {
    if (!saveInProgress && event.target == saveModal) {
      $("#saveModal").hide();
      modalCounter++;
    }
  }


  $('#editor').trumbowyg({
    btns: [
      ['viewHTML','preformatted'],
      ['historyUndo','historyRedo'],
      ['formatting'],
      ['strong', 'em', 'del', 'underline'],
      ['superscript', 'subscript'],
      ['fontsize','foreColor', 'backColor','fontfamily'],
      ['link'],
      ['justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull'],
      ['indent', 'outdent'],
      ['lineheight'],
      ['unorderedList', 'orderedList'],
      ['horizontalRule'],
      ['removeformat'],
      ['fullscreen'],
      ['table'],
      ['base64'],
      ['specialChars']
    ]
  });

  //Set the HTML content for the editor from the hidden field
  $('#editor').trumbowyg('html', $('#notes').val())

  $('#editor').trumbowyg().on('tbwchange', function(){
    markFormAsDirty();
  });


  var formName = '<c:out value="${formName}" />';
  new YAHOO.widget.Button("saveButton");
  
  function submitError() {
    alert("Error saving notes.");
  }
  
  function saveNotes(showModal, callback) {
    let callbackName = 'undefined';
    if (typeof callback !== 'undefined') {
      callbackName = callback.name;
    }
    consoleLog('saveNotes(showModal=' + showModal + ', callback=' + callbackName + ')');
    let saveAlreadyInProgress = saveInProgress;
    saveInProgress = true;
    
    if (typeof showModal === 'undefined') {
      showModal = true;
    }
    
    if(showModal) {
      consoleLog('saveNotes showing modal');
      $(".saveMessage").hide();
      $("#saveModal").show();
      showProcessing();
    }
    
    if(saveAlreadyInProgress) {
      if (typeof callback !== 'undefined') {
        consoleLog('saveNotes->callback (saveAlreadyInProgress)');
        setTimeout(callback, AUTOSAVE_INTERVAL)
      }
    } else {
      consoleLog('saveNotes saving');
      // Copy the HTML content from the editor to the notes hidden field
      $('#notes').val($('#editor').trumbowyg('html'));
      
      var notesForm = $('#' + formName)
      var serializedForm = notesForm.serialize();
      $.ajax({
        method: 'POST',
        async: true,
        url: '<html:rewrite action="saveVerificationNotes" />',
        data: serializedForm,
        success: function(data, status) {
          consoleLog('saveNotes->success');
          saveInProgress = false;
          undoShowProcessing();
          $("#" + data.status + "Message").show();
          markFormAsNotDirty();
          $('#autoSavingMessage').hide();
          
          // Hide the modal box after the "Saved" message is displayed for one second.
          // In case the user closes the box and then clicks save again, use the counter
          // to make sure this doesn't close it until the second save completes. 
          setTimeout(
            function(preTimeoutCounter) {
              if(preTimeoutCounter == modalCounter) {
                consoleLog('saveNotes->success->hide modal');
                $("#saveModal").hide();
              }
            },
            1000,
            modalCounter
          );
          
          if (typeof callback !== 'undefined') {
            consoleLog('saveNotes->success->callback');
            setTimeout(callback, AUTOSAVE_INTERVAL)
          }
        },
        error: function(data) {
          alert("Error saving notes");
        }
      });
    }
  }
  
  YAHOO.util.Event.addListener(document.getElementById("saveButton"), "click", saveNotes);
  
  var isCtrl = false;
  window.addEventListener('load', function(){
    document.getElementById('editor').addEventListener('keydown', function(e){
      if (e.keyCode == 17) {
        e.preventDefault();
        isCtrl = true;
      }

      if (isCtrl){
        if(e.keyCode == 83 || e.keyCode == 10 || e.keyCode == 13) {
          e.preventDefault();
          saveNotes(true);
        }
      }
    });
  });
  
  document.getElementById('editor').addEventListener('keyup', function(e){
    e.preventDefault();
    isCtrl = false;
  });
  
  function autosave() {
    consoleLog('autosaving');
    if(formIsDirty) {
      consoleLog('autosave->saveNotes');
      $('#autoSavingMessage').show();
      saveNotes(false, autosave);
    } else {
      consoleLog('autosave->setTimeout(autosave)');
      setTimeout(autosave, AUTOSAVE_INTERVAL);
    }
  }
  
  setTimeout(autosave, AUTOSAVE_INTERVAL);
  
</script>

<c:if test="${ ! form.readOnly }">
  <script>
    registerFormForDirtyCheck(document.getElementById(formName));
  </script>
</c:if>

/* global variables */
var doDirtyFormCheck = true;
var formIsDirty = false;
var fieldsExcludedFromDirtyCheck = [];

const DEBUG = false;
function consoleLog(msg) {
  if(DEBUG) {
    console.log(msg);
  }
}


function hideRow(elementId) {
  document.getElementById(elementId).style.display='none';
}

function showRow(elementId) {
  document.getElementById(elementId).style.display='';
}

function toggleDisplay(elementId) {
  var element = document.getElementById(elementId);
  if (element.style.display=='none') {
    element.style.display = '';
  }
  else {
    element.style.display = 'none';
  }
}

function toggleDisplayElementsAndImage(elementIds, imageElementId) {
    var imageElement = document.getElementById(imageElementId);

    for (var x = 0; x < elementIds.length; x++) {
        var element = document.getElementById(elementIds[x]);
        if (element.style.display=='none') {
            element.style.display = '';
            imageElement.src = 'yui/2.8.2r1/build/assets/skins/sam/menubaritem_submenuindicator.png';
            imageElement.width = '16';
            imageElement.height = '4';
        } else {
            element.style.display = 'none';
            imageElement.src = 'yui/2.8.2r1/build/assets/skins/sam/menuitem_submenuindicator.png';
            imageElement.width = '12';
            imageElement.height = '7';
        }
    }
}

function toggleDisplayAndImage(elementId, imageElementId) {
  var element = document.getElementById(elementId);
  var imageElement = document.getElementById(imageElementId);
  if (element.style.display=='none') {
    element.style.display = '';
    imageElement.src = 'yui/2.8.2r1/build/assets/skins/sam/menubaritem_submenuindicator.png';
    imageElement.width = '16';
    imageElement.height = '4';
  }
  else {
    element.style.display = 'none';
    imageElement.src = 'yui/2.8.2r1/build/assets/skins/sam/menuitem_submenuindicator.png';
    imageElement.width = '12';
    imageElement.height = '7';
  }
}

function pad(number,length) {
  var str = '' + number;
  while (str.length < length) {
    str = '0' + str;
  }
  return str;
}

function getToday() {
  var now = new Date();
  var today = now.getFullYear() + '-' + pad(eval(now.getMonth() + 1), 2) + '-' + pad(now.getDate(),2);
  return today;
}

function setCellText(elementId, text) {
    var element = document.getElementById(elementId);
    element.innerHTML = text;
}

function setInputText(elementId, text) {
    var element = document.getElementById(elementId);
    element.value = text;
}

function setToday(elementId) {
  setInputText(elementId, getToday());
}

function setTDToday(elementId) {
  setCellText(elementId, getToday());
}

function copyTDToInput(elementId1, elementId2) {
  var element1 = document.getElementById(elementId1);
  var element2 = document.getElementById(elementId2);
  element2.value = element1.innerHTML;
}


function showAdjustmentPanel(originalValue, adjustedValue, changeDate, changedBy, changeDescription) {  
    var panel;
    
    panel = new YAHOO.widget.Panel("adjustmentPanel",{constraintoviewport:true, width:"400px"});
    panel.setHeader("Adjustment");
    panel.setBody("<table class=\"details\"> \
        <tr> \
            <th>Original Value:</th> \
            <td>"+ originalValue +"</td> \
        </tr> \
        <tr> \
          <th>Adjusted Value:</th> \
            <td>"+ adjustedValue +"</td> \
      </tr> \
        <tr> \
            <th>Change Date:</th> \
            <td>"+ changeDate +"</td> \
        </tr> \
        <tr> \
            <th>Changed By:</th> \
            <td>"+ changedBy +"</td> \
        </tr> \
        <tr> \
            <th>Change Description:</th> \
            <td>"+ changeDescription +"</td> \
        </tr> \
    </table>");
    
    panel.render("bcgov-content");
    panel.show();
    panel.center();
    panel.bringToTop();
    
    var closeHandler = function() {
        panel.destroy();
    };
    panel.hideEvent.subscribe(closeHandler);
}


function startAnimation() { 
  var processingImage = document.getElementById("processingImage");
  processingImage.src = processingImage.src; 
} 


function showProcessing() {
  document.getElementById('bcgov-content-liner').style.display = 'none';
  document.getElementById('bcgov-content-processing').style.display = '';
  setTimeout("startAnimation()", 100); 
}


function undoShowProcessing() {
  document.getElementById('bcgov-content-liner').style.display = '';
  document.getElementById('bcgov-content-processing').style.display = 'none';
}


function reportChange(){
  var rtype = document.getElementById("reportType").value;

  if(rtype == "REPORT_610") {
     window.location.href="farm610.do";
  } else if(rtype == "REPORT_620") {
     window.location.href="farm620.do";
  } else if(rtype == "REPORT_NATIONAL_SURVEILLANCE_STRATEGY") {
     window.location.href="farm650.do";
  } else if(rtype == "REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY") {
	  window.location.href="farm655.do";
  }
}


function exportChange(){
  var extype = document.getElementById("exportType").value;

  if(extype == "REPORT_600"){
     window.location.href="farm600.do";
  } else if(extype == "STA"){
     window.location.href="farm601.do";
  } else if(extype == "AAFMA"){
     window.location.href="farm719.do";
  } else {
     window.location.href="farm720.do";
  }
}


function showConfirmDialog(title, message) {
    var handleCancel = function(o) {
        p.hide();
    };

    var handleOk = function(o) {
        p.hide();
    };

    var p = new YAHOO.widget.Panel("p", 
             { width: "300px",
               fixedcenter: true,
               visible: false,
               draggable: true,
               close: true,
               text: message,
               icon: YAHOO.widget.SimpleDialog.ICON_HELP,
               constraintoviewport: true,
               buttons: [ { text:"Ok", handler:handleOk, isDefault:true },
                          { text:"Cancel",  handler:handleCancel } ]
             } );
    
    p.setHeader(title);
    p.render("bcgov-content-liner");
    p.show();
    p.center();
}


function openNewWindow(href) {
   window.open(href, 'popupWindow', 'width=1000,height=700,toolbar=0,location=0,directories=0,status=0,menubar=1,scrollbars=1,resizable=1').focus();
}

function destroyPanel() {
    this.destroy();
}

function openPlainWindow(href) {
   window.open(href, 'popupWindow', 'width=850,height=700,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1,resizable=1').focus();
}

var panel;
function init() {
    
    panel = new YAHOO.widget.Panel("adjustmentPanel", {
    width:"400px",
    constraintoviewport: true, 
    underlay:"shadow", 
    close:true, 
    visible:false, 
    draggable:true,
    dragOnly:true} );
}
YAHOO.util.Event.addListener(window, "load", init);

//Simple Javascript show hide buttons
function completeText(id){
    YAHOO.util.Dom.setStyle('elipsis'+id, 'display', 'none'); 
    YAHOO.util.Dom.setStyle('finishText'+id, 'display', 'inline'); 
}
function shortenText(id){
    YAHOO.util.Dom.setStyle('elipsis'+id, 'display', ''); 
    YAHOO.util.Dom.setStyle('finishText'+id, 'display', 'none'); 
}

function loadWindow(url, width, height, name){
	var popupWindowHandle;

	if (name == null) {
		name = "PopupWindow";
	}
    if(popupWindowHandle && popupWindowHandle.open && !popupWindowHandle.closed){
        // already open so setfocus and refresh content
        popupWindowHandle.focus();
        popupWindowHandle.location = url;
    } else {
        // not open so create a new one
        var iMyWidth;
        var iMyHeight;
        
        //half the screen width minus half the new window width (plus 5 pixel borders).
        iMyWidth = (window.screen.width/2) - (width/2 + 10);
        //half the screen height minus half the new window height (plus title and status bars).
        iMyHeight = (window.screen.height/2) - (height/2 + 50);
        //Open the window.
        popupWindowHandle = window.open(url,name,"status=no,height=" + height + ",width=" + width + ",resizable=yes,left=" + iMyWidth + ",top=" + iMyHeight + ",screenX=" + iMyWidth + ",screenY=" + iMyHeight + ",toolbar=no,menubar=no,scrollbars=yes,location=no,directories=no,status=no");
        popupWindowHandle.focus();
    }
}

if(!Array.indexOf){
  Array.prototype.indexOf = function(obj){
   for(var i=0; i<this.length; i++){
    if(this[i]==obj){
     return i;
    }
   }
   return -1;
  };
}   

if(!Array.insert){
	Array.prototype.insert = function(index) {
	    index = Math.min(index, this.length);
	    arguments.length > 1
	        && this.splice.apply(this, [index, 0].concat([].pop.call(arguments)))
	        && this.insert.apply(this, arguments);
	    return this;
	};
}   

var Farm = YAHOO.namespace("farm");

function popIframePanel(title, url, myWidth, myHeight, isModal) {

    YAHOO.farm.iFrame_panel = new YAHOO.widget.Panel("statusPanel", 
        { 
            constraintoviewport: true, 
            width:myWidth + "px",
            height:myHeight + "px",
            underlay:"shadow", 
            close:true, 
            visible:false,
            modal:isModal,
            draggable:true,
            dragOnly:true
        } 
        ); 
    YAHOO.farm.iFrame_panel.setHeader(title);
    YAHOO.farm.iFrame_panel.setBody("<iframe id='popUpPreviewBody' src='" + url + "' frameborder='0' style='width:100%; height:100%;border:0px;'></iframe>");
    YAHOO.farm.iFrame_panel.render("bcgov-content-liner");
    YAHOO.farm.iFrame_panel.show();
    YAHOO.farm.iFrame_panel.center();
}


function isFormDirty(form) {
    if(form) {
      if(formIsDirty) {
        return true;
      }
      var el, opt, hasDefault, i = 0, j;
      while (el = form.elements[i++]) {
        var excluded = fieldsExcludedFromDirtyCheck.indexOf(el.name) >= 0;
        if( ! excluded ) {
          switch (el.type) {
          case 'text' :
          case 'textarea' :
              //if current value exists and is different than original, then dirty...
              if (!/^\s*$/.test(el.value) && el.value != el.defaultValue) {
                  return true;
              }
              //if original value exists and current is empty, then dirty...
              if (!/^\s*$/.test(el.defaultValue) && /^\s*$/.test(el.value)) {
                  return true;
              }
              break;
          case 'checkbox' :
          case 'radio' :
              if (el.checked != el.defaultChecked) {
                  return true;
              }
              break;
          case 'select-one' :
          case 'select-multiple' :
              j = 0, hasDefault = false;
              while (opt = el.options[j++])
                  if (opt.defaultSelected)
                      hasDefault = true;
              j = hasDefault ? 0 : 1;
              while (opt = el.options[j++])
                  if (opt.selected != opt.defaultSelected) {
                      return true;
                  }
              break;
          }
        }
      }
    }
    return false;
}

function registerFormForDirtyCheck(form) {
  window.onbeforeunload = function() {
    if (doDirtyFormCheck) {
      if(isFormDirty(form)) { 
        return 'You have unsaved changes.';
      }
    }
  };
}

function excludeFieldFromDirtyCheck(fieldName) {
  fieldsExcludedFromDirtyCheck.push(fieldName);
}

function disableDirtyFormCheck() {
  doDirtyFormCheck = false;
}

function markFormAsDirty() {
  formIsDirty = true;
}

function markFormAsNotDirty() {
  formIsDirty = false;
}

function submitForm(form) {
  disableDirtyFormCheck();
  showProcessing();
  form.submit();
}

function selectAll(field) {
  field.focus();
  field.select();
}

function disableEnterKey(e) {
  var key;
  if(window.event) {
    key = window.event.keyCode;  // IE, Chrome
  } else {
    key = e.which;  // Firefox
  }
  if(key == 13) {
    return false;
  } else {
    return true;
  }
}

function roundCurrency(value) {
  if (value == null || value == "" || isNaN(value)) {
    return value;
  }
  
  let num = parseFloat(value);
  num = num.toFixed(2);
  num = parseFloat(num);
  
  return num;
}
        
function getLivestockUnitCode(inventoryItemCode) {

  switch (inventoryItemCode) {
    case 7603:        // Bees
    case 7600:        // Beeswax
    case 7604:        // Honey
    case 7602:        // Beeswax Produced
    case 7614:        // Honey Produced
      return 1;
    case 7606:        // Honey Bees
    case 7608:        // Honey Bees Colony
    case 7610:        // Honey Bees Package
    case 7612:        // Honey Bees; Producing (Hives)
      return 99;
    case 7663:        // Eggs for Hatching
    case 7664:        // Eggs for Consumption 
    case 7665:        // Eggs for Consumption; Organic
      return 3;
    default:
      return 32;
    }
  }

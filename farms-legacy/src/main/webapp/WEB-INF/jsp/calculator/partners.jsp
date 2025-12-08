<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/vue3.global.prod.js"></script>

<script type="text/javascript" src="yui/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="yui/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>

<table id="partnersProcessing" style="margin:0 auto; height:300px;">
  <tr>
    <td style="vertical-align:middle"><img id="processingImage" src="images/processing.gif" alt="" title=""/></td>
    <td id="processingMessage"><fmt:message key="Processing"/>...</td>
  </tr>
</table>    

<html:form action="savePartners" styleId="mainForm" method="post" style="display: none;">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="scenarioRevisionCount"/>

  <div id="vueApp">

    <div v-cloak>

      <html:hidden property="formDataJson" styleId="formDataJson"/>
      <html:hidden property="metaDataJson" styleId="metaDataJson"/>
  
      <fieldset>
        <legend><fmt:message key="Partners"/></legend>
        <div class="rTable numeric blueGrayTable" style="width: 100%;">
          <div class="rTableRow">
            <div class="rTableHead"><fmt:message key="Operation"/></div> 
            <div class="rTableHead"><fmt:message key="Participant.PIN"/></div> 
            <div class="rTableHead"><fmt:message key="Partnership.Percent"/></div>
            <div class="rTableHead"><fmt:message key="First.Name"/></div> 
            <div class="rTableHead"><fmt:message key="Last.Name"/></div> 
            <div class="rTableHead"><fmt:message key="Corporation.Name"/></div> 
            <c:if test="${ ! form.readOnly }">
              <div class="rTableHead"></div>
            </c:if> 
          </div>
          <div class="rTableRow" v-for="(partner, index) in partners">
            <div class="rTableCell">
              <select v-model="partner.operationSchedule">
                <option v-for="schedule in metaData.operationSchedules" :value="schedule">{{ schedule }}</option>
              </select>
            </div>
            <div class="rTableCell">
              <input type="text" maxlength="9" v-model="partner.participantPin" style="width: 80px;" />
            </div>
            <div class="rTableCell">
              <input type="text" maxlength="5" v-model="partner.partnerPercent" style="width: 100px;" />
            </div>
            <div class="rTableCell">
              <input type="text" maxlength="100" v-model="partner.firstName" style="width: 100%;" />
            </div>
            <div class="rTableCell">
              <input type="text" maxlength="100" v-model="partner.lastName" style="width: 100%;" />
            </div>
            <div class="rTableCell">
              <input type="text" maxlength="100" v-model="partner.corpName" style="width: 100%;" />
            </div>
            <c:if test="${ ! form.readOnly }">
              <div class="rTableCell">
                <span class="yui-button yui-push-button" style="float: right;">
                  <span class="first-child">
                    <button type="button" v-on:click="removePartner(index)"><fmt:message key="Remove"/></button>
                  </span>
                </span>
              </div>
            </c:if>
          </div>
        </div>
        <p></p>
      </fieldset>

      <c:if test="${ ! form.readOnly }">
        <span class="yui-button yui-push-button">
          <span class="first-child">
            <button type="button" v-on:click='addPartner()'><fmt:message key="Add.Partner"/></button>
          </span>
        </span>
      </c:if>
    
    </div>
  </div>


<script>

  var vueData = {
    formData: JSON.parse($('#formDataJson').val()),
    metaData: JSON.parse($('#metaDataJson').val())
  };
  
  for(var i = 0; i < vueData.formData.partners.length; i++) {
    let partner = vueData.formData.partners[i];
    partner.id = i;
  }
  
  const app = Vue.createApp({
    data(){
      return vueData;
    },
    methods: {
      notBlank: function(str) {
        return str !== null && str !== "";
      },
      addPartner: function(participantPin, partnerPercent, firstName, lastName, corpName){
        let index = this.formData.partners.length;
        this.formData.partners.push({
          id: index,
          participantPin: participantPin,
          partnerPercent: partnerPercent,
          firstName: firstName,
          lastName: lastName,
          corpName: corpName
        });
      },
      removePartner: function(index){
        this.formData.partners.splice(index, 1)
        for(var i = 0; i < this.formData.partners.length; i++) {
          let partner = vueData.partners[i];
          partner.id = i;
        }
      }
    },
    computed: {
      partners: function(){
        return this.formData.partners;
      }
    },
    mounted: function (){
      excludeFieldFromDirtyCheck(''); // Vue.js creates fields without IDs 
    },
    updated: function (){
      markFormAsDirty();
    }
  });

  const vueRoot = app.mount('#vueApp');

</script>
  
  <c:if test="${ ! form.readOnly }">
  
    <p></p>
    <div class="rTable">
      <div class="rTableRow">
        <div class="rTableHead"><div style="width: 230px;"><fmt:message key="Find.Partner.by.Participant.PIN"/>:</div></div>
        <div class="rTableCell">
          <div id="autoParnterPinContainer">
            <input type="text" id="parnerPinSearchInput" />
            <div id="parnterPinContainer"></div>
          </div>
        </div>
      </div>
      <div class="rTableRow">
        <div class="rTableHead"><fmt:message key="Find.Partner.by.Name"/>:</div>
        <div class="rTableCell">
          <div id="autoParnterNameContainer" style="width: 600px;">
            <input type="text" id="parnerNameSearchInput" />
            <div id="parnterNameContainer"></div>
          </div>
        </div>
      </div>
    </div>

    <div style="text-align:right;width:70%">
      <script>
        function save() {
          $('#formDataJson').val( JSON.stringify(vueData.formData) );
          submitForm(document.getElementById('mainForm'));
        }
      </script>
    
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" function="save" />
  
      <u:dirtyFormCheck formId="mainForm"/>
    </div>

    <script type="text/javascript" src="<html:rewrite action="partnersJavaScriptList"/>"></script>
  
    <script>
      function getItemSelectHandler(oDS, itemContainer, searchInputField, schemaFields, queryMatchContains) {
        return function() {
          // Use a LocalDataSource
          oDS.responseSchema = {fields : schemaFields};
          
          // Instantiate the AutoComplete
          var oAC = new YAHOO.widget.AutoComplete(searchInputField, itemContainer, oDS);
          oAC.resultTypeList = false;
          oAC.forceSelection = true;
          oAC.queryMatchContains = queryMatchContains;
  
          var clearHandler = function(sType, aArgs) {
              var myAC = aArgs[0]; // reference back to the AC instance
              var clearedValue = aArgs[1]; // The cleared value that did not match anything
  
              // update hidden form field with the selected item's ID
              $('#' + searchInputField).val('');
          };
          oAC.selectionEnforceEvent.subscribe(clearHandler);


          // Define an event handler to populate a hidden form field
          // when an item gets selected
          var selectionHandler = function(sType, aArgs) {
              var myAC = aArgs[0]; // reference back to the AC instance
              var elLI = aArgs[1]; // reference to the selected LI element
              var oData = aArgs[2]; // object literal of selected item's result data
  
              // update hidden form field with the selected item's ID
              vueRoot.addPartner(oData.p,
                                 oData.t,
                                 oData.f,
                                 oData.l,
                                 oData.c);
  
              $('#' + searchInputField).val('');
          };
          oAC.itemSelectEvent.subscribe(selectionHandler);
  
          return {
              oDS: oDS,
              oAC: oAC
          };
        }();
      }
      
      YAHOO.farm.ItemSelectHandler = getItemSelectHandler(new YAHOO.util.LocalDataSource(YAHOO.farm.Data.partners), 'parnterPinContainer', 'parnerPinSearchInput', ["pn", "p", "t", "f", "l", "c", "n"], false);
      YAHOO.farm.ItemSelectHandler = getItemSelectHandler(new YAHOO.util.LocalDataSource(YAHOO.farm.Data.partners), 'parnterNameContainer', 'parnerNameSearchInput', ["n", "p", "t", "f", "l", "c"], true);
    </script>
  </c:if>

  <script>
    $('#partnersProcessing').hide();
    $('#mainForm').show();
  </script>

</html:form>

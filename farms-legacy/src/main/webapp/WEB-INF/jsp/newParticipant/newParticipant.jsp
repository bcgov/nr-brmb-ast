<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/vue3.global.prod.js"></script>

<h1>
  <fmt:message key="New.Participant.Profile"/>
</h1>
<p></p>


<html:form action="saveNewParticipant" styleId="newParticipantForm" method="post">

  <div id="vueApp">

    <div v-cloak>

      <html:hidden property="newParticipantJson" styleId="newParticipantJson"/>
      <html:hidden property="metaDataJson" styleId="metaDataJson"/>
  
      <fieldset>
        <legend><fmt:message key="Participant"/></legend>
          <div class="rTable" style="width: 100%;">
          <text-field-row v-model="participant.participantPin" label="<fmt:message key="Participant.Identification.Number.PIN"/>"
            field-id="participantPin" maxlength="9" :required="true"></text-field-row>
          
          <text-field-row v-model="participant.owner.firstName" label="<fmt:message key="First.Name"/>"
            field-id="ownerFirstName" maxlength="100" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.owner.lastName" label="<fmt:message key="Last.Name"/>"
            field-id="ownerLastName" maxlength="100" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.owner.corpName" label="<fmt:message key="Corporation.Name"/>"
            field-id="ownerCorpName" maxlength="100" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.sin" label="<fmt:message key="SIN"/>"
            field-id="sin" maxlength="9" :required="false" v-if="isOwnerIndividual"></text-field-row>
          
          <text-field-row v-model="participant.businessNumber" label="<fmt:message key="Business.Number.BN"/>"
            field-id="businessNumber" maxlength="15" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.trustNumber" label="<fmt:message key="Trust.Number.CTN"/>"
            field-id="trustNumber" maxlength="9" :required="false" v-if="isOwnerCorporation"></text-field-row>
          
          <text-field-row v-model="participant.owner.addressLine1" label="<fmt:message key="Address.1"/>"
            field-id="ownerAddressLine1" maxlength="80" :required="true"></text-field-row>
          
          <text-field-row v-model="participant.owner.addressLine2" label="<fmt:message key="Address.2"/>"
            field-id="ownerAddressLine2" maxlength="80" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.owner.city" label="<fmt:message key="City"/>"
            field-id="ownerCity" maxlength="80" :required="true"></text-field-row>
          
          <text-field-row v-model="participant.owner.postalCode" label="<fmt:message key="Postal.Code"/>"
            field-id="ownerPostalCode" maxlength="6" :required="true"></text-field-row>
          
          <text-field-row v-model="participant.owner.provinceState" label="<fmt:message key="Province"/>"
            field-id="ownerProvince" maxlength="2" :required="true"></text-field-row>
          
          <text-field-row v-model="participant.owner.daytimePhone" label="<fmt:message key="Phone.Day"/>"
            field-id="ownerDaytimePhone" maxlength="20" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.owner.eveningPhone" label="<fmt:message key="Phone.Evening"/>"
            field-id="ownerEveningPhone" maxlength="20" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.owner.cellNumber" label="<fmt:message key="Phone.Cell"/>"
            field-id="ownerEveningPhone" maxlength="20" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.owner.faxNumber" label="<fmt:message key="Phone.FAX"/>"
            field-id="ownerFaxNumber" maxlength="20" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.owner.emailAddress" label="<fmt:message key="Email.Address"/>"
            field-id="ownerEmailAddress" maxlength="150" :required="false"></text-field-row>
          
        </div>
      </fieldset>
  
      <fieldset>
        <legend><fmt:message key="Contact"/></legend>
        <div class="rTable" style="width: 100%;">
          
          <text-field-row v-model="participant.contact.firstName" label="<fmt:message key="First.Name"/>"
            field-id="contactFirstName" maxlength="100" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.lastName" label="<fmt:message key="Last.Name"/>"
            field-id="contactLastName" maxlength="100" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.corpName" label="<fmt:message key="Business.Name"/>"
            field-id="contactCorpName" maxlength="100" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.addressLine1" label="<fmt:message key="Address.1"/>"
            field-id="contactAddressLine2" maxlength="80" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.addressLine2" label="<fmt:message key="Address.2"/>"
            field-id="contactAddressLine1" maxlength="80" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.city" label="<fmt:message key="City"/>"
            field-id="contactCity" maxlength="80" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.postalCode" label="<fmt:message key="Postal.Code"/>"
            field-id="contactPostalCode" maxlength="6" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.provinceState" label="<fmt:message key="Province"/>"
            field-id="ownerProvince" maxlength="2" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.daytimePhone" label="<fmt:message key="Phone"/>"
            field-id="contactDaytimePhone" maxlength="20"></text-field-row>
          
          <text-field-row v-model="participant.contact.cellNumber" label="<fmt:message key="Phone.Cell"/>"
            field-id="contactEveningPhone" maxlength="20" :required="false"></text-field-row>
          
          <text-field-row v-model="participant.contact.faxNumber" label="<fmt:message key="Phone.FAX"/>"
            field-id="contactFaxNumber" maxlength="20"></text-field-row>
          
          <text-field-row v-model="participant.contact.emailAddress" label="<fmt:message key="Email.Address"/>"
            field-id="contactEmailAddress" maxlength="150"></text-field-row>
          
        </div>
      </fieldset>

      <fieldset>
        <legend><fmt:message key="Other"/></legend>
        <div class="rTable" style="width: 100%;">
          
          <text-field-row v-model="participant.programYear" label="<fmt:message key="Program.Year"/>"
            field-id="programYear" maxlength="4" :required="true"></text-field-row>

          <div class="rTableRow">
            <div class="rTableHead"><label for="municipalityCode" class="required"><fmt:message key="Municipality"/>:</label></div>
            <div class="rTableCell">
              <select v-model="participant.municipalityCode" id="municipalityCode">
                <template v-for="municipality in metaData.municipalities">
                  <option :value="municipality.value" v-if="municipality.value != '-1' && municipality.value != '0'">{{ municipality.label }}</option>
                </template>
              </select>
            </div>
          </div>
        </div>
      </fieldset>
  
      <h2>
        <fmt:message key="Operations"/>
      </h2>
      <p></p>

      <fieldset v-for="(operation, index) in participant.operations" :key="operation.id">
        <legend><fmt:message key="Operation"/> {{operation.operationNumber}}</legend>
        <div class="rTable" style="width: 100%;">
          
          <text-field-row v-model="operation.partnershipPin" label="<fmt:message key="Partnership.PIN"/>"
            :field-id="'partnershipPercent' + operation.operationNumber" maxlength="9">
            <span class="yui-button yui-push-button" style="float: right;">
              <span class="first-child">
                <button type="button" v-if="operation.operationNumber > 1" v-on:click="removeOperation(index)">Remove Operation</button>
              </span>
            </span>
          </text-field-row>
          
          <text-field-row v-model="operation.partnershipName" label="<fmt:message key="Partnership.Name"/>"
            :field-id="'partnershipName' + operation.operationNumber" maxlength="42" :required="false"></text-field-row>

          <text-field-row v-model="operation.partnershipPercent" label="<fmt:message key="Partnership.Percent"/>"
            :field-id="'partnershipPercent' + operation.operationNumber" maxlength="5"></text-field-row>

          <div class="rTableRow">
            <div class="rTableHead"><label for="accountingCode" class="required"><fmt:message key="Accounting.Code"/>:</label></div>
            <div class="rTableCell">
              <select v-model="operation.accountingCode" id="accountingCode">
                <option v-for="accountingCode in metaData.accountingCodes" :value="accountingCode.value">{{ accountingCode.label }}</option>
              </select>
            </div>
          </div>
          <div class="rTableRow">
            <div class="rTableHead"><label for="fiscalStartDate"><fmt:message key="Fiscal.Start"/>:</label></div>
            <div class="rTableCell">
              <div style="float:left;width:175px;">
                <input type="text" maxlength="10" v-model="operation.fiscalStartDate" id="fiscalStartDate" />
              </div>
              (YYYY-MM-DD)
            </div>
          </div>
          <div class="rTableRow">
            <div class="rTableHead"><label for="fiscalEndDate"><fmt:message key="Fiscal.End"/>:</label></div>
            <div class="rTableCell">
              <div style="float:left;width:175px;">
                <input type="text" maxlength="10" v-model="operation.fiscalEndDate" id="fiscalEndDate" />
              </div>
              (YYYY-MM-DD)
            </div>
          </div>
        </div>
      </fieldset>

      <span class="yui-button yui-push-button">
        <span class="first-child">
          <button type="button" v-on:click='addOperation()'><fmt:message key="Add.Operation"/></button>
        </span>
      </span>

    </div>
  </div>
  
  <div style="text-align:right;width:70%">
      <script>
        function saveNewParticipant() {
          $('#newParticipantJson').val( JSON.stringify(vueData.participant) );
          submitForm(document.getElementById('newParticipantForm'));
        }
      </script>
    
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" function="saveNewParticipant" />
  
      <u:dirtyFormCheck formId="newParticipantForm"/>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="welcome" />
  </div>

</html:form>

<script>

  var vueData = {
    participant: JSON.parse($('#newParticipantJson').val()),
    metaData: JSON.parse($('#metaDataJson').val())
  };
  
  for(var i = 0; i < vueData.participant.operations.length; i++) {
    let curOp = vueData.participant.operations[i];
    curOp.id = curOp.operationNumber;
  }
  
  const app = Vue.createApp({
    data(){
      return vueData;
    },
    methods: {
      notBlank: function(str) {
        return str !== null && str !== "";
      },
      addOperation: function (){
        let opNum = "" + (this.participant.operations.length + 1);
        this.participant.operations.push({
          operationNumber: opNum,
          id: opNum,
          fiscalStartDate: this.programYear + "-1-1",
          fiscalEndDate: this.programYear + "-12-31"
        });
      },
      removeOperation: function (index){
        this.participant.operations.splice(index, 1)
        for(var i = 0; i < this.participant.operations.length; i++) {
          let curOp = this.participant.operations[i];
          curOp.id = i + 1;
          curOp.operationNumber = i + 1;
        }
      }
    },
    computed: {
      isOwnerCorporation: function() {
        return this.notBlank(this.participant.owner.corpName);
      },
      isOwnerIndividual: function() {
        return this.notBlank(this.participant.owner.firstName) || this.notBlank(this.participant.owner.lastName);
      },
      isContactCorporation: function() {
        return this.notBlank(this.participant.contact.corpName);
      },
      isContactIndividual: function() {
        return this.notBlank(this.participant.contact.firstName) || this.notBlank(this.participant.contact.lastName);
      },
      programYear: function() {
        return this.participant.programYear;
      }
    },
    mounted: function (){
      excludeFieldFromDirtyCheck(''); // Vue.js creates fields without IDs 
    },
    updated: function (){
      markFormAsDirty();
    },
    watch: {
      programYear(newYear, oldYear) {
        if(newYear.length == 4) {
          let dateRegex = new RegExp("^\\d{4}-\\d{1,2}-\\d{1,2}$");
          
          for(let i = 0; i < this.participant.operations.length; i++) {
            let operation = this.participant.operations[i];
            
            if(operation.fiscalStartDate == null || operation.fiscalStartDate.length == 0) {
              operation.fiscalStartDate = newYear + "-1-1";
            } else if(dateRegex.test(operation.fiscalStartDate)) {
              operation.fiscalStartDate = newYear + operation.fiscalStartDate.substring(4);
            }
            
            if(operation.fiscalEndDate == null || operation.fiscalEndDate.length == 0) {
              operation.fiscalEndDate = newYear + "-12-31";
            } else if(dateRegex.test(operation.fiscalEndDate)) {
              operation.fiscalEndDate = newYear + operation.fiscalEndDate.substring(4);
            }
            
          }
        }
      }
    }
  });

  app.component('text-field-row', {
    props: ['modelValue', 'label','fieldId','maxlength', 'required'],
    methods: {
      updateValue(event) {
          this.$emit('update:modelValue', event.target.value);
      }
    },
    template: '\
      <div class="rTableRow">\
        <div class="rTableHead" style="width: 300px;"><label :for="fieldId" :class="{required: required}">{{ label }}:</label></div>\
        <div class="rTableCell">\
          <input type="text" :value="modelValue" @input="updateValue" :maxlength="maxlength" :id="fieldId" />\
          <slot></slot>\
        </div>\
      </div>'
  });

  app.mount('#vueApp');

</script>

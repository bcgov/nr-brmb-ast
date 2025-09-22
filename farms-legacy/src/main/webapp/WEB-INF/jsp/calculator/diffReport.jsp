<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/vue3.global.prod.js"></script>

<%@ include file="/WEB-INF/jsp/common/vue3-filters.jsp" %>

<script type="text/javascript">

  function returnToStatus() {
    if(window.opener && !window.opener.closed) {
      window.opener.location.href = "<html:rewrite action="farm800"/>?<c:out value="${form.scenarioParamsString}&refresh=true" escapeXml="false"/>";
      window.opener.showProcessing();
    }
    window.close();
  }
  
  function onPageLoad(){
    <c:if test="${form.pyvDiff == null}">
      returnToStatus();
    </c:if>
  }

</script>

<html:form action="updateToLatestPyVersion" styleId="updatePyvForm" method="post" onsubmit="showProcessing()">
  <html:hidden property="pin"/>
  <html:hidden property="year"/>
  <html:hidden property="scenarioNumber"/>
  <html:hidden property="diffYear"/>

  <div id="vueApp">

    <div v-cloak>

      <html:hidden property="formDataJson" styleId="formDataJson"/>
      <html:hidden property="pyvDiffJson" styleId="pyvDiffJson"/>
  
      <div class="rTable" style="width: 100%;">
        <div class="rTableRow">
          <div class="rTableHead"> 
          
            <div v-if=" ! pyvDiff.isHasDifferences">
              <fmt:message key="diff.report.no.differences"/>
            </div>
            <div v-else>

              <fieldset>
                <legend><fmt:message key="Program.Year.Detail"/></legend>
                
                <div v-if=" ! pyvDiff.isHasPyvDetailDifferences">
                  <fmt:message key="No.Differences"/>
                </div>
                
                <div v-else>
                
                  <div class="rTable diffreport" style="width: 100%;">
                    <div class="rTableRow">
                      <div class="rTableHead"><fmt:message key="Attribute"/></div>
                      <div class="rTableHead"><fmt:message key="Existing.Value"/></div>
                      <div class="rTableHead"><fmt:message key="New.Value"/></div>
                    </div>
  
                    <div class="rTableRow" v-for="field in pyvDiff.fieldDiffs">
                      <div class="rTableCell alignleft">{{ field.fieldName }}</div>
                      <div class="rTableCell">{{ field.oldValue }}</div>
                      <div class="rTableCell">{{ field.newValue }}</div>
                    </div>
                  </div>

                  <div class="rTable diffreport" style="width: 100%;">
                    <div class="rTableRow" v-if="pyvDiff.isLocallyUpdated">
                      <div class="rTableCell">
                        <div align="center">
                          <span class="negativeAmount" style="font-weight: bold">
                            <fmt:message key="data.locally.updated"/>
                            <template v-if=" ! formData.readOnly">
                              <fmt:message key="accept.new.data.question"/>
                            </template>
                          </span>
                          <div v-if="! formData.readOnly">
                            <br />
                            <span class="negativeAmount" style="font-weight: bold">
                              <input type="radio" v-model="formData.pyvAcceptNewData" id="pyvAcceptNewDataYes" value="true"/>
                                <label for="pyvAcceptNewDataYes"><fmt:message key="Yes"/></label>
                              <input type="radio" v-model="formData.pyvAcceptNewData" id="pyvAcceptNewDataNo" value="false"/>
                                <label for="pyvAcceptNewDataNo"><fmt:message key="No"/></label>
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                </div>
              </fieldset>                  
      
              <fieldset v-for="foDiff in pyvDiff.farmingOperationDiffs">
                <legend><fmt:message key="Operation"/> {{ foDiff.schedule }} <fmt:message key="Detail"/></legend>
                
                <div v-if=" ! foDiff.oldOpExists">
                  <fmt:message key="Operation"/> {{ foDiff.schedule }}
                  <fmt:message key="exists.in.the.new.version.but.not.in.the.current.version"/>
                </div>
                <div v-else-if=" ! foDiff.newOpExists">
                  <fmt:message key="Operation"/> {{ foDiff.schedule }}
                  <fmt:message key="exists.in.the.current.version.but.not.in.the.new.version"/>
                </div>
                <div v-else-if=" ! foDiff.isHasDifferences">
                  <fmt:message key="No.Differences"/>
                </div>
                <div v-else>
	                <fieldset v-if="foDiff.fieldDiffs.length > 0" style="margin-top: 10px;">
	                  <legend><fmt:message key="Operation"/> {{foDiff.schedule}} <fmt:message key="Attributes"/></legend>
	
	                  <div class="rTable diffreport" style="width: 100%;">
	
	                    <div class="rTableRow">
	                      <div class="rTableHead"><fmt:message key="Attribute"/></div>
	                      <div class="rTableHead"><fmt:message key="Existing.Value"/></div>
	                      <div class="rTableHead"><fmt:message key="New.Value"/></div>
	                    </div>

                       <div class="rTableRow" v-for="field in foDiff.fieldDiffs">
                         <div class="rTableCell alignLeft">{{ field.fieldName }}</div>
                         <div class="rTableCell">{{ field.oldValue }}</div>
                         <div class="rTableCell">{{ field.newValue }}</div>
                       </div>
                    </div>

                    <div class="rTable diffreport" style="width: 100%;">
                       <div class="rTableRow" v-if="foDiff.isLocallyUpdated">
                         <div class="rTableCell">
                           <div align="center">
                             <span class="negativeAmount" style="font-weight: bold">
                               <fmt:message key="data.locally.updated"/>
                               <div v-if=" ! formData.readOnly">
                                 <fmt:message key="accept.new.data.question"/>
                               </div>
                             </span>
                             <div v-if=" ! formData.readOnly">
                               <br />
                               <span class="negativeAmount" style="font-weight: bold">
                                 <input type="radio" v-model="formData.operationAcceptNewData[foDiff.operationNumber]" id="operationAcceptNewDataYes" value="true"/>
                                   <label for="operationAcceptNewDataYes"><fmt:message key="Yes"/></label>
                                 <input type="radio" v-model="formData.operationAcceptNewData[foDiff.operationNumber]" id="operationAcceptNewDataNo" value="false"/>
                                   <label for="operationAcceptNewDataNo"><fmt:message key="No"/></label>
                               </span>
                             </div>
                           </div>
                         </div>
                       </div>
	                  </div>
                    
	                </fieldset>
  
                  <fieldset v-if="foDiff.incomeExpenseDiffs.length > 0">
                    <legend><fmt:message key="Income.and.Expenses"/></legend>
                    <div class="rTable diffreport" style="width: 100%;">
                      <div class="rTableRow">
                        <div class="rTableHead"><fmt:message key="Code"/></div>
                        <div class="rTableHead"><fmt:message key="Expense.Indicator"/></div>
                        <div class="rTableHead">Existing Value</div>
                        <div class="rTableHead"><fmt:message key="New.Value"/></div>
                      </div>

                      <div class="rTableRow" v-for="ieDiff in foDiff.incomeExpenseDiffs">
                        <div class="rTableCell alignLeft">{{ieDiff.code}} - {{ieDiff.description}}</div>
                        <div class="rTableCell">
                          <template v-if="ieDiff.isExpense">Y</template>
                          <template v-else>N</template>
                        </div>
                        <div class="rTableCell">{{ $f.toCurrency( ieDiff.oldValue ) }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( ieDiff.newValue ) }}</div>
                      </div>

                    </div>
                  </fieldset>
  
                  <fieldset v-if="foDiff.cropDiffs.length > 0">
                    <legend><fmt:message key="Crops"/></legend>
                    <div class="rTable diffreport" style="width: 100%;">
                      <div class="rTableRow">
                        <div class="rTableHead"><fmt:message key="Code"/></div>
                        <div class="rTableHead"><fmt:message key="Crop.Unit"/></div>
                        <div class="rTableHead"><fmt:message key="On.Farm.Acres"/></div>
                        <div class="rTableHead"><fmt:message key="Unseedable.Acres"/></div>
                        <div class="rTableHead"><fmt:message key="Qty.Produced"/></div>
                        <div class="rTableHead"><fmt:message key="Qty.Start"/></div>
                        <div class="rTableHead"><fmt:message key="Price.Start"/></div>
                        <div class="rTableHead"><fmt:message key="Qty.End"/></div>
                        <div class="rTableHead"><fmt:message key="Price.End"/></div>
                        <div class="rTableHead"><fmt:message key="Action"/></div>
                      </div>

                      <div class="rTableRow" v-for="cropDiff in foDiff.cropDiffs">
                        <div class="rTableCell alignLeft">{{ cropDiff.code }} - {{ cropDiff.description }}</div>
                        <div class="rTableCell">{{ cropDiff.cropUnitCode }} - {{ cropDiff.cropUnitCodeDescription }}</div>
                        <div class="rTableCell">{{ $f.toDecimal3( cropDiff.onFarmAcres)}}</div>
                        <div class="rTableCell">{{ $f.toDecimal3( cropDiff.unseedableAcres  ) }}</div>
                        <div class="rTableCell">{{ $f.toDecimal3( cropDiff.quantityProduced ) }}</div>
                        <div class="rTableCell">{{ $f.toDecimal3( cropDiff.quantityStart    ) }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( cropDiff.priceStart       ) }}</div>
                        <div class="rTableCell">{{ $f.toDecimal3( cropDiff.quantityEnd      ) }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( cropDiff.priceEnd         ) }}</div>
                        <div class="rTableCell">{{                      cropDiff.action             }}</div>
                      </div>
                    </div>
                  </fieldset>
  
                  <fieldset v-if="foDiff.livestockDiffs.length > 0">
                    <legend><fmt:message key="Livestock"/></legend>
                    <div class="rTable diffreport" style="width: 100%;">
                      <div class="rTableRow">
                        <div class="rTableHead"><fmt:message key="Code"/></div>
                        <div class="rTableHead"><fmt:message key="Qty.Start"/></div>
                        <div class="rTableHead"><fmt:message key="Price.Start"/></div>
                        <div class="rTableHead"><fmt:message key="Qty.End"/></div>
                        <div class="rTableHead"><fmt:message key="Price.End"/></div>
                        <div class="rTableHead"><fmt:message key="Action"/></div>
                      </div>

                      <div class="rTableRow" v-for="livestockDiff in foDiff.livestockDiffs">
                        <div class="rTableCell alignLeft">{{ livestockDiff.code }} - {{ livestockDiff.description }}</div>
                        <div class="rTableCell">{{ $f.toDecimal3( livestockDiff.quantityStart ) }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( livestockDiff.priceStart    ) }}</div>
                        <div class="rTableCell">{{ $f.toDecimal3( livestockDiff.quantityEnd   ) }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( livestockDiff.priceEnd      ) }}</div>
                        <div class="rTableCell">{{                      livestockDiff.action          }}</div>
                      </div>
                    </div>
                  </fieldset>
  
                  <fieldset v-if="foDiff.inputDiffs.length > 0">
                    <legend><fmt:message key="Purchased.Inputs"/></legend>
                    <div class="rTable diffreport" style="width: 100%;">
                      <div class="rTableRow">
                        <div class="rTableHead"><fmt:message key="Code"/></div>
                        <div class="rTableHead"><fmt:message key="Start.Value"/></div>
                        <div class="rTableHead"><fmt:message key="End.Value"/></div>
                        <div class="rTableHead"><fmt:message key="Action"/></div>
                      </div>

                      <div class="rTableRow" v-for="inputDiff in foDiff.inputDiffs">
                        <div class="rTableCell alignLeft">{{ inputDiff.code }} - {{ inputDiff.description }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( inputDiff.startValue ) }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( inputDiff.endValue   ) }}</div>
                        <div class="rTableCell">{{                      inputDiff.action       }}</div>
                      </div>
                    </div>
                  </fieldset>
  
                  <fieldset v-if="foDiff.receivableDiffs.length > 0">
                    <legend><fmt:message key="Receivables"/></legend>
                    <div class="rTable diffreport" style="width: 100%;">
                      <div class="rTableRow">
                        <div class="rTableHead"><fmt:message key="Code"/></div>
                        <div class="rTableHead"><fmt:message key="Start.Value"/></div>
                        <div class="rTableHead"><fmt:message key="End.Value"/></div>
                        <div class="rTableHead"><fmt:message key="Action"/></div>
                      </div>

                      <div class="rTableRow" v-for="receivableDiff in foDiff.receivableDiffs">
                        <div class="rTableCell alignLeft">{{ receivableDiff.code }} - {{ receivableDiff.description }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( receivableDiff.startValue ) }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( receivableDiff.endValue   ) }}</div>
                        <div class="rTableCell">{{                      receivableDiff.action       }}</div>
                      </div>
                    </div>
                  </fieldset>
  
                  <fieldset v-if="foDiff.payableDiffs.length > 0">
                    <legend><fmt:message key="Payables"/></legend>
                    <div class="rTable diffreport" style="width: 100%;">
                      <div class="rTableRow">
                        <div class="rTableHead"><fmt:message key="Code"/></div>
                        <div class="rTableHead"><fmt:message key="Start.Value"/></div>
                        <div class="rTableHead"><fmt:message key="End.Value"/></div>
                        <div class="rTableHead"><fmt:message key="Action"/></div>
                      </div>

                      <div class="rTableRow" v-for="payableDiff in foDiff.payableDiffs">
                        <div class="rTableCell alignLeft">{{ payableDiff.code }} - {{ payableDiff.description }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( payableDiff.startValue ) }}</div>
                        <div class="rTableCell">{{ $f.toCurrency( payableDiff.endValue   ) }}</div>
                        <div class="rTableCell">{{                      payableDiff.action       }}</div>
                      </div>

                    </div>
                  </fieldset>
  
                  <fieldset v-if="foDiff.productiveUnitDiffs.length > 0">
                    <legend><fmt:message key="Productive.Units"/></legend>
                    <div class="rTable diffreport" style="width: 100%;">
                      <div class="rTableRow">
                        <div class="rTableHead"><fmt:message key="Code"/></div>
                        <div class="rTableHead"><fmt:message key="Existing.Value"/></div>
                        <div class="rTableHead"><fmt:message key="New.Value"/></div>
                      </div>

                      <div class="rTableRow" v-for="pucDiff in foDiff.productiveUnitDiffs">
                        <div class="rTableCell alignLeft">{{ pucDiff.code }} - {{ pucDiff.description }}</div>
                        <div class="rTableCell">{{ $f.toDecimal3(pucDiff.oldValue) }}</div>
                        <div class="rTableCell">{{ $f.toDecimal3(pucDiff.newValue) }}</div>
                      </div>
                    </div>
                  </fieldset>
                </div>

                <p>&nbsp;</p>
    
              </fieldset>
  
              <fieldset v-if="formData.hasLocallyGeneratedOperations">
                <legend><fmt:message key="Locally.Generated.Operations"/></legend>
                <p><fmt:message key="has.locally.generated.operations"/></p>
              </fieldset>
            </div>

          </div>
        </div>
        <div class="rTableRow">
          <div class="rTableHead">&nbsp;</div>
        </div>
      </div>
    </div>
  </div>

  <c:if test="${form.formData.newCRAMissingOperations}">
    <c:set var="updateMessage"><fmt:message key="operations.missing.from.latest.cra"/></c:set>
  </c:if>

</html:form>


<script>

  var vueData = {
    formData: JSON.parse($('#formDataJson').val()),
    pyvDiff: JSON.parse($('#pyvDiffJson').val())
  };
  
  const app = Vue.createApp({
    data(){
      return vueData;
    },
    mounted: function (){
      excludeFieldFromDirtyCheck(''); // Vue.js creates fields without IDs 
    },
    updated: function (){
      markFormAsDirty();
    }, methods: {
    }
  });
  
  app.config.globalProperties.$f = vueFilters;

  const vueRoot = app.mount('#vueApp');

</script>


<c:choose>
  <c:when test="${ form.readOnly }">
    <c:set var="disableUpdate" value="true"></c:set>
  </c:when>
  <c:otherwise>
    <c:set var="disableUpdate" value="false"></c:set>
    <script type="text/javascript">
      //<![CDATA[
        function updateToLatestCra() {
          var updateMessage = "<c:out value='${updateMessage}'/>";
          if(!updateMessage || confirm(updateMessage)) {
            $('#formDataJson').val( JSON.stringify(vueData.formData) );
            submitForm(document.getElementById('updatePyvForm'));
            showProcessing();
          }
        }
      //]]>
      </script>
  </c:otherwise>
</c:choose>

<div class="rTable" style="width: 100%;">
  <div class="rTableRow">
    <div class="rTableCell" align="right">
      <u:yuiButton buttonLabel="Update.to.Latest.CRA.Revision" buttonId="updateButton" function="updateToLatestCra" disabled="${disableUpdate}" />
      <a id="closeButton" href="javascript:window.close()"><fmt:message key="Cancel"/></a>
    </div>
  </div>
</div>

<script type="text/javascript">
  new YAHOO.widget.Button("closeButton");
</script>

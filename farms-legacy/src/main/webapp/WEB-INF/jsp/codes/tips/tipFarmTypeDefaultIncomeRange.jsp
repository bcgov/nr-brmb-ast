<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/vue.min.js"></script>
<script type="text/javascript" src="javascript/tip-farm-type-save.js"></script>

<w:ifUserCanPerformAction action="editTipCodes">
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
  <fmt:message key="Default.Income.Ranges"/>
</h1>


<html:form action="/saveTipFarmTypeDefaultIncomeRange" styleId="codeForm" method="post">
  
  <div id="vueApp">
    <html:hidden property="incomeRangeJson" styleId="incomeRangeJson"/>
    <input type="hidden" name="incomeRangeJsonReactive" id="incomeRangeJsonReactive" v-model="JSON.stringify(farmTypeIncomeData)" />

    <fieldset>
      <legend><fmt:message key="Income.Range"/></legend>      
      <div class="rTable">
        <div class="rTableRow">
        
          <div class="rTableHead"><fmt:message key="Range.Low"/></div>
          <div class="rTableHead"><fmt:message key="Range.High"/></div>
          <div class="rTableHead"><fmt:message key="Minimum.Group.Count"/></div>
          <div class="rTableHead"></div>

        </div>
        <range-item
          v-for="(rangeItem, index) in farmTypeIncomeData.incomeRange"
          v-bind:item="rangeItem"
          v-bind:index="index"
          v-bind:key="rangeItem.id"
          v-bind:can-edit="canEdit"
          v-on:remove="farmTypeIncomeData.incomeRange.splice(index, 1)">
        </range-item>
      </div>
      <div v-if="canEdit" v-cloak style="padding: 10px 10px">
        <span class="yui-button yui-push-button">
          <span class="first-child">
            <button type="button" v-on:click='addRange()'>Add Range</button>
          </span>
        </span>
      </div>
      <div style="padding: 10px; font-weight: bold;"><fmt:message key="TIP.blank.range.high.message"/></div>
    </fieldset>
  </div>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editTipCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm" function="saveFarmType" />
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
  </div>

</html:form>

<script>
  var randomInteger = function () {
    return Math.floor( Math.random() * Math.pow(10, 17) );
  };

  Vue.component('range-item', {
    props: ['item','canEdit'],
    template: '\
      <div class="rTableRow numeric alignBottom">\
        <div class="rTableCell">\
          <input v-if="canEdit" v-model="item.rangeLow" />\
              <span v-else>{{item.rangeLow}}</span>\
        </div>\
        <div class="rTableCell">\
          <input v-if="canEdit" v-model="item.rangeHigh" />\
          <span v-else>{{item.rangeHigh}}</span>\
        </div>\
        <div class="rTableCell">\
          <input v-if="canEdit" v-model="item.minimumGroupCount" />\
          <span v-else>{{item.minimumGroupCount}}</span>\
        </div>\
        <div class="rTableCell alignBottom">\
          <span class="yui-button yui-push-button">\
            <span class="first-child">\
              <button type="button" v-if="canEdit" v-on:click="$emit(\'remove\')">Remove</button>\
            </span>\
          </span>\
        </div>\
      </div>'
  });
  
  var vueData = {
        farmTypeIncomeData: JSON.parse($('#incomeRangeJson').val()),
        canEdit: "<c:out value='${canEdit}' />",
      };
  
  for(var i = 0; i < vueData.farmTypeIncomeData.incomeRange.length; i++) {
    vueData.farmTypeIncomeData.incomeRange[i].id = randomInteger();
  }
  
  var vm = new Vue({
    el: '#vueApp',
    data: dataWithMaxValBlanked,
    methods: {
      randomInteger: randomInteger,
      addRange: function (){
        this.farmTypeIncomeData.incomeRange.push({
          rangeLow: "",
          rangeHigh: "",
          minimumGroupCount: "",
          id: randomInteger()});
      },
    },
    mounted: function (){
      excludeFieldFromDirtyCheck(''); // Vue.js creates fields without IDs
    },
    updated: function (){
      markFormAsDirty();
    }
  });
  
</script>
<%@ include file="/WEB-INF/jsp/common/include.jsp" %>

<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/vue.min.js"></script>
<script type="text/javascript" src="javascript/tip-farm-type-save.js"></script>

<w:ifUserCanPerformAction action="editTipCodes">
  <c:set var="canEdit" value="true"/>
</w:ifUserCanPerformAction>

<c:if test="${form.usingDefaultRange}">
  <c:set var="isDefaultRange" value="true"/>
</c:if>

<c:if test="${form.isInherited}">
  <c:set var="isInheritedRange" value="true"/>
</c:if>

<h1>
  <c:choose>
    <c:when test="${!canEdit}">
      <fmt:message key="View"/>
    </c:when>
    <c:when test="${form.new}">
      <fmt:message key="Create.New"/>
    </c:when>
    <c:otherwise>
      <fmt:message key="Edit"/>
    </c:otherwise>
  </c:choose>
  <fmt:message key="Farm.Type.1"/>
</h1>


<html:form action="saveFarmSubtypeBItem" styleId="codeForm" method="post">
  <html:hidden property="new"/>
  <html:hidden property="id"/>
  <html:hidden property="usingDefaultRange" styleId="usingDefaultRange" />
  <html:hidden property="isInherited" styleId="isInherited" />
  
  <fieldset style="width:70%">
    <table class="formInput" style="width:100%">
      <tr>
        <th style="width:100px"><fmt:message key="Farm.Type.2"/>:</th>
        <td>
        <div style="width:150px";>
          <c:choose>
            <c:when test="${canEdit}">
              <html:select property="parentName">
                <option value=""></option>
                <html:optionsCollection property="farmSubtypeAListViewItems" /> 
              </html:select>
            </c:when>
            <c:otherwise>
              <c:out value="${form.parentName}"/>
            </c:otherwise>
          </c:choose>
        </div>
        </td>
      </tr>
      <tr>
        <th style="width:100px"><fmt:message key="Farm.Type.1"/>:</th>
        <td>
          <c:choose>
            <c:when test="${canEdit}">
            <div style="float:left;width:100%">
              <html:text property="name" maxlength="256"/>
            </div>
            </c:when>
            <c:otherwise>
              <html:hidden property="name"/>
              <c:out value="${form.name}"/>            
            </c:otherwise>
          </c:choose>        
        </td>
      </tr>         
    </table>
  </fieldset>
  
  <div id="vueApp">
    <html:hidden property="incomeRangeJson" styleId="incomeRangeJson"/>

    <fieldset>
      <legend><fmt:message key="Income.Range"/></legend>
      <div v-if="isDefaultRange" class="infoMessage" v-cloak><fmt:message key="Tip.Income.Range.Default"/></div>
      <div v-if="isInheritedRange" class="infoMessage" v-cloak><fmt:message key="Tip.ranges.inherited.from"/> <c:out value="${form.inheritedFrom}"/>:</div>
      <div v-if="!isDefaultRange && !isInheritedRange" class="infoMessage" v-cloak><fmt:message key="TIP.Using.custom.ranges"/></div>
      
      <div class="rTable" style="width: 60%">
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
          v-bind:is-not-custom="isInheritedRange.concat(isDefaultRange)"
          v-on:remove="farmTypeIncomeData.incomeRange.splice(index, 1)">
        </range-item>
      </div>
      <div v-if="canEdit && !isDefaultRange && !isInheritedRange" v-cloak style="padding: 10px 10px">
        <span class="yui-button yui-push-button">
          <span class="first-child">
            <button type="button" v-on:click='addRange()'><fmt:message key="Add.Range"/></button>
          </span>
        </span>
      </div>
      <div v-if="canEdit && !isDefaultRange && !isInheritedRange" v-cloak style="padding: 10px 10px">
        <span class="yui-button yui-push-button">
          <span class="first-child">
            <button type="button" v-on:click='resetToDefault()'><fmt:message key="Reset.to.Default"/></button>
          </span>
        </span>
      </div>
      <div v-if="canEdit && (isDefaultRange || isInheritedRange)" v-cloak style="padding: 10px 10px">
        <span class="yui-button yui-push-button">
          <span class="first-child">
            <button type="button" v-on:click='setCustomRange()'><fmt:message key="Tip.use.custom.ranges.button.label"/></button>
          </span>
        </span>
      </div>
      <div style="padding: 10px; font-weight: bold;"><fmt:message key="TIP.blank.range.high.message"/></div>
    </fieldset>
  </div>

  <div style="text-align:right;width:70%">
    <w:ifUserCanPerformAction action="editTipCodes">
      <u:yuiButton buttonId="saveButton" buttonLabel="Save" formId="codeForm" function="saveFarmType" />
      <c:if test="${!form.new}">
        <u:yuiButton buttonId="deleteButton" buttonLabel="Delete" formId="codeForm" action="deleteFarmSubtypeBItem" confirmMessage="delete.farm.subtype.item.warning"/>
      </c:if>
      <u:dirtyFormCheck formId="codeForm"/>
    </w:ifUserCanPerformAction>
    <u:yuiButton buttonId="cancelButton" buttonLabel="Cancel" action="farm535"/>
  </div>

</html:form>

<script>

  var randomInteger = function () {
    return Math.floor( Math.random() * Math.pow(10, 17) );
  };

  Vue.component('range-item', {
    props: ['item','canEdit', 'isNotCustom'],
    template: '\
      <div class="rTableRow numeric alignBottom">\
        <div class="rTableCell">\
          <input v-if="canEdit" v-model="item.rangeLow" :disabled="isNotCustom.length!=0" />\
              <span v-else>{{item.rangeLow}}</span>\
        </div>\
        <div class="rTableCell">\
          <input v-if="canEdit" v-model="item.rangeHigh" :disabled="isNotCustom.length!=0"/>\
          <span v-else>{{item.rangeHigh}}</span>\
        </div>\
        <div class="rTableCell">\
          <input v-if="canEdit" v-model="item.minimumGroupCount" :disabled="isNotCustom.length!=0" />\
          <span v-else>{{item.minimumGroupCount}}</span>\
        </div>\
        <div class="rTableCell alignBottom">\
          <span class="yui-button yui-push-button">\
            <span class="first-child">\
              <button type="button" v-if="canEdit && !isNotCustom" v-on:click="$emit(\'remove\')">Remove</button>\
            </span>\
          </span>\
        </div>\
      </div>'
  });
  
  var vueData = {
        farmTypeIncomeData: JSON.parse($('#incomeRangeJson').val()),
        canEdit: "<c:out value='${canEdit}' />",
        isDefaultRange: "<c:out value='${isDefaultRange}' />",
        isInheritedRange: "<c:out value='${isInheritedRange}' />"
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
          id: randomInteger()});
      },
      setCustomRange: function () {
        $('#usingDefaultRange').val(false);
        $('#isInherited').val(false);
      
        this.isDefaultRange = "";
        this.isInheritedRange = "";
        markFormAsDirty();
      },
      resetToDefault: function () {
        this.farmTypeIncomeData.incomeRange = [];
        markFormAsDirty();
        saveFarmType();
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
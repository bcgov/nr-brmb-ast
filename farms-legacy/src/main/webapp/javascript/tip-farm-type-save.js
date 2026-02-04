  var dataWithMaxValBlanked =  function() {
    for (var i = 0; i < vueData.farmTypeIncomeData.incomeRange.length; i++) {
    var rangeHighStr = vueData.farmTypeIncomeData.incomeRange[i].rangeHigh;

      if (rangeHighStr == "9.999999999E9") {
        vueData.farmTypeIncomeData.incomeRange[i].rangeHigh = "";
      }
    }
    return vueData;
  };

function saveFarmType() {
  $('#incomeRangeJson').val( JSON.stringify(vueData.farmTypeIncomeData) );
  
  var isValid = true;
  var prevHigh;
  var length = vueData.farmTypeIncomeData.incomeRange.length;
  
  for (var i = 0; i < length; i++) {
    var rangeHighStr = vueData.farmTypeIncomeData.incomeRange[i].rangeHigh;
    var rangeLowStr = vueData.farmTypeIncomeData.incomeRange[i].rangeLow;

    if (i + 1 == length) {
      if (rangeHighStr != "") {
        alert("The last range high must be left blank, to indicate infinity.");
        isValid = false;
        return;
      } else {
        rangeHighStr = "9999999999";
      }
    }

    if ((i + 1 == length) && rangeHighStr == "") {
      rangeHighStr = "9999999999";
    }

    if (!rangeHighStr || !rangeLowStr) {
      alert("Range Low and Range High cannot be blank.");
      isValid = false;
      return;
    }

    if (isNaN(rangeHighStr) || isNaN(rangeLowStr)) {
      alert("Range must be provided as numbers.");
      isValid = false;
      return;
    }

    var rangeHigh = Number(rangeHighStr);
    var rangeLow = Number(rangeLowStr);

    if (i == 0 && rangeLow != 0) {
      alert("The first range low must be 0.");
      isValid = false;
      return;
    }

    if (rangeLow > rangeHigh) {
      alert("Range Low cannot be greater than Range High.");
      isValid = false;
      return;
    }
    
    if (i == 0) {
      prevHigh = rangeHigh;
    } else {
      if (rangeLow != prevHigh+1) {
        alert("Each range low must exceed the previous range high by 1.");
        isValid = false;
        return;
      } else {
        prevHigh = rangeHigh;
      }
    }
  }
  
  if (isValid) {
    submitForm(document.getElementById('codeForm'));
  }
}

<script>

  Vue.filter('toCurrency', function (value) {
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2
    });
    return formatter.format(value);
  });

  Vue.filter('toCurrency0', function (value) {
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0
    });
    return formatter.format(value);
  });

  Vue.filter('toPercent0', function (value) {
	    if(value == null) {
	        return 'N/A';
	     }
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'percent',
      minimumFractionDigits: 0
    });
    return formatter.format(value);
  });

  Vue.filter('toPercent1', function (value) {
    if(value == null) {
       return 'N/A';
    }
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      style: 'percent',
      minimumFractionDigits: 1
    });
    return formatter.format(value);
  });

  Vue.filter('toDecimal0', function (value) {
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 0
    });
    return formatter.format(value);
  });

  Vue.filter('toDecimal2', function (value) {
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 2
    });
    return formatter.format(value);
  });

  Vue.filter('toDecimal3', function (value) {
    if(value == null) {
       return 'N/A';
    }
    if (typeof value !== "number") {
      return value;
    }
    var formatter = new Intl.NumberFormat('en-US', {
      minimumFractionDigits: 3
    });
    return formatter.format(value);
  });

  Vue.filter('toYesNo', function (value) {
    if(value == null) {
       return '';
    }
    if (value) {
      return '<fmt:message key="Yes"/>';
    } else {
      return '<fmt:message key="No"/>';
    }
  });

</script>

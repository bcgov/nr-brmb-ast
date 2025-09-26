<script>

  const vueFilters = {
      
    toCurrency: function (value) {
      if (typeof value !== "number") {
        return value;
      }
      var formatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
      });
      return formatter.format(value);
    },
  
    toCurrency0: function (value) {
      if (typeof value !== "number") {
        return value;
      }
      var formatter = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 0
      });
      return formatter.format(value);
    },
  
    toPercent0: function (value) {
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
    },
  
    toPercent1: function (value) {
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
    },
  
    toDecimal0: function (value) {
      if (typeof value !== "number") {
        return value;
      }
      var formatter = new Intl.NumberFormat('en-US', {
        minimumFractionDigits: 0
      });
      return formatter.format(value);
    },
  
    toDecimal2: function (value) {
      if (typeof value !== "number") {
        return value;
      }
      var formatter = new Intl.NumberFormat('en-US', {
        minimumFractionDigits: 2
      });
      return formatter.format(value);
    },
  
    toDecimal3: function (value) {
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
    },
  
    toYesNo: function (value) {
      if(value == null) {
         return '';
      }
      if (value) {
        return '<fmt:message key="Yes"/>';
      } else {
        return '<fmt:message key="No"/>';
      }
    }
  }

</script>

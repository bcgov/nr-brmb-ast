/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 */
public final class StringUtils {
  
  private static DecimalFormat twoDecimalPlacesFormat = new DecimalFormat("#0.00");
  private static DecimalFormat threeDecimalPlacesFormat = new DecimalFormat("#0.000");
  private static DecimalFormat percentFormat = new DecimalFormat("#0%");
  private static DecimalFormat percentTwoDecimalPlacesFormat = new DecimalFormat("#0.00%");
  private static DecimalFormat percentUpToTwoDecimalPlacesFormat = new DecimalFormat("#0.##%");
  private static DecimalFormat currencyFormat = new DecimalFormat("$#0.00");
  private static DecimalFormat currencyFormatNoDecimalPlaces = new DecimalFormat("$#0");

  /** private constructor */
  private StringUtils() {
  }

  public static String concat(Object[] o) {

    if (o == null) {
      return "";
    }

    String s = "";

    for (int i = 0; i < o.length; i++) {

      if (o[i] != null) {
        s += o[i].toString();
      }
    }

    return s;
  }
  
  /**
   * @param rawPhoneNumber Unformatted phone number.
   * @return Phone number formatted with dashes.
   */
  public static String formatPhoneNumber(String rawPhoneNumber) {
    String trimPhone;
    if(rawPhoneNumber == null) {
      trimPhone = null;
    } else {
      trimPhone = rawPhoneNumber.trim();
    }
    
    final int one = 1;
    final int three = 3;
    final int four = 4;
    final int six = 6;
    final int seven = 7;
    final int ten = 10;
    final int eleven = 11;
    
    
    try {
      // make sure the number is numeric
      Long.parseLong(trimPhone);
    } catch(NumberFormatException nfe) {
      trimPhone = null;
    }

    String formattedPhone;
    if(trimPhone == null) {
      formattedPhone = "";
    } else if (trimPhone.length() == ten) {
      formattedPhone = trimPhone.substring(0, three) +
          "-" + trimPhone.substring(three, six) +
          "-" + trimPhone.substring(six, ten);
    } else if (trimPhone.length() == eleven && trimPhone.startsWith("1")) {
      formattedPhone = trimPhone.substring(0, one) +
        "-" + trimPhone.substring(one, four) +
        "-" + trimPhone.substring(four, seven) +
        "-" + trimPhone.substring(seven, eleven);
    } else {
      formattedPhone = trimPhone;
    }
    
    return formattedPhone;
  }
  
  /**
   * 
   * @param i Integer to convert to a string.
   * @return A String representation of i or an empty string if i is null.
   */
  public static String toString(Integer i) {
    String result;
    if(i == null) {
      result = "";
    } else {
      result = i.toString();
    }
    return result;
  }
  
  /**
   * 
   * @param d Double to convert to a string
   * @return A String representation of d or an empty string if d is null.
   */
  public static String toString(Double d) {
    String result;
    if(d ==  null) {
      result = "";
    } else {
      result = d.toString();
    }
    return result;
  }
  
  /**
   * 
   * @param d Double to convert to a string
   * @return A String representation of d or an empty string if d is null.
   */
  public static String toString(BigDecimal d) {
    String result;
    if(d ==  null) {
      result = "";
    } else {
      result = d.toPlainString();
    }
    return result;
  }
  
  /**
   * @param d double to convert to a string
   * @param df DecimalFormat
   * @return A String representation of d or an empty string if d is null.
   */
  public static String formatDouble(double d, DecimalFormat df) {
    return formatDouble(Double.valueOf(d), df);
  }
  
  /**
   * @param d Double to convert to a string
   * @param df DecimalFormat
   * @return A String representation of d or an empty string if d is null.
   */
  public static String formatDouble(Double d, DecimalFormat df) {
    return formatNumber(d, df);
  }
  
  public static String formatNumber(Number d, DecimalFormat df) {
    String result;
    if(d ==  null) {
      result = "";
    } else {
      result = df.format(d);
    }
    return result;
  }

  
  public static String formatTwoDecimalPlaces(Double value) {
    return formatNumber(value, twoDecimalPlacesFormat);
  }
  
  public static String formatThreeDecimalPlaces(Double value) {
    return formatNumber(value, threeDecimalPlacesFormat);
  }
  
  public static String formatCurrency(Number value) {
    return formatNumber(value, currencyFormat);
  }
  
  public static String formatCurrencyNoDecimalPlaces(Number value) {
    return formatNumber(value, currencyFormatNoDecimalPlaces);
  }
  
  public static String formatPercent(Number value) {
    return formatNumber(value, percentFormat);
  }
  
  public static String formatPercentNoSymbol(Number value) {
    return formatNumber(value, percentFormat).replaceAll("%", "");
  }
  
  public static String formatPercentTwoDecimalPlaces(Number value) {
    return formatNumber(value, percentTwoDecimalPlacesFormat);
  }
  
  public static String formatPercentTwoDecimalPlacesNoSymbol(Number value) {
    return formatPercentTwoDecimalPlaces(value).replaceAll("%", "");
  }
  
  public static String formatPercentUpToTwoDecimalPlaces(Number value) {
    return formatNumber(value, percentUpToTwoDecimalPlacesFormat);
  }
  
  public static String formatPercentUpToTwoDecimalPlacesNoSymbol(Number value) {
    return formatPercentUpToTwoDecimalPlaces(value).replaceAll("%", "");
  }
  
  /**
   * 
   * @param l Long to convert to a string
   * @return A String representation of l or an empty string if l is null.
   */
  public static String toString(Long l) {
    String result;
    if(l ==  null) {
      result = "";
    } else {
      result = l.toString();
    }
    return result;
  }
  
  /**
   * @param value String
   * @return boolean
   */
  public static boolean isBlank(String value) {
    return org.apache.commons.lang.StringUtils.isBlank(value);
  }
  
  /**
   * @param value String
   * @return boolean
   */
  public static boolean isNotBlank(String value) {
    return !isBlank(value);
  }
  
  /**
   * @param text String
   * @return String
   */
  public static String escapeSlashes(String text) {
    String escapedString = "";
    if(text != null) {
      escapedString = text.replaceAll("\\\\", "\\\\\\\\");
    }
    return escapedString;
  }

  /**
   * @param userId String
   * @return The userId with IDIR\ stripped off the start.
   */
  public static String formatUserIdForDisplay(String userId) {
    final int idirLength = 5;
    String result;
    if(userId == null) {
      result = null;
    } else {
      if(userId.toUpperCase().startsWith("IDIR\\")
          && userId.length() > idirLength) {
        result = userId.substring(idirLength);
      } else {
        result = userId;
      }
    }
    return escapeSlashes(result);
  }
  
  /**
   * 
   * @param list of objects
   * @return comma separated list
   */
  public static String toCsv(List<?> list) {
  	StringBuffer buff = new StringBuffer();
  	int lastNum = list.size()-1;
  	
  	for(int ii = 0; ii < list.size(); ii++ ) {
  		Object current = list.get(ii);
  		buff.append(current);
  		
  		if(ii != lastNum) {
  			buff.append(", ");
  		}
  	}
  	
  	return buff.toString();
  }
  
  
  /**
   * @param a String
   * @param b String
   * @return boolean
   */
  public static boolean equal(String a, String b) {
    boolean result;
    if(a == null && b == null) {
      result = true;
    } else {
      if(a != null && a.equals(b)) {
        result = true;
      } else {
        result = false;
      }
    }
    return result;
  }
  
  public static boolean equalNullSameAsEmpty(String a, String b) {
    boolean result;
    if(isBlank(a) && isBlank(b)) {
      result = true;
    } else if(a != null && a.equals(b)) {
      result = true;
    } else {
      result = false;
    }
    return result;
  }
  
  public static boolean notEqualNullSameAsEmpty(String a, String b) {
    return ! equalNullSameAsEmpty(a, b);
  }
  
  public static boolean isOneOf(String value, String... stringArray) {
    boolean result = false;
    if(value != null) {
      List<String> stringList = Arrays.asList(stringArray);
      result = stringList.contains(value);
    }
    return result;
  }
  
  public static String ifNull(String value, String valueIfNull) {
    if(value == null) {
      return valueIfNull;
    } else {
      return value;
    }
  }
  
  public static String listToDelimitedString(char delimiter, Format format, List<?> values) {
    String result;
    if(values == null || values.isEmpty()) {
      result = null;
    } else {

      StringBuilder builder = new StringBuilder();
      for(Iterator<?> valueIter = values.iterator(); valueIter.hasNext(); ) {
        Object value = valueIter.next();
        String formattedValue;
        if(value == null) {
          formattedValue = "";
        } else if(format == null) {
          formattedValue = value.toString();
        } else {
          formattedValue = format.format(value);
        }
        builder.append(formattedValue);
        if(valueIter.hasNext()) {
          builder.append(delimiter);
        }
      }
      
      result = builder.toString();
    }
    
    return result;
  }

  public static String formatWithNullAsEmptyString(String format, Object... params) {
    
    Object[] fixedParams = new Object[params.length];
    
    for(int i = 0; i < params.length; i++) {
      if(params[i] == null) {
        fixedParams[i] = "";
      } else {
        fixedParams[i] = params[i];
      }
    }
    
    return String.format(format, fixedParams);
  }
}

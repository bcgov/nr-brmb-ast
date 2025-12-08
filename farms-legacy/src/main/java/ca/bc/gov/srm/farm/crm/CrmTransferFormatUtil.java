/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author awilkinson
 */
public final class CrmTransferFormatUtil {
  
  private CrmTransferFormatUtil() {
    // private constructor
  }
  
  public static String getIndicatorString(Boolean ind) {
    if(ind == null) {
      return getIndicatorString(false);
    }
    return getIndicatorString(ind.booleanValue());
  }
  
  public static String getIndicatorString(boolean ind) {
    if(ind) {
      return "Y";
    }
    return "N";
  }

  public static Boolean getIndicator(String ind) {
    if(ind == null || ind.equals("")) {
      return null;
    } else if(ind.equals("Y")) {
      return Boolean.TRUE;
    } else if(ind.equals("N")) {
      return Boolean.FALSE;
    } else {
      throw new IllegalArgumentException("Indicator must be Y or N. Actual value: [" + ind + "]");
    }
  }

  public static String getFieldValue(String[] fields, int index) {
    String result = null;
    if(index < fields.length) {
      result = fields[index];
    }
    return result;
  }
  
  public static String convertForCsv(String val) {
    StringBuilder sb = new StringBuilder();
    if(val != null) {
      sb.append('"');
      sb.append(val);
      sb.append('"');
    }
    return sb.toString();
  }
  
  public static String convertForCsv(Integer val) {
    if(val == null) {
      return "";
    }
    return val.toString();
  }

  public static String formatDate(Date date) {
    String result = null;
    
    if(date != null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ssZ");
      
      result = dateFormat.format(date);
    }
    
    return result;
  }

  public static String formatNavigationPropertyValue(String endpoint, String value) {
    if(value != null) {
      return "/" + endpoint + "(" + value + ")";
    } else {
      return null;
    }
  }
}

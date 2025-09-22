/**
 * Copyright (c) 2011,
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

/**
 * @author awilkinson
 * @created Mar 18, 2011
 */
public final class MathUtils {

  /** */
  private MathUtils() {
  }
  
  public static final int CURRENCY_DECIMAL_PLACES = 2;
  public static final int WHOLE_NUMBER_DECIMAL_PLACES = 0;
  public static final int PRODUCTIVE_UNITS_DECIMAL_PLACES = 3;
  public static final int INVENTORY_QUANTITY_DECIMAL_PLACES = 3;

  /**
   * @param value value
   * @param scale scale
   * @return value rounded to "scale" decimal places
   */
  public static Double round(Double value, int scale) {
    Double result;
    if(value == null) {
      result = null;
    } else {
      result = new Double(roundPrimitiveDouble(value.doubleValue(), scale));
    }
    return result;
  }
  
  /**
   * @param value value
   * @param scale scale
   * @return value rounded to "scale" decimal places
   */
  private static double roundPrimitiveDouble(double value, int scale) {
    final double scaleMultiplier = Math.pow(10d, scale);
    double result = value;
    boolean negative = result < 0;
    
    // Convert negative to positive for rounding purposes so that
    // negative and positive will cancel each other out when added together.
    if(negative) {
      result = 0 - result;
    }
    
    result = Math.round(result * scaleMultiplier) / scaleMultiplier;
    
    // Convert back to negative
    if(negative) {
      result = 0 - result;
    }
    
    return result;
  }
  
  public static Double roundCurrency(double value) {
    return round(value, CURRENCY_DECIMAL_PLACES);
  }
  
  public static Double roundToWholeNumber(double value) {
    return round(value, WHOLE_NUMBER_DECIMAL_PLACES);
  }
  
  public static Double roundProduction(double value) {
    return round(value, PRODUCTIVE_UNITS_DECIMAL_PLACES);
  }
  
  public static Double roundInventoryQuantity(double value) {
    return round(value, INVENTORY_QUANTITY_DECIMAL_PLACES);
  }
  
  
  /**
   * @param a Double
   * @param b Double
   * @return boolean
   */
  public static boolean equalToTwoDecimalPlaces(Double a, Double b) {
    final int scale = 2;
    return valuesEqual(round(a, scale), round(b, scale));
  }
  
  
  /**
   * @param a Double
   * @param b Double
   * @return boolean
   */
  public static boolean equalToThreeDecimalPlaces(Double a, Double b) {
    final int scale = 3;
    return valuesEqual(round(a, scale), round(b, scale));
  }
  
  
  /**
   * @param a Double
   * @param b Double
   * @param scale int
   * @return boolean
   */
  public static boolean valuesEqual(Double a, Double b, int scale) {
    return valuesEqual(round(a, scale), round(b, scale));
  }
  
  
  /**
   * @param a Double
   * @param b Double
   * @return boolean
   */
  private static boolean valuesEqual(Double a, Double b) {
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
  
  
  /**
   * @param a Integer
   * @param b Integer
   * @return boolean
   */
  public static boolean valuesEqual(Integer a, Integer b) {
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
  
  public static boolean valuesNotEqual(Integer a, Integer b) {
    return ! valuesEqual(a, b);
  }

  
  /**
   * @param d Double
   * @return double
   */
  public static double getPrimitiveValue(Double d) {
    double result = 0;
    
    if(d != null) {
      result = d.doubleValue();
    }
    
    return result;
  }

  public static Double toDouble(BigDecimal value) {
    if(value != null) {
      return Double.valueOf(value.doubleValue());
    }
    return null;
  }
}

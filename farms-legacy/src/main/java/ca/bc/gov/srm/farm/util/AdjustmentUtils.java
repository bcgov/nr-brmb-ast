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

import java.text.ParseException;

/**
 * @author awilkinson
 * @created Mar 18, 2011
 */
public final class AdjustmentUtils {

  /** empty, private constructor */
  private AdjustmentUtils() {
  }

  
  /**
   * @param value String
   * @return boolean
   */
  public static boolean isBlankValue(String value) {
    boolean result =
      value == null ||
      value.trim().equals("") ||
      value.trim().equals("-");
    return result;
  }
  
  
  /**
   * @param value String
   * @return Double
   * @throws ParseException On ParseException
   */
  public static Double parseDouble(String value) throws ParseException {
    Double result = null;
    if(!isBlankValue(value)) {
      result = DataParseUtils.parseDoubleObject(value);
    }
    return result;
  }


  /**
   * @param total Double
   * @param reported Double
   * @return Double
   */
  public static Double calculateQuantityAdjustment(Double total, Double reported) {
    final int decimalPlaces = 3;
    return MathUtils.round(calculateAdjustment(total, reported), decimalPlaces);
  }
  
  
  /**
   * @param total Double
   * @param reported Double
   * @return Double
   */
  public static Double calculatePriceAdjustment(Double total, Double reported) {
    final int decimalPlaces = 2;
    return MathUtils.round(calculateAdjustment(total, reported), decimalPlaces);
  }
  
  
  /**
   * @param total Double
   * @param reported Double
   * @return Double
   */
  public static Double calculateAccrualAdjustment(Double total, Double reported) {
    final int decimalPlaces = 2;
    return MathUtils.round(calculateAdjustment(total, reported), decimalPlaces);
  }


  /**
   * @param total Double
   * @param reported Double
   * @return Double
   */
  private static Double calculateAdjustment(Double total, Double reported) {
    Double adjustment;
    if(total == null && reported == null) {
      adjustment = null;
    } else {
      double totalValue;
      double reportedValue;
      
      if(total == null) {
        totalValue = 0;
      } else {
        totalValue = total.doubleValue();
      }
      
      if(reported == null) {
        reportedValue = 0;
      } else {
        reportedValue = reported.doubleValue();
      }
      
      double adj = totalValue - reportedValue;
      if(adj == 0 && reported != null) {
        adjustment = null;
      } else {
        adjustment = new Double(adj);
      }
    }

    return adjustment;
  }


  /**
   * @param value Double
   * @param min double
   * @param max double
   * @return boolean
   */
  public static boolean outOfRange(Double value, double min, double max) {
    boolean result;
    if(value == null) {
      result = false;
    } else {
      result = value.doubleValue() < min || value.doubleValue() > max;
    }
    return result;
  }
  
}

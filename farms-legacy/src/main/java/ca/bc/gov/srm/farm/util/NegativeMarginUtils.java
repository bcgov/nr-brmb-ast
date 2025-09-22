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
import java.text.ParseException;

/**
 * @author hwang
 */
public class NegativeMarginUtils {

  /** empty, private constructor */
  private NegativeMarginUtils() {
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
   * @return BigDecimal
   * @throws ParseException On ParseException
   */
  public static BigDecimal parseBigDecimal(String value) throws ParseException {
    BigDecimal result = null;
    if(!isBlankValue(value)) {
      result = DataParseUtils.parseBigDecimal(value);
    }
    return result;
  }

  /**
   * @param value BigDecimal
   * @param min
   * @param max
   * @return boolean
   */
  public static boolean outOfRange(BigDecimal value, BigDecimal min, BigDecimal max) {
    boolean result = false;
    if (value != null) {
      if (min != null && value.compareTo(min) < 0) {
        result = true;
      }
      if (max != null && value.compareTo(max) > 0) {
        result = true;
      }
    }
    return result;
  }
}

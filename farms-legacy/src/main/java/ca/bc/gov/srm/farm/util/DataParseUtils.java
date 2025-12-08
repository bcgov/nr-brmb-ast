/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;


/**
 * DataParseUtils.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 4416 $
 */
public final class DataParseUtils {

  /** INTEGER_FORMAT. */
  public static final String INTEGER_FORMAT = "#,##0";

  public static final String NUMBER_FORMAT = "#,##0.00";
  public static final String CURRENCY_FORMAT = "$#,##0.00";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_PICKER_DATE_FORMAT = "yyyy-M-d";
  public static final String TIME_FORMAT = "HH:mm:ss";
  public static final String DATETIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
  public static final String ORACLE_DEFAULT_DATE_FORMAT = "dd-MMM-yy";

  public static final String[] TRUE_INDICATORS = {
      "true", "yes", "on", "t", "y", "1"
    };

  public static final String[] DATE_FORMATS = {
      DATETIME_FORMAT,
      DATE_FORMAT,
      "MMM dd, yyyy",
      "MMM d, y",
      "MM/dd/yyyy",
      ORACLE_DEFAULT_DATE_FORMAT
    };

  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
      DATE_FORMAT);

  private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(
      TIME_FORMAT);

  private static final SimpleDateFormat DATE_TIME_FORMATTER =
    new SimpleDateFormat(DATETIME_FORMAT);


  private DataParseUtils() {

  }

  /**
   * parseBoolean.
   *
   * @param   value  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ParseException  On exception.
   */
  public static boolean parseBoolean(final String value) throws ParseException {

    try {

      if (!StringUtils.isBlank(value)) {

        for (int i = 0; i < TRUE_INDICATORS.length; i++) {
          String s = TRUE_INDICATORS[i];

          if (s.equalsIgnoreCase(value)) {
            return true;
          }
        }
      }

      return false;
    } catch (Exception e) {
      throw new ParseException(e.getMessage(), 0);
    }

  }

  /**
   * parseDate.
   *
   * @param   value  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ParseException  On exception.
   */
  public static Date parseDate(final String value) throws ParseException {
    Date result = null;

    if (!StringUtils.isBlank(value)) {
      result = DateUtils.parseDate(value, DATE_FORMATS);
    }

    return result;
  }

  /**
   * parseDouble.
   *
   * @param   value  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ParseException  On exception.
   */
  public static double parseDouble(final String value) throws ParseException {
    double result = 0;

    if (!StringUtils.isBlank(value)) {

      try {
        result = new Double(value).doubleValue();
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), 0);
      }
    }

    return result;
  }

  /**
   * parseInt.
   *
   * @param   value  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ParseException  On exception.
   */
  public static int parseInt(final String value) throws ParseException {
    int result = 0;

    if (!StringUtils.isBlank(value)) {

      try {
        result = new Integer(value).intValue();
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), 0);
      }
    }

    return result;
  }

  /**
   * parseLong.
   *
   * @param   value  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ParseException  On exception.
   */
  public static long parseLong(final String value) throws ParseException {
    long result = 0;

    if (!StringUtils.isBlank(value)) {

      try {
        result = new Long(value).longValue();
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), 0);
      }
    }

    return result;
  }

  /**
   * 
   * @param value value
   * @return A Double parsed from the String or null if value is null or blank.
   * @throws ParseException On Exception
   */
  public static Double parseDoubleObject(final String value) throws ParseException {
    Double result = null;
    
    if (!StringUtils.isBlank(value)) {
      
      try {
        result = Double.valueOf(value);
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), 0);
      }
    }
    
    return result;
  }

  public static BigDecimal parseBigDecimal(final String value) throws ParseException {
    BigDecimal result = null;
    
    if (!StringUtils.isBlank(value)) {
      
      try {
        result = new BigDecimal(value);
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), 0);
      }
    }
    
    return result;
  }
  
  /**
   * 
   * @param value value
   * @return  An Integer parsed from the String or null if value is null or blank.
   * @throws ParseException On Exception
   */
  public static Integer parseIntegerObject(final String value) throws ParseException {
    Integer result = null;
    
    if (!StringUtils.isBlank(value)) {
      
      try {
        result = Integer.valueOf(value);
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), 0);
      }
    }
    
    return result;
  }

  /**
   * 
   * @param value value
   * @return  A Long parsed from the String or null if value is null or blank.
   * @throws ParseException On Exception
   */
  public static Long parseLongObject(final String value) throws ParseException {
    Long result = null;
    
    if (!StringUtils.isBlank(value)) {
      
      try {
        result = Long.valueOf(value);
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), 0);
      }
    }
    
    return result;
  }

  /**
   * toDateString.
   *
   * @param   date  The parameter value.
   *
   * @return  The return value.
   */
  public static String toDateString(final Date date) {

    if (date == null) {
      return null;
    } else {
      return toDateString(date, null);
    }
  }

  /**
   * toDateString.
   *
   * @param   date    The parameter value.
   * @param   format  The parameter value.
   *
   * @return  The return value.
   */
  public static String toDateString(final Date date, final String format) {
    SimpleDateFormat f = null;

    if (StringUtils.isNotBlank(format)) {
      f = new SimpleDateFormat(format);
    } else {
      f = DATE_FORMATTER;
    }

    return f.format(date);
  }

  /**
   * toDateTimeString.
   *
   * @param   date  The parameter value.
   *
   * @return  The return value.
   */
  public static String toDateTimeString(final Date date) {

    if (date == null) {
      return null;
    } else {
      return toDateTimeString(date, null);
    }
  }

  /**
   * toDateTimeString.
   *
   * @param   time  The parameter value.
   *
   * @return  The return value.
   */
  public static String toDateTimeString(final long time) {
    return toDateTimeString(new Date(time), null);
  }

  /**
   * toDateTimeString.
   *
   * @param   date    The parameter value.
   * @param   format  The parameter value.
   *
   * @return  The return value.
   */
  public static String toDateTimeString(final Date date, final String format) {
    SimpleDateFormat f = null;

    if (StringUtils.isNotBlank(format)) {
      f = new SimpleDateFormat(format);
    } else {
      f = DATE_TIME_FORMATTER;
    }

    return f.format(date);
  }

  /**
   * toTimeString.
   *
   * @param   date  The parameter value.
   *
   * @return  The return value.
   */
  public static String toTimeString(final Date date) {

    if (date == null) {
      return null;
    } else {
      return toTimeString(date, null);
    }
  }

  /**
   * toTimeString.
   *
   * @param   date    The parameter value.
   * @param   format  The parameter value.
   *
   * @return  The return value.
   */
  public static String toTimeString(final Date date, final String format) {
    SimpleDateFormat f = null;

    if (StringUtils.isNotBlank(format)) {
      f = new SimpleDateFormat(format);
    } else {
      f = TIME_FORMATTER;
    }

    return f.format(date);
  }
}

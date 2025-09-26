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
package ca.bc.gov.srm.farm.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;


/**
 * @author  dzwiers
 */
public final class ParseUtils {

  /**  */
  private ParseUtils() {
  }

  /**  */
  private static Logger logger = LoggerFactory.getLogger(ParseUtils.class);

  /**  */
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

  /**
   * @param   s  x
   *
   * @return  x
   */
  public static Date date(final String s) {

    if (s != null) {

      try {
        String t = s.trim();

        if (!"".equals(t)) {
          return DATE_FORMAT.parse(t);
        }
      } catch (ParseException e) {
        logger.error("Unexpected error: ", e);
      }
    }

    return null;
  }

  /**
   * @param   s  x
   *
   * @return  x
   */
  public static Integer integer(final String s) {

    if (s != null) {

      try {
        String t = s.trim();

        if (!"".equals(t)) {
          return new Integer(t);
        }
      } catch (NumberFormatException e) {
        logger.error("Unexpected error: ", e);
      }
    }

    return null;
  }

  /**
   * @param   s  x
   *
   * @return  x
   */
  public static Float flt(final String s) {

    if (s != null) {

      try {
        String t = s.trim();

        if (!"".equals(t)) {
          return new Float(t);
        }
      } catch (NumberFormatException e) {
        logger.error("Unexpected error: ", e);
      }
    }

    return null;
  }

  /**
   * @param   s  x
   *
   * @return  x
   */
  public static Double dbl(final String s) {

    if (s != null) {

      try {
        String t = s.trim();

        if (!"".equals(t)) {
          return new Double(t);
        }
      } catch (NumberFormatException e) {
        logger.error("Unexpected error: ", e);
      }
    }

    return null;
  }

  /**
   * @param   s  x
   *
   * @return  x
   */
  public static Boolean indicator(final String s) {

    if ((s != null)
        && (s.equals("Y") || s.equals("y") || s.equals("T") || s.equals("t")
          || s.equals("TRUE") || s.equals("true"))) {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }
}

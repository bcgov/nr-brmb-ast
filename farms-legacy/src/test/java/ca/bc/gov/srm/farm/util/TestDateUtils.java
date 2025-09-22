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
package ca.bc.gov.srm.farm.util;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * @author awilkinson
 * @created November 30, 2023
 */
public class TestDateUtils {

  @Test
  public void dateEqual() {
    
    {
      Date d1 = null;
      Date d2 = null;
      boolean equal = DateUtils.equal(d1, d2);
      assertTrue(equal);
    }
    
    {
      try {
        Date d1 = DataParseUtils.parseDate("2023-01-01");
        Date d2 = DataParseUtils.parseDate("2023-01-01");
        boolean equal = DateUtils.equal(d1, d2);
        assertTrue(equal);
      } catch (ParseException e) {
        e.printStackTrace();
        fail();
      }
    }
    
    {
      try {
        Date d1 = DataParseUtils.parseDate("2023-01-01");
        Date d2 = DataParseUtils.parseDate("2024-03-03");
        boolean equal = DateUtils.equal(d1, d2);
        assertFalse(equal);
      } catch (ParseException e) {
        e.printStackTrace();
        fail();
      }
    }
    
    {
      try {
        Date d1 = DataParseUtils.parseDate("2023-01-01");
        Date d2 = null;
        boolean equal = DateUtils.equal(d1, d2);
        assertFalse(equal);
      } catch (ParseException e) {
        e.printStackTrace();
        fail();
      }
    }
    
    {
      try {
        Date d1 = null;
        Date d2 = DataParseUtils.parseDate("2023-01-01");
        boolean equal = DateUtils.equal(d1, d2);
        assertFalse(equal);
      } catch (ParseException e) {
        e.printStackTrace();
        fail();
      }
    }
    
  }
}

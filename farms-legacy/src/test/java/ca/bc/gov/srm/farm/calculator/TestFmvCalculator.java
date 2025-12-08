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
package ca.bc.gov.srm.farm.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.CropItem;

/**
 * @author awilkinson
 * @created Mar 4, 2011
 */
public class TestFmvCalculator {
  

  @Test
  public void testPriceOutOfVariance() {
    FmvCalculator calc = CalculatorFactory.getFmvCalculator();

    CropItem item = new CropItem();
    item.setReportedPriceStart(new Double(6));
    item.setFmvStart(new Double(5));
    item.setReportedPriceEnd(new Double(3));
    item.setFmvEnd(new Double(5));
    item.setFmvVariance(new Double(10));
    item.setIsEligible(Boolean.TRUE);
    
    boolean startIsOut = calc.isPriceStartOutOfVariance(item);
    boolean endIsOut = calc.isPriceEndOutOfVariance(item);
    assertEquals(true, startIsOut);
    assertEquals(true, endIsOut);


    item.setAdjPriceStart(new Double(-1));
    item.setAdjPriceEnd(new Double(2));
    
    startIsOut = calc.isPriceStartOutOfVariance(item);
    endIsOut = calc.isPriceEndOutOfVariance(item);
    assertEquals(false, startIsOut);
    assertEquals(false, endIsOut);


    item = new CropItem();
    item.setReportedPriceStart(new Double(5.5));
    item.setFmvStart(new Double(5));
    item.setReportedPriceEnd(new Double(4.5));
    item.setFmvEnd(new Double(5));
    item.setFmvVariance(new Double(10));
    item.setIsEligible(Boolean.TRUE);
    
    startIsOut = calc.isPriceStartOutOfVariance(item);
    endIsOut = calc.isPriceEndOutOfVariance(item);
    
    assertEquals(false, startIsOut);
    assertEquals(false, endIsOut);
    
  }


}

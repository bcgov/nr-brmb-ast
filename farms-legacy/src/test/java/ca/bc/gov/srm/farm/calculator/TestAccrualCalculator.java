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
package ca.bc.gov.srm.farm.calculator;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.ClientService;
import  ca.bc.gov.srm.farm.service.impl.ClientServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;


public class TestAccrualCalculator {

  private static Connection conn;


  @BeforeAll
  protected static void setUp() throws Exception {
    conn = TestUtils.openConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    TestUtils.closeConnection(conn);
  }

  @Test
  public void testKnownVerified() throws Exception {
  	calculateKnown(3231917, 2009);
  	calculateKnown(23446396, 2009);
  }

  public void calculateKnown(int pin, int year) throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(pin, year, null, ClientService.DEF_FIRST_MODE);

    assertNotNull(sc);
    assertEquals(1, sc.getFarmingYear().getFarmingOperationCount());

    FarmingYear fy = sc.getFarmingYear();

    // year accruals
    double[] knownTotals = getAccruals(fy);

    AccrualCalculator calc = CalculatorFactory.getAccrualCalculator(sc);
    calc.calculateTotals();

    double[] newTotals = getAccruals(fy);

    for(int jj = 0; jj < knownTotals.length; jj++) {
    	assertEquals(knownTotals[jj], newTotals[jj], 0.1);
    }
  }


  private double[] getAccruals(FarmingYear fy) {
    double[] totals = new double[5];
    int ii = 0;

    totals[ii++] = fy.getMarginTotal().getAccrualAdjsCropInventory().doubleValue();
    totals[ii++] = fy.getMarginTotal().getAccrualAdjsLvstckInventory().doubleValue();
    totals[ii++] = fy.getMarginTotal().getAccrualAdjsPayables().doubleValue();
    totals[ii++] = fy.getMarginTotal().getAccrualAdjsPurchasedInputs().doubleValue();
    totals[ii++] = fy.getMarginTotal().getAccrualAdjsReceivables().doubleValue();

    return totals;

  }

}

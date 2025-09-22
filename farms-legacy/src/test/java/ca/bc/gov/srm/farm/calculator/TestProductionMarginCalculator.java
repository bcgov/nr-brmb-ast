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

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.ClientService;
import  ca.bc.gov.srm.farm.service.impl.ClientServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;


public class TestProductionMarginCalculator {

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
  public final void testKnownVerified() throws Exception {
  	// no yardage, payments, or contract work 
  	calcKnownCalculateUnadjusted(3231917, 2009);
  	
  	// verified with contract work expenses
  	calcKnownCalculateUnadjusted(3755360, 2009);
  }

  public final void calcKnownCalculateUnadjusted(int pin, int year) throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(pin, year, null, ClientService.DEF_FIRST_MODE);

    assertNotNull(sc);
    assertEquals(1, sc.getFarmingYear().getFarmingOperationCount());
    
    FarmingYear fy = sc.getFarmingYear();
    Integer farmNumber = new Integer(1);
    FarmingOperation fo = fy.getFarmingOperationByNumber(farmNumber);
    
    double totalMargin = fy.getMarginTotal().getUnadjustedProductionMargin().doubleValue();
    double farmMargin = fo.getMargin().getUnadjustedProductionMargin().doubleValue();
    assertEquals(totalMargin, farmMargin, 0.1);
    
    ProductionMarginCalculator calc = CalculatorFactory.getProductionMarginCalculator(sc);
    calc.calculateUnadjusted();
    
    double calculatedFarmMargin = fo.getMargin().getUnadjustedProductionMargin().doubleValue();
    assertEquals(farmMargin, calculatedFarmMargin, 0.1);
    
    double calculatedTotalMargin = fy.getMarginTotal().getUnadjustedProductionMargin().doubleValue();
    assertEquals(totalMargin, calculatedTotalMargin, 0.1);
  }

}

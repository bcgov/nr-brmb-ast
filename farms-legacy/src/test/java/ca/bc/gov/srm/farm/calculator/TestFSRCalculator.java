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

import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.impl.ClientServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 */
public class TestFSRCalculator {

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
  public void test1() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(3138799, 2009, null, ClientService.DEF_FIRST_MODE);

    assertNotNull(sc);
    assertEquals(1, sc.getFarmingYear().getFarmingOperationCount());
    
    FarmSizeRatioCalculator calc = CalculatorFactory.getFarmSizeRatioCalculator(sc);
    calc.calculateRatio();
    
    // only makes sense for reference years
		for(ReferenceScenario rs : sc.getReferenceScenarios()) {
			
      double ratio = rs.getFarmingYear().getMarginTotal().getFarmSizeRatio().doubleValue();
      assertTrue(ratio != 0);
      System.out.println(ratio);
		}
  }
}

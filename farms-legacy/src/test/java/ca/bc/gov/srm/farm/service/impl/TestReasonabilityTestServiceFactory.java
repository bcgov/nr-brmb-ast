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
package ca.bc.gov.srm.farm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.ReasonabilityTestService;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkiinson
 */
public class TestReasonabilityTestServiceFactory {

  private static Connection conn;

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    conn = TestUtils.openConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    TestUtils.closeConnection(conn);
  }

  @Test
  public final void testReasonability() throws Exception {
    int pin = 3712601;
    final int year = 2019;

    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario scenario = cs.getClientInfoWithHistory(pin, year, new Integer(3),
        ClientService.DEF_FIRST_MODE);
    assertNotNull(scenario);

    ReasonabilityTestService rs = ReasonabilityTestServiceFactory.getInstance();

    ReasonabilityTestResults results = rs.test(scenario);

    System.out.println("Pin: "+pin+"\n");
    
    assertNotNull(results);

    System.out.println("\n********************************************\n");

  }

}

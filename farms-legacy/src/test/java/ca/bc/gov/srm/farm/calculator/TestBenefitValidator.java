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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.service.ClientService;
import  ca.bc.gov.srm.farm.service.impl.ClientServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;

public class TestBenefitValidator {

  private Connection conn = null;
  

  @BeforeEach
  protected final void setUp() throws Exception {
    conn = TestUtils.openConnection();
  }

  @AfterEach
  protected final void tearDown() throws Exception {
    TestUtils.closeConnection(conn);
  }
  
  
  @Test
  public final void QtestMissingBpus() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(23015787, 2009, null, ClientService.DEF_FIRST_MODE);

    assertNotNull(sc);
    assertEquals(ScenarioStateCodes.VERIFIED, sc.getScenarioStateCode());
    
    BenefitValidator validator = CalculatorFactory.getBenefitValidator(sc);
    boolean ok = validator.validateBpus(sc);
    
    assertTrue(ok);
  }
  
  
  @Test
  public final void testFindOK() throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    int[] pins = {
    		3115649,
    		3138799,
    		3144136,
    		3159324,
    		3171550,
    		3181658,
    		3240017,
    		3371143,
    		3498789,
    		3579091,
    		3697265,
    		3699410,
    		3700424,
    		3703154,
    		3707528
    };
    
    for( int ii = 0; ii < pins.length; ii++ ) {
	    Scenario sc = cs.getClientInfoWithHistory(pins[ii], 2009, null, ClientService.DEF_FIRST_MODE);
	    BenefitValidator validator = CalculatorFactory.getBenefitValidator(sc);
	    boolean ok = validator.validateBpus(sc);
	    if(ok) {
	    	System.out.println("OK PIN: " + pins[ii]);
	    }
    }
  }

}

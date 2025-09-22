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
package ca.bc.gov.srm.farm.agent;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.util.TestUtils;
import ca.bc.gov.webade.Application;

public class TestImportAgent {


  @BeforeAll
  protected static void setUp() throws Exception {
    // do nothing
  }


  @Disabled
  @Test
  public void testRun(){
  	try {
  		Application app = TestUtils.loadWebADEApplication();
  		
  		ImportAgent.getInstance().initialize(app);
  		
  		System.out.println("Waiting 2 seconds...");
			Thread.sleep(2000);
  	} catch(Exception ex) {
  		ex.printStackTrace();
  		fail(ex.getMessage());
  	}
  }
}

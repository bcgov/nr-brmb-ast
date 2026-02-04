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
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.factory.ObjectFactory;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.StructuralChangeService;
import ca.bc.gov.srm.farm.ui.domain.calculator.StructuralChangeRow;
import ca.bc.gov.srm.farm.util.TestUtils;

public class TestStructuralChangeService {

  @BeforeAll
  protected static void setUp() throws Exception {
  	String key = "ca.bc.gov.srm.farm.transaction.TransactionProvider";
  	String value = "ca.bc.gov.srm.farm.transaction.TestTransactionProvider";
  	
  	// this beats changing the implementation.properties file
  	ServiceFactory.getInstance().setImplementingClass(key, value);
  	ObjectFactory.getInstance().setImplementingClass(key, value);
  }

  @Test
  public final void testGetRows(){
    try(Connection conn = TestUtils.openConnection();) {
  		ClientService cs = ClientServiceFactory.getInstance(conn);
      Scenario sc = cs.getClientInfoWithHistory(3755360, 2009, null, ClientService.DEF_FIRST_MODE);
      
      StructuralChangeService service = ServiceFactory.getStructuralChangeService();
      List<StructuralChangeRow> rows = service.getStructuralChanges(sc);
      
      assertTrue(rows.size() > 0);
      
  	} catch(Exception ex) {
  		ex.printStackTrace();
  		fail(ex.getMessage());
  	}
  }
}

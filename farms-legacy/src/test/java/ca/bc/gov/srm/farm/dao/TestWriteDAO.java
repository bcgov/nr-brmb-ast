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
package ca.bc.gov.srm.farm.dao;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.transaction.TestTransactionProvider;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.transaction.TransactionProvider;


public class TestWriteDAO {

  private static final String USER_ID = "JUNIT";
	
  private TransactionProvider transactionProvider;
  private WriteDAO dao;
  private Transaction transaction;
  
  
  @BeforeEach
  public void setUp() throws Exception {
    transactionProvider = new TestTransactionProvider();
    dao = new WriteDAO();
    transaction = transactionProvider.getTransaction(null);
  }

 
  @Disabled
  @Test
  public final void saveClaim(){
    try {
    	Benefit benefit = new Benefit();
    	Integer scenarioId = new Integer(64303); // find a scenario without a claim
    	
    	// set required fields
    	benefit.setProgramYearMargin(new Double(-666));
    	benefit.setTotalBenefit(new Double(-666));
    	
    	transaction.begin();
    	
    	// insert
    	assertNull(benefit.getClaimId());
    	dao.writeBenefit(transaction, benefit, scenarioId, USER_ID);
    	assertNotNull(benefit.getClaimId());
    	assertEquals(1, benefit.getRevisionCount().intValue());
    	
    	// update
    	Integer claimId = benefit.getClaimId();
    	benefit.setStructuralChangeMethodCode("RATIO");
    	dao.writeBenefit(transaction, benefit, scenarioId, USER_ID);
    	assertEquals(claimId.intValue(), benefit.getClaimId().intValue());
    	assertEquals(2, benefit.getRevisionCount().intValue());
    	
    	transaction.rollback();
    } catch (Exception ex) {
    	ex.printStackTrace();
    	fail(ex.getMessage());
    }
  }
  
  
  @Test
  public final void saveMargin(){
    try {
    	// find a scenario/operation without a margin
    	Integer scenarioId = new Integer(63973); 
    	Integer opId = new Integer(31903);
    	
    	// set required fields
    	Margin margin = new Margin();
    	margin.setTotalAllowableExpenses(new Double(-666));
    	margin.setTotalAllowableIncome(new Double(-666));
    	margin.setUnadjustedProductionMargin(new Double(-666));
    	
    	transaction.begin();
    	
    	// insert
    	assertNull(margin.getMarginId());
    	dao.writeMargin(transaction, margin, scenarioId, opId, USER_ID);
    	assertNotNull(margin.getMarginId());
    	assertEquals(1, margin.getRevisionCount().intValue());
    	
    	// update
    	Integer id = margin.getMarginId();
    	dao.writeMargin(transaction, margin, scenarioId, opId, USER_ID);
    	assertEquals(id.intValue(), margin.getMarginId().intValue());
    	assertEquals(2, margin.getRevisionCount().intValue());
    	
    	transaction.rollback();
    } catch (Exception ex) {
    	ex.printStackTrace();
    	fail(ex.getMessage());
    }
  }
  
  
  @Disabled
  @Test
  public final void saveMarginTotal(){
    try {
    	// find a scenario without a margin total
    	Integer scenarioId = new Integer(64184);
    	
    	// set required fields
    	MarginTotal mt = new MarginTotal();
    	mt.setTotalAllowableExpenses(new Double(-666));
    	mt.setTotalAllowableIncome(new Double(-666));
    	mt.setUnadjustedProductionMargin(new Double(-666));
    	
    	transaction.begin();
    	
    	// insert
    	assertNull(mt.getMarginTotalId());
    	dao.writeMarginTotal(transaction, mt, scenarioId, USER_ID);
    	assertNotNull(mt.getMarginTotalId());
    	assertEquals(1, mt.getRevisionCount().intValue());
    	
    	// update
    	Integer id = mt.getMarginTotalId();
    	dao.writeMarginTotal(transaction, mt, scenarioId, USER_ID);
    	assertEquals(id.intValue(), mt.getMarginTotalId().intValue());
    	assertEquals(2, mt.getRevisionCount().intValue());
    	
    	transaction.rollback();
    } catch (Exception ex) {
    	ex.printStackTrace();
    	fail(ex.getMessage());
    }
  }
  
  
  @Test
  public final void testSaveComment(){
    try {
    	Integer programYearId = new Integer(1);
    	StringBuffer comments = new StringBuffer();
    	
    	//
    	// I thought there was a 32,000 character limit to
    	// using CLOBs a parameter in a procedure, but this
    	// seems to work OK.
    	//
    	for(int ii = 0; ii < 33000; ii++) {
    		comments.append("A");
    	}
    	
    	transaction.begin();
    	
    	dao.writeFinalVerificationNotes(
    			transaction, 
    			comments.toString(), 
    			programYearId,
    			USER_ID);
    	
    	transaction.commit();
    } catch (Exception ex) {
    	ex.printStackTrace();
    	fail(ex.getMessage());
    }
  }
  
}

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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.FarmUser;
import ca.bc.gov.srm.farm.transaction.TestTransactionProvider;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.transaction.TransactionProvider;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * 
 */
public class TestUserDAO {
  
private static final String USER_ID = "JUNIT";
  
  private static TransactionProvider transactionProvider;
  private static UserDAO dao;
  private static Transaction transaction;
  
  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    transactionProvider = new TestTransactionProvider();
    transaction = transactionProvider.getTransaction(null);
    dao = new UserDAO();
  }

  @Disabled
  @Test
  public final void testCreateVerifier() {

    FarmUser user = new FarmUser();
    user.setUserGuid("TESTER00000000");
    user.setSourceDirectory("IDIR");
    user.setAccountName("TESTER");
    user.setEmailAddress("TESTER@mail.com");
    user.setVerifierInd(true);
    user.setDeletedInd(false);

    try {
      transaction.begin();
      
      dao.createUser(transaction, user, USER_ID);
      
      transaction.commit();
      

    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }
}

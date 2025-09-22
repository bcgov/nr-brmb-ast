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
package ca.bc.gov.srm.farm.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmUser;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.UtilityException;
import ca.bc.gov.srm.farm.list.UserListView;
import ca.bc.gov.srm.farm.security.SecurityUtility;
import ca.bc.gov.srm.farm.util.TestUtils;

public class UserServiceTest {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private String user = this.getClass().getSimpleName();

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
  }

  @Test
  public final void testCreateUser() {

    String userGuid = "TESTERuserService";
    FarmUser farmUser = new FarmUser();
    farmUser.setUserGuid(userGuid);
    farmUser.setSourceDirectory("IDIR");
    farmUser.setAccountName("TESTER");
    farmUser.setEmailAddress("TESTER@mail.com");
    farmUser.setVerifierInd(true);
    farmUser.setDeletedInd(false);

    UserService service = ServiceFactory.getUserService();
    try {
      service.deleteUserByUser(userGuid);
      service.createUser(farmUser, user);
      FarmUser foundUser = service.getUserByUserGuid(userGuid);

      assertEquals(farmUser.getUserGuid(), foundUser.getUserGuid());
      assertEquals(farmUser.getAccountName(), foundUser.getAccountName());
      assertEquals(farmUser.getEmailAddress(), foundUser.getEmailAddress());
      assertEquals(farmUser.getSourceDirectory(), foundUser.getSourceDirectory());
      assertEquals(farmUser.getUserGuid(), foundUser.getUserGuid());
      assertEquals(farmUser.getVerifierInd(), foundUser.getVerifierInd());
      assertEquals(farmUser.getDeletedInd(), foundUser.getDeletedInd());
      
      foundUser = service.getUserByUserId(foundUser.getUserId());

      assertEquals(farmUser.getUserGuid(), foundUser.getUserGuid());
      assertEquals(farmUser.getAccountName(), foundUser.getAccountName());
      assertEquals(farmUser.getEmailAddress(), foundUser.getEmailAddress());
      assertEquals(farmUser.getSourceDirectory(), foundUser.getSourceDirectory());
      assertEquals(farmUser.getUserGuid(), foundUser.getUserGuid());
      assertEquals(farmUser.getVerifierInd(), foundUser.getVerifierInd());
      assertEquals(farmUser.getDeletedInd(), foundUser.getDeletedInd());

      service.deleteUserByUser(userGuid);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public final void testUpdateUser() {
    FarmUser farmUser = new FarmUser();
    farmUser.setUserGuid("TESTER0001service");
    farmUser.setSourceDirectory("IDIR");
    farmUser.setAccountName("TESTERChanged");
    farmUser.setEmailAddress("TESTER@mail.com");
    farmUser.setVerifierInd(true);
    farmUser.setDeletedInd(false);

    UserService service = ServiceFactory.getUserService();
    try {
      service.deleteUserByUser(farmUser.getUserGuid());
      service.createUser(farmUser, user);
      FarmUser foundUser = service.getUserByUserGuid(farmUser.getUserGuid());
      assertNotNull(foundUser);
      foundUser.setVerifierInd(false);
      service.updateUser(foundUser, user);
      assertEquals(farmUser.getAccountName(), foundUser.getAccountName());
      assertFalse(foundUser.getVerifierInd());
      assertEquals(farmUser.getDeletedInd(), foundUser.getDeletedInd());
      logger.debug(foundUser.toString());

      service.deleteUserByUser(foundUser.getUserGuid());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public final void testGetAllUsers() {
    List<FarmUser> farmUsers = new ArrayList<>();

    UserService service = ServiceFactory.getUserService();
    try {
      farmUsers = service.getAllUsers();
      for (FarmUser userUser : farmUsers) {
        logger.debug(userUser.toString());
      }
      assertNotNull(farmUsers);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
    
    try {
      farmUsers = service.getAllUsers(true);
      for (FarmUser userUser : farmUsers) {
        logger.debug(userUser.toString());
        assertTrue(userUser.getDeletedInd());
      }
      assertNotNull(farmUsers);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public final void testGetVerifiers() {

    SecurityUtility securityUtil = SecurityUtility.getInstance();

    try {
      UserListView[] verifiers = securityUtil.getVerifiers();

      for (UserListView ulw : verifiers) {
        logger.debug("UserId = " + ulw.getUserId());
        logger.debug("Email = " + ulw.getEmailAddress());
        logger.debug("Guid = " + ulw.getGuid());
        logger.debug("AccountName = " + ulw.getAccountName());
      }

      assertEquals(5, verifiers.length);
    } catch (UtilityException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Test
  public final void testSyncVerifiers() {
    UserService service = ServiceFactory.getUserService();
    try {
      service.syncVerifiers(user);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

}

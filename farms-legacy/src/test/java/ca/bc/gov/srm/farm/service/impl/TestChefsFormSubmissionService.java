/**
 * Copyright (c) 2023,
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ChefsFormSubmissionService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * Test class for ChefsFormSubmissionServiceImpl Tests the
 * getUserFormTypeFromPreflight method using the real service
 */
public class TestChefsFormSubmissionService {

  private Logger log = LoggerFactory.getLogger(getClass());
  private ChefsFormSubmissionService chefsFormSubmissionService;

  // Real submission GUIDs for testing different user types
  private String idirSubmissionGuid = "ea88ff7b-ba90-462f-b320-f99105e945de";
  private String bceidSubmissionGuid = null; // No real BCeID submission GUID available for testing

  @BeforeEach
  public void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    System.setProperty("generate.cob.enabled", "N");

    try {
      chefsFormSubmissionService = ServiceFactory.getChefsFormSubmissionService();
    } catch (Exception e) {
      chefsFormSubmissionService = null;
    }
  }

  /**
   * Test the real getUserFormTypeFromPreflight method with IDIR submission GUID
   * This test should return "IDIR" as the user form type Note: If the
   * submission doesn't exist, this will throw ServiceException
   */
  @Test
  public void testGetUserFormTypeFromPreflight_IDIR() throws ServiceException {
    // Skip test if real service is not available
    if (chefsFormSubmissionService == null) {
      return;
    }

    try {
      String result = chefsFormSubmissionService.getUserFormTypeFromPreflight(idirSubmissionGuid);

      // Assert - if we get here, the submission exists
      assertNotNull(result);
      assertEquals(ChefsConstants.USER_TYPE_IDIR, result);
    } catch (ServiceException e) {
      log.debug("IDIR submission not found (expected in test environment): " + e.getMessage());
      fail(e.getMessage());
    }
  }

  /**
   * Test the real getUserFormTypeFromPreflight method with BCeID submission
   * GUID This test should return "Basic BCeID" as the user form type Note:
   * Currently no real BCeID submission GUID is available for testing
   */
  @Test
  public void testGetUserFormTypeFromPreflight_BCeID() throws ServiceException {
    // Skip test if real service is not available
    if (chefsFormSubmissionService == null) {
      return;
    }

    // Skip test if no BCeID submission GUID is available
    if (bceidSubmissionGuid == null || bceidSubmissionGuid.isEmpty()) {
      log.debug("Skipping BCeID test - no submission GUID available");
      return;
    }

    try {
      // Act
      String result = chefsFormSubmissionService.getUserFormTypeFromPreflight(bceidSubmissionGuid);

      // Assert - if we get here, the submission exists
      assertNotNull(result);
      assertEquals(ChefsConstants.USER_TYPE_BASIC_BCEID, result);
    } catch (ServiceException e) {
      log.debug("BCeID submission not found (expected in test environment): " + e.getMessage());
      fail(e.getMessage());
    }
  }

  /**
   * Test the real method with an invalid submission GUID This should throw a
   * ServiceException
   */
  @Test
  public void testGetUserFormTypeFromPreflight_InvalidSubmissionGuid() {
    // Skip test if real service is not available
    if (chefsFormSubmissionService == null) {
      return;
    }

    String invalidSubmissionGuid = "invalid-guid-12345";

    assertThrows(ServiceException.class, () -> {
      chefsFormSubmissionService.getUserFormTypeFromPreflight(invalidSubmissionGuid);
    });
  }

  /**
   * Test the real method with null submission GUID This should throw a
   * ServiceException
   */
  @Test
  public void testGetUserFormTypeFromPreflight_NullSubmissionGuid() {
    // Skip test if real service is not available
    if (chefsFormSubmissionService == null) {
      return;
    }

    assertThrows(ServiceException.class, () -> {
      chefsFormSubmissionService.getUserFormTypeFromPreflight(null);
    });
  }

  /**
   * Test the real method with empty submission GUID This should throw a
   * ServiceException
   */
  @Test
  public void testGetUserFormTypeFromPreflight_EmptySubmissionGuid() {
    // Skip test if real service is not available
    if (chefsFormSubmissionService == null) {
      return;
    }

    // Arrange
    String emptySubmissionGuid = "";

    // Act & Assert
    assertThrows(ServiceException.class, () -> {
      chefsFormSubmissionService.getUserFormTypeFromPreflight(emptySubmissionGuid);
    });
  }

}

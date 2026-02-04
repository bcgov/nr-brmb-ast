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

import java.sql.Blob;
import java.sql.Connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.dao.CobDAO;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;

public class TestCdogsService {

  @SuppressWarnings("unused")
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private String userId = this.getClass().getSimpleName();

  @BeforeEach
  public void setUp() throws Exception {
    TestUtils.standardTestSetUp();

    System.setProperty("generate.cob.enabled", "N");
  }

  @Test
  public final void testCreateCdogsCoverageNoticeReport() {
    CdogsService cdogsService = ServiceFactory.getCdogsService();
    CobDAO dao = new CobDAO();

    try {
      int participantPin = 98765684;
      int programYear = 2023;
      int scenarioNumber = 3;

      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = null;
      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, scenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      assertNotNull(scenario);
      ReportService reportService = ServiceFactory.getReportService();
      reportService.deleteBenefitDocument(scenario.getScenarioId());
      
      try (Connection conn = TestUtils.openConnection();) {
        Blob blob = dao.getBlob(conn, scenario.getScenarioId(), false);
        assertNull(blob);
      }

      assertNotNull(scenario.getClient());
      Person owner = null;
      owner = scenario.getClient().getOwner();
      assertNotNull(owner);

      cdogsService.createCdogsCoverageNoticeReport(scenario, userId);

      try (Connection conn = TestUtils.openConnection();) {

        Blob blob = dao.getBlob(conn, scenario.getScenarioId(), false);
        assertNotNull(blob);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }

}

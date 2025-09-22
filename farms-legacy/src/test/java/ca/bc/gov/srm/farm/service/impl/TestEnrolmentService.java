/**
 *
 * Copyright (c) 2013,
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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.EnrolmentService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.EnrolmentUtils;
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 * @author  awilkinson
 */
public class TestEnrolmentService {

  private static Connection conn = null;

  @BeforeAll
  public static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    conn = TestUtils.openConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    if (conn != null) {
      conn.commit();
      conn.close();
    }
  }
  
  @Disabled
  @Test
  public void postEnrolment() {
    CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();
    
    Enrolment enrolment = new Enrolment();
    enrolment.setPin(23445877);
    enrolment.setProducerName("Star, Vicker");
    enrolment.setEnrolmentYear(2025);
    enrolment.setIsLateParticipant(true);
    enrolment.setEnrolmentFee(CalculatorConfig.LATE_ENROLMENT_FEE);
    enrolment.setGeneratedDate(new Date());
    
    Integer importVersionId = 151264;
    
    try {
      crmTransferService.postEnrolment(enrolment, importVersionId, "POST_ENROLMENT_TEST");
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }
  
  /**
   * Test auto sending event that will occur on verification of Late Participant.
   */
  @Disabled
  @Test
  public final void growingForward2017() throws ServiceException, SQLException {

    try {
      TestUtils.loadWebADEApplication();
    } catch(Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
    
    CacheFactory.getRequestCache().addItem(CacheKeys.CURRENT_BUSINESS_ACTION, new BusinessAction("editScenario"));
    
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario scenario = cs.getClientInfoWithHistory(23370109, 2019, 2, ClientService.ENROLMENT_MODE);
    
    EnrolmentService enrService = ServiceFactory.getEnrolmentService();
    
    enrService.processEnrolmentFromScenarioWorkflow(scenario, false, true, "ENROLMENT_GROW_FWD_TEST", conn);
    conn.commit();
  }

  @Disabled
  @Test
  public final void growingForward2013() throws IOException, ServiceException {

    // 3743549  - has 2004, 2008, 2009 CRA only - fail - insufficient margin data
    // 3693736  - has 2007-2009 CRA only
    // 5474564  - has 2006-2009 CRA only
    // 22245898 - has 2005-2009 CRA only
    // 23374085 - has zero margins CRA only
    // 23150097 - has zero margins COMP scenario
    // 23774946 - has zero margins COMP scenario
    String pinsString = "3743549,3693736,5474564,22245898,23374085,23150097,23774946";
    Integer enrolmentYear = new Integer(2011);
    boolean createTaskInBarn = true;

    // easier to just use an existing enrolment import version
    Integer importVersionId = new Integer(5767);

    File enrolmentFile = EnrolmentUtils.createEnrolmentFile(pinsString, enrolmentYear, createTaskInBarn);

//    CacheFactory.getRequestCache().addItem(CacheKeys.CURRENT_BUSINESS_ACTION, new BusinessAction("importData"));
//
//    ImportService importService = ServiceFactory.getImportService();
//
//    // create a farm_import_versions entry, and save the file to a blob
//    try (FileInputStream importFileInputStream = new FileInputStream(enrolmentFile);) {
//      String description = "2009 - unit test";
//      ImportVersion importVersion = importService.createImportVersion(
//          ImportClassCodes.ENROL,
//          description,
//          enrolmentFile.getPath(),
//          importFileInputStream,
//          "ENROLMENT_SERVICE");
//  
//    }

    EnrolmentServiceImpl enrService = new EnrolmentServiceImpl();
    
    enrService.generateEnrolmentsStaging(conn, importVersionId, enrolmentFile, "ENROLMENT_GROW_FWD_TEST");
    
    enrolmentFile.delete();
  }
  
  @Disabled
  @Test
  public final void missingBpus() throws IOException, ServiceException {
    
    // 23374085 - CRA only - fail - missing BPUs
    // 3693736  - CRA only - success - not missing BPUs
    String pinsString = "23374085,3693736";
    Integer enrolmentYear = new Integer(2011);
    boolean createTaskInBarn = false;
    
    // easier to just use an existing enrolment import version
    Integer importVersionId = new Integer(5767);
    
    File enrolmentFile = EnrolmentUtils.createEnrolmentFile(pinsString, enrolmentYear, createTaskInBarn);
    
    EnrolmentServiceImpl enrService = new EnrolmentServiceImpl();
    
    enrService.generateEnrolmentsStaging(conn, importVersionId, enrolmentFile, "ENROLMENT_MISSING_BPU_TEST");
    
    enrolmentFile.delete();
  }
  
  @Disabled
  @Test
  public final void missingProductiveUnits() throws IOException, ServiceException {
    
    // 23037385 - CRA only - missing PUCs for all years
    // 22277891 - CRA only - has PUCs for 2007, 2009
    // 22984868 - CRA only - has PUCs for all years
    String pinsString = "23037385,22277891,22984868";
    Integer enrolmentYear = new Integer(2011);
    
    // easier to just use an existing enrolment import version
    Integer importVersionId = new Integer(5767);
    boolean createTaskInBarn = false;
    
    File enrolmentFile = EnrolmentUtils.createEnrolmentFile(pinsString, enrolmentYear, createTaskInBarn);
    
    EnrolmentServiceImpl enrService = new EnrolmentServiceImpl();
    
    enrService.generateEnrolmentsStaging(conn, importVersionId, enrolmentFile, "ENROLMENT_MISSING_BPU_TEST");
    
    enrolmentFile.delete();
  }
}

/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.benefit.triage;

import java.sql.Connection;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.crm.resource.CrmCoreConfigurationResource;
import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.ReadDAO;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BenefitTriageService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ConfigurationService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 */
public abstract class AbstractBenefitTriageTest {

  private static Logger logger = LoggerFactory.getLogger(AbstractBenefitTriageTest.class);
  protected static CalculatorService calculatorService;
  protected static BenefitTriageService benefitTriageService;
  private static ConfigurationService configService;
  protected static CalculatorDAO calculatorDao = new CalculatorDAO();
  protected static Connection conn;
  protected static ReadDAO readDAO;
  protected static CrmRestApiDao crmDao;
  protected String user = this.getClass().getSimpleName();

  @BeforeAll
  protected static void parentSetUp() throws Exception {
    TestUtils.standardTestSetUp();
    
    calculatorService = ServiceFactory.getCalculatorService();
    benefitTriageService = ServiceFactory.getBenefitTriageService();
    configService = ServiceFactory.getConfigurationService();
  
    configService.loadConfigurationParameters();
    configService.loadYearConfigurationParameters();
    
    conn = TestUtils.openConnection();
    readDAO = new ReadDAO(conn);
    crmDao = new CrmRestApiDao();
  }

  @AfterAll
  protected static void parentTearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }

  /**
   * 
   */
  public AbstractBenefitTriageTest() {
    super();
  }

  protected void logErrorMessages(List<String> errorMessages) {
    logger.debug("Error messages:");
    for (String msg : errorMessages) {
      logger.debug(msg);
    }
  }
  
  protected void logFailMessages(List<String> errorMessages) {
    logger.debug("Fail messages:");
    for (String msg : errorMessages) {
      logger.debug(msg);
    }
  }

  protected CrmCoreConfigurationResource getCoreConfiguration() throws ServiceException {
    return crmDao.getCoreConfiguration();
  }

}
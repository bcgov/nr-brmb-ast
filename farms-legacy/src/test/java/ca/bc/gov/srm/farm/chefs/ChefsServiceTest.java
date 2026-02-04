/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ChefsService;
import ca.bc.gov.srm.farm.service.ServiceFactory;

/**
 * @author awilkinson
 */
public class ChefsServiceTest extends ChefsSubmissionTest {

  @SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(ChefsServiceTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.NOL;


  @Disabled 
  @Test
  public void processSubmissionsOfAllFormTypesFromRealChefsForms() {

    ChefsService chefsService = ServiceFactory.getChefsService();
    try {
      chefsService.processSubmissions(conn);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }

  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }

}

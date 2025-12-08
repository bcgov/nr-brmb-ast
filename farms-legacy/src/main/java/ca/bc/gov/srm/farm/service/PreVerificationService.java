/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service;

import ca.bc.gov.srm.farm.domain.PreVerificationChecklist;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;

/**
 * PreVerificationService evaluates a scenario to get answers to a list
 * of questions pertinent to how work on the scenario should be routed,
 * that is, which task queue it should be added to.
 * 
 * @author awilkinson
 * @created Sep 16, 2021
 */
public interface PreVerificationService {
  
  PreVerificationChecklist getPreVerificationChecklist(Scenario scenario)
      throws ServiceException;

}

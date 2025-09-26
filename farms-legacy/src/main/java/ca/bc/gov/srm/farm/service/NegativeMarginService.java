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
package ca.bc.gov.srm.farm.service;

import java.util.List;

import ca.bc.gov.srm.farm.domain.NegativeMargin;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;

public interface NegativeMarginService {

  List<NegativeMargin> getNegativeMargins(
    Integer farmingOperationId,
    Integer scenarioId) throws ServiceException;

  void updateNegativeMargins(
    List<NegativeMargin> negativeMargins,
    Scenario scenario,
    String user) throws ServiceException;

  void calculateNegativeMargins(
    Integer farmingOperationId,
    Integer scenarioId,
    String user) throws ServiceException;
}

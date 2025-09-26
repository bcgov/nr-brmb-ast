/**
 * Copyright (c) 2009,
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

import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.ui.domain.AccountSearchResult;


/**
 * SearchService.
 */
public interface SearchService {

  /**
   * searchAccounts.
   *
   * @param   pin               pin
   * @param   name              name
   * @param   city              city
   * @param   postalCode        postalCode
   * @param   userId            userId
   * @param   federalIdentifier federalIdentifier
   *
   * @return  List of AccountSearchResult
   *
   * @throws  ServiceException  On exception.
   */
  List<AccountSearchResult> searchAccounts(
    final String pin, 
    final String name, 
    final String city,
    final String postalCode, 
    final String userId) throws ServiceException;
}

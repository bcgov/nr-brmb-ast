/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import java.util.List;

import ca.bc.gov.srm.farm.dao.SearchDAO;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.SearchService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.domain.AccountSearchResult;


/**
 * SearchServiceImpl.
 */
final class SearchServiceImpl extends BaseService implements SearchService {

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
  @Override
  public List<AccountSearchResult> searchAccounts(
    final String pin, 
    final String name,
    final String city, 
    final String postalCode, 
    final String userId)
    throws ServiceException {
    List<AccountSearchResult> results = null;
    Transaction transaction = null;
    SearchDAO dao = new SearchDAO();

    try {
      transaction = openTransaction();
      results = dao.searchAccounts(
          transaction,
          pin,
          name,
          city,
          postalCode,
          userId);
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return results;
  }
}

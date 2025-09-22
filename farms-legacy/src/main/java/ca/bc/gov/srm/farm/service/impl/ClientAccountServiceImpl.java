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

import java.util.Iterator;
import java.util.List;

import ca.bc.gov.srm.farm.dao.SubscriptionDAO;
import ca.bc.gov.srm.farm.dao.ClaimHistoryDAO;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ClientAccountService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;

/**
 * ClientAccountServiceImpl.
 */
final class ClientAccountServiceImpl extends BaseService
  implements ClientAccountService {
  

  /**
   * Get the AgristabilityClients (and the owner person) that this user is
   * authorized to represent.
   *
   * @return  the clients
   *
   * @throws  ServiceException  on exception
   */
  @Override
  public Client[] getClientsForCurrentUser()
    throws ServiceException {
    String guid = CurrentUser.getUser().getGuid();
    SubscriptionDAO dao = new SubscriptionDAO();
    Transaction transaction = null;
    Client[] clients = null;

    try {
      transaction = openTransaction();

      List cl = dao.getClientsForUser(transaction, guid);
      clients = (Client[]) cl.toArray(new Client[cl.size()]);
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return clients;
  }
  
  
  
  /**
   * Used by screen 400 to get the benefits for all years including reference
   * years.
   * 
   * @param programYearScenario programYearScenario
   * @throws ServiceException on exception
   */
  @Override
  public void getReferenceYearBenefits(Scenario programYearScenario) 
  throws ServiceException {
	  ClaimHistoryDAO dao = new ClaimHistoryDAO();
	  Transaction transaction = null;

	  try {
	    transaction = openTransaction();
	
	    Iterator iter = programYearScenario.getReferenceScenarios().iterator();
			while(iter.hasNext()) {
				ReferenceScenario rs = (ReferenceScenario) iter.next();

				Benefit b = dao.getReferenceYearBenefit(transaction, rs.getScenarioId());
				rs.getFarmingYear().setBenefit(b);
			}
	  } catch (Exception e) {
	    throw new ServiceException(e);
	  } finally {
	    closeTransaction(transaction);
	  }
	}
  

}

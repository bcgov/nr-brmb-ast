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
package ca.bc.gov.srm.farm.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.NegativeMarginDAO;
import ca.bc.gov.srm.farm.domain.NegativeMargin;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.NegativeMarginService;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author hwang
 */
public class NegativeMarginServiceImpl extends BaseService implements NegativeMarginService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public List<NegativeMargin> getNegativeMargins(
      final Integer farmingOperationId,
      final Integer scenarioId)
      throws ServiceException {

    Transaction transaction = null;
    List<NegativeMargin> negativeMargins = null;
    NegativeMarginDAO dao = new NegativeMarginDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      negativeMargins = dao.getNegativeMargins(transaction, farmingOperationId, scenarioId);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      if (e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return negativeMargins;
  }

  @Override
  public void updateNegativeMargins(
      final List<NegativeMargin> negativeMargins,
      final Scenario scenario,
      final String user)
  throws ServiceException {

    if (negativeMargins == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    NegativeMarginDAO dao = new NegativeMarginDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateNegativeMargins(transaction, negativeMargins, user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);

      transaction.commit();
    } catch (ServiceException se) {
      rollback(transaction);
      throw se;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  @Override
  public void calculateNegativeMargins(
      final Integer farmingOperationId,
      final Integer scenarioId,
      final String user)
      throws ServiceException {

    Transaction transaction = null;
    NegativeMarginDAO dao = new NegativeMarginDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.calculateNegativeMargins(transaction, farmingOperationId, scenarioId, user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      if (e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

  }
}

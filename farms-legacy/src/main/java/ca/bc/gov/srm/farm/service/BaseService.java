/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.MessageItem;
import ca.bc.gov.srm.farm.StatusType;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.exception.CheckConstraintException;
import ca.bc.gov.srm.farm.exception.ChildRecordsExistException;
import ca.bc.gov.srm.farm.exception.DataNotCurrentException;
import ca.bc.gov.srm.farm.exception.DuplicateDataException;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.message.MessageKeys;
import ca.bc.gov.srm.farm.message.MessageUtility;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.transaction.TransactionProvider;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;


/**
 * BaseService.
 */
public class BaseService {

  /** log. */
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * addSuccessMessage.
   *
   * @param  result  The parameter value.
   */
  protected void addSuccessMessage(final ServiceResult result) {
    String key = MessageKeys.CUSTOM_MESSAGE;
    String msg = "Operation completed successfully.";
    MessageItem mi = MessageUtility.getInstance().createInfoMessage(key,
        new String[] {msg});
    result.addStatusMessage(mi);
  }

  /**
   * getBusinessAction.
   *
   * @return  The return value.
   */
  protected BusinessAction getBusinessAction() {
    BusinessAction ba = (BusinessAction) CacheFactory.getRequestCache().getItem(
        CacheKeys.CURRENT_BUSINESS_ACTION);

    return ba;
  }

  /**
   * @return  a new transaction
   *
   * @throws  ProviderException  on exception
   */
  protected Transaction openTransaction() throws ProviderException {
    TransactionProvider provider = TransactionProvider.getInstance();
    BusinessAction businessAction = getBusinessAction();

    Transaction transaction;
    if(businessAction.equals(BusinessAction.system())) {
      transaction = provider.getTransaction(businessAction);
    } else {
      transaction = provider.getTransaction(businessAction, CurrentUser.getUser());
    }
    
    return transaction;
  }


  /**
   * @param  transaction  to close
   */
  protected void closeTransaction(Transaction transaction) {
    TransactionProvider provider = TransactionProvider.getInstance();
    provider.close(transaction);
  }
  
  
  /**
   * @param  transaction  to rollback
   */
  protected void rollback(Transaction transaction) {
    TransactionProvider provider = TransactionProvider.getInstance();
    provider.rollback(transaction);
  }

  /**
   * handleException.
   *
   * @param   result       The parameter value.
   * @param   transaction  The parameter value.
   * @param   e            The parameter value.
   * @param   projectId    caption The parameter value.
   *
   * @throws  ServiceException  On exception.
   */
  protected void handleException(final ServiceResult result,
    final Transaction transaction, final ServiceException e,
    final Long projectId) throws ServiceException {
    TransactionProvider transactionProvider = TransactionProvider.getInstance();

    // first, rollback the transaction...
    transactionProvider.rollback(transaction);

    // set the result to failure...
    result.setStatusType(StatusType.failure());

    if (e != null) {
      log.error("Exception on project (" + String.valueOf(projectId)
        + ") during " + getBusinessAction().getActionName() + "(): "
        + e.getMessage(), e);

      //
      // on these known data access exceptions, we want to return failure
      // and a well known key so the caller can take specific actions.
      // let all other exceptions be thrown.
      //
      if (e instanceof DataNotCurrentException) {

        // when the data is not current, we need to purge the project from
        // cache to allow the caller to get the current data.
        log.error("DataNotCurrentException on project ("
          + String.valueOf(projectId) + "): removing from cache.");

        result.addStatusMessage(MessageUtility.getInstance().createErrorMessage(
            MessageKeys.EXCEPTION_DATA_NOT_CURRENT));
      } else if (e instanceof CheckConstraintException) {
        result.addStatusMessage(MessageUtility.getInstance().createErrorMessage(
            MessageKeys.EXCEPTION_CHECK_CONSTRAINT));
      } else if (e instanceof ChildRecordsExistException) {
        result.addStatusMessage(MessageUtility.getInstance().createErrorMessage(
            MessageKeys.EXCEPTION_CHILD_RECORD_EXISTS));
      } else if (e instanceof DuplicateDataException) {
        result.addStatusMessage(MessageUtility.getInstance().createErrorMessage(
            MessageKeys.EXCEPTION_DUPLICATE_DATA));
      } else {

        // throw the exception
        throw e;
      }
    } else {
      throw new ServiceException("Unknown exception during "
        + getBusinessAction().getActionName() + "()");
    }

  }
}

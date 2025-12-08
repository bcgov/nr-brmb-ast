/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.transaction;

import ca.bc.gov.srm.farm.Provider;
import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.factory.ObjectFactory;
import ca.bc.gov.srm.farm.security.BusinessAction;


/**
 * TransactionProvider.
 */
public abstract class TransactionProvider extends Provider {

  /** Creates a new TransactionProvider object. */
  TransactionProvider() {

  }

  /**
   * close.
   *
   * @param  transaction  The parameter value.
   */
  public abstract void close(Transaction transaction);

  /**
   * getTransaction.
   *
   * @param   businessAction  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract Transaction getTransaction(BusinessAction businessAction)
    throws ProviderException;

  /**
   * getTransaction.
   *
   * @param   businessAction  Input parameter.
   * @param   user            Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract Transaction getTransaction(BusinessAction businessAction,
    User user) throws ProviderException;

  /**
   * rollback.
   *
   * @param  transaction  The parameter value.
   */
  public abstract void rollback(Transaction transaction);

  /**
   * getInstance.
   *
   * @return  The return value.
   */
  public static TransactionProvider getInstance() {
    TransactionProvider result = (TransactionProvider) ObjectFactory
      .createObject(TransactionProvider.class, null);

    return result;
  }

}

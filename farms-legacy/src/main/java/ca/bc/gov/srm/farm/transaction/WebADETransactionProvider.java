/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.transaction;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.exception.UtilityException;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.security.SecurityRule;
import ca.bc.gov.srm.farm.security.SecurityUtility;
import ca.bc.gov.srm.farm.webade.WebADEProviderUtils;
import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.user.WebADEUserPermissions;

import java.sql.SQLException;


/**
 * WebADETransactionProvider.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2899 $
 */
final class WebADETransactionProvider extends TransactionProvider {

  /** Creates a new WebADETransactionProvider object. */
  WebADETransactionProvider() {
    initialize(null);
  }

  /**
   * close.
   *
   * @param  transaction  The parameter value.
   */
  @Override
  public void close(final Transaction transaction) {

    if (getLog().isDebugEnabled()) {
      getLog().debug("> close(" + (transaction != null) + ")");
    }

    if (transaction != null) {
      transaction.close();
    }

    if (getLog().isDebugEnabled()) {
      getLog().debug("< close()");
    }
  }

  /**
   * getTransaction.
   *
   * @param   businessAction  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public Transaction getTransaction(final BusinessAction businessAction)
    throws ProviderException {

    checkInitialized();

    Transaction result = null;

    if (getLog().isDebugEnabled()) {
      getLog().debug("> getTransaction(" + businessAction + ")");
    }

    try {

      // get the webade application
      Application app = WebADERequest.getInstance().getApplication();

      // create the webade action based on our business action..
      SecurityRule rule = SecurityUtility.getInstance().getSecurityRule(
          businessAction);
      Action action = new Action(rule.getRuleName());

      result = new WebADETransaction();
      result.setDatastore(app.getConnectionByPriviledgedAction(action));
    } catch (UtilityException e) {

      if (getLog().isErrorEnabled()) {
        getLog().error("UtilityException on getTransaction: " + e.getMessage());
      }

      throw new ProviderException(e);
    } catch (WebADEException e) {

      if (getLog().isErrorEnabled()) {
        getLog().error("WebADEException on getTransaction: " + e.getMessage());
      }

      throw new ProviderException(e);
    } catch (SQLException e) {

      if (getLog().isErrorEnabled()) {
        getLog().error("SQLException on getTransaction: " + e.getMessage());
      }

      throw new ProviderException(e);
    }

    getLog().debug("< getTransaction");

    return result;
  }

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
  @Override
  public Transaction getTransaction(final BusinessAction businessAction,
    final User user) throws ProviderException {
    checkInitialized();

    Transaction result = null;

    if (getLog().isDebugEnabled()) {
      getLog().debug("> getTransaction(" + businessAction + ", " + user + ")");
    }

    try {

      // get the webade application
      Application app = WebADERequest.getInstance().getApplication();

      // create the webade action based on our business action..
      SecurityRule rule = SecurityUtility.getInstance().getSecurityRule(
          businessAction);
      Action action = new Action(rule.getRuleName());

      // get the user's webade permissions...
      WebADEUserPermissions perms = WebADEProviderUtils.getPermissions(app,
          user);
      result = new WebADETransaction();
      result.setDatastore(app.getConnectionByAction(perms, action));
    } catch (UtilityException e) {
      getLog().error("UtilityException on getTransaction: " + e.getMessage());
      throw new ProviderException(e);
    } catch (WebADEException e) {
      getLog().error("WebADEException on getTransaction: " + e.getMessage());
      throw new ProviderException(e);
    } catch (SQLException e) {
      getLog().error("SQLException on getTransaction: " + e.getMessage());
      throw new ProviderException(e);
    }

    if (getLog().isDebugEnabled()) {
      getLog().debug("< getTransaction");
    }

    return result;

  }

  /**
   * rollback.
   *
   * @param  transaction  The parameter value.
   */
  @Override
  public void rollback(final Transaction transaction) {

    if (getLog().isDebugEnabled()) {
      getLog().debug("> rollback(" + (transaction != null) + ")");
    }

    if (transaction != null) {
      transaction.rollback();
    }

    if (getLog().isDebugEnabled()) {
      getLog().debug("< rollback()");
    }
  }

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
   */
  @Override
  public void initialize(final Object resource) {
    setInitialized(false);

    SecurityUtility su = SecurityUtility.getInstance();

    if ((su != null) && su.isInitialized()) {
      setInitialized(true);
    }

  }

}

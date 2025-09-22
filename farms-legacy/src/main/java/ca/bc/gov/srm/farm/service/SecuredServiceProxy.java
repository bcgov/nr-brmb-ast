/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.service;

import ca.bc.gov.srm.farm.factory.BaseProxy;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.log.TimeLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * SecuredServiceProxy.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
class SecuredServiceProxy implements BaseProxy {

  /** delegate. */
  private Object delegate;

  /** log. */
  private Logger log;

  /**
   * Creates a new SecuredServiceProxy object.
   *
   * @param  serviceClass  Input parameter to initialize class.
   */
  SecuredServiceProxy(final Class<?> serviceClass) {
    // do nothing
  }

  /**
   * initialize.
   *
   * @param  delegateObject  The parameter value.
   */
  @Override
  public void initialize(final Object delegateObject) {
    this.delegate = delegateObject;
    log = LoggerFactory.getLogger(this.delegate.getClass());
  }

  /**
   * invoke.
   *
   * @param   proxy   The parameter value.
   * @param   method  The parameter value.
   * @param   args    The parameter value.
   *
   * @return  The return value.
   *
   * @throws  Throwable  On exception.
   */
  @Override
  public Object invoke(final Object proxy, final Method method,
    final Object[] args) throws Throwable {
    boolean logMethod = !ignoreMethod(method);
    TimeLogger timer = new TimeLogger(log, method.getName());

    if (logMethod) {
      timer.start();
      LoggingUtils.entry(log, method.getName(), args);
    }

    try {

      //
      // There used to be some code here that would check that the last
      // parameter was a user, and would check that the user had premission
      // to perform the action, but that code has now been moved into the
      // SecureAction.
      //
      Object result = method.invoke(delegate, args);

      if (logMethod) {
        timer.stop();

        if (method.getReturnType() == void.class) {
          LoggingUtils.voidExit(log, method.getName());
        } else {
          LoggingUtils.exit(log, method.getName(), result);
        }
      }

      return result;
    } catch (InvocationTargetException e) {
      Throwable targetException = e.getTargetException();

      if (logMethod) {
        timer.stop();
        LoggingUtils.exception(log, method.getName(), targetException);
      }

      throw targetException;
    }
  }

  /**
   * ignoreMethod.
   *
   * @param   method  The parameter value.
   *
   * @return  The return value.
   */
  private boolean ignoreMethod(final Method method) {

    if (method.getName().startsWith("get")) {
      return true;
    }

    if (method.getName().startsWith("is")) {
      return true;
    }

    if (method.getName().startsWith("set")) {
      return true;
    }

    if (method.getName().equals("toString")) {
      return true;
    }

    return false;
  }
}

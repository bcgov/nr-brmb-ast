/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.factory;

import java.lang.reflect.InvocationHandler;


/**
 * BaseProxy.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public interface BaseProxy extends InvocationHandler {

  /**
   * initialize.
   *
   * @param  delegate  The parameter value.
   */
  void initialize(Object delegate);
}

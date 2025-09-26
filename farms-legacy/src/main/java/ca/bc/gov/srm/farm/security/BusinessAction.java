/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.security;

import java.util.Objects;

import ca.bc.gov.srm.farm.BaseObject;


/**
 * BusinessAction.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
public class BusinessAction extends BaseObject {

  /** actionName. */
  private String actionName;

  /** Creates a new BusinessAction object. */
  public BusinessAction() {
  }

  /**
   * Creates a new BusinessAction object.
   *
   * @param  value  Input parameter to initialize class.
   */
  public BusinessAction(final String value) {
    this.actionName = value;
  }


  /**
   * system.
   *
   * @return  The return value.
   */
  public static BusinessAction system() {
    return new BusinessAction("system");
  }


  /**
   * getActionName.
   *
   * @return  The return value.
   */
  public String getActionName() {
    return actionName;
  }

  /**
   * Sets the value for action name.
   *
   * @param  value  Input parameter.
   */
  public void setActionName(final String value) {
    this.actionName = value;
  }

  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    StringBuffer retValue = new StringBuffer();
    retValue.append("BusinessAction(");
    retValue.append("actionName=");
    retValue.append(actionName).append(")");

    return retValue.toString();
  }

  /**
   * generateHashCode.
   *
   * @return  The return value.
   */
  @Override
  protected int generateHashCode() {
    int result = HASH_SEED;
    result = hash(result, actionName);

    return result;
  }
  
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    
    if(o instanceof BusinessAction) {
      BusinessAction other = (BusinessAction) o;
      result = Objects.equals(actionName, other.actionName);
    }
    
    return result;
  }
}

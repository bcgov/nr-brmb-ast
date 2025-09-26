/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.security;

import ca.bc.gov.srm.farm.BaseObject;


/**
 * SecurityRule.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class SecurityRule extends BaseObject {

  /** ruleName. */
  private String ruleName;


  /** Creates a new SecurityRule object. */
  public SecurityRule() {
    super();
  }

  /**
   * Creates a new SecurityRule object.
   *
   * @param  value  Input parameter to initialize class.
   */
  public SecurityRule(final String value) {
    super();
    this.ruleName = value;
  }


  /**
   * getRuleName.
   *
   * @return  The return value.
   */
  public String getRuleName() {
    return ruleName;
  }


  /**
   * Sets the value for rule name.
   *
   * @param  value  Input parameter.
   */
  public void setRuleName(final String value) {
    this.ruleName = value;
  }

  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    StringBuffer retValue = new StringBuffer();
    retValue.append("SecurityRule(");
    retValue.append("ruleName=");
    retValue.append(ruleName).append(")");

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
    result = hash(result, ruleName);

    return result;
  }

}

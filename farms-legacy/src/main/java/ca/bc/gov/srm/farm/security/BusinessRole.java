/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.security;

import ca.bc.gov.srm.farm.BaseObject;


/**
 * BusinessRole.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
public class BusinessRole extends BaseObject {

  /** roleName. */
  private String roleName;


  /** Creates a new BusinessRole object. */
  public BusinessRole() {
    super();
  }


  /**
   * Creates a new BusinessRole object.
   *
   * @param  value  Input parameter to initialize class.
   */
  public BusinessRole(final String value) {
    super();
    this.roleName = value;
  }

  /**
   * administrator.
   *
   * @return  The return value.
   */
  public static BusinessRole administrator() {
    return new BusinessRole("ADMIN");
  }
  
  public static BusinessRole seniorVerifier() {
    return new BusinessRole("SENIOR_VERIFIER");
  }
  
  public static BusinessRole verifier() {
    return new BusinessRole("VERIFIER");
  }

  /**
   * administrator.
   *
   * @return  The return value.
   */
  public static BusinessRole user() {
    return new BusinessRole("USER");
  }

  /**
   * getRoleName.
   *
   * @return  The return value.
   */
  public String getRoleName() {
    return roleName;
  }


  /**
   * Sets the value for role name.
   *
   * @param  value  Input parameter.
   */
  public void setRoleName(final String value) {
    this.roleName = value;
  }

  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    StringBuffer retValue = new StringBuffer();
    retValue.append("BusinessRole(");
    retValue.append("roleName=");
    retValue.append(roleName).append(")");

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
    result = hash(result, roleName);

    return result;
  }

}

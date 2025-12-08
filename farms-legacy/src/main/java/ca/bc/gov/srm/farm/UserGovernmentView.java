/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

/**
 * UserGovernmentView.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class UserGovernmentView extends UserBasicView {

  /** accountType. */
  private String accountType;

  /** employeeId. */
  private String employeeId;


  /**
   * getAccountType.
   *
   * @return  The return value.
   */
  public String getAccountType() {
    return accountType;
  }


  /**
   * getEmployeeId.
   *
   * @return  The return value.
   */
  public String getEmployeeId() {
    return employeeId;
  }


  /**
   * Sets the value for account type.
   *
   * @param  value  accountType Input parameter.
   */
  public void setAccountType(final String value) {
    this.accountType = value;
  }


  /**
   * Sets the value for employee id.
   *
   * @param  value  employeeId Input parameter.
   */
  public void setEmployeeId(final String value) {
    this.employeeId = value;
  }

  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    StringBuffer retValue = new StringBuffer();
    retValue.append(classnameToString()).append("(");
    retValue.append("super=");
    retValue.append(super.toString());
    retValue.append("employeeId=");
    retValue.append(employeeId).append(",");
    retValue.append("accountType=");
    retValue.append(accountType).append(")");

    return retValue.toString();
  }
}

/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

import java.util.Date;


/**
 * User.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
public interface User {

  /**
   * getAccountName.
   *
   * @return  The return value.
   */
  String getAccountName();

  /**
   * getDisplayName.
   *
   * @return  The return value.
   */
  String getDisplayName();

  /**
   * getEmailAddress.
   *
   * @return  The return value.
   */
  String getEmailAddress();

  /**
   * getFirstName.
   *
   * @return  The return value.
   */
  String getFirstName();

  /**
   * getGuid.
   *
   * @return  The return value.
   */
  String getGuid();

  /**
   * getId.
   *
   * @return  The return value.
   */
  Long getId();

  /**
   * getLastName.
   *
   * @return  The return value.
   */
  String getLastName();

  /**
   * getMiddleInitial.
   *
   * @return  The return value.
   */
  String getMiddleInitial();

  /**
   * getPhoneNumber.
   *
   * @return  The return value.
   */
  String getPhoneNumber();

  /**
   * getSourceDirectory.
   *
   * @return  The return value.
   */
  String getSourceDirectory();

  /**
   * getUpdatedDate.
   *
   * @return  The return value.
   */
  Date getUpdatedDate();

  /**
   * getUserId.
   *
   * @return  The return value.
   */
  String getUserId();

  /**
   * getUserTypeCode.
   *
   * @return  The return value.
   */
  String getUserTypeCode();

  /**
   * isEmployee.
   *
   * @return  The return value.
   */
  boolean isEmployee();

  /**
   * Sets the value for account name.
   *
   * @param  accountName  Input parameter.
   */
  void setAccountName(String accountName);

  /**
   * Sets the value for display name.
   *
   * @param  displayName  Input parameter.
   */
  void setDisplayName(String displayName);

  /**
   * Sets the value for email address.
   *
   * @param  emailAddress  Input parameter.
   */
  void setEmailAddress(String emailAddress);

  /**
   * Sets the value for first name.
   *
   * @param  firstName  Input parameter.
   */
  void setFirstName(String firstName);

  /**
   * Sets the value for guid.
   *
   * @param  guid  Input parameter.
   */
  void setGuid(String guid);

  /**
   * Sets the value for id.
   *
   * @param  id  Input parameter.
   */
  void setId(Long id);

  /**
   * Sets the value for is employee.
   *
   * @param  isEmployee  Input parameter.
   */
  void setIsEmployee(boolean isEmployee);

  /**
   * Sets the value for last name.
   *
   * @param  lastName  Input parameter.
   */
  void setLastName(String lastName);

  /**
   * Sets the value for middle initial.
   *
   * @param  middleInitial  Input parameter.
   */
  void setMiddleInitial(String middleInitial);

  /**
   * Sets the value for phone number.
   *
   * @param  phoneNumber  Input parameter.
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Sets the value for source directory.
   *
   * @param  sourceDirectory  Input parameter.
   */
  void setSourceDirectory(String sourceDirectory);

  /**
   * Sets the value for updated date.
   *
   * @param  updatedDate  Input parameter.
   */
  void setUpdatedDate(Date updatedDate);

  /**
   * Sets the value for user id.
   *
   * @param  userId  Input parameter.
   */
  void setUserId(String userId);

  /**
   * Sets the value for user type code.
   *
   * @param  userTypeCode  Input parameter.
   */
  void setUserTypeCode(String userTypeCode);

}

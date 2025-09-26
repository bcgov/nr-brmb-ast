/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

import java.util.Date;


/**
 * UserBasicView.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
public class UserBasicView extends BaseObject implements User {

  /** accountName. */
  private String accountName;

  /** displayName. */
  private String displayName;

  /** emailAddress. */
  private String emailAddress;

  /** firstName. */
  private String firstName;

  /** guid. */
  private String guid;

  /** id. */
  private Long id;

  /** isEmployee. */
  private boolean isEmployee;

  /** lastName. */
  private String lastName;

  /** middleInitial. */
  private String middleInitial;

  /** phoneNumber. */
  private String phoneNumber;

  /** sourceDirectory. */
  private String sourceDirectory;

  /** updatedDate. */
  private Date updatedDate;

  /** userId. */
  private String userId;

  /** userTypeCode. */
  private String userTypeCode;


  /** Creates a new UserBasicView object. */
  public UserBasicView() {
    setIsEmployee(false);
  }


  /**
   * getAccountName.
   *
   * @return  The return value.
   */
  @Override
  public String getAccountName() {
    return accountName;
  }


  /**
   * getDisplayName.
   *
   * @return  The return value.
   */
  @Override
  public String getDisplayName() {
    return displayName;
  }


  /**
   * getEmailAddress.
   *
   * @return  The return value.
   */
  @Override
  public String getEmailAddress() {
    return emailAddress;
  }


  /**
   * getFirstName.
   *
   * @return  The return value.
   */
  @Override
  public String getFirstName() {
    return firstName;
  }


  /**
   * getGuid.
   *
   * @return  The return value.
   */
  @Override
  public String getGuid() {
    return guid;
  }


  /**
   * getId.
   *
   * @return  The return value.
   */
  @Override
  public Long getId() {
    return id;
  }


  /**
   * getLastName.
   *
   * @return  The return value.
   */
  @Override
  public String getLastName() {
    return lastName;
  }


  /**
   * getMiddleInitial.
   *
   * @return  The return value.
   */
  @Override
  public String getMiddleInitial() {
    return middleInitial;
  }


  /**
   * getPhoneNumber.
   *
   * @return  The return value.
   */
  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * getSourceDirectory.
   *
   * @return  The return value.
   */
  @Override
  public String getSourceDirectory() {
    return sourceDirectory;
  }


  /**
   * getUpdatedDate.
   *
   * @return  The return value.
   */
  @Override
  public Date getUpdatedDate() {
    return updatedDate;
  }


  /**
   * getUserId.
   *
   * @return  The return value.
   */
  @Override
  public String getUserId() {
    return userId;
  }


  /**
   * getUserTypeCode.
   *
   * @return  The return value.
   */
  @Override
  public String getUserTypeCode() {
    return userTypeCode;
  }

  /**
   * isEmployee.
   *
   * @return  The return value.
   */
  @Override
  public boolean isEmployee() {
    return isEmployee;
  }


  /**
   * Sets the value for account name.
   *
   * @param  value  accountName Input parameter.
   */
  @Override
  public void setAccountName(final String value) {
    this.accountName = value;
  }


  /**
   * Sets the value for display name.
   *
   * @param  value  displayName Input parameter.
   */
  @Override
  public void setDisplayName(final String value) {
    this.displayName = value;
  }


  /**
   * Sets the value for email address.
   *
   * @param  value  emailAddress Input parameter.
   */
  @Override
  public void setEmailAddress(final String value) {
    this.emailAddress = value;
  }


  /**
   * Sets the value for first name.
   *
   * @param  value  firstName Input parameter.
   */
  @Override
  public void setFirstName(final String value) {
    this.firstName = value;
  }


  /**
   * Sets the value for guid.
   *
   * @param  value  guid Input parameter.
   */
  @Override
  public void setGuid(final String value) {
    this.guid = value;
  }


  /**
   * Sets the value for id.
   *
   * @param  value  id Input parameter.
   */
  @Override
  public void setId(final Long value) {
    this.id = value;
  }


  /**
   * Sets the value for is employee.
   *
   * @param  value  isEmployee Input parameter.
   */
  @Override
  public void setIsEmployee(final boolean value) {
    this.isEmployee = value;
  }

  /**
   * Sets the value for last name.
   *
   * @param  value  lastName Input parameter.
   */
  @Override
  public void setLastName(final String value) {
    this.lastName = value;
  }


  /**
   * Sets the value for middle initial.
   *
   * @param  value  middleInitial Input parameter.
   */
  @Override
  public void setMiddleInitial(final String value) {
    this.middleInitial = value;
  }


  /**
   * Sets the value for phone number.
   *
   * @param  value  phoneNumber Input parameter.
   */
  @Override
  public void setPhoneNumber(final String value) {
    this.phoneNumber = value;
  }


  /**
   * Sets the value for source directory.
   *
   * @param  value  sourceDirectory Input parameter.
   */
  @Override
  public void setSourceDirectory(final String value) {
    this.sourceDirectory = value;
  }


  /**
   * Sets the value for updated date.
   *
   * @param  value  updatedDate Input parameter.
   */
  @Override
  public void setUpdatedDate(final Date value) {
    this.updatedDate = value;
  }

  /**
   * Sets the value for user id.
   *
   * @param  value  userId Input parameter.
   */
  @Override
  public void setUserId(final String value) {
    this.userId = value;
  }

  /**
   * Sets the value for user type code.
   *
   * @param  value  userTypeCode Input parameter.
   */
  @Override
  public void setUserTypeCode(final String value) {
    this.userTypeCode = value;
  }

  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append(classnameToString()).append("(");
    result.append("userId=");
    result.append(userId).append(",");
    result.append("userTypeCode=");
    result.append(userTypeCode).append(",");
    result.append("guid=");
    result.append(guid).append(")");

    return result.toString();
  }

  /**
   * generateHashCode.
   *
   * @return  The return value.
   */
  @Override
  protected int generateHashCode() {
    int result = HASH_SEED;
    result = hash(result, getClass().getName());
    result = hash(result, guid);
    result = hash(result, userId);

    return result;
  }
}

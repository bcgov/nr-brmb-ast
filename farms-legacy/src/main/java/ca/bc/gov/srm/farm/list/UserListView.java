/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.list;

import java.util.Date;

import ca.bc.gov.srm.farm.User;


/**
 * UserListView.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
public class UserListView extends BaseListView implements User {

  /** user. */
  private User user = null;

  /**
   * Creates a new UserListView object.
   *
   * @param  value  user Input parameter to initialize class.
   */
  public UserListView(final User value) {
    this.user = value;
  }

  /**
   * getAccountName.
   *
   * @return  The return value.
   */
  @Override
  public String getAccountName() {
    return user.getAccountName();
  }

  /**
   * getDisplayName.
   *
   * @return  The return value.
   */
  @Override
  public String getDisplayName() {
    return user.getDisplayName();
  }

  /**
   * getEmailAddress.
   *
   * @return  The return value.
   */
  @Override
  public String getEmailAddress() {
    return user.getEmailAddress();
  }

  /**
   * getFirstName.
   *
   * @return  The return value.
   */
  @Override
  public String getFirstName() {
    return user.getFirstName();
  }

  /**
   * getGuid.
   *
   * @return  The return value.
   */
  @Override
  public String getGuid() {
    return user.getGuid();
  }

  /**
   * getId.
   *
   * @return  The return value.
   */
  @Override
  public Long getId() {
    return user.getId();
  }

  /**
   * getLabel.
   *
   * @return  The return value.
   */
  @Override
  public String getLabel() {
    return user.getAccountName();
  }

  /**
   * getLastName.
   *
   * @return  The return value.
   */
  @Override
  public String getLastName() {
    return user.getLastName();
  }

  /**
   * getMiddleInitial.
   *
   * @return  The return value.
   */
  @Override
  public String getMiddleInitial() {
    return user.getMiddleInitial();
  }

  /**
   * getPhoneNumber.
   *
   * @return  The return value.
   */
  @Override
  public String getPhoneNumber() {
    return user.getPhoneNumber();
  }

  /**
   * getSourceDirectory.
   *
   * @return  The return value.
   */
  @Override
  public String getSourceDirectory() {
    return user.getSourceDirectory();
  }

  /**
   * getUpdatedDate.
   *
   * @return  The return value.
   */
  @Override
  public Date getUpdatedDate() {
    return user.getUpdatedDate();
  }

  /**
   * getUserId.
   *
   * @return  The return value.
   */
  @Override
  public String getUserId() {
    return user.getUserId();
  }

  /**
   * getUserTypeCode.
   *
   * @return  The return value.
   */
  @Override
  public String getUserTypeCode() {
    return user.getUserTypeCode();
  }

  /**
   * getValue.
   *
   * @return  The return value.
   */
  @Override
  public String getValue() {
    if (getId() == null) {
      return null;
    }
    return Long.toString(getId());
  }

  /**
   * isEmployee.
   *
   * @return  The return value.
   */
  @Override
  public boolean isEmployee() {
    return user.isEmployee();
  }

  /**
   * Sets the value for account name.
   *
   * @param  value  accountName Input parameter.
   */
  @Override
  public void setAccountName(final String value) {
    // don't allow
  }

  /**
   * Sets the value for display name.
   *
   * @param  value  displayName Input parameter.
   */
  @Override
  public void setDisplayName(final String value) {
    // don't allow
  }

  /**
   * Sets the value for email address.
   *
   * @param  value  emailAddress Input parameter.
   */
  @Override
  public void setEmailAddress(final String value) {
    // don't allow
  }

  /**
   * Sets the value for first name.
   *
   * @param  value  firstName Input parameter.
   */
  @Override
  public void setFirstName(final String value) {
    // don't allow
  }

  /**
   * Sets the value for guid.
   *
   * @param  value  guid Input parameter.
   */
  @Override
  public void setGuid(final String value) {
    // don't allow
  }

  /**
   * Sets the value for id.
   *
   * @param  value  id Input parameter.
   */
  @Override
  public void setId(final Long value) {
    // don't allow
  }

  /**
   * Sets the value for is employee.
   *
   * @param  value  isEmployee Input parameter.
   */
  @Override
  public void setIsEmployee(final boolean value) {
    // don't allow
  }

  /**
   * Sets the value for last name.
   *
   * @param  value  lastName Input parameter.
   */
  @Override
  public void setLastName(final String value) {
    // don't allow
  }

  /**
   * Sets the value for middle initial.
   *
   * @param  value  middleInitial Input parameter.
   */
  @Override
  public void setMiddleInitial(final String value) {
    // don't allow
  }

  /**
   * Sets the value for phone number.
   *
   * @param  value  phoneNumber Input parameter.
   */
  @Override
  public void setPhoneNumber(final String value) {
    // don't allow
  }

  /**
   * Sets the value for source directory.
   *
   * @param  value  sourceDirectory Input parameter.
   */
  @Override
  public void setSourceDirectory(final String value) {
    // don't allow
  }

  /**
   * Sets the value for updated date.
   *
   * @param  value  updatedDate Input parameter.
   */
  @Override
  public void setUpdatedDate(final Date value) {
    // don't allow
  }

  /**
   * Sets the value for user id.
   *
   * @param  value  userId Input parameter.
   */
  @Override
  public void setUserId(final String value) {
    // don't allow
  }

  /**
   * Sets the value for user type code.
   *
   * @param  value  userTypeCode Input parameter.
   */
  @Override
  public void setUserTypeCode(final String value) {
    // don't allow
  }

}

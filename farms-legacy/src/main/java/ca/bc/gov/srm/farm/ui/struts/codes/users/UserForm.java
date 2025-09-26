/**
 * Copyright (c) 2011, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.users;

import java.util.List;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.FarmUser;

public class UserForm extends ValidatorForm {

  private static final long serialVersionUID = 2341666418825962316L;
  private List<FarmUser> users;
  private int numUsers;
  private String userGuid;

  private FarmUser user;
  private boolean isVerifier;
  private boolean isDeleted;

  private Integer revisionCount;

  public List<FarmUser> getUsers() {
    return users;
  }

  public void setUsers(List<FarmUser> users) {
    this.users = users;
  }

  public int getNumUsers() {
    return numUsers;
  }

  public void setNumUsers(int numUsers) {
    this.numUsers = numUsers;
  }

  public String getUserGuid() {
    return userGuid;
  }

  public void setUserGuid(String userGuid) {
    this.userGuid = userGuid;
  }

  public FarmUser getUser() {
    return user;
  }

  public void setUser(FarmUser user) {
    this.user = user;
  }

  public boolean isVerifier() {
    return isVerifier;
  }

  public void setVerifier(boolean isVerifier) {
    this.isVerifier = isVerifier;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

}

/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain;

import java.util.Date;

public class FarmUser {

  private Integer userId;
  private String userGuid;
  private String sourceDirectory;
  private String accountName;
  private String emailAddress;
  private Boolean verifierInd;
  private Boolean deletedInd;

  private Integer revisionCount;
  
  private Date whenCreated;
  private String whoCreated;
  private Date whenUpdated;
  private String whoUpdated;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUserGuid() {
    return userGuid;
  }

  public void setUserGuid(String userGuid) {
    this.userGuid = userGuid;
  }

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  public void setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public Boolean getVerifierInd() {
    return verifierInd;
  }

  public void setVerifierInd(Boolean verifierInd) {
    this.verifierInd = verifierInd;
  }

  public Boolean getDeletedInd() {
    return deletedInd;
  }

  public void setDeletedInd(Boolean deletedInd) {
    this.deletedInd = deletedInd;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  public Date getWhenCreated() {
    return whenCreated;
  }

  public void setWhenCreated(Date whenCreated) {
    this.whenCreated = whenCreated;
  }

  public String getWhoCreated() {
    return whoCreated;
  }

  public void setWhoCreated(String whoCreated) {
    this.whoCreated = whoCreated;
  }

  public Date getWhenUpdated() {
    return whenUpdated;
  }

  public void setWhenUpdated(Date whenUpdated) {
    this.whenUpdated = whenUpdated;
  }

  public String getWhoUpdated() {
    return whoUpdated;
  }

  public void setWhoUpdated(String whoUpdated) {
    this.whoUpdated = whoUpdated;
  }

  @Override
  public String toString() {
    return "FarmUser [userId=" + userId + ", userGuid=" + userGuid + ", sourceDirectory=" + sourceDirectory + ", accountName=" + accountName
        + ", emailAddress=" + emailAddress + ", verifierInd=" + verifierInd + ", deletedInd=" + deletedInd + ", revisionCount=" + revisionCount
        + ", whenCreated=" + whenCreated + ", whoCreated=" + whoCreated + ", whenUpdated=" + whenUpdated + ", whoUpdated=" + whoUpdated + "]";
  }

}

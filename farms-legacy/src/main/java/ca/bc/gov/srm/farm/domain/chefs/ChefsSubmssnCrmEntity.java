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
package ca.bc.gov.srm.farm.domain.chefs;

import java.util.Date;

public class ChefsSubmssnCrmEntity {

  private Integer chefSubmssnCrmEntityId;
  private String CrmEntityGuid;
  private String crmEntityTypeCode;
  private Integer chefSubmissionId;
  private Integer revisionCount;
  private Date createdDate;
  private Date updatedDate;

  public Integer getChefSubmssnCrmEntityId() {
    return chefSubmssnCrmEntityId;
  }

  public void setChefSubmssnCrmEntityId(Integer chefSubmssnCrmEntityId) {
    this.chefSubmssnCrmEntityId = chefSubmssnCrmEntityId;
  }

  public String getCrmEntityGuid() {
    return CrmEntityGuid;
  }

  public void setCrmEntityGuid(String crmEntityGuid) {
    CrmEntityGuid = crmEntityGuid;
  }

  public String getCrmEntityTypeCode() {
    return crmEntityTypeCode;
  }

  public void setCrmEntityTypeCode(String crmEntityTypeCode) {
    this.crmEntityTypeCode = crmEntityTypeCode;
  }

  public Integer getChefSubmissionId() {
    return chefSubmissionId;
  }

  public void setChefSubmissionId(Integer chefSubmissionId) {
    this.chefSubmissionId = chefSubmissionId;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Override
  public String toString() {
    return "ChefSubmssnCrmEntity [chefSubmssnCrmEntityId=" + chefSubmssnCrmEntityId + ", CrmEntityGuid=" + CrmEntityGuid + ", crmEntityTypeCode="
        + crmEntityTypeCode + ", chefSubmissionId=" + chefSubmissionId + ", revisionCount=" + revisionCount + ", createdDate=" + createdDate
        + ", updatedDate=" + updatedDate + "]";
  }

}

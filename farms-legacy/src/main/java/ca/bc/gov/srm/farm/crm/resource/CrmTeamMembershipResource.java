/**
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author hwang
 */
public class CrmTeamMembershipResource {

    @JsonProperty("ownerid")
    private String ownerId;

    @JsonProperty("domainname")
    private String domainName;

    @JsonProperty("azureactivedirectoryobjectid")
    private String azureActiveDirectoryObjectId;

    @JsonProperty("systemuserid")
    private String systemUserId;

    @JsonProperty("fullname")
    private String fullName;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getAzureActiveDirectoryObjectId() {
        return azureActiveDirectoryObjectId;
    }

    public void setAzureActiveDirectoryObjectId(String azureActiveDirectoryObjectId) {
        this.azureActiveDirectoryObjectId = azureActiveDirectoryObjectId;
    }

    public String getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(String systemUserId) {
        this.systemUserId = systemUserId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "CrmTeamMembershipResource [ownerId=" + ownerId + ", domainName=" + domainName
                + ", azureActiveDirectoryObjectId=" + azureActiveDirectoryObjectId + ", systemUserId=" + systemUserId
                + ", fullName=" + fullName + "]";
    }
}

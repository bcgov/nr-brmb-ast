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
public class CrmTeamResource extends CrmResource {

    @JsonProperty("ownerid")
    private String ownerId;

    private String name;

    @JsonProperty("teamid")
    private String teamId;

    @JsonProperty("teamtype")
    private Integer teamType;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Integer getTeamType() {
        return teamType;
    }

    public void setTeamType(Integer teamType) {
        this.teamType = teamType;
    }

    @Override
    public String toString() {
        return "CrmTeamResource [ownerId=" + ownerId + ", name=" + name + ", teamId=" + teamId + ", teamType="
                + teamType + "]";
    }
}

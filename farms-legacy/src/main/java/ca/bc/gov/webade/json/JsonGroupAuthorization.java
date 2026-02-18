package ca.bc.gov.webade.json;

import java.util.Date;

public class JsonGroupAuthorization {
    private String profileName;
    private String profileDescription;
    private Date effectiveDate;
    private Date expiryDate;
    private String groupName;
    private Boolean enabled;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}

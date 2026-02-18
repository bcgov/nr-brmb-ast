package ca.bc.gov.webade.json;

import java.util.Date;
import java.util.List;

public class JsonProfile {
    private String name;
    private String description;
    private Boolean secureByOrganization;
    private List<String> availibleTo;
    private Date effectiveDate;
    private Date expiryDate;
    private List<JsonProfileRole> profileRoles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSecureByOrganization() {
        return secureByOrganization;
    }

    public void setSecureByOrganization(Boolean secureByOrganization) {
        this.secureByOrganization = secureByOrganization;
    }

    public List<String> getAvailibleTo() {
        return availibleTo;
    }

    public void setAvailibleTo(List<String> availibleTo) {
        this.availibleTo = availibleTo;
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

    public List<JsonProfileRole> getProfileRoles() {
        return profileRoles;
    }

    public void setProfileRoles(List<JsonProfileRole> profileRoles) {
        this.profileRoles = profileRoles;
    }
}

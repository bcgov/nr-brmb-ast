package ca.bc.gov.webade.json;

import java.util.List;

public class JsonApplicationConfiguration {
    private String applicationAcronym;
    private Integer custodianNumber;
    private String applicationName;
    private String applicationDescription;
    private String applicationObjectPrefix;
    private Boolean enabledInd;
    private String distributeTypeCd;
    private Boolean managementEnabledInd;
    private String applicationVersion;
    private String reportedWebadeVersion;
    private List<JsonAction> actions;
    private List<JsonRole> roles;
    private List<JsonApplicationPreference> applicationPreferences;
    private List<JsonProfile> profiles;
    private List<JsonGroupAuthorization> groupAuthorizations;

    public String getApplicationAcronym() {
        return applicationAcronym;
    }

    public void setApplicationAcronym(String applicationAcronym) {
        this.applicationAcronym = applicationAcronym;
    }

    public Integer getCustodianNumber() {
        return custodianNumber;
    }

    public void setCustodianNumber(Integer custodianNumber) {
        this.custodianNumber = custodianNumber;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public String getApplicationObjectPrefix() {
        return applicationObjectPrefix;
    }

    public void setApplicationObjectPrefix(String applicationObjectPrefix) {
        this.applicationObjectPrefix = applicationObjectPrefix;
    }

    public Boolean getEnabledInd() {
        return enabledInd;
    }

    public void setEnabledInd(Boolean enabledInd) {
        this.enabledInd = enabledInd;
    }

    public String getDistributeTypeCd() {
        return distributeTypeCd;
    }

    public void setDistributeTypeCd(String distributeTypeCd) {
        this.distributeTypeCd = distributeTypeCd;
    }

    public Boolean getManagementEnabledInd() {
        return managementEnabledInd;
    }

    public void setManagementEnabledInd(Boolean managementEnabledInd) {
        this.managementEnabledInd = managementEnabledInd;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getReportedWebadeVersion() {
        return reportedWebadeVersion;
    }

    public void setReportedWebadeVersion(String reportedWebadeVersion) {
        this.reportedWebadeVersion = reportedWebadeVersion;
    }

    public List<JsonAction> getActions() {
        return actions;
    }

    public void setActions(List<JsonAction> actions) {
        this.actions = actions;
    }

    public List<JsonRole> getRoles() {
        return roles;
    }

    public void setRoles(List<JsonRole> roles) {
        this.roles = roles;
    }

    public List<JsonApplicationPreference> getApplicationPreferences() {
        return applicationPreferences;
    }

    public void setApplicationPreferences(List<JsonApplicationPreference> applicationPreferences) {
        this.applicationPreferences = applicationPreferences;
    }

    public List<JsonProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<JsonProfile> profiles) {
        this.profiles = profiles;
    }

    public List<JsonGroupAuthorization> getGroupAuthorizations() {
        return groupAuthorizations;
    }

    public void setGroupAuthorizations(List<JsonGroupAuthorization> groupAuthorizations) {
        this.groupAuthorizations = groupAuthorizations;
    }
}

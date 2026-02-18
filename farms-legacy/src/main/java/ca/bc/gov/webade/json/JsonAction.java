package ca.bc.gov.webade.json;

public class JsonAction {
    private String name;
    private String description;
    private Boolean privilegedInd;

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

    public Boolean getPrivilegedInd() {
        return privilegedInd;
    }

    public void setPrivilegedInd(Boolean privilegedInd) {
        this.privilegedInd = privilegedInd;
    }
}

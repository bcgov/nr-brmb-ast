package ca.bc.gov.webade.json;

import java.util.List;

public class JsonRole {
    private String name;
    private String description;
    private List<String> actionNames;

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

    public List<String> getActionNames() {
        return actionNames;
    }

    public void setActionNames(List<String> actionNames) {
        this.actionNames = actionNames;
    }
}

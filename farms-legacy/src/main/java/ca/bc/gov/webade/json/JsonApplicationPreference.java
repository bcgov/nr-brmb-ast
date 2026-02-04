package ca.bc.gov.webade.json;

public class JsonApplicationPreference {
    private String subTypeCode;
    private String setName;
    private String name;
    private String description;
    private String dataTypeCode;
    private Boolean sensitiveDataInd;
    private String value;

    public String getSubTypeCode() {
        return subTypeCode;
    }

    public void setSubTypeCode(String subTypeCode) {
        this.subTypeCode = subTypeCode;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

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

    public String getDataTypeCode() {
        return dataTypeCode;
    }

    public void setDataTypeCode(String dataTypeCode) {
        this.dataTypeCode = dataTypeCode;
    }

    public Boolean getSensitiveDataInd() {
        return sensitiveDataInd;
    }

    public void setSensitiveDataInd(Boolean sensitiveDataInd) {
        this.sensitiveDataInd = sensitiveDataInd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

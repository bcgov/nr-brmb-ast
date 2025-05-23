package ca.bc.gov.farms.persistence.v1.dto;

import java.io.Serializable;

public class ImportLogDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long importLogId;
    private String logMessage;
    private Long importVersionId;
    private Long programYearVersionId;

    public Long getImportLogId() {
        return importLogId;
    }

    public void setImportLogId(Long importLogId) {
        this.importLogId = importLogId;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public Long getImportVersionId() {
        return importVersionId;
    }

    public void setImportVersionId(Long importVersionId) {
        this.importVersionId = importVersionId;
    }

    public Long getProgramYearVersionId() {
        return programYearVersionId;
    }

    public void setProgramYearVersionId(Long programYearVersionId) {
        this.programYearVersionId = programYearVersionId;
    }
}

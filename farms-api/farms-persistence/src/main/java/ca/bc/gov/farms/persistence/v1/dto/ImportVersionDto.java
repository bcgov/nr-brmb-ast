package ca.bc.gov.farms.persistence.v1.dto;

import java.io.Serializable;
import java.util.Date;

public class ImportVersionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long importVersionId;
    private String importedByUser;
    private String description;
    private Date importControlFileDate;
    private String importControlFileInformation;
    private Date importDate;
    private String importFileName;
    private String importFilePassword;
    private byte[] importFile;
    private String stagingAuditInformation;
    private String importAuditInformation;
    private String lastStatusMessage;
    private Date lastStatusDate;
    private String importStateCode;
    private String importClassCode;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public Long getImportVersionId() {
        return importVersionId;
    }

    public void setImportVersionId(Long importVersionId) {
        this.importVersionId = importVersionId;
    }

    public String getImportedByUser() {
        return importedByUser;
    }

    public void setImportedByUser(String importedByUser) {
        this.importedByUser = importedByUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getImportControlFileDate() {
        return importControlFileDate;
    }

    public void setImportControlFileDate(Date importControlFileDate) {
        this.importControlFileDate = importControlFileDate;
    }

    public String getImportControlFileInformation() {
        return importControlFileInformation;
    }

    public void setImportControlFileInformation(String importControlFileInformation) {
        this.importControlFileInformation = importControlFileInformation;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getImportFileName() {
        return importFileName;
    }

    public void setImportFileName(String importFileName) {
        this.importFileName = importFileName;
    }

    public String getImportFilePassword() {
        return importFilePassword;
    }

    public void setImportFilePassword(String importFilePassword) {
        this.importFilePassword = importFilePassword;
    }

    public byte[] getImportFile() {
        return importFile;
    }

    public void setImportFile(byte[] importFile) {
        this.importFile = importFile;
    }

    public String getStagingAuditInformation() {
        return stagingAuditInformation;
    }

    public void setStagingAuditInformation(String stagingAuditInformation) {
        this.stagingAuditInformation = stagingAuditInformation;
    }

    public String getImportAuditInformation() {
        return importAuditInformation;
    }

    public void setImportAuditInformation(String importAuditInformation) {
        this.importAuditInformation = importAuditInformation;
    }

    public String getLastStatusMessage() {
        return lastStatusMessage;
    }

    public void setLastStatusMessage(String lastStatusMessage) {
        this.lastStatusMessage = lastStatusMessage;
    }

    public Date getLastStatusDate() {
        return lastStatusDate;
    }

    public void setLastStatusDate(Date lastStatusDate) {
        this.lastStatusDate = lastStatusDate;
    }

    public String getImportStateCode() {
        return importStateCode;
    }

    public void setImportStateCode(String importStateCode) {
        this.importStateCode = importStateCode;
    }

    public String getImportClassCode() {
        return importClassCode;
    }

    public void setImportClassCode(String importClassCode) {
        this.importClassCode = importClassCode;
    }

    public Integer getRevisionCount() {
        return revisionCount;
    }

    public void setRevisionCount(Integer revisionCount) {
        this.revisionCount = revisionCount;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

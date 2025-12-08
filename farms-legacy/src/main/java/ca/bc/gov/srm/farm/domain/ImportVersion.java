/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;

/**
 * ImportVersion defines the number used to identify the batch in which one or
 * more ProgramYear VERSIONs were processed.
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:06:53 PM
 */
public final class ImportVersion implements Serializable {
  
  private static final long serialVersionUID = -1196126656579078558L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private ReferenceScenario referenceScenario;

  /** importVersionId is a surrogate unique identifier for ImportVersions. */
  private Integer importVersionId;

  /**
   * importedByUser identifies the name of the user who initiated the import
   * process for the ImportVersion.
   */
  private String importedByUser;

  /** description is a note provided by the user about the import process. */
  private String description;

  /**
   * auditInfo captures logging information about the import process such as
   * exceptions or other relevent import comments.
   */
  private String auditInfo;

  /** importFileName identifies the name of the uploaded import file. */
  private String importFileName;

  /**
   * importControlFileDate identifies the federal extract date for the import
   * file.
   */
  private Date importControlFileDate;

  /**
   * importControlFileInfo describes information about the control file
   * information such as rowcount per extract file number.
   */
  private String importControlFileInfo;

  /**
   * importDate is the date the Agristability data was imported into the system.
   */
  private Date importDate;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  private String importStateCode;

  private String importStateDescription;
  
  private String importClassCode;

  private String importClassDescription;
  
  private String lastStatusMessage;
  
  private Date lastStatusDate;
  
  /** Is this the most recent import version with the same code. */
  private Boolean isLatestOfClass;


  /** Constructor. */
  public ImportVersion() {
  }


  /**
   * importVersionId is a surrogate unique identifier for ImportVersions.
   *
   * @return  Integer
   */
  public Integer getImportVersionId() {
    return importVersionId;
  }

  /**
   * importVersionId is a surrogate unique identifier for ImportVersions.
   *
   * @param  newVal  The new value for this property
   */
  public void setImportVersionId(final Integer newVal) {
    importVersionId = newVal;
  }

  /**
   * ImportedByUser identifies the name of the user who initiated the import
   * process for the ImportVersion.
   *
   * @return  String
   */
  public String getImportedByUser() {
    return importedByUser;
  }

  /**
   * ImportedByUser identifies the name of the user who initiated the import
   * process for the ImportVersion.
   *
   * @param  newVal  The new value for this property
   */
  public void setImportedByUser(final String newVal) {
    importedByUser = newVal;
  }

  /**
   * Description is a note provided by the user about the import process.
   *
   * @return  String
   */
  public String getDescription() {
    return description;
  }

  /**
   * Description is a note provided by the user about the import process.
   *
   * @param  newVal  The new value for this property
   */
  public void setDescription(final String newVal) {
    description = newVal;
  }

  /**
   * AuditInfo captures logging information about the import process such as
   * exceptions or other relevent import comments.
   *
   * @return  String
   */
  public String getAuditInfo() {
    return auditInfo;
  }

  /**
   * AuditInfo captures logging information about the import process such as
   * exceptions or other relevent import comments.
   *
   * @param  newVal  The new value for this property
   */
  public void setAuditInfo(final String newVal) {
    auditInfo = newVal;
  }

  /**
   * ImportFileName identifies the name of the uploaded import file.
   *
   * @return  String
   */
  public String getImportFileName() {
    return importFileName;
  }

  /**
   * ImportFileName identifies the name of the uploaded import file.
   *
   * @param  newVal  The new value for this property
   */
  public void setImportFileName(final String newVal) {
    importFileName = newVal;
  }

  /**
   * ImportControlFileDate identifies the federal extract date for the import
   * file.
   *
   * @return  Date
   */
  public Date getImportControlFileDate() {
    return importControlFileDate;
  }

  /**
   * ImportControlFileDate identifies the federal extract date for the import
   * file.
   *
   * @param  newVal  The new value for this property
   */
  public void setImportControlFileDate(final Date newVal) {
    importControlFileDate = newVal;
  }

  /**
   * ImportControlFileInfo describes information about the control file
   * information such as rowcount per extract file number.
   *
   * @return  String
   */
  public String getImportControlFileInfo() {
    return importControlFileInfo;
  }

  /**
   * ImportControlFileInfo describes information about the control file
   * information such as rowcount per extract file number.
   *
   * @param  newVal  The new value for this property
   */
  public void setImportControlFileInfo(final String newVal) {
    importControlFileInfo = newVal;
  }

  /**
   * ImportDate is the date the Agristability data was imported into the system.
   *
   * @return  Date
   */
  public Date getImportDate() {
    return importDate;
  }

  /**
   * ImportDate is the date the Agristability data was imported into the system.
   *
   * @param  newVal  The new value for this property
   */
  public void setImportDate(final Date newVal) {
    importDate = newVal;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  Integer
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final Integer newVal) {
    revisionCount = newVal;
  }


  /**
   * @return  the importStateCode
   */
  public String getImportStateCode() {
    return importStateCode;
  }


  /**
   * @param  importStateCode  the importStateCode to set
   */
  public void setImportStateCode(String importStateCode) {
    this.importStateCode = importStateCode;
  }


  /**
   * @return  the importStateDescription
   */
  public String getImportStateDescription() {
    return ImportStateCodes.translateImportStateDescription(importClassCode, importStateCode, importStateDescription);
  }


  /**
   * @param  importStateDescription  the importStateDescription to set
   */
  public void setImportStateDescription(String importStateDescription) {
    this.importStateDescription = importStateDescription;
  }
  
  
  /**
   * @return  the importClassCode
   */
  public String getImportClassCode() {
    return importClassCode;
  }


  /**
   * @param  importClassCode  the importClassCode to set
   */
  public void setImportClassCode(String importClassCode) {
    this.importClassCode = importClassCode;
  }


  /**
   * @return  the importClassDescription
   */
  public String getImportClassDescription() {
    return importClassDescription;
  }


  /**
   * @param  importClassDescription  the importClassDescription to set
   */
  public void setImportClassDescription(String importClassDescription) {
    this.importClassDescription = importClassDescription;
  }
  
  

  /**
   * @return the lastStatusDate
   */
  public Date getLastStatusDate() {
    return lastStatusDate;
  }


  /**
   * @param lastStatusDate the lastStatusDate to set
   */
  public void setLastStatusDate(Date lastStatusDate) {
    this.lastStatusDate = lastStatusDate;
  }


  /**
   * @return the lastStatusMessage
   */
  public String getLastStatusMessage() {
    return lastStatusMessage;
  }


  /**
   * @param lastStatusMessage the lastStatusMessage to set
   */
  public void setLastStatusMessage(String lastStatusMessage) {
    this.lastStatusMessage = lastStatusMessage;
  }


  /**
   * @return the referenceScenario
   */
  public ReferenceScenario getReferenceScenario() {
    return referenceScenario;
  }


  /**
   * @param referenceScenario the referenceScenario to set the value to
   */
  public void setReferenceScenario(ReferenceScenario referenceScenario) {
    this.referenceScenario = referenceScenario;
  }


  /**
   * Gets isLatestOfClass
   *
   * @return the isLatestOfClass
   */
  public Boolean getIsLatestOfClass() {
    return isLatestOfClass;
  }


  /**
   * Sets isLatestOfClass
   *
   * @param isLatestOfClass the isLatestOfClass to set
   */
  public void setIsLatestOfClass(Boolean isLatestOfClass) {
    this.isLatestOfClass = isLatestOfClass;
  }


  /**
   * @return boolean
   */
  @JsonIgnore
  public boolean isCra() {
    return ImportClassCodes.isCra(importClassCode);
  }
  
  /**
   * @return boolean
   */
  @JsonIgnore
  public boolean isBpu() {
    return ImportClassCodes.isBpu(importClassCode);
  }

  /**
   * @return boolean
   */
  @JsonIgnore
  public boolean isIvpr() {
    return ImportClassCodes.isIvpr(importClassCode);
  }

  /**
   * @return boolean
   */
  @JsonIgnore
  public boolean isFmv() {
    return ImportClassCodes.isFmv(importClassCode);
  }
  
  /**
   * @return boolean
   */
  @JsonIgnore
  public boolean isEnrolment() {
    return ImportClassCodes.isEnrolment(importClassCode);
  }
  
  /**
   * @return boolean
   */
  @JsonIgnore
  public boolean isTransfer() {
    return ImportClassCodes.isTransfer(importClassCode);
  }
  
  /**
   * @return boolean
   */
  @JsonIgnore
  public boolean isTipReport() {
    return ImportClassCodes.isTipReport(importClassCode);
  }
  
  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer referenceScenarioId = null;
    if(referenceScenario != null) {
      referenceScenarioId = referenceScenario.getScenarioId();
    }

    return "ImportVersion"+"\n"+
    "\t referenceScenario : "+referenceScenarioId+
    "\t importVersionId : "+importVersionId+"\n"+
    "\t importedByUser : "+importedByUser+"\n"+
    "\t description : "+description+"\n"+
    "\t auditInfo : "+auditInfo+"\n"+
    "\t importFileName : "+importFileName+"\n"+
    "\t importControlFileDate : "+importControlFileDate+"\n"+
    "\t importControlFileInfo : "+importControlFileInfo+"\n"+
    "\t importDate : "+importDate+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t importStateCode : "+importStateCode+"\n"+
    "\t importStateDescription : "+importStateDescription+"\n"+
    "\t importClassCode : "+importClassCode+"\n"+
    "\t importClassDescription : "+importClassDescription+"\n"+
    "\t isLatestOfClass : "+isLatestOfClass+"\n";
  }
}

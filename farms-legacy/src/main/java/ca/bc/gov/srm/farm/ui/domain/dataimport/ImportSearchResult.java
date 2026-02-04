package ca.bc.gov.srm.farm.ui.domain.dataimport;

import java.util.Date;

import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;


/**
 * Search results bean used by screen 250.
 */
public class ImportSearchResult {

  private String importVersionId;

  private String importedBy;

  private String description;

  private String stateCode;

  private String stateDescription;
  
  private String classCode;

  private String classDescription;

  private Date updateDate;

  /**
   * @return  the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param  value  the description to set
   */
  public void setDescription(String value) {

    if (value != null) {
      this.description = value.trim();
    } else {
      this.description = null;
    }
  }

  /**
   * @return  the importedBy
   */
  public String getImportedBy() {
    return importedBy;
  }

  /**
   * @param  importedBy  the importedBy to set
   */
  public void setImportedBy(String importedBy) {
    this.importedBy = importedBy;
  }

  /**
   * @return  the stateCode
   */
  public String getStateCode() {
    return stateCode;
  }

  /**
   * @param  stateCode  the stateCode to set
   */
  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  /**
   * @return  the stateDescription
   */
  public String getStateDescription() {
    return ImportStateCodes.translateImportStateDescription(classCode, stateCode, stateDescription);
  }

  /**
   * @param  stateDescription  the stateDescription to set
   */
  public void setStateDescription(String stateDescription) {
    this.stateDescription = stateDescription;
  }

  /**
   * @return  the updateDate
   */
  public Date getUpdateDate() {
    return updateDate;
  }

  /**
   * @param  updateDate  the updateDate to set
   */
  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  /**
   * @return  the importVersionId
   */
  public String getImportVersionId() {
    return importVersionId;
  }

  /**
   * @param  importVersionId  the importVersionId to set
   */
  public void setImportVersionId(String importVersionId) {
    this.importVersionId = importVersionId;
  }

	/**
	 * @return the classCode
	 */
	public String getClassCode() {
		return classCode;
	}

	/**
	 * @param classCode the classCode to set
	 */
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	/**
	 * @return the classDescription
	 */
	public String getClassDescription() {
		return classDescription;
	}

	/**
	 * @param classDescription the classDescription to set
	 */
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}
}

package ca.bc.gov.srm.farm.ui.domain.dataimport;

/**
 * Bean used by screen 230.
 */
public class CommodityCode {

  private String table;

  private String code;

  private String newDescription;

  private String oldDescription;

  private String errorMessage;


  /**
   * @return  the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param  errorMessage  the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * @return  the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param  code  the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return  the newDescription
   */
  public String getNewDescription() {
    return newDescription;
  }

  /**
   * @param  newDescription  the newDescription to set
   */
  public void setNewDescription(String newDescription) {
    this.newDescription = newDescription;
  }

  /**
   * @return  the oldDescription
   */
  public String getOldDescription() {
    return oldDescription;
  }

  /**
   * @param  oldDescription  the oldDescription to set
   */
  public void setOldDescription(String oldDescription) {
    this.oldDescription = oldDescription;
  }

  /**
   * @return  the table
   */
  public String getTable() {
    return table;
  }

  /**
   * @param  table  the table to set
   */
  public void setTable(String table) {
    this.table = table;
  }

}

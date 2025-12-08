package ca.bc.gov.srm.farm.ui.domain.dataimport;

import ca.bc.gov.srm.farm.domain.ImportVersion;

import java.util.ArrayList;
import java.util.List;


/**
 * Bean used by screen 210.
 */
public class StagingResults {

  private ImportVersion importVersion;

  private int numberOfItems;

  private List<?> errors = new ArrayList<>();

  private List<?> warnings = new ArrayList<>();

  /**
   * @return  the errors
   */
  public List<?> getErrors() {
    return errors;
  }

  /**
   * @param  errors  the errors to set
   */
  public void setErrors(List<?> errors) {
    this.errors = errors;
  }

  /**
   * @return  the numberOfItems
   */
  public int getNumberOfItems() {
    return numberOfItems;
  }

  /**
   * @param  numberOfItems  the numberOfItems to set
   */
  public void setNumberOfItems(int numberOfItems) {
    this.numberOfItems = numberOfItems;
  }

  /**
   * @return  the warnings
   */
  public List<?> getWarnings() {
    return warnings;
  }

  /**
   * @param  warnings  the warnings to set
   */
  public void setWarnings(List<?> warnings) {
    this.warnings = warnings;
  }

  /**
   * @return  the importVersion
   */
  public ImportVersion getImportVersion() {
    return importVersion;
  }

  /**
   * @param  importVersion  the importVersion to set
   */
  public void setImportVersion(ImportVersion importVersion) {
    this.importVersion = importVersion;
  }

  /**
   * There's no "getSize()" for a list, so JSTL can't handle it.
   *
   * @return  the numberOfErrors
   */
  public int getNumberOfErrors() {
    return errors.size();
  }

  /**
   * @return  the numberOfWarnings
   */
  public int getNumberOfWarnings() {
    return warnings.size();
  }
}

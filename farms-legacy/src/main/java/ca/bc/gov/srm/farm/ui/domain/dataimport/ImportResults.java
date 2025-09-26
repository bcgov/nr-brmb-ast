package ca.bc.gov.srm.farm.ui.domain.dataimport;

import ca.bc.gov.srm.farm.domain.ImportVersion;

import java.util.ArrayList;
import java.util.List;


/**
 * Bean used by screen 230.
 */
public class ImportResults {

  private ImportVersion importVersion;

  private List commodities = new ArrayList();
  
  private List productionUnits = new ArrayList();

  private List accounts = new ArrayList();

  private List<FileLineMessage> errors = new ArrayList<>();
  
  private List<FileLineMessage> warnings = new ArrayList<>();
  
  /** only for FMV and BPU imports */
  private int numberOfItems;
  
  /** only for FMV and BPU imports */
  private int numberOfAdditions;
  
  /** only for FMV and BPU imports */
  private int numberOfUpdates;

  /** only for BPU imports */
  private int numberOfValueUpdates;
  

  /**
   * There's no "getSize()" for a list, so JSTL can't handle it.
   *
   * @return  the getNumberOfCommodities
   */
  public int getNumberOfCommodities() {
    return commodities.size();
  }
  
  /**
   * @return  the getNumberOfProductionUnits
   */
  public int getNumberOfProductionUnits() {
    return productionUnits.size();
  }

  /**
   * @return  the getNumberOfAccounts
   */
  public int getNumberOfAccounts() {
    return accounts.size();
  }

  /**
   * @return  the accounts
   */
  public List getAccounts() {
    return accounts;
  }

  /**
   * @param  accounts  the accounts to set
   */
  public void setAccounts(List accounts) {
    this.accounts = accounts;
  }

  /**
   * @return  the commodities
   */
  public List getCommodities() {
    return commodities;
  }

  /**
   * @param  commodities  the commodities to set
   */
  public void setCommodities(List commodities) {
    this.commodities = commodities;
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
   * @return  the startupError
   */
  public boolean getHasTopLevelErrors() {
    return errors.size() > 0;
  }
  
  /**
   * @return  the errors
   */
  public List<FileLineMessage> getErrors() {
    return errors;
  }

  /**
   * @param  errors  the errors to set
   */
  public void setErrors(List<FileLineMessage> errors) {
    this.errors = errors;
  }

  /**
   * @return  the startupError
   */
  public boolean getHasTopLevelWarnings() {
    return warnings.size() > 0;
  }

	/**
   * @return the warnings
   */
  public List<FileLineMessage> getWarnings() {
    return warnings;
  }

  /**
   * @param warnings the warnings to set
   */
  public void setWarnings(List<FileLineMessage> warnings) {
    this.warnings = warnings;
  }

  /**
	 * @return the numberOfAdditions
	 */
	public int getNumberOfAdditions() {
		return numberOfAdditions;
	}

	/**
	 * @param numberOfAdditions the numberOfAdditions to set
	 */
	public void setNumberOfAdditions(int numberOfAdditions) {
		this.numberOfAdditions = numberOfAdditions;
	}

	/**
	 * @return the numberOfItems
	 */
	public int getNumberOfItems() {
		return numberOfItems;
	}

	/**
	 * @param numberOfItems the numberOfItems to set
	 */
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	/**
	 * @return the numberOfUpdates
	 */
	public int getNumberOfUpdates() {
		return numberOfUpdates;
	}

	/**
	 * @param numberOfUpdates the numberOfUpdates to set
	 */
	public void setNumberOfUpdates(int numberOfUpdates) {
		this.numberOfUpdates = numberOfUpdates;
	}
	
	public int getNumberOfValueUpdates() {
		return numberOfValueUpdates;
	}

	public void setNumberOfValueUpdates(int numberOfValueUpdates) {
		this.numberOfValueUpdates = numberOfValueUpdates;
	}

	/**
	 * @return the number of errors
	 */
	public int getNumberOfErrors() {
		return errors.size();
	}
	
	/**
	 * @return the number of errors
	 */
	public int getNumberOfWarnings() {
	  return warnings.size();
	}

	/**
	 * @return the productionUnits
	 */
	public List getProductionUnits() {
		return productionUnits;
	}

	/**
	 * @param productionUnits the productionUnits to set
	 */
	public void setProductionUnits(List productionUnits) {
		this.productionUnits = productionUnits;
	}
	
	
}

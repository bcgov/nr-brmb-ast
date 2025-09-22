package ca.bc.gov.srm.farm.ui.struts.export;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.report.BackgroundReportForm;


/**
 * ExportForm.
 */
public class ExportForm extends ValidatorForm implements BackgroundReportForm {

  private static final long serialVersionUID = -1845502921365403099L;

  private String exportType;

  private String exportUrl;

  private String year;

  private String inventoryCodes;
  
  private String exportFileName;
  
  private String exportFileDate;
  
  private String requestorAccountName;
  
  private String reportRequestDate;
  
  private List<ListView> yearSelectOptions;

  @Override
  public String getReportType() {
    return exportType;
  }

	/**
	 * @return the exportType
	 */
	public String getExportType() {
		return exportType;
	}

	/**
	 * @param exportType the exportType to set
	 */
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}

	/**
	 * @return the exportUrl
	 */
	public String getExportUrl() {
		return exportUrl;
	}

	/**
	 * @param exportUrl the exportUrl to set
	 */
	public void setExportUrl(String exportUrl) {
		this.exportUrl = exportUrl;
	}

	/**
	 * @return the inventoryCodes
	 */
	public String getInventoryCodes() {
		return inventoryCodes;
	}

	/**
	 * @param inventoryCodes the inventoryCodes to set
	 */
	public void setInventoryCodes(String inventoryCodes) {
		this.inventoryCodes = inventoryCodes;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

  /**
   * @return the exportFileDate
   */
  public String getExportFileDate() {
    return exportFileDate;
  }

  /**
   * @param exportFileDate the exportFileDate to set
   */
  public void setExportFileDate(String exportFileDate) {
    this.exportFileDate = exportFileDate;
  }

  /**
   * @return the exportFileName
   */
  public String getExportFileName() {
    return exportFileName;
  }

  /**
   * @param exportFileName the exportFileName to set
   */
  public void setExportFileName(String exportFileName) {
    this.exportFileName = exportFileName;
  }

  /**
   * @return the requestorAccountName
   */
  @Override
  public String getRequestorAccountName() {
    return requestorAccountName;
  }

  /**
   * @param requestorAccountName the requestorAccountName to set
   */
  @Override
  public void setRequestorAccountName(String requestorAccountName) {
    this.requestorAccountName = requestorAccountName;
  }

  /**
   * @return the reportRequestDate
   */
  @Override
  public String getReportRequestDate() {
    return reportRequestDate;
  }

  /**
   * @param reportRequestDate the reportRequestDate to set
   */
  @Override
  public void setReportRequestDate(String reportRequestDate) {
    this.reportRequestDate = reportRequestDate;
  }

  public List<ListView> getYearSelectOptions() {
    return yearSelectOptions;
  }
  
  public void setYearSelectOptions(List<ListView> yearSelectOptions) {
    this.yearSelectOptions = yearSelectOptions;
  }

}

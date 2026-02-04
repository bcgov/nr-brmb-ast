package ca.bc.gov.srm.farm.ui.struts.report;

import org.apache.struts.validator.ValidatorForm;


/**
 * ReportForm.
 *
 * @author   $author$
 * @version  $Revision: 3961 $, $Date: 2021-03-03 22:56:26 -0800 (Wed, 03 Mar 2021) $
 */
public class ReportForm extends ValidatorForm implements BackgroundReportForm {

  private static final long serialVersionUID = -1845502921365403099L;

  private String reportType;

  private String reportUrl;

  private String year;

  private String name;

  private String sin;

  private String pin;
  
  private String reportFileName;
  
  private String reportFileDate;
  
  private String requestorAccountName;
  
  private String reportRequestDate;

  /**
   * @return  the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param  value  the name to set
   */
  public void setName(String value) {
    this.name = value;
  }


  /**
   * @return  the pin
   */
  public String getPin() {
    return pin;
  }

  /**
   * @param  pin  the pin to set
   */
  public void setPin(String pin) {
    this.pin = pin;
  }

  /**
   * @return  the reportType
   */
  @Override
  public String getReportType() {
    return reportType;
  }

  /**
   * @param  reportType  the reportType to set
   */
  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  /**
   * @return  the sin
   */
  public String getSin() {
    return sin;
  }

  /**
   * @param  sin  the sin to set
   */
  public void setSin(String sin) {
    this.sin = sin;
  }

  /**
   * @return  the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @param  year  the year to set
   */
  public void setYear(String year) {
    this.year = year;
  }

  /**
   * @return  the reportUrl
   */
  public String getReportUrl() {
    return reportUrl;
  }

  /**
   * @param  reportUrl  the reportUrl to set
   */
  public void setReportUrl(String reportUrl) {
    this.reportUrl = reportUrl;
  }

  /**
   * @return the reportFileName
   */
  public String getReportFileName() {
    return reportFileName;
  }

  /**
   * @param reportFileName the reportFileName to set
   */
  public void setReportFileName(String reportFileName) {
    this.reportFileName = reportFileName;
  }

  /**
   * @return the reportFileDate
   */
  public String getReportFileDate() {
    return reportFileDate;
  }

  /**
   * @param reportFileDate the reportFileDate to set
   */
  public void setReportFileDate(String reportFileDate) {
    this.reportFileDate = reportFileDate;
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

}

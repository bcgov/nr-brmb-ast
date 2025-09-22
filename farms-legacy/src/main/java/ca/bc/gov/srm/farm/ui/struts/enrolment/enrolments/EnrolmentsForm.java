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
package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ListServiceConstants;

/**
 * @author awilkinson
 * @created Dec 3, 2010
 */
public class EnrolmentsForm extends ValidatorForm {

  private static final long serialVersionUID = -2645752781365403736L;
  
  public static final String REGIONAL_OFFICE_CODE_ALL = "ALL";
  public static final String REGIONAL_OFFICE_CODE_ALL_DISPLAY = "Province Wide";
  
  public static final String ENROLMENT_STATUS_FILTER_UNGENERATED = "ungenerated";
  
  public static final String SCENARIO_STATE_FILTER_UNGENERATED = "all";

  private List<ListView> enrolmentYearSelectOptions;
  
  private List<ListView> regionSelectOptions;

  private int year;

  private String regionalOfficeCode;

  private String enrolmentStatusFilter;
  
  private String scenarioStateFilter;
  
  private String startDateFilter;
  
  private String endDateFilter;
  
  private String pinFilter;
  
  private String failedReasonFilter;

  private String pins;
  
  private List<Enrolment> enrolments;
  
  private boolean allowedToGenerate;
  
  private boolean createTaskInBarn;
  
  private String exportUrl;

  private FormFile file;


  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
  }

  /**
   * @return the enrolmentYearSelectOptions
   */
  public List<ListView> getEnrolmentYearSelectOptions() {
    return enrolmentYearSelectOptions;
  }

  /**
   * @param enrolmentYearSelectOptions the enrolmentYearSelectOptions to set
   */
  public void setEnrolmentYearSelectOptions(List<ListView> enrolmentYearSelectOptions) {
    this.enrolmentYearSelectOptions = enrolmentYearSelectOptions;
  }

  /**
   * @return the regionSelectOptions
   */
  public List<ListView> getRegionSelectOptions() {
    return regionSelectOptions;
  }

  /**
   * @param regionSelectOptions the regionSelectOptions to set
   */
  public void setRegionSelectOptions(List<ListView> regionSelectOptions) {
    this.regionSelectOptions = regionSelectOptions;
  }

  /**
   * @return the year
   */
  public int getYear() {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(int year) {
    this.year = year;
  }

  /**
   * @return the regionalOfficeCode
   */
  public String getRegionalOfficeCode() {
    return regionalOfficeCode;
  }

  /**
   * @param regionalOfficeCode the regionalOfficeCode to set
   */
  public void setRegionalOfficeCode(String regionalOfficeCode) {
    this.regionalOfficeCode = regionalOfficeCode;
  }
  
  /**
   * Gets enrolmentStatusFilter
   *
   * @return the enrolmentStatusFilter
   */
  public String getEnrolmentStatusFilter() {
    return enrolmentStatusFilter;
  }

  /**
   * Sets enrolmentStatusFilter
   *
   * @param enrolmentStatusFilter the enrolmentStatusFilter to set
   */
  public void setEnrolmentStatusFilter(String enrolmentStatusFilter) {
    this.enrolmentStatusFilter = enrolmentStatusFilter;
  }

  /**
   * @return the scenarioStateFilter
   */
  public String getScenarioStateFilter() {
    return scenarioStateFilter;
  }

  /**
   * @param scenarioStateFilter the scenarioStateFilter to set
   */
  public void setScenarioStateFilter(String scenarioStateFilter) {
    this.scenarioStateFilter = scenarioStateFilter;
  }

  /**
   * @return the pins
   */
  public String getPins() {
    return pins;
  }

  /**
   * @param pins the pins to set
   */
  public void setPins(String pins) {
    this.pins = pins;
  }

  /**
   * @return constant
   */
  public String getStatusConstantUngenerated() {
    return ListServiceConstants.ENROLMENT_STATUS_UNGENERATED;
  }
  
  /**
   * @return constant
   */
  public String getStatusConstantGenerated() {
    return ListServiceConstants.ENROLMENT_STATUS_GENERATED;
  }
  
  /**
   * @return constant
   */
  public String getStatusConstantFailedToGenerate() {
    return ListServiceConstants.ENROLMENT_STATUS_FAILED_TO_GENERATE;
  }
  
  /**
   * @return constant
   */
  public String getStateConstantVerified() {
    return ScenarioStateCodes.VERIFIED;
  }
  
  /**
   * @return constant
   */
  public String getStateConstantEnInProgress() {
    return Enrolment.ENROLMENT_SCENARIO_STATE_EN_IN_PROGRESS;
  }
  
  /**
   * @return constant
   */
  public String getStateConstantEnComplete() {
    return Enrolment.ENROLMENT_SCENARIO_STATE_EN_COMPLETE;
  }
  
  /**
   * @return constant
   */
  public String getReasonConstantExcessiveMargin() {
    return Enrolment.REASON_OVERSIZE_MARGIN;
  }
  
  /**
   * @return constant
   */
  public String getReasonConstantInsufficientMargin() {
    return Enrolment.REASON_INSUFF_REF_MARGIN_DATA;
  }

  /**
   * @return constant
   */
  public String getReasonConstantMissingBpu() {
    return Enrolment.REASON_BPU_SET_INCOMPLETE;
  }

  /**
   * @return the enrolments
   */
  public List<Enrolment> getEnrolments() {
    return enrolments;
  }

  /**
   * @param enrolments the enrolments to set
   */
  public void setEnrolments(List<Enrolment> enrolments) {
    this.enrolments = enrolments;
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
   * @return the file
   */
  public FormFile getFile() {
    return file;
  }

  /**
   * @param file the file to set
   */
  public void setFile(FormFile file) {
    this.file = file;
  }

  /**
   * @return the size of the enrolments list
   */
  public int getEnrolmentCount() {
    int result;
    if(enrolments == null) {
      result = 0;
    } else {
      result = enrolments.size();
    }
    return result;
  }

  /**
   * Gets allowedToGenerate
   *
   * @return the allowedToGenerate
   */
  public boolean isAllowedToGenerate() {
    return allowedToGenerate;
  }

  /**
   * Sets allowedToGenerate
   *
   * @param allowedToGenerate the allowedToGenerate to set
   */
  public void setAllowedToGenerate(boolean allowedToGenerate) {
    this.allowedToGenerate = allowedToGenerate;
  }
  
  /**
   * Gets createTaskInBarn
   *
   * @return the createTaskInBarn
   */
  public boolean isCreateTaskInBarn() {
    return createTaskInBarn;
  }

  /**
   * Sets createTaskInBarn
   *
   * @param createTaskInBarn the createTaskInBarn to set
   */
  public void setCreateTaskInBarn(boolean createTaskInBarn) {
    this.createTaskInBarn = createTaskInBarn;
  }

  /**
   * @return the startDateFilter
   */
  public String getStartDateFilter() {
    return startDateFilter;
  }

  /**
   * @param startDateFilter the startDateFilter to set
   */
  public void setStartDateFilter(String startDateFilter) {
    this.startDateFilter = startDateFilter;
  }

  /**
   * @return the endDateFilter
   */
  public String getEndDateFilter() {
    return endDateFilter;
  }

  /**
   * @param endDateFilter the endDateFilter to set
   */
  public void setEndDateFilter(String endDateFilter) {
    this.endDateFilter = endDateFilter;
  }

  /**
   * @return the pinFilter
   */
  public String getPinFilter() {
    return pinFilter;
  }

  /**
   * @param pinFilter the pinFilter to set
   */
  public void setPinFilter(String pinFilter) {
    this.pinFilter = pinFilter;
  }

  /**
   * @return the failedReasonFilter
   */
  public String getFailedReasonFilter() {
    return failedReasonFilter;
  }

  /**
   * @param failedReasonFilter the failedReasonFilter to set
   */
  public void setFailedReasonFilter(String failedReasonFilter) {
    this.failedReasonFilter = failedReasonFilter;
  }

}
